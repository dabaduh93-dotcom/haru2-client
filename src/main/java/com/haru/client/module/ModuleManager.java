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
        add("AutoClick", "Automatic clicking", Category.COMBAT);
        add("AimAssist", "Aim assistance", Category.COMBAT);
        add("AutoRod", "Fishing rod assistance", Category.COMBAT);
        add("AutoRefill", "Hotbar refill", Category.COMBAT);
        add("AutoBlock", "Sword blocking assistance", Category.COMBAT);
        add("JumpReset", "Jump reset assistance", Category.COMBAT);
        add("KeepSprint", "Maintain sprint while attacking", Category.COMBAT);
        add("Criticals", "Critical hit assistance", Category.COMBAT);
        add("Reach", "Reach settings", Category.COMBAT);
        add("WTap", "W-tap assistance", Category.COMBAT);
        add("Velocity", "Knockback settings", Category.COMBAT);

        add("InvManager", "Inventory management", Category.PLAYER);
        add("Stealer", "Container looting", Category.PLAYER);
        add("FastPlace", "Fast block placement", Category.PLAYER);
        add("LegitScaffold", "Scaffold assistance", Category.PLAYER);
        add("BridgeAssist", "Bridge placement assistance", Category.PLAYER);
        add("Sprint", "Automatic sprint", Category.PLAYER);
        add("Blink", "Network packet delay", Category.PLAYER);
        add("NoSlow", "Movement slowdown settings", Category.PLAYER);
        add("NoFall", "Fall damage assistance", Category.PLAYER);

        add("AutoPlay", "Automatic play actions", Category.OTHER);
        add("AutoLeave", "Automatic leave settings", Category.OTHER);
        add("AutoTool", "Automatic tool selection", Category.OTHER);
        add("SelfDestruct", "Client shutdown and cleanup", Category.OTHER);
        add("MiddleClick", "Middle-click actions", Category.OTHER);
        add("Inventory", "Inventory utilities", Category.OTHER);
        add("AutoChest", "Chest interaction assistance", Category.OTHER);

        add("Ambience", "World ambience", Category.VISUALS);
        add("Fullbright", "Brightness settings", Category.VISUALS);
        add("FreeLook", "Free camera view", Category.VISUALS);
        add("ClickGUI", "Client module interface", Category.VISUALS);
        add("HUD", "Heads-up display", Category.VISUALS);
        add("ArmorStatus", "Armor status display", Category.VISUALS);
        add("CpsDisplay", "Clicks-per-second display", Category.VISUALS);
        add("TargetHUD", "Target information display", Category.VISUALS);
        add("Trajectories", "Projectile trajectory display", Category.VISUALS);
        add("NameTags", "Entity name tags", Category.VISUALS);
        add("ESP", "Entity highlighting", Category.VISUALS);

        add("ChatBypass", "Chat formatting options", Category.EXPLOIT);
        add("PingSpoof", "Network latency simulation", Category.EXPLOIT);
        add("FakeLag", "Network delay simulation", Category.EXPLOIT);
        add("Test", "Development test module", Category.EXPLOIT);
    }

    private void add(String name, String description, Category category) {
        register(new BasicModule(name, description, category));
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
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
