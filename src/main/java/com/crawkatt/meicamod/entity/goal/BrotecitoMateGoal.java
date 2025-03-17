package com.crawkatt.meicamod.entity.goal;

import com.crawkatt.meicamod.item.ModItems;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemStack;

public class BrotecitoMateGoal extends AnimalMateGoal {
    public BrotecitoMateGoal(AnimalEntity animal, double speed) {
        super(animal, speed);
    }

    @Override
    protected void breed() {
        if (this.animal != null && this.mate != null) {
            if (this.animal.getRandom().nextBoolean()) {
                this.animal.dropStack(new ItemStack(ModItems.BROTECITO_SEEDS));
            } else {
                this.mate.dropStack(new ItemStack(ModItems.BROTECITO_SEEDS));
            }

            this.animal.setBreedingAge(6000);
            this.mate.setBreedingAge(6000);
            this.animal.resetLoveTicks();
            this.mate.resetLoveTicks();

            this.world.sendEntityStatus(this.animal, (byte) 18);
        }
    }
}
