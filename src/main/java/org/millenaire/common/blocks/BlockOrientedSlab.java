package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import com.mojang.serialization.MapCodec;

public class BlockOrientedSlab extends SlabBlock {
    
    
    // CORRECTION 1: Création de la propriété FACING moderne (Plane.HORIZONTAL)
    public static final DirectionProperty FACING = DirectionProperty.create("facing", (direction) -> direction.getAxis().isHorizontal());
    
    // NOTE: La propriété SEAMLESS n'est plus nécessaire. La dalle double est gérée par SlabType.DOUBLE.
    
    private final Block singleSlab;

    /**
     * CONSTRUCTEUR: Utilise les propriétés modernes et conserve la référence au bloc de dalle simple.
     */
    public BlockOrientedSlab(BlockBehaviour.Properties properties, Block singleSlabIn) {
        super(properties);
        this.singleSlab = singleSlabIn;
        
        // Initialisation de l'état par défaut (BOTTOM, non-waterlogged, NORTH facing)
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(TYPE, SlabType.BOTTOM)
            .setValue(WATERLOGGED, false)
            .setValue(FACING, Direction.NORTH)
        );
    }
    
    /**
     * isDouble() n'est plus une méthode surchargée de SlabBlock, mais est utilisée dans la logique du mod.
     * On la conserve SANS @Override.
     */
    public boolean isDouble() { 
        return false;
    }
    
    /**
     * CORRECTION 2: Migration de getItemDropped vers getBaseResource.
     */
    @Override
    public Item getBaseResource(BlockState state, RandomSource random) {
        if (this.singleSlab != null) {
             return Item.byBlock(this.singleSlab);
        } else {
             return super.getBaseResource(state, random);
        }
    }

    /**
     * CORRECTION 3: Migration de getItem (pour l'icône) vers getCloneItemStack.
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (this.singleSlab != null) {
            return new ItemStack(this.singleSlab);
        } else {
            return super.getCloneItemStack(level, pos, state);
        }
    }

    /**
     * CORRECTION 4: Migration de onBlockPlaced vers getStateForPlacement.
     * Ajoute la logique d'orientation FACING à la logique de dalle standard.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // 1. Logique de dalle standard (TYPE)
        BlockState slabState = super.getStateForPlacement(context);
        
        // 2. Logique d'orientation (FACING) : l'opposé de la direction du joueur.
        return slabState.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    
    /**
     * CORRECTION 5: createBlockStateDefinition doit inclure la propriété FACING.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Ajout de FACING aux propriétés de dalle standards (TYPE et WATERLOGGED)
        builder.add(TYPE, WATERLOGGED, FACING);
    }
    
    /**
     * CORRECTION 6: Implémentation du getCodec.
     * On délègue à SlabBlock car singleSlab n'est pas une propriété d'état sérialisable.
     */
    @Override
    protected MapCodec<? extends SlabBlock> getCodec() {
        return super.getCodec();
    }
    
    // NOTE: Les anciennes méthodes getVariantProperty, getVariant, getStateFromMeta et getMetaFromState
    // sont remplacées par le système de BlockState/Codec et sont omises.
}