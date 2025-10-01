package org.blu3fishez.colima_tray;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ColimaCommandImpl implements ColimaCommand {
    private static final String COLIMA_PATH = "/opt/homebrew/bin/colima";
    private final CustomProcessBuilder processBuilder = CustomProcessBuilder.getInstance();
    private final MenuItem statusMenuItem;
    private boolean isToggling = false;

    public ColimaCommandImpl(MenuItem statusMenuItem) {
        this.statusMenuItem = statusMenuItem;

        setupEnvironment();
    }

    private void setupEnvironment() {
        try {
            ProcessBuilder pb = processBuilder.getCommand("ls", COLIMA_PATH);
            pb.redirectErrorStream(true);
            Process p = pb.start();

            // fetching process output
            p.waitFor();

            if (p.exitValue() != 0) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            null, // 부모 컴포넌트 (null이면 화면 중앙)
                            "colima is not available: did you install colima with brew?", // 메시지 내용
                            "Colima Tray", // 창 제목
                            JOptionPane.ERROR_MESSAGE // 메시지 타입 아이콘
                    );
                });
                throw new IllegalStateException("Colima is not available");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ColimaStatus getColimaStatus() {
        if (isToggling) {
            return ColimaStatus.TOGGLING;
        }
        try {
            ProcessBuilder pb = processBuilder.getCommand(COLIMA_PATH, "status");
            pb.redirectErrorStream(true);
            Process p = pb.start();

            var reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            var output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            p.waitFor();

            System.out.println(new String(output));

            return output.toString().contains("colima is running") ? ColimaStatus.ACTIVE : ColimaStatus.INACTIVE;
        } catch (IOException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null, // 부모 컴포넌트 (null이면 화면 중앙)
                        "Failed to get Colima status", // 메시지 내용
                        "Colima Tray", // 창 제목
                        JOptionPane.ERROR_MESSAGE // 메시지 타입 아이콘
                );
            });

            return ColimaStatus.ERROR;
        }
    }

    @Override
    public void toggleColimaStatus() {
        ColimaStatus status = getColimaStatus();

        ProcessBuilder pb = null;

        if (status == ColimaStatus.INACTIVE) {
            pb = processBuilder.getCommand(COLIMA_PATH, "start", "--memory", "4");
        } else if (status == ColimaStatus.ACTIVE) {
            pb = processBuilder.getCommand(COLIMA_PATH, "stop");
        } else {
            throw new IllegalStateException("Colima is not available");
        }

        isToggling = true;
        statusMenuItem.setLabel("Status: " + ColimaStatus.TOGGLING);

        try {
            pb.redirectErrorStream(true);
            Process p = pb.start();

            p.waitFor();

            var t = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = t.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
            final String s = sb.toString();


            if (p.exitValue() != 0) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            null, // 부모 컴포넌트 (null이면 화면 중앙)
                            "Failed to toggle colima : " + s, // 메시지 내용
                            "Colima Tray", // 창 제목
                            JOptionPane.ERROR_MESSAGE // 메시지 타입 아이콘
                    );
                });
            }

            System.out.println("toggled colima status");
            getColimaStatus();

            statusMenuItem.setLabel("Status: " + status);

        } catch (IOException | InterruptedException e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                        null, // 부모 컴포넌트 (null이면 화면 중앙)
                        "Failed to toggle colima status due to unexpected error", // 메시지 내용
                        "Colima Tray", // 창 제목
                        JOptionPane.ERROR_MESSAGE // 메시지 타입 아이콘
                );
            });
        } finally {
            isToggling = false;
        }
    }
}
