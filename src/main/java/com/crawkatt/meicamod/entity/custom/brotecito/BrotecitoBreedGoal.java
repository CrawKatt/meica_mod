package com.crawkatt.meicamod.entity.custom.brotecito;

import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;

public class BrotecitoBreedGoal extends BreedGoal {
    public BrotecitoBreedGoal(Animal pAnimal, double pSpeedModifier) {
        super(pAnimal, pSpeedModifier);
    }

    @Override
    protected void breed() {
        if (this.partner != null) {
            if (this.animal.getRandom().nextBoolean()) {
                this.animal.spawnAtLocation(new ItemStack(ModItems.BROTECITO_SEEDS.get()));
            } else {
                this.partner.spawnAtLocation(new ItemStack(ModItems.BROTECITO_SEEDS.get()));
            }

            this.animal.setAge(6000);
            this.partner.setAge(6000);
            this.animal.resetLove();
            this.partner.resetLove();

            this.level.broadcastEntityEvent(this.animal, (byte) 18);
        }
    }
}
