package org.millenaire.common.items;

// Imports de base de Minecraft/NeoForge
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

// Import du bloc correspondant (ajusté pour la nouvelle structure)
import org.millenaire.common.blocks.BlockDecorativeEarth;

/**
 * ItemBlock pour BlockDecorativeEarth.
 * Cette classe permet de récupérer le nom localisé de l'item
 * directement à partir des propriétés du BlockDecorativeEarth,
 * qui gère les différentes variantes (textures/meta).
 */
public class ItemBlockDecorativeEarth extends BlockItem
{
    // Le constructeur est standard pour les BlockItem
    public ItemBlockDecorativeEarth(Block block, Properties properties) 
    {
        super(block, properties);
    }

    /**
     * Surcharge pour obtenir la clé de localisation de l'item à partir de l'instance du bloc.
     * C'est essentiel pour que les différentes "sous-variantes" du bloc
     * (gérées dans BlockDecorativeEarth) affichent le nom correct dans l'inventaire.
     */
    @Override
    public String getDescriptionId(ItemStack stack)
    {
        // On s'assure que le bloc est bien une instance de BlockDecorativeEarth avant le cast
        if (this.getBlock() instanceof BlockDecorativeEarth decorativeEarthBlock) {
             return decorativeEarthBlock.getDescriptionId(stack);
        }
        
        // Fallback si le cast échoue (ne devrait pas arriver si l'enregistrement est correct)
        return super.getDescriptionId(stack);
    }
    
    // Les méthodes getDamage, getMaxDamage, et isDamaged sont omises ou non surchargées.
    // L'ancienne logique basée sur l'int damage (métadonnées) est remplacée 
    // par les BlockStates dans le bloc lui-même et la manipulation de DataComponents 
    // pour la gestion des variantes et de l'état dans 1.21.
}
