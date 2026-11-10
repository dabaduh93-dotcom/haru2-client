package com.haru.client.module;

import com.haru.client.settings.BooleanSetting;
import com.haru.client.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class JumpResetModule extends Module {
    private final NumberSetting chance = new NumberSetting("Chance", 100, 0, 100, 5);
    private final BooleanSetting onlyOnGround = new BooleanSetting("Only On Ground", true);
    private int previousHurtTime;
    private boolean pendingJump;

    public JumpResetModule() {
        super("JumpReset", "Attempts a jump reset when the local player takes a hit", Category.COMBAT);
        addSetting(chance);
        addSetting(onlyOnGround);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !isEnabled()) return;
        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.isDead) {
            previousHurtTime = 0;
            pendingJump = false;
            return;
        }

        int hurtTime = player.hurtTime;
        if (hurtTime > previousHurtTime && Math.random() * 100.0D < chance.getValue()) {
            if (!onlyOnGround.getValue() || player.onGround) pendingJump = true;
        }
        previousHurtTime = hurtTime;

        if (!pendingJump) return;
        pendingJump = false;
        if (player.onGround) {
            player.jump();
        } else if (!onlyOnGround.getValue()) {
            player.motionY = Math.max(player.motionY, 0.42D);
        }
    }

    @Override
    protected void onDisable() {
        pendingJump = false;
        previousHurtTime = 0;
    }
}
