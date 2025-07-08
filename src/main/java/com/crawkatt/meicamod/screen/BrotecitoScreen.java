package com.crawkatt.meicamod.screen;

import com.crawkatt.meicamod.MeicaMod;
import com.crawkatt.meicamod.entity.custom.BrotecitoEntity;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BrotecitoScreen extends HandledScreen<BrotecitoScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(MeicaMod.MOD_ID, "textures/gui/brotecito_inventory.png");
    private final BrotecitoEntity entity;
    private float mouseX;
    private float mouseY;

    public BrotecitoScreen(BrotecitoScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.entity = handler.entity;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        InventoryScreen.drawEntity(context,
                x + 85,
                y + 70,
                30,
                (float)(x + 85) - this.mouseX,
                (float)(y + 60 - 50) - this.mouseY,
                this.entity
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        this.mouseX = (float)mouseX;
        this.mouseY = (float)mouseY;
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
