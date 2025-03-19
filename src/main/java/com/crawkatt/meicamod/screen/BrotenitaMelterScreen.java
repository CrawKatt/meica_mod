package com.crawkatt.meicamod.screen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.screen.renderer.IronMeltDisplayTooltipArea;
import com.crawkatt.meicamod.screen.renderer.FluidTankRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BrotenitaMelterScreen extends AbstractContainerScreen<BrotenitaMelterMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(MeicaMod.MODID, "textures/gui/brotenita_melter.png");
    private IronMeltDisplayTooltipArea ironMeltInfoArea;
    private FluidTankRenderer fluidRenderer;

    public BrotenitaMelterScreen(BrotenitaMelterMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;

        assignIronInfoArea();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        fluidRenderer = new FluidTankRenderer(64000, true, 8, 64);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        // Convertir coordenadas absolutas a relativas
        double adjustedMouseX = pMouseX - leftPos;
        double adjustedMouseY = pMouseY - topPos;

        // Tooltip hierro
        if (isMouseOverIronArea(pMouseX, pMouseY)) {
            guiGraphics.renderTooltip(
                    this.font,
                    ironMeltInfoArea.getTooltips(),
                    Optional.empty(),
                    (int) adjustedMouseX,
                    (int) adjustedMouseY
            );
        }

        // Tooltip fluido
        if (isMouseOverFluidArea(pMouseX, pMouseY)) {
            guiGraphics.renderTooltip(
                    this.font,
                    fluidRenderer.getTooltip(
                            menu.blockEntity.getFluid(),
                            TooltipFlag.Default.NORMAL
                    ),
                    Optional.empty(),
                    (int) adjustedMouseX,
                    (int) adjustedMouseY
            );
        }
    }

    private void assignIronInfoArea() {
        ironMeltInfoArea = new IronMeltDisplayTooltipArea(leftPos + 26, topPos + 11, menu.blockEntity.getIronStorage(), 16, 39);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics);

        // Render fluid
        fluidRenderer.render(guiGraphics,
                leftPos + 156,
                topPos + 11,
                menu.blockEntity.getFluid()
        );

        // Render iron
        ironMeltInfoArea.render(guiGraphics);
    }


    private void renderProgressArrow(GuiGraphics guiGraphics) {
        if (menu.isCrafting()) {
            guiGraphics.blit(TEXTURE,
                    leftPos + 85,
                    topPos + 30,
                    176,
                    0,
                    8,
                    menu.getScaledProgress()
            );
        }
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float delta) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, delta);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private boolean isMouseOverIronArea(double pMouseX, double pMouseY) {
        int x = leftPos + 26;
        int y = topPos + 11;
        int width = 16;
        int height = 39;

        return (pMouseX >= x && pMouseX <= x + width) &&
                (pMouseY >= y && pMouseY <= y + height);
    }

    private boolean isMouseOverFluidArea(double pMouseX, double pMouseY) {
        int x = leftPos + 156;
        int y = topPos + 11;
        int width = 8;
        int height = 64;

        return (pMouseX >= x && pMouseX <= x + width) &&
                (pMouseY >= y && pMouseY <= y + height);
    }
}
