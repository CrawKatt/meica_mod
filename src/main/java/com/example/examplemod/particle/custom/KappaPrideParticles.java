package com.example.examplemod.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class KappaPrideParticles extends TextureSheetParticle {
    protected KappaPrideParticles(ClientLevel level, double xCoord, double yCoord, double zCoord,
                                  double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
    }

     @Override
     public float getQuadSize(float scaleFactor) {
        return super.getQuadSize(scaleFactor) * 2.0F;
     }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)lifetime) * age + 1);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel world,
                double x,
                double y,
                double z,
                double motionX,
                double motionY,
                double motionZ
        ) {
            KappaPrideParticles particle = new KappaPrideParticles(world, x, y, z, motionX, motionY, motionZ);
            particle.pickSprite(this.spriteSet);
            return particle;
        }
    }
}