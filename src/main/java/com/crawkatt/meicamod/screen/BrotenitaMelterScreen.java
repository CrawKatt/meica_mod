package com.crawkatt.meicamod.screen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.screen.renderer.IronInfoArea;
import com.crawkatt.meicamod.screen.renderer.FluidStackRenderer;
import com.crawkatt.meicamod.util.MouseUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class BrotenitaMelterScreen extends HandledScreen<BrotenitaMelterScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(MeicaMod.MOD_ID, "textures/gui/brotenita_melter.png");

    private IronInfoArea ironInfoArea;
    private FluidStackRenderer fluidStackRenderer;

    public BrotenitaMelterScreen(BrotenitaMelterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleY = 1000;
        playerInventoryTitleY = 1000;

        assignIronInfoArea();
        assignFluidStackRenderer();
    }

    private void assignIronInfoArea() {
        this.ironInfoArea = new IronInfoArea(
                ((width - backgroundWidth) / 2) + 26,
                ((height - backgroundHeight) / 2) + 11,
                handler.blockEntity.ironStorage
        );
    }

    private void assignFluidStackRenderer() {
        fluidStackRenderer = new FluidStackRenderer(
                (FluidConstants.BUCKET / 81) * 64,
                true,
                8,
                64
        );
    }

    private void renderIronAreaTooltips(DrawContext context, int pMouseX, int pMouseY, int x, int y) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, 26, 6, 16, 39)) {
            context.drawTooltip(
                    Screens.getTextRenderer(this),
                    ironInfoArea.getTooltips(),
                    Optional.empty(),
                    pMouseX - x,
                    pMouseY - y
            );
        }
    }

    private void renderFluidTooltip(DrawContext context, int pMouseX, int pMouseY, int x, int y,
                                    int offsetX, int offsetY, FluidStackRenderer renderer) {
        if (isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, renderer)) {
            context.drawTooltip(
                    Screens.getTextRenderer(this),
                    renderer.getTooltip(handler.blockEntity.fluidStorage, TooltipContext.Default.BASIC),
                    Optional.empty(),
                    pMouseX - x,
                    pMouseY - y
            );
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderIronAreaTooltips(context, mouseX, mouseY, x, y);
        renderFluidTooltip(context, mouseX, mouseY, x, y, 156, 11, fluidStackRenderer);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
        renderProgressArrow(context, x, y);
        ironInfoArea.draw(context);
        fluidStackRenderer.drawFluid(
                context,
                handler.blockEntity.fluidStorage,
                x + 156,
                y + 11,
                8,
                64,
                (FluidConstants.BUCKET / 81) * 64
        );
    }

    private void renderProgressArrow(DrawContext context, int x, int y) {
        if (handler.isCrafting()) {
            context.drawTexture(TEXTURE, x + 85, y + 30, 176, 0, 8, handler.getScaledProgress());
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y,
                                     int offsetX, int offsetY, FluidStackRenderer renderer) {
        return MouseUtil.isMouseOver(
                pMouseX, pMouseY,
                x + offsetX, y + offsetY,
                renderer.getWidth(),
                renderer.getHeight()
        );
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y,
                                     int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(
                pMouseX, pMouseY,
                x + offsetX, y + offsetY,
                width, height
        );
    }
}