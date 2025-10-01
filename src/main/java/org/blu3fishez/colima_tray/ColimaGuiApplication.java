package org.blu3fishez.colima_tray;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import java.awt.AWTException;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.net.URL;

public class ColimaGuiApplication {
    public static void startTrayMenu() {
        // tray icon 추가 가능한지 확인
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported");
            return;
        }

        // ----------------------------------
        // tray menu 설정 시작

        PopupMenu popupMenu = new TrayMenu().getPopupMenu();


        // ----------------------------------
        // tray Image 추가
        URL imageURL = ColimaGuiApplication.class.getResource("/tray_icon.png");
        if (imageURL == null) {
            System.err.println("Resource not found: icon.png");
            return;
        }

        Image image;
        try {
            image = ImageIO.read(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        TrayIcon trayIcon = new TrayIcon(image, "Colima GUI", popupMenu);
        trayIcon.setImageAutoSize(true); // 아이콘 크기 자동 조절

        // ----------------------------------
        // System 에 추가
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }


    }

    // main 메서드를 추가하여 시스템 프로퍼티를 가장 먼저 설정
    public static void main(String[] args) {
        // instantiate
        CustomProcessBuilder.getInstance();

        // Platform이 맥인지 확인. 맥이 아닌 경우 종료.
        if (System.getProperty("os.name") == null || !System.getProperty("os.name").toLowerCase().contains("mac os")) {
            System.err.println("This program is only supported on Mac OS");
        }

        System.setProperty("apple.awt.enableTemplateImages", "true");
        System.setProperty("apple.awt.UIElement", "true");

        SwingUtilities.invokeLater(ColimaGuiApplication::startTrayMenu);
    }
}
