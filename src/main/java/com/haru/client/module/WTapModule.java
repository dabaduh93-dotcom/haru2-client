package com.haru.client.module;

import com.haru.client.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class WTapModule extends Module {
    private final NumberSetting chance = new NumberSetting("Chance", 75, 0, 100, 5);
    private final NumberSetting delay = new NumberSetting("Delay", 50, 0, 300, 5);
    private int ticksRemaining;
    private boolean restoreSprint;

    public WTapModule() {
        super("WTap", "Briefly releases sprint after a melee hit", Category.COMBAT);
        addSetting(chance);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!isEnabled() || mc.thePlayer == null || event.entityPlayer != mc.thePlayer) return;
        if (!(event.target instanceof EntityLivingBase)) return;
        if (Math.random() * 100.0D >= chance.getValue()) return;

        EntityPlayerSP player = mc.thePlayer;
        restoreSprint = player.isSprinting();
        if (!restoreSprint) return;
        player.setSprinting(false);
        ticksRemaining = Math.max(1, (int) Math.ceil(delay.getValue() / 50.0D));
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled() || ticksRemaining <= 0) return;
        ticksRemaining--;
        if (ticksRemaining > 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (restoreSprint && player != null && !player.isDead
                && mc.gameSettings.keyBindForward.isKeyDown()) {
            player.setSprinting(true);
        }
        restoreSprint = false;
    }

    @Override
    protected void onDisable() {
        ticksRemaining = 0;
        restoreSprint = false;
    }
}
