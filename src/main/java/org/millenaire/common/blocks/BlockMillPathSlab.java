package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.millenaire.common.blocks.BlockMillPath.EnumType; // Import de l'énumération nécessaire

public class BlockMillPathSlab extends SlabBlock {
    
    // CORRECTION 1: VARIANT comme EnumProperty moderne
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);

    // CORRECTION 2: Définition des VoxelShape personnalisées (migration de setBlockBoundsBasedOnState)
    // 7/16ème de hauteur (0.4375F)
    protected static final VoxelShape SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0); 
    // De 8/16ème à 15/16ème (0.5F à 0.9375F)
    protected static final VoxelShape SHAPE_TOP = Block.box(0.0, 8.0, 0.0, 16.0, 15.0, 16.0); 
    // Double dalle: 15/16ème de hauteur (0.9375F)
    protected static final VoxelShape SHAPE_DOUBLE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0); 
    
    public static final MapCodec<BlockMillPathSlab> CODEC = simpleCodec(BlockMillPathSlab::new);

    // CORRECTION 3: Constructeur moderne
    public BlockMillPathSlab(BlockBehaviour.Properties properties) {
        super(properties);
        
        // Initialisation de l'état par défaut (BOTTOM, non-waterlogged, VARIANT.DIRT)
        this.registerDefaultState(this.defaultBlockState()
            .setValue(TYPE, SlabType.BOTTOM)
            .setValue(WATERLOGGED, false)
            .setValue(VARIANT, EnumType.DIRT) // Assumer DIRT comme défaut
        );
    }
    
    // Migration de isDouble()
    public boolean isDouble() { return false; }

    // CORRECTION 4: getShape remplace setBlockBoundsBasedOnState
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        SlabType slabtype = state.getValue(TYPE);
        
        return switch (slabtype) {
            // Le double slab hérite cette méthode et utilisera SHAPE_DOUBLE
            case DOUBLE -> SHAPE_DOUBLE; 
            case TOP -> SHAPE_TOP;
            case BOTTOM -> SHAPE_BOTTOM;
        };
    }
    
    // Migration de getItemDropped (getBaseResource pour les items)
    @Override
    public Item getBaseResource(BlockState state, RandomSource random) {
        // Retourne le BlockItem du bloc lui-même.
        return Item.byBlock(this);
    }
    
    // Migration de getVariantProperty
    // On laisse vide, VARIANT est accessible publiquement.

    // CORRECTION 5: createBlockStateDefinition doit inclure VARIANT
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, WATERLOGGED, VARIANT);
    }
    
    // CORRECTION 6: Implémentation du getCodec
    @Override
    protected MapCodec<? extends SlabBlock> getCodec() {
        return CODEC;
    }
    
    // Migration de damageDropped/getMetaFromState/getStateFromMeta:
    // La gestion de la métadonnée est maintenant implicite via le système de BlockState et MapCodec.
    // Pour les drops, on expose une méthode pour la valeur de la variante (utilisé dans BlockItem si nécessaire).
    public int getMetadata(BlockState state) {
        return state.getValue(VARIANT).getMetadata();
    }
}