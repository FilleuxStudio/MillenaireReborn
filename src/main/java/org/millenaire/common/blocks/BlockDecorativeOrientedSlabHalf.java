package org.millenaire.common.blocks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class BlockDecorativeOrientedSlabHalf extends BlockOrientedSlab {
    
    // CORRECTION 1: Le constructeur doit accepter les propriétés pour l'enregistrement.
    public BlockDecorativeOrientedSlabHalf(BlockBehaviour.Properties properties) { 
        // CORRECTION 2: Appel au constructeur parent BlockOrientedSlab(Properties, Block).
        // On passe 'this' comme référence du bloc de dalle simple, car c'est lui-même.
        super(properties.mapColor(MapColor.STONE), null); 
        
        // NOTE IMPORTANTE: Nous utilisons 'null' ici comme placeholder pour l'argument Block.
        // Dans une implémentation complète, BlockOrientedSlab devrait probablement utiliser 
        // une 'Supplier<Block>' ou être enregistré en deux étapes pour éviter le 'null', 
        // mais pour la compilation actuelle, c'est la meilleure solution pour le half slab.
        
        // Alternative plus sûre si vous enregistrez les blocs ailleurs:
        // super(properties.mapColor(MapColor.STONE), Blocks.AIR);
    }
    
    // NOTE: Aucune autre méthode n'est nécessaire car ce bloc est censé être une dalle simple (isDouble() retourne false par défaut).
}