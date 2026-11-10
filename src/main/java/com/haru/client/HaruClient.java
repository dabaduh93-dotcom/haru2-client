package com.haru.client;

import com.haru.client.gui.ClickGUI;
import com.haru.client.module.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = HaruClient.MOD_ID, name = HaruClient.NAME, version = HaruClient.VERSION, clientSideOnly = true)
public class HaruClient {
    public static final String MOD_ID = "haru";
    public static final String NAME = "Haru2.0";
    public static final String VERSION = "1.0.0";

    private static HaruClient instance;
    private final ModuleManager moduleManager = new ModuleManager();
    private KeyBinding clickGuiKey;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        instance = this;
        moduleManager.registerDefaults();
        clickGuiKey = new KeyBinding("Haru2.0 ClickGUI", Keyboard.KEY_RSHIFT, "Haru2.0");
        ClientRegistry.registerKeyBinding(clickGuiKey);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (clickGuiKey != null && clickGuiKey.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new ClickGUI());
        }
    }

    public static HaruClient getInstance() { return instance; }
    public ModuleManager getModuleManager() { return moduleManager; }
}