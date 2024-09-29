package com.crawkatt.meicamod.screen.renderer;

import com.crawkatt.meicamod.util.ModIronStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class IronMeltDisplayTooltipArea {
    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;
    private final ModIronStorage ironStorage;

    public IronMeltDisplayTooltipArea(int xMin, int yMin, ModIronStorage ironStorage) {
        this(xMin, yMin, ironStorage, 8, 64);
    }

    public IronMeltDisplayTooltipArea(int xMin, int yMin, ModIronStorage ironStorage, int width, int height) {
        xPos = xMin;
        yPos = yMin;
        this.width = width;
        this.height = height;
        this.ironStorage = ironStorage;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(ironStorage.getIronStored() + "/" + ironStorage.getCapacity() + " Iron Units"));
    }

    public void render(GuiGraphics guiGraphics) {
        int stored = (int)(height * (ironStorage.getIronStored() / (float)ironStorage.getCapacity()));
        guiGraphics.fillGradient(xPos, yPos + (height - stored), xPos + width,
                yPos + height, 0xFFFFD700, 0xFFFFA500);
    }
}
