package com.haru.client.gui;

import com.haru.client.HaruClient;
import com.haru.client.module.Category;
import com.haru.client.module.Module;
import com.haru.client.settings.BooleanSetting;
import com.haru.client.settings.NumberSetting;
import com.haru.client.settings.Setting;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ClickGUI extends GuiScreen {
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
    private Module selectedModule;
    private int settingsScroll;

    public ClickGUI() {
        for (Category category : Category.values()) scroll.put(category, 0);
    }

    private int getColumns() {
        return width < 760 ? 3 : Category.values().length;
    }

    private int getPanelWidth() {
        int columns = getColumns();
        return Math.max(90, Math.min(118, (width - 10 - (columns - 1) * GAP) / columns));
    }

    private int getPanelHeight() {
        int rows = (Category.values().length + getColumns() - 1) / getColumns();
        return Math.max(HEADER_HEIGHT + ROW_HEIGHT + 4,
                (height - TOP - 8 - (rows - 1) * GAP) / rows);
    }

    private int getMaxVisibleRows() {
        return Math.max(1, (getPanelHeight() - HEADER_HEIGHT - 4) / ROW_HEIGHT);
    }

    private int getCategoryX(Category category) {
        int columns = getColumns();
        int panelWidth = getPanelWidth();
        int totalWidth = columns * panelWidth + (columns - 1) * GAP;
        int startX = Math.max(5, (width - totalWidth) / 2);
        return startX + (category.ordinal() % columns) * (panelWidth + GAP);
    }

    private int getCategoryY(Category category) {
        int row = category.ordinal() / getColumns();
        return TOP + row * (getPanelHeight() + GAP);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int panelWidth = getPanelWidth();
        int panelHeight = getPanelHeight();
        int maxRows = getMaxVisibleRows();

        for (Category category : Category.values()) {
            int x = getCategoryX(category);
            int y = getCategoryY(category);
            List<Module> modules = HaruClient.getInstance().getModuleManager().getModules(category);
            int maxScroll = Math.max(0, modules.size() - maxRows);
            int offset = Math.max(0, Math.min(maxScroll, scroll.get(category)));
            scroll.put(category, offset);
            int visible = Math.min(maxRows, Math.max(0, modules.size() - offset));

            Gui.drawRect(x, y, x + panelWidth, y + panelHeight, BACKGROUND);
            Gui.drawRect(x, y, x + panelWidth, y + HEADER_HEIGHT, HEADER);
            Gui.drawRect(x, y, x + 3, y + HEADER_HEIGHT, PURPLE);
            fontRendererObj.drawString(category.getDisplayName(), x + 9, y + 6, 0xFFFFFFFF);

            for (int i = 0; i < visible; i++) {
                Module module = modules.get(offset + i);
                int rowY = y + HEADER_HEIGHT + i * ROW_HEIGHT;
                boolean hovered = mouseX >= x && mouseX <= x + panelWidth
                        && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT;
                Gui.drawRect(x + 2, rowY, x + panelWidth - 2, rowY + ROW_HEIGHT - 1,
                        hovered ? ROW_HOVER : ROW);
                String name = module.getName();
                int maxTextWidth = panelWidth - 30;
                if (fontRendererObj.getStringWidth(name) > maxTextWidth) {
                    while (name.length() > 3 && fontRendererObj.getStringWidth(name + "...") > maxTextWidth) {
                        name = name.substring(0, name.length() - 1);
                    }
                    name += "...";
                }
                fontRendererObj.drawString(name, x + 7, rowY + 5,
                        module.isEnabled() ? PURPLE : 0xFFB8B8C5);
                Gui.drawRect(x + panelWidth - 17, rowY + 5, x + panelWidth - 7, rowY + 13,
                        module.isEnabled() ? PURPLE : 0xFF555563);
            }
        }

        drawSettingsPanel(mouseX, mouseY);
        fontRendererObj.drawString("Haru2.0", 8, 8, PURPLE);
        fontRendererObj.drawString("Left-click: toggle | Right-click: settings | Right Shift: close",
                8, 20, 0xFFAAAAAA);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private int getSettingsX() {
        return Math.max(5, width - 166);
    }

    private int getSettingsVisibleRows() {
        return Math.max(1, (height - TOP - HEADER_HEIGHT - 8) / ROW_HEIGHT);
    }

    private void drawSettingsPanel(int mouseX, int mouseY) {
        if (selectedModule == null) return;
        int x = getSettingsX();
        int y = TOP;
        int panelWidth = 158;
        int rows = getSettingsVisibleRows();
        int maxScroll = Math.max(0, selectedModule.getSettings().size() - rows);
        settingsScroll = Math.max(0, Math.min(maxScroll, settingsScroll));
        int visible = Math.min(rows, Math.max(0, selectedModule.getSettings().size() - settingsScroll));

        Gui.drawRect(x, y, x + panelWidth, y + HEADER_HEIGHT + visible * ROW_HEIGHT + 4, BACKGROUND);
        Gui.drawRect(x, y, x + panelWidth, y + HEADER_HEIGHT, HEADER);
        Gui.drawRect(x, y, x + 3, y + HEADER_HEIGHT, PURPLE);
        fontRendererObj.drawString(selectedModule.getName(), x + 9, y + 6, 0xFFFFFFFF);

        for (int i = 0; i < visible; i++) {
            Setting<?> setting = selectedModule.getSettings().get(settingsScroll + i);
            int rowY = y + HEADER_HEIGHT + i * ROW_HEIGHT;
            boolean hovered = mouseX >= x && mouseX <= x + panelWidth
                    && mouseY >= rowY && mouseY < rowY + ROW_HEIGHT;
            Gui.drawRect(x + 2, rowY, x + panelWidth - 2, rowY + ROW_HEIGHT - 1,
                    hovered ? ROW_HOVER : ROW);
            String value = setting.getValue() instanceof Double
                    ? String.format(java.util.Locale.US, "%.2f", (Double) setting.getValue())
                    : String.valueOf(setting.getValue());
            fontRendererObj.drawString(setting.getName(), x + 7, rowY + 5, 0xFFCCCCD6);
            fontRendererObj.drawString(value, x + panelWidth - 7 - fontRendererObj.getStringWidth(value),
                    rowY + 5, PURPLE);
        }
        if (selectedModule.getSettings().isEmpty()) {
            fontRendererObj.drawString("No settings yet", x + 7, y + 27, 0xFFAAAAAA);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (selectedModule != null && mouseButton == 0) {
            int x = getSettingsX();
            int y = TOP + HEADER_HEIGHT;
            int row = (mouseY - y) / ROW_HEIGHT + settingsScroll;
            if (mouseX >= x && mouseX <= x + 158 && mouseY >= y
                    && row >= 0 && row < selectedModule.getSettings().size()) {
                Setting<?> setting = selectedModule.getSettings().get(row);
                if (setting instanceof BooleanSetting) {
                    ((BooleanSetting) setting).toggle();
                } else if (setting instanceof NumberSetting) {
                    NumberSetting number = (NumberSetting) setting;
                    if (mouseX < x + 80) number.decrease(); else number.increase();
                }
                return;
            }
        }

        int panelWidth = getPanelWidth();
        int maxRows = getMaxVisibleRows();
        for (Category category : Category.values()) {
            int x = getCategoryX(category);
            int y = getCategoryY(category);
            if (mouseX < x || mouseX > x + panelWidth
                    || mouseY < y + HEADER_HEIGHT || mouseY >= y + getPanelHeight()) continue;

            List<Module> modules = HaruClient.getInstance().getModuleManager().getModules(category);
            int row = (mouseY - y - HEADER_HEIGHT) / ROW_HEIGHT + scroll.get(category);
            if (row >= 0 && row < modules.size() && row < scroll.get(category) + maxRows) {
                Module module = modules.get(row);
                if (mouseButton == 0) module.toggle();
                else if (mouseButton == 1) {
                    selectedModule = selectedModule == module ? null : module;
                    settingsScroll = 0;
                }
            }
            break;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel == 0 || mc == null) return;

        int mx = Mouse.getEventX() * width / mc.displayWidth;
        int my = height - Mouse.getEventY() * height / mc.displayHeight - 1;

        if (selectedModule != null && mx >= getSettingsX() && mx <= getSettingsX() + 158
                && my >= TOP) {
            int max = Math.max(0, selectedModule.getSettings().size() - getSettingsVisibleRows());
            settingsScroll = Math.max(0, Math.min(max, settingsScroll + (wheel < 0 ? 1 : -1)));
            return;
        }

        for (Category category : Category.values()) {
            int x = getCategoryX(category);
            int y = getCategoryY(category);
            if (mx >= x && mx <= x + getPanelWidth()
                    && my >= y + HEADER_HEIGHT && my <= y + getPanelHeight()) {
                int max = Math.max(0, HaruClient.getInstance().getModuleManager()
                        .getModules(category).size() - getMaxVisibleRows());
                int next = scroll.get(category) + (wheel < 0 ? 1 : -1);
                scroll.put(category, Math.max(0, Math.min(max, next)));
                break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
