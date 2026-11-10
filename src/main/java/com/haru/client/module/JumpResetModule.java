package com.haru.client.module;

import com.haru.client.settings.BooleanSetting;
import com.haru.client.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class JumpResetModule extends Module {
    private final NumberSetting chance = new NumberSetting("Chance", 100, 0, 100, 5);
    private final BooleanSetting onlyOnGround = new BooleanSetting("Only On Ground", true);
    private boolean pendingJump;

    public JumpResetModule() {
        super("JumpReset", "Attempts a timed jump reset when the local player is hurt", Category.COMBAT);
        addSetting(chance);
        addSetting(onlyOnGround);
    }

    @SubscribeEvent
    public void onPlayerHurt(LivingHurtEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        if (event.entity != mc.thePlayer || !mc.theWorld.isRemote) return;
        if (Math.random() * 100.0D >= chance.getValue()) return;
        if (onlyOnGround.getValue() && !mc.thePlayer.onGround) return;
        pendingJump = true;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled() || !pendingJump) return;
        pendingJump = false;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.isDead) return;
        if (player.onGround) {
            player.jump();
        } else if (!onlyOnGround.getValue()) {
            player.motionY = Math.max(player.motionY, 0.42D);
        }
    }

    @Override
    protected void onDisable() {
        pendingJump = false;
    }
}
