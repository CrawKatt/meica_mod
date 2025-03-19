package com.crawkatt.meicamod.mixin;

import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CustomPortalBlock.class)
public abstract class CustomPortalBlockMixin {
    @Redirect(
            method = "randomDisplayTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            )
    )
    private void redirectParticles(World world, ParticleEffect effect, double x, double y, double z, double dx, double dy, double dz) {
        if (effect instanceof BlockStateParticleEffect) {
            world.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, x, y, z, dx, dy, dz);
        }
    }
}