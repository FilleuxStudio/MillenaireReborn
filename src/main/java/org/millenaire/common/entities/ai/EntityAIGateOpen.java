package org.millenaire.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EntityAIGateOpen extends OpenDoorGoal {
    private final Mob mob;
    
    public EntityAIGateOpen(Mob mob, boolean closeAfter) {
        super(mob, closeAfter);
        this.mob = mob;
    }

    private boolean findAndSetGate() {
        BlockPos mobPos = this.mob.blockPosition();
        
        for (BlockPos checkPos : BlockPos.betweenClosed(mobPos.offset(-1, 0, -1), mobPos.offset(1, 1, 1))) {
            BlockState state = this.mob.level().getBlockState(checkPos);
            if (isValidGate(state)) {
                return tryOpenGate(checkPos, state);
            }
        }
        return false;
    }

    private boolean isValidGate(BlockState state) {
        return state.getBlock() instanceof FenceGateBlock;
    }

    private boolean tryOpenGate(BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.OPEN)) {
            this.mob.level().setBlock(pos, state.setValue(BlockStateProperties.OPEN, true), 3);
            return true;
        }
        return false;
    }
}