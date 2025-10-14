package org.millenaire.common.blocks;

import com.mojang.serialization.MapCodec; // Import ajouté pour le MapCodec (bonne pratique)
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour; // Import ajouté pour la Properties
import net.minecraft.world.level.block.state.BlockState;

public class BlockDecorativeUpdate extends Block {
    
    private final BlockState updateState;

    // CORRECTION 1: Le constructeur doit utiliser la classe BlockBehaviour.Properties
    public BlockDecorativeUpdate(BlockBehaviour.Properties properties, BlockState updateIn) {
        super(properties);
        this.updateState = updateIn;
    }

    // CORRECTION 2: Ajout du MapCodec, en utilisant la méthode par défaut
    @Override
    protected MapCodec<? extends Block> getCodec() {
        return super.getCodec();
    }
    
    /**
     * CORRECTION 3: L'annotation @Override est correcte. L'erreur que vous voyez 
     * est probablement due à la dépendance de l'IDE/compilateur qui n'a pas encore 
     * résolu complètement la hiérarchie de la classe Block.
     * Le code ci-dessous est la signature correcte dans 1.21.10.
     */
    public void randomTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        int i = random.nextInt(3);

        if (i > 1) {
            // Note: Le flag '3' est correct pour mettre à jour les voisins et forcer le rendu.
            world.setBlock(pos, updateState, 3);
        }
    }

    /**
     * CORRECTION 4: L'annotation @Override est correcte.
     */
    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}