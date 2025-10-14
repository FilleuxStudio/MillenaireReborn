package org.millenaire.common.entities.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.millenaire.common.entities.EntityMillVillager;

public class EntityAIBuild extends Goal {
    private final LivingEntity theEntity;
    
    public EntityAIBuild(LivingEntity entityIn) {
        this.theEntity = entityIn;

        if (!(entityIn instanceof EntityMillVillager)) {
            throw new IllegalArgumentException("Unsupported mob type for BuildGoal");
        } else if (!((EntityMillVillager)entityIn).getVillagerType().canBuild) {
            throw new IllegalArgumentException("Villager does not support BuildGoal");
        }
    }

    @Override
    public boolean canUse() { 
        return false; 
    }

    @Override
    public boolean canContinueToUse() { 
        return false; 
    }
    
    @Override
    public void start() {
        // Start building task
    }
    
    @Override
    public void tick() {
        // Update building task
    }
    
    @Override
    public void stop() {
        // Reset building task
    }
}