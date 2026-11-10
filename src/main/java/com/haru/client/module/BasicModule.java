package com.haru.client.module;

import com.haru.client.settings.BooleanSetting;
import com.haru.client.settings.NumberSetting;

public class BasicModule extends Module {
    public BasicModule(String name, String description, Category category) {
        super(name, description, category);
        configureDefaults(name);
    }

    private void configureDefaults(String name) {
        if ("AutoClick".equals(name)) {
            addSetting(new NumberSetting("CPS Min", 8, 1, 20, 1));
            addSetting(new NumberSetting("CPS Max", 12, 1, 20, 1));
            addSetting(new BooleanSetting("Only Weapon", true));
        } else if ("AimAssist".equals(name)) {
            addSetting(new NumberSetting("Range", 4.0, 1.0, 6.0, 0.1));
            addSetting(new NumberSetting("Speed", 35, 1, 100, 1));
            addSetting(new BooleanSetting("Players", true));
        } else if ("AutoBlock".equals(name)) {
            addSetting(new BooleanSetting("Only Weapon", true));
            addSetting(new NumberSetting("Delay", 100, 0, 500, 10));
        } else if ("JumpReset".equals(name)) {
            addSetting(new NumberSetting("Chance", 100, 0, 100, 5));
            addSetting(new BooleanSetting("Only On Ground", false));
        } else if ("WTap".equals(name)) {
            addSetting(new NumberSetting("Chance", 75, 0, 100, 5));
            addSetting(new NumberSetting("Delay", 50, 0, 300, 5));
        } else if ("Velocity".equals(name)) {
            addSetting(new NumberSetting("Horizontal", 0, 0, 100, 5));
            addSetting(new NumberSetting("Vertical", 0, 0, 100, 5));
            addSetting(new BooleanSetting("Only Players", true));
        } else if ("Reach".equals(name)) {
            addSetting(new NumberSetting("Distance", 3.0, 3.0, 6.0, 0.1));
        } else if ("Sprint".equals(name) || "KeepSprint".equals(name)) {
            addSetting(new BooleanSetting("Omni Direction", false));
        } else if ("Fullbright".equals(name)) {
            addSetting(new NumberSetting("Brightness", 100, 0, 100, 5));
        } else if ("HUD".equals(name) || "ClickGUI".equals(name)) {
            addSetting(new BooleanSetting("Show Background", true));
        }
    }
}