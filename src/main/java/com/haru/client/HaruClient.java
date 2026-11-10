package com.haru.client;

import com.haru.client.module.ModuleManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = HaruClient.MOD_ID, name = HaruClient.NAME, version = HaruClient.VERSION, clientSideOnly = true)
public class HaruClient {
    public static final String MOD_ID = "haru";
    public static final String NAME = "Haru2.0";
    public static final String VERSION = "1.0.0";

    private static HaruClient instance;
    private final ModuleManager moduleManager = new ModuleManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        moduleManager.registerDefaults();
    }

    public static HaruClient getInstance() {
        return instance;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
