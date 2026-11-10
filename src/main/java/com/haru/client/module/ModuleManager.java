package com.haru.client.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<Module>();

    public void register(Module module) {
        if (module != null && getModule(module.getName()) == null) {
            modules.add(module);
        }
    }

    public void registerDefaults() {
        // Default modules will be registered as their implementations are added.
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
        for (Module module : modules) {
            if (module.getCategory() == category) result.add(module);
        }
        return result;
    }
}
