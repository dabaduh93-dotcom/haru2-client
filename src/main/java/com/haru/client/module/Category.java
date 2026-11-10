package com.haru.client.module;

public enum Category {
    SETTINGS("Settings"),
    COMBAT("Combat"),
    PLAYER("Player"),
    OTHER("Other"),
    VISUALS("Visuals"),
    EXPLOIT("Exploit");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
