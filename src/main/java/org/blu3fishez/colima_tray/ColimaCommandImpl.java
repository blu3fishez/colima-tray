package org.blu3fishez.colima_tray;

import java.awt.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ColimaCommandImpl implements ColimaCommand {
    private final MenuItem statusMenuItem;
    private boolean isToggling = false;

    public ColimaCommandImpl(MenuItem statusMenuItem) {
        this.statusMenuItem = statusMenuItem;
    }

    @Override
    public ColimaStatus getColimaStatus() {
        if (isToggling) {
            return ColimaStatus.TOGGLING;
        }
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.command("colima", "status");
            // processBuilder.inheritIO();

            StringBuilder output = new StringBuilder();

            Process p = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }

            p.waitFor();

            System.out.println(new String(output));

            return output.toString().contains("colima is running") ? ColimaStatus.ACTIVE : ColimaStatus.INACTIVE;
        } catch (IOException | InterruptedException e) {
            return ColimaStatus.ERROR;
        }
    }

    @Override
    public void toggleColimaStatus() {
        ColimaStatus status = getColimaStatus();

        var processBuilder = new ProcessBuilder();

        if (status == ColimaStatus.INACTIVE) {
            processBuilder.command("colima", "start", "--memory", "4");
        } else if (status == ColimaStatus.ACTIVE) {
            processBuilder.command("colima", "stop");
        }

        isToggling = true;
        statusMenuItem.setLabel("Status: " + ColimaStatus.TOGGLING);

        try {
            Process p = processBuilder.start();

            p.waitFor();

            System.out.println("toggled colima status");

            statusMenuItem.setLabel("Status: " + status);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            isToggling = false;
        }
    }
}
