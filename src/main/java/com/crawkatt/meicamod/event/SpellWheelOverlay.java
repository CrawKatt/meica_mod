package com.crawkatt.meicamod.event;

import com.crawkatt.meicamod.MeicaMod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import org.joml.Vector4f;

import java.util.List;

public class SpellWheelOverlay implements HudRenderCallback {
    public static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/item/brotenita.png");

    private final Vector4f lineColor = new Vector4f(1f, 0.85f, 0.7f, 1f);
    private final Vector4f radialButtonColor = new Vector4f(0.04f, 0.03f, 0.01f, 0.6f);
    private final Vector4f highlightColor = new Vector4f(0.8f, 0.7f, 0.55f, 0.7f);

    private final double ringInnerEdge = 20;
    private double ringOuterEdge = 80;
    private final double ringOuterEdgeMax = 80;
    private final double ringOuterEdgeMin = 65;

    public static boolean active;
    private static int wheelSelection;

    private static class MockSpell {
        final Identifier icon;
        final String name;
        final List<String> description;

        MockSpell(String iconPath, String name, List<String> description) {
            this.icon = new Identifier(MeicaMod.MOD_ID, iconPath);
            this.name = name;
            this.description = description;
        }
    }

    private static final List<MockSpell> mockSpells = List.of(
            new MockSpell("textures/item/brotenita.png", "Fuego", List.of("Quema a los enemigos.")),
            new MockSpell("textures/item/brotenita.png", "Hielo", List.of("Congela al objetivo.")),
            new MockSpell("textures/item/brotenita.png", "Rayo", List.of("Electrocuta rápidamente.")),
            new MockSpell("textures/item/brotenita.png", "Luz", List.of("Dispara proyectiles de luz al colisionar la flecha con un objetivo."))
    );

    //private SpellSelectionManager swsm;

    public static void open() {
        active = true;
        wheelSelection = -1;
        MinecraftClient.getInstance().mouse.unlockCursor();
    }

    public void close() {
        active = false;
        MinecraftClient.getInstance().mouse.lockCursor();

        /*
        if (wheelSelection >= 0) {
            swsm.makeSelection(wheelSelection);
        }
        */
    }

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        render(drawContext);
    }

    private void render(DrawContext drawContext) {
        if (!active)
            return;

        MinecraftClient client = MinecraftClient.getInstance();
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();
        PlayerEntity player = client.player;

        if (player == null || client.currentScreen != null || client.mouse.isCursorLocked()) {
            close();
            return;
        }

        //swsm = ClientMagicData.getSpellSelectionManager();
        //int totalSpellsAvailable = swsm.getSpellCount();
        int totalSpellsAvailable = mockSpells.size();

        if (totalSpellsAvailable <= 0) {
            close();
            return;
        }

        MatrixStack poseStack = drawContext.getMatrices();
        poseStack.push();

        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        Vec2f screenCenter = new Vec2f(client.getWindow().getScaledWidth() * 0.5f, client.getWindow().getScaledHeight() * 0.5f);
        Vec2f mousePos = new Vec2f((float) client.mouse.getX() * client.getWindow().getScaledWidth() / (float) client.getWindow().getWidth(),
                (float) client.mouse.getY() * client.getWindow().getScaledHeight() / (float) client.getWindow().getHeight());
        //System.out.println("Mouse X: " + mousePos.x + ", Y: " + mousePos.y);

        double radiansPerSpell = Math.toRadians(360 / (float) totalSpellsAvailable);
        float mouseRotation = (getAngle(mousePos, screenCenter) + 1.570f + (float) radiansPerSpell * .5f) % MathHelper.TAU;
        wheelSelection = (int) MathHelper.clamp(mouseRotation / radiansPerSpell, 0, totalSpellsAvailable - 1);
        if (mousePos.distanceSquared(screenCenter) < ringOuterEdgeMin * ringOuterEdgeMin) {
            //wheelSelection = Math.max(0, swsm.getSelectionIndex());
            wheelSelection = 0;
        }

        drawContext.fill(0, 0, screenWidth, screenHeight, 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        drawRadialBackgrounds(buffer, centerX, centerY, wheelSelection);
        drawDivingLines(buffer, centerX, centerY, totalSpellsAvailable);

        tessellator.draw();
        RenderSystem.disableBlend();

        // Text background
        //var selectedSpell = swsm.getSpellData(wheelSelection);
        MockSpell selectedSpell = mockSpells.get(wheelSelection);
        var font = client.textRenderer;
        //var info = selectedSpell.getSpell().getUniqueInfo(client.player);
        int textHeight = Math.max(2, selectedSpell.description.size()) * font.fontHeight + 5;
        int textCenterMargin = 5;
        int textTitleMargin = 5;
        //String title = selectedSpell.getSpell().getDisplayName(client.player).getString();

        //drawTextBackground(drawContext, centerX, centerY, ringOuterEdge + textHeight - textTitleMargin - font.fontHeight, textCenterMargin, Math.max(2, info.size()) * font.fontHeight);
        drawTextBackground(drawContext, centerX, centerY, ringOuterEdge + textHeight - textTitleMargin - font.fontHeight, textCenterMargin, textHeight);
        drawContext.drawText(font, selectedSpell.name, (int) (centerX - font.getWidth(selectedSpell.name) / 2), (int) (centerY - (ringOuterEdge + textHeight)), 0xFFFFFF, true);

        for (int i = 0; i < selectedSpell.description.size(); i++) {
            //String line = info.get(i);
            drawContext.drawText(font, selectedSpell.description.get(i), (int) (centerX + textCenterMargin), (int) (centerY - (ringOuterEdgeMax + textHeight) + font.fontHeight * (i + 1) + textTitleMargin), 0x3be33b, true);
        }

        // Spell Icons
        float scale = MathHelper.lerp(totalSpellsAvailable / 15f, 2, 1.25f) * 0.65f;
        double radius = 3 / scale * (ringInnerEdge + ringInnerEdge) * 0.5 * (0.85f + 0.25f * (totalSpellsAvailable / 15f));
        Vec2f[] locations = new Vec2f[totalSpellsAvailable];
        for (int i = 0; i < locations.length; i++) {
            locations[i] = new Vec2f((float) (Math.sin(radiansPerSpell * i) * radius), (float) (-Math.cos(radiansPerSpell * i) * radius));
        }

        for (int i = 0; i < locations.length; i++) {
            //var spell = swsm.getSpellData(i);
            MockSpell spell = mockSpells.get(i);
            if (spell != null) {
                //Identifier texture = spell.getSpell().getSpellIconResource();
                poseStack.push();
                poseStack.translate(centerX, centerY, 0);
                poseStack.scale(scale, scale, scale);

                int iconWidth = 16 / 2;
                int borderWidth = 32 / 2;
                //int cdWidth = 16 / 2;

                drawContext.drawTexture(spell.icon, (int) locations[i].x - iconWidth, (int) locations[i].y - iconWidth, 0, 0, 16, 16, 16, 16);
                drawContext.drawTexture(TEXTURE, (int) locations[i].x - borderWidth, (int) locations[i].y - borderWidth, wheelSelection == i ? 32 : 0, 106, 32, 32);

                /*
                float f = ClientMagicData.getCooldownPercent(spell.getSpell());
                if (f > 0) {
                    RenderSystem.enableBlend();
                    int pixels = (int) (16 * f + 1f);
                    drawContext.drawTexture(TEXTURE, (int) locations[i].x - cdWidth, (int) locations[i].y + cdWidth - pixels, 47, 87, 16, pixels);
                }
                */
                poseStack.pop();
            }
        }

        poseStack.pop();
    }

    private void drawTextBackground(DrawContext drawContext, double centerX, double centerY, double textYOffset, int textCenterMargin, int textHeight) {
        drawContext.fill(0, 0, (int) (centerX * 2), (int) (centerY * 2), 0);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        centerY = centerY - textYOffset - 2;
        int heightMax = textHeight / 2 + 4;

        int widthMin = -1;
        int widthMax = 1;
        buffer.vertex(centerX + widthMin, centerY, 0).color(radialButtonColor.x(), radialButtonColor.y(), radialButtonColor.z(), 0).next();
        buffer.vertex(centerX + widthMin, centerY + heightMax, 0).color(radialButtonColor.x(), radialButtonColor.y(), radialButtonColor.z(), radialButtonColor.w()).next();
        buffer.vertex(centerX + widthMax, centerY + heightMax, 0).color(radialButtonColor.x(), radialButtonColor.y(), radialButtonColor.z(), radialButtonColor.w()).next();
        buffer.vertex(centerX + widthMax, centerY, 0).color(radialButtonColor.x(), radialButtonColor.y(), radialButtonColor.z(), 0).next();

        tessellator.draw();
        RenderSystem.disableBlend();
    }

    private void drawRadialBackgrounds(BufferBuilder buffer, double centerX, double centerY, int selectedSpellIndex) {
        double quarterCircle = Math.PI / 2;
        //totalSpellsAvailable = swsm.getSpellCount();
        int totalSpellsAvailable = mockSpells.size();
        int segments;
        if (totalSpellsAvailable < 6) {
            segments = totalSpellsAvailable % 2 == 1 ? 15 : 12;
        } else {
            segments = totalSpellsAvailable * 2;
        }

        double radiansPerObject = 2 * Math.PI / segments;
        double radiansPerSpell = 2 * Math.PI / totalSpellsAvailable;
        ringOuterEdge = Math.max(ringOuterEdgeMin, ringOuterEdgeMax);
        for (int i = 0; i < segments; i++) {
            final double beginRadians = i * radiansPerObject - (quarterCircle + (radiansPerSpell / 2));
            final double endRadians = (i + 1) * radiansPerObject - (quarterCircle + (radiansPerSpell / 2));

            final double x1m1 = Math.cos(beginRadians) * ringInnerEdge;
            final double x2m1 = Math.cos(endRadians) * ringInnerEdge;
            final double y1m1 = Math.sin(beginRadians) * ringInnerEdge;
            final double y2m1 = Math.sin(endRadians) * ringInnerEdge;

            final double x1m2 = Math.cos(beginRadians) * ringOuterEdge;
            final double x2m2 = Math.cos(endRadians) * ringOuterEdge;
            final double y1m2 = Math.sin(beginRadians) * ringOuterEdge;
            final double y2m2 = Math.sin(endRadians) * ringOuterEdge;

            boolean isHighlighted = (i * totalSpellsAvailable) / segments == selectedSpellIndex;

            Vector4f color = radialButtonColor;
            if (isHighlighted) color = highlightColor;

            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(color.x(), color.y(), color.z(), 0).next();
            buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(color.x(), color.y(), color.z(), 0).next();

            // Category Line
            color = lineColor;
            double categoryLineWidth = 2;
            final double categoryLineOuterEdge = ringInnerEdge + categoryLineWidth;

            final double x1m3 = Math.cos(beginRadians) * categoryLineOuterEdge;
            final double x2m3 = Math.cos(endRadians) * categoryLineOuterEdge;
            final double y1m3 = Math.sin(beginRadians) * categoryLineOuterEdge;
            final double y2m3 = Math.sin(endRadians) * categoryLineOuterEdge;

            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m3, centerY + y2m3, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x1m3, centerY + y1m3, 0).color(color.x(), color.y(), color.z(), color.w()).next();
        }
    }

    private void drawDivingLines(BufferBuilder buffer, double centerX, double centerY, int totalSpellsAvailable) {
        //int totalSpellsAvailable = swsm.getSpellCount();

        if (totalSpellsAvailable <= 1)
            return;

        double quarterCircle = Math.PI / 2;
        double radiansPerSpell = 2 * Math.PI / totalSpellsAvailable;
        ringOuterEdge = Math.max(ringOuterEdgeMin, ringOuterEdgeMax);

        for (int i = 0; i < totalSpellsAvailable; i++) {
            final double closeWidth = 8 * MathHelper.RADIANS_PER_DEGREE;
            final double farWidth = closeWidth / 4;
            final double beginCloseRadians = i * radiansPerSpell - (quarterCircle + (radiansPerSpell / 2)) - (closeWidth / 4);
            final double endCloseRadians = beginCloseRadians + closeWidth;
            final double beginFarRadians = i * radiansPerSpell - (quarterCircle + (radiansPerSpell / 2)) - (farWidth / 4);
            final double endFarRadians = beginCloseRadians + farWidth;

            final double x1m1 = Math.cos(beginCloseRadians) * ringInnerEdge;
            final double x2m1 = Math.cos(endCloseRadians) * ringInnerEdge;
            final double y1m1 = Math.sin(beginCloseRadians) * ringInnerEdge;
            final double y2m1 = Math.sin(endCloseRadians) * ringInnerEdge;

            final double x1m2 = Math.cos(beginFarRadians) * ringOuterEdge * 1.4;
            final double x2m2 = Math.cos(endFarRadians) * ringOuterEdge * 1.4;
            final double y1m2 = Math.sin(beginFarRadians) * ringOuterEdge * 1.4;
            final double y2m2 = Math.sin(endFarRadians) * ringOuterEdge * 1.4;

            Vector4f color = lineColor;
            buffer.vertex(centerX + x1m1, centerY + y1m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m1, centerY + y2m1, 0).color(color.x(), color.y(), color.z(), color.w()).next();
            buffer.vertex(centerX + x2m2, centerY + y2m2, 0).color(color.x(), color.y(), color.z(), 0).next();
            buffer.vertex(centerX + x1m2, centerY + y1m2, 0).color(color.x(), color.y(), color.z(), 0).next();
        }
    }

    private void setOpaqueTexture(Identifier texture) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    private void setTranslucentTexture(Identifier texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, texture);
    }

    private boolean isTriangle(final double x1, final double y1, final double x2, final double y2,
                               final double x3, final double y3, final double x, final double y) {
        final double ab = (x1 - x) * (y2 - y) - (x2 - x) * (y1 - y);
        final double bc = (x2 - x) * (y3 - y) - (x3 - x) * (y2 - y);
        final double ca = (x3 - x) * (y1 - y) - (x1 - x) * (y3 - y);
        return sign(ab) == sign(bc) && sign(bc) == sign(ca);
    }

    private int sign(final double n) {
        return n > 0 ? 1 : -1;
    }

    public static float getAngle(Vec2f a, Vec2f b) {
        return getAngle(a.x, a.y, b.x, b.y);
    }

    public static float getAngle(double ax, double ay, double bx, double by) {
        return (float) (Math.atan2(by - ay, bx - ax)) + 3.141f; // + (a.x > b.x ? Math.PI : 0));
    }
}
