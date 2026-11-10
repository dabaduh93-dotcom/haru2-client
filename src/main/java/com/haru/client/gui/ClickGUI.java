package com.haru.client.gui;

import com.haru.client.HaruClient;
import com.haru.client.module.Category;
import com.haru.client.module.Module;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends GuiScreen {
    private static final int PANEL_WIDTH = 118;
    private static final int HEADER_HEIGHT = 20;
    private static final int ROW_HEIGHT = 18;
    private static final int GAP = 5;
    private static final int TOP = 38;
    private static final int PURPLE = 0xFF8B5CF6;
    private static final int BACKGROUND = 0xE9161620;
    private static final int HEADER = 0xFF242432;
    private static final int ROW = 0xFF1D1D29;
    private static final int ROW_HOVER = 0xFF303044;
    private final Map<Category, Integer> scroll = new EnumMap<Category, Integer>(Category.class);

    public ClickGUI() {
        for (Category category : Category.values()) scroll.put(category, 0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int totalWidth = Category.values().length * PANEL_WIDTH + (Category.values().length - 1) * GAP;
        int startX = Math.max(5, (width - totalWidth) / 2);

        for (Category category : Category.values()) {
            int x = startX + category.ordinal() * (PANEL_WIDTH + GAP);
            List<Module> modules = HaruClient.getInstance().getModuleManager().getModules(category);
            int maxRows = Math.max(1, (height - TOP - 28) / ROW_HEIGHT);
            int offset = scroll.get(category);
            int visible = Math.min(maxRows, Math.max(0, modules.size() - offset));
            int panelHeight = HEADER_HEIGHT + visible * ROW_HEIGHT + 4;

            Gui.drawRect(x, TOP, x + PANEL_WIDTH, TOP + panelHeight, BACKGROUND);
            Gui.drawRect(x, TOP, x + PANEL_WIDTH, TOP + HEADER_HEIGHT, HEADER);
            Gui.drawRect(x, TOP, x + 3, TOP + HEADER_HEIGHT, PURPLE);
            fontRendererObj.drawString(category.getDisplayName(), x + 9, TOP + 6, 0xFFFFFFFF);

            for (int i = 0; i < visible; i++) {
                Module module = modules.get(offset + i);
                int y = TOP + HEADER_HEIGHT + i * ROW_HEIGHT;
                boolean hovered = mouseX >= x && mouseX <= x + PANEL_WIDTH && mouseY >= y && mouseY < y + ROW_HEIGHT;
                Gui.drawRect(x + 2, y, x + PANEL_WIDTH - 2, y + ROW_HEIGHT - 1, hovered ? ROW_HOVER : ROW);
                int color = module.isEnabled() ? PURPLE : 0xFFB8B8C5;
                fontRendererObj.drawString(module.getName(), x + 7, y + 5, color);
                Gui.drawRect(x + PANEL_WIDTH - 17, y + 5, x + PANEL_WIDTH - 7, y + 13,
                        module.isEnabled() ? PURPLE : 0xFF555563);
            }
        }
        fontRendererObj.drawString("Haru2.0", 8, 8, PURPLE);
        fontRendererObj.drawString("Right Shift: close", 8, 20, 0xFFAAAAAA);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int totalWidth = Category.values().length * PANEL_WIDTH + (Category.values().length - 1) * GAP;
            int startX = Math.max(5, (width - totalWidth) / 2);
            for (Category category : Category.values()) {
                int x = startX + category.ordinal() * (PANEL_WIDTH + GAP);
                if (mouseX < x || mouseX > x + PANEL_WIDTH || mouseY < TOP + HEADER_HEIGHT) continue;
                List<Module> modules = HaruClient.getInstance().getModuleManager().getModules(category);
                int row = (mouseY - TOP - HEADER_HEIGHT) / ROW_HEIGHT + scroll.get(category);
                if (row >= 0 && row < modules.size()) modules.get(row).toggle();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            int totalWidth = Category.values().length * PANEL_WIDTH + (Category.values().length - 1) * GAP;
            int startX = Math.max(5, (width - totalWidth) / 2);
            int mx = Mouse.getEventX() * width / mc.displayWidth;
            int my = height - Mouse.getEventY() * height / mc.displayHeight - 1;
            for (Category category : Category.values()) {
                int x = startX + category.ordinal() * (PANEL_WIDTH + GAP);
                if (mx >= x && mx <= x + PANEL_WIDTH && my >= TOP) {
                    int next = scroll.get(category) + (wheel < 0 ? 1 : -1);
                    int max = Math.max(0, HaruClient.getInstance().getModuleManager().getModules(category).size() - 1);
                    scroll.put(category, Math.max(0, Math.min(max, next)));
                    break;
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}