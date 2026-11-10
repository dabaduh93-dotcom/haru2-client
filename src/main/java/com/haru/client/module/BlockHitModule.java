package com.haru.client.module;

import com.haru.client.settings.BooleanSetting;
import com.haru.client.settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BlockHitModule extends Module {
    private final BooleanSetting onlySword = new BooleanSetting("Only Sword", true);
    private final NumberSetting delay = new NumberSetting("Delay", 0, 0, 300, 5);
    private long lastBlockTime;
    private boolean releasePending;

    public BlockHitModule() {
        super("BlockHit", "Performs one short sword block action after each melee hit", Category.COMBAT);
        addSetting(onlySword);
        addSetting(delay);
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (!isEnabled() || mc.thePlayer == null || mc.theWorld == null) return;
        if (event.entityPlayer != mc.thePlayer || releasePending || mc.thePlayer.isUsingItem()) return;

        ItemStack held = mc.thePlayer.getHeldItem();
        if (held == null || (onlySword.getValue() && !(held.getItem() instanceof ItemSword))) return;

        long now = System.currentTimeMillis();
        if (now - lastBlockTime < delay.getValue()) return;

        if (mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, held)) {
            lastBlockTime = now;
            releasePending = true;
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !releasePending) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && mc.playerController != null) {
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
        }
        releasePending = false;
    }

    @Override
    protected void onDisable() {
        Minecraft mc = Minecraft.getMinecraft();
        if (releasePending && mc.thePlayer != null && mc.playerController != null) {
            mc.playerController.onStoppedUsingItem(mc.thePlayer);
        }
        releasePending = false;
    }
}
