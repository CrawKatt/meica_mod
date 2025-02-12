package com.crawkatt.meicamod.particle.custom;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class KappaPrideParticles extends SpriteBillboardParticle {
    protected KappaPrideParticles(ClientWorld world, double xCoord, double yCoord, double zCoord,
                                  SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(world, xCoord, yCoord, zCoord, xd, yd, zd);

        this.velocityMultiplier = 0.8F;
        this.velocityX = xd;
        this.velocityY = yd;
        this.velocityZ = zd;
        this.scale *= 0.85F;
        this.maxAge = 20;
        this.setSpriteForAge(spriteSet);

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public float getSize(float scaleFactor) {
        return super.getSize(scaleFactor) * 2.0F;
    }

    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }

    private void fadeOut() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider sprites;

        public Provider(SpriteProvider spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(DefaultParticleType particleType, ClientWorld world, double x, double y, double z,
                                       double dx, double dy, double dz) {
            KappaPrideParticles particle = new KappaPrideParticles(world, x, y, z, sprites, dx, dy, dz);
            particle.setSprite(this.sprites);
            return particle;
        }
    }
}
