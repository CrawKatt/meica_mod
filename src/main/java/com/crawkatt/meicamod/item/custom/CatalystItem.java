package com.crawkatt.meicamod.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;

public class CatalystItem extends Item {
    public CatalystItem() {
        super(new Item.Settings()
                .maxCount(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public @NotNull ActionResult useOnBlock(ItemUsageContext context) {
        return super.useOnBlock(context);
        /*
        if (context.getPlayer() != null) {
            if (context.getPlayer().getWorld().getDimension() == ModDimensions.MEICADIM_LEVEL_KEY || context.getPlayer().getWorld().getDimension() == World.OVERWORLD) {
                for (Direction direction :Direction.Type.VERTICAL) {
                    BlockPos framePos = context.getBlockPos().offset(direction);
                    if (((MeicaPortalBlock) ModBlocks.MEICA_PORTAL).trySpawnPortal(pContext.getLevel(), framePos)) {
                        context.getWorld().playSound(context.getPlayer(), framePos, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        return ActionResult.CONSUME;
                    } else return ActionResult.FAIL;
                }
            }
        }
        */
        //return ActionResult.FAIL;
    }
}
