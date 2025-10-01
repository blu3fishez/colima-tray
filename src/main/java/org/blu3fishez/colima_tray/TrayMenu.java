package org.blu3fishez.colima_tray;


import javax.swing.SwingUtilities;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TrayMenu {
    private final PopupMenu popupMenu;
    private final Map<String, MenuItem> menuItems;
    private final ColimaCommand colimaCommand;

    public TrayMenu() {
        this.popupMenu = new PopupMenu();
        this.menuItems = new HashMap<>();
        this.menuItems.put("status", new MenuItem("Status"));
        this.menuItems.put("toggle", new MenuItem("Toggle"));
        this.menuItems.put("exit", new MenuItem("Exit"));
        this.colimaCommand = new ColimaCommandImpl(this.menuItems.get("status"));

        initPopupMenu();
        startStatusUpdateTask();
    }

    private void initPopupMenu() {
        // setup exit menu
        var exit = this.menuItems.get("exit");
        exit.addActionListener(e -> {
            System.exit(0);
        });

        // setup toggle menu
        var toggle = this.menuItems.get("toggle");
        toggle.setLabel("Toggle colima");
        toggle.addActionListener(e -> {
            this.colimaCommand.toggleColimaStatus();
        });

        // setup status menu

        var status = this.menuItems.get("status");
        status.setLabel("Status");
        status.setEnabled(false);


        popupMenu.add(status);
        popupMenu.addSeparator();
        popupMenu.add(toggle);
        popupMenu.addSeparator();
        popupMenu.add(exit);
    }

    private void startStatusUpdateTask() {
        var scheduleService = Executors.newSingleThreadScheduledExecutor();
        Runnable statusUpdater = () -> {
            try {
                // 백그라운드 스레드에서 상태 확인
                // 유지보수성을 위해서 Interface로 만들었고, 필요한 구현 메서드는 알아서 구현하시라.
                ColimaStatus status = colimaCommand.getColimaStatus();

                // UI 업데이트는 SwingUtilities.invokeLater를 통해 EDT에서 실행
                SwingUtilities.invokeLater(() -> {
                    // TODO: 직관적이지 못한 접근이므로 개선이 필요해보임
                    this.menuItems.get("status").setLabel("Status: " + status.toString());
                });

            } catch (Exception e) {
                // 에러 발생 시 UI에 표시
                SwingUtilities.invokeLater(() -> {
                    this.menuItems.get("status").setLabel("Status: Error");
                });
                e.printStackTrace();
            }
        };

        // TODO: We need to check the colima status only before when we open the tray menu.
        scheduleService.scheduleAtFixedRate(statusUpdater, 0, 5, TimeUnit.SECONDS);
    }

    public PopupMenu getPopupMenu() {
        return popupMenu;
    }


}
