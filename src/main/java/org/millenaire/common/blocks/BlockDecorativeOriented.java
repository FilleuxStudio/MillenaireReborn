package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec; // Import nécessaire pour le Codec
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block; // Import nécessaire
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition; // Import nécessaire
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativeOriented extends HorizontalDirectionalBlock {
    // La propriété FACING est déjà gérée par HorizontalDirectionalBlock
    // public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING; 

    public BlockDecorativeOriented(Properties properties) {
        // Le constructeur est correct
        super(properties.mapColor(MapColor.STONE));
        // Enregistrement de l'état par défaut (nécessite l'initialisation du StateDefinition)
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    /**
     * Utilise simpleCodec pour la sérialisation de l'état du bloc.
     */
    public MapCodec<BlockDecorativeOriented> codec() {
        return simpleCodec(BlockDecorativeOriented::new);
    }
    
    protected MapCodec<? extends HorizontalDirectionalBlock> getCodec() {
        return codec();
    }

    // --- LOGIQUE DE PLACEMENT ---
    
    /**
     * La logique originale (placer face à l'opposé du joueur) est conservée.
     */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Renvoie l'état par défaut avec la propriété FACING réglée sur
        // la direction horizontale du joueur, inversée (opposée).
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // --- DÉFINITION DE L'ÉTAT DU BLOC ---

    /**
     * C'était déjà correct dans votre code de migration, mais inclus ici pour complétude.
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    // NOTE: Si vous aviez getStateFromMeta / getMetaFromState, ils sont désormais
    // gérés automatiquement par le MapCodec et la méthode createBlockStateDefinition.
}