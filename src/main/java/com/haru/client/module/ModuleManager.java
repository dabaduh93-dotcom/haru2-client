package com.haru.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<Module>();
    private boolean defaultsRegistered;

    public void register(Module module) {
        if (module != null && getModule(module.getName()) == null) modules.add(module);
    }

    public void registerDefaults() {
        if (defaultsRegistered) return;
        defaultsRegistered = true;
        add("Custom Colors", "Client color customization", Category.SETTINGS);
        add("Targets", "Target selection preferences", Category.SETTINGS);
        add("Tweaks", "Client behavior preferences", Category.SETTINGS);

        String[] combat = {"AutoClick", "AimAssist", "AutoRod", "AutoRefill", "AutoBlock", "JumpReset", "KeepSprint", "Criticals", "Reach", "WTap", "Velocity"};
        for (String name : combat) add(name, "Configuration and behavior pending implementation", Category.COMBAT);
        String[] player = {"InvManager", "Stealer", "FastPlace", "LegitScaffold", "BridgeAssist", "Sprint", "Blink", "NoSlow", "NoFall"};
        for (String name : player) add(name, "Configuration and behavior pending implementation", Category.PLAYER);
        String[] other = {"AutoPlay", "AutoLeave", "AutoTool", "SelfDestruct", "MiddleClick", "Inventory", "AutoChest"};
        for (String name : other) add(name, "Configuration and behavior pending implementation", Category.OTHER);
        String[] visuals = {"Ambience", "Fullbright", "FreeLook", "ClickGUI", "HUD", "ArmorStatus", "CpsDisplay", "TargetHUD", "Trajectories", "NameTags", "ESP"};
        for (String name : visuals) add(name, "Configuration and behavior pending implementation", Category.VISUALS);
        String[] exploit = {"ChatBypass", "PingSpoof", "FakeLag", "Test"};
        for (String name : exploit) add(name, "Configuration and behavior pending implementation", Category.EXPLOIT);
    }

    private void add(String name, String description, Category category) {
        register(new BasicModule(name, description, category));
    }

    public Module getModule(String name) {
        for (Module module : modules) if (module.getName().equalsIgnoreCase(name)) return module;
        return null;
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<Module> getModules(Category category) {
        List<Module> result = new ArrayList<Module>();
        for (Module module : modules) if (module.getCategory() == category) result.add(module);
        return result;
    }
}