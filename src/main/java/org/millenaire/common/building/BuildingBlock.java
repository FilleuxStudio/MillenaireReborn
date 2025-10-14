package org.millenaire.common.building;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.millenaire.common.CommonUtilities;
import org.millenaire.common.items.MillItems;

import java.util.List;
import java.util.Optional;

public class BuildingBlock {
    public static final byte OAKSPAWN = 1;
    public static final byte SPRUCESPAWN = 2;
    public static final byte BIRCHSPAWN = 3;
    public static final byte JUNGLESPAWN = 4;
    public static final byte ACACIASPAWN = 5;
    public static final byte PRESERVEGROUNDDEPTH = 6;
    public static final byte PRESERVEGROUNDSURFACE = 7;
    public static final byte CLEARTREE = 8;
    public static final byte CLEARGROUND = 9;
    public static final byte SPAWNERSKELETON = 10;
    public static final byte SPAWNERZOMBIE = 11;
    public static final byte SPAWNERSPIDER = 12;
    public static final byte SPAWNERCAVESPIDER = 13;
    public static final byte SPAWNERCREEPER = 14;
    public static final byte SPAWNERBLAZE = 15;
    public static final byte DISPENDERUNKNOWNPOWDER = 16;
    public static final byte TAPESTRY = 17;
    public static final byte BYZANTINEICONSMALL = 18;
    public static final byte BYZANTINEICONMEDIUM = 19;
    public static final byte BYZANTINEICONLARGE = 20;
    public static final byte INDIANSTATUE = 21;
    public static final byte MAYANSTATUE = 22;
    
    public BlockState blockState;
    public BlockPos position;
    public byte specialBlock;
    
    public BuildingBlock(BlockState state, BlockPos pos, byte special) {
        blockState = state;
        position = pos;
        specialBlock = special;
    }

    public BuildingBlock(BlockState state, BlockPos pos) {
        blockState = state;
        position = pos;
        specialBlock = 0;
    }
    
    public void build(Level worldIn, boolean onGeneration) {
        if (specialBlock != BuildingBlock.PRESERVEGROUNDDEPTH && 
            specialBlock != BuildingBlock.PRESERVEGROUNDSURFACE && 
            specialBlock != BuildingBlock.CLEARTREE) {
            
            worldIn.setBlock(position, blockState, 3);
            worldIn.playSound(null, position, blockState.getSoundType().getPlaceSound(), 
                SoundSource.BLOCKS, 0.3F, 0.6F);
        }
        
        if (specialBlock == BuildingBlock.PRESERVEGROUNDDEPTH || specialBlock == BuildingBlock.PRESERVEGROUNDSURFACE) {
            Block block = worldIn.getBlockState(position).getBlock();
            final boolean surface = specialBlock == BuildingBlock.PRESERVEGROUNDSURFACE;
            final Block validGroundBlock = CommonUtilities.getValidGroundBlock(block, surface);

            if (validGroundBlock == null) {
                BlockPos below = position.below();
                Block targetblock = null;
                while (targetblock == null && below.getY() > worldIn.getMinBuildHeight()) {
                    block = worldIn.getBlockState(below).getBlock();
                    if (CommonUtilities.getValidGroundBlock(block, surface) != null)
                        targetblock = CommonUtilities.getValidGroundBlock(block, surface);
                    below = below.below();
                }

                if (targetblock == Blocks.DIRT && onGeneration) {
                    targetblock = Blocks.GRASS_BLOCK;
                } else if (targetblock == Blocks.GRASS_BLOCK && !onGeneration) {
                    targetblock = Blocks.DIRT;
                }

                if (targetblock == Blocks.AIR) {
                    targetblock = onGeneration ? Blocks.GRASS_BLOCK : Blocks.DIRT;
                }

                if (targetblock != null) {
                    worldIn.setBlock(position, targetblock.defaultBlockState(), 3);
                    worldIn.playSound(null, position, targetblock.defaultBlockState().getSoundType().getPlaceSound(), 
                        SoundSource.BLOCKS, 0.3F, 0.6F);
                }
            } else if (onGeneration && validGroundBlock == Blocks.DIRT && worldIn.isEmptyBlock(position.above())) {
                worldIn.setBlock(position, Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                worldIn.playSound(null, position, Blocks.GRASS_BLOCK.defaultBlockState().getSoundType().getPlaceSound(), 
                    SoundSource.BLOCKS, 0.3F, 0.6F);
            } else if (validGroundBlock != block && !(validGroundBlock == Blocks.DIRT && block == Blocks.GRASS_BLOCK)) {
                worldIn.setBlock(position, validGroundBlock.defaultBlockState(), 3);
                worldIn.playSound(null, position, validGroundBlock.defaultBlockState().getSoundType().getPlaceSound(), 
                    SoundSource.BLOCKS, 0.3F, 0.6F);
            }
        } else if (specialBlock == BuildingBlock.CLEARTREE) {
            Block block = worldIn.getBlockState(position).getBlock();

            if (block == Blocks.OAK_LOG || block == Blocks.OAK_LEAVES || 
                block == Blocks.SPRUCE_LOG || block == Blocks.SPRUCE_LEAVES ||
                block == Blocks.BIRCH_LOG || block == Blocks.BIRCH_LEAVES ||
                block == Blocks.JUNGLE_LOG || block == Blocks.JUNGLE_LEAVES ||
                block == Blocks.ACACIA_LOG || block == Blocks.ACACIA_LEAVES ||
                block == Blocks.DARK_OAK_LOG || block == Blocks.DARK_OAK_LEAVES) {
                
                worldIn.removeBlock(position, false);
                worldIn.playSound(null, position, block.defaultBlockState().getSoundType().getBreakSound(), 
                    SoundSource.BLOCKS, 0.3F, 0.6F);

                final Block blockBelow = worldIn.getBlockState(position.below()).getBlock();
                final Block targetBlock = CommonUtilities.getValidGroundBlock(blockBelow, true);

                if (onGeneration && targetBlock == Blocks.DIRT) {
                    worldIn.setBlock(position.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
                } else if (targetBlock != null) {
                    worldIn.setBlock(position.below(), targetBlock.defaultBlockState(), 3);
                }
            }
        } else if (specialBlock == BuildingBlock.CLEARGROUND) {
            Block block = worldIn.getBlockState(position).getBlock();
            
            worldIn.removeBlock(position, false);
            worldIn.playSound(null, position, block.defaultBlockState().getSoundType().getBreakSound(), 
                SoundSource.BLOCKS, 0.3F, 0.6F);

            final Block blockBelow = worldIn.getBlockState(position.below()).getBlock();
            final Block targetBlock = CommonUtilities.getValidGroundBlock(blockBelow, true);

            if (onGeneration && targetBlock == Blocks.DIRT) {
                worldIn.setBlock(position.below(), Blocks.GRASS_BLOCK.defaultBlockState(), 3);
            } else if (targetBlock != null) {
                worldIn.setBlock(position.below(), targetBlock.defaultBlockState(), 3);
            }
        } else if (specialBlock >= BuildingBlock.SPAWNERSKELETON && specialBlock <= BuildingBlock.SPAWNERBLAZE) {
            worldIn.setBlock(position, Blocks.SPAWNER.defaultBlockState(), 3);
            BlockEntity blockEntity = worldIn.getBlockEntity(position);
            if (blockEntity instanceof SpawnerBlockEntity spawner) {
                EntityType<?> entityType = switch (specialBlock) {
                    case SPAWNERSKELETON -> EntityType.SKELETON;
                    case SPAWNERZOMBIE -> EntityType.ZOMBIE;
                    case SPAWNERSPIDER -> EntityType.SPIDER;
                    case SPAWNERCAVESPIDER -> EntityType.CAVE_SPIDER;
                    case SPAWNERCREEPER -> EntityType.CREEPER;
                    case SPAWNERBLAZE -> EntityType.BLAZE;
                    default -> EntityType.ZOMBIE;
                };
                spawner.setEntityId(entityType, worldIn.getRandom());
            }
        } else if (specialBlock == BuildingBlock.DISPENDERUNKNOWNPOWDER) {
            worldIn.setBlock(position, Blocks.DISPENSER.defaultBlockState(), 3);
            BlockEntity blockEntity = worldIn.getBlockEntity(position);
            if (blockEntity instanceof DispenserBlockEntity dispenser) {
                ItemStack powderStack = new ItemStack(MillItems.UNKNOWN_POWDER.get(), 2);
                // Add item to dispenser inventory
                for (int i = 0; i < dispenser.getContainerSize(); i++) {
                    if (dispenser.getItem(i).isEmpty()) {
                        dispenser.setItem(i, powderStack);
                        break;
                    }
                }
            }
        }
        // Note: Tree spawning would need to be reimplemented with modern worldgen features
    }
    
    public void buildPath() {
        // Make code to build paths - to be implemented
    }
}