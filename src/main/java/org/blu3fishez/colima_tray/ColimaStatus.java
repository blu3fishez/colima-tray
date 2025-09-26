package org.blu3fishez.colima_tray;

public enum ColimaStatus {
    ACTIVE, INACTIVE, ERROR, TOGGLING;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
