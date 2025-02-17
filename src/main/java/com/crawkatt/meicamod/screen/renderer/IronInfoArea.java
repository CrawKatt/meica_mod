package com.crawkatt.meicamod.screen.renderer;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

public class IronInfoArea {
    private final Rect2i area;
    private final EnergyStorage iron;

    public IronInfoArea(int xMin, int yMin) {
        this(xMin, yMin, null, 16, 39);
    }

    public IronInfoArea(int xMin, int yMin, EnergyStorage iron) {
        this(xMin, yMin, iron, 16, 39);
    }

    public IronInfoArea(int xMin, int yMin, EnergyStorage iron, int width, int height) {
        area = new Rect2i(xMin, yMin, width, height);
        this.iron = iron;
    }

    public List<Text> getTooltips() {
        return List.of(Text.literal(iron.getAmount() + "/" + iron.getCapacity() + "E"));
    }

    public void draw(DrawContext context) {
        final int height = area.getHeight();
        int stored = (int)(height*(iron.getAmount()/(float) iron.getCapacity()));
        context.fillGradient(
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() + area.getHeight(),
                0xFFFFD700, 0xFFFFA500
        );
    }
}
