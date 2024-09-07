package com.crawkatt.meicamod.item.custom;

import com.crawkatt.meicamod.block.ModBlocks;
import com.crawkatt.meicamod.block.portal.MeicaPortalBlock;
import com.crawkatt.meicamod.worldgen.dimension.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CatalystItem extends Item {
    public CatalystItem() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.RARE));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getPlayer() != null) {
            if (pContext.getPlayer().level().dimension() == ModDimensions.MEICADIM_LEVEL_KEY || pContext.getPlayer().level().dimension() == Level.OVERWORLD) {
                for (Direction direction : Direction.Plane.VERTICAL) {
                    BlockPos framePos = pContext.getClickedPos().relative(direction);
                    if (((MeicaPortalBlock) ModBlocks.MEICA_PORTAL.get()).trySpawnPortal(pContext.getLevel(), framePos)) {
                        pContext.getLevel().playSound(pContext.getPlayer(), framePos, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        return InteractionResult.CONSUME;
                    } else return InteractionResult.FAIL;
                }
            }
        }
        return InteractionResult.FAIL;
    }
}
