package org.millenaire.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class EntityAIGateOpen extends Goal {
    private final LivingEntity theEntity;
    private BlockPos gatePosition = BlockPos.ZERO;
    private FenceGateBlock gateBlock;
    private boolean hasStoppedDoorInteraction;
    private final boolean closeGate;
    private int closeGateTemporisation;

    public EntityAIGateOpen(LivingEntity entityIn, boolean shouldClose) {
        this.theEntity = entityIn;
        this.closeGate = shouldClose;
    }

    @Override
    public boolean canUse() {
        if (!this.theEntity.horizontalCollision) {
            return false;
        } else {
            Path path = this.theEntity.getNavigation().getPath();
            
            if (path != null && !path.isDone()) {
                for (int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                    Node node = path.getNode(i);
                    this.gatePosition = new BlockPos(node.x, node.y, node.z);

                    if (this.theEntity.distanceToSqr(this.gatePosition.getX(), this.theEntity.getY(), this.gatePosition.getZ()) <= 2.25D) {
                        this.gateBlock = this.getBlockGate(this.gatePosition);
                        if (this.gateBlock != null) {
                            return true;
                        }
                    }
                }

                this.gatePosition = this.theEntity.blockPosition().above();
                this.gateBlock = this.getBlockGate(this.gatePosition);
                return this.gateBlock != null;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.closeGate && this.closeGateTemporisation > 0 && this.canUse();
    }

    @Override
    public void start() {
        this.closeGateTemporisation = 20;
        this.toggleGate(this.theEntity.level(), this.gatePosition, true);
    }

    @Override
    public void tick() {
        --this.closeGateTemporisation;
        
        // Additional logic for determining when to stop interaction
    }
    
    @Override
    public void stop() {
        if (this.closeGate) {
            this.toggleGate(this.theEntity.level(), this.gatePosition, false);
        }
    }

    private FenceGateBlock getBlockGate(BlockPos pos) {
        BlockState state = this.theEntity.level().getBlockState(pos);
        return state.getBlock() instanceof FenceGateBlock ? (FenceGateBlock) state.getBlock() : null;
    }
    
    private void toggleGate(Level worldIn, BlockPos pos, boolean open) {
        if (gateBlock == null) {
            return;
        }
        
        BlockState state = worldIn.getBlockState(pos);
        if (!(state.getBlock() instanceof FenceGateBlock)) {
            return;
        }
        
        if (open) {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.OPEN, true), 3);
        } else {
            worldIn.setBlock(pos, state.setValue(BlockStateProperties.OPEN, false), 3);
        }
    }
}