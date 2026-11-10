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
    private long restoreAt;
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
        if (!player.isSprinting()) return;
        restoreSprint = true;
        player.setSprinting(false);
        restoreAt = System.currentTimeMillis() + (long) delay.getValue();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled() || !restoreSprint) return;
        if (System.currentTimeMillis() < restoreAt) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (player != null && !player.isDead && mc.gameSettings.keyBindForward.isKeyDown()) {
            player.setSprinting(true);
        }
        restoreSprint = false;
    }

    @Override
    protected void onDisable() {
        restoreSprint = false;
        restoreAt = 0L;
    }
}
