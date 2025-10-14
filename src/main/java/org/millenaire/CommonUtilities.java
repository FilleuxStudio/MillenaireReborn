package org.millenaire; // Nouveau package, plus organisé

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.RandomSource; // Remplacement de java.util.Random
import net.minecraft.world.entity.player.Player; // Remplacement de EntityPlayer
import net.minecraft.world.item.ItemStack; // Remplacement de net.minecraft.item.ItemStack
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks; // net.minecraft.init.Blocks est devenu net.minecraft.world.level.block.Blocks
import org.millenaire.common.items.MillItems;

public class CommonUtilities 
{
    // Remplacement de java.util.Random par l'instance de RandomSource fournie par la Level/World,
    // mais pour une classe utilitaire, nous pouvons utiliser un RandomSource statique si nécessaire.
    // Cependant, il est préférable d'utiliser RandomSource.create() si on ne veut pas de seed spécifique.
    public static final RandomSource random = RandomSource.create();
    
    // Constante pour la conversion (64 est une valeur standard)
    private static final int CONVERSION_RATE = 64; 
    
    /**
     * Organise les devises (denier, argent, or) dans l'inventaire du joueur, 
     * en convertissant les petites coupures en grandes coupures (64 deniers = 1 argent, 64 argent = 1 or).
     * @param playerIn Le joueur dont l'inventaire doit être organisé.
     */
    public static void changeMoney(Player playerIn)
    {
        // Utilisation de longs pour les totaux pour éviter les débordements (même si c'est peu probable avec 64).
        long totalDeniers = 0;
        long totalArgents = 0;
        long totalOrs = 0;
        
        // 1. Collecte de toute la monnaie et suppression de l'inventaire.
        // Utilisation de l'API d'inventaire moderne
        for(int i = 0; i < playerIn.getInventory().getContainerSize(); i++)
        {
            // playerIn.getInventory() est de type PlayerInventory.
            ItemStack stack = playerIn.getInventory().getItem(i); 
            
            if(!stack.isEmpty()) // Remplacement de stack != null
            {
                // Note: On suppose que MillItems.denier/denierArgent/denierOr sont des Suppliers<Item> et on utilise .get()
                if(stack.is(MillItems.denier.get()))
                {
                    totalDeniers += stack.getCount(); // Remplacement de stack.stackSize
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY); // Suppression du stack
                }
                else if(stack.is(MillItems.denierArgent.get()))
                {
                    totalArgents += stack.getCount();
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                }
                else if(stack.is(MillItems.denierOr.get()))
                {
                    totalOrs += stack.getCount();
                    playerIn.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
        
        // 2. Conversion Denier -> Argent
        totalArgents += totalDeniers / CONVERSION_RATE;
        totalDeniers %= CONVERSION_RATE;
        
        // 3. Conversion Argent -> Or
        totalOrs += totalArgents / CONVERSION_RATE;
        totalArgents %= CONVERSION_RATE;
        
        // 4. Achievement: MillAchievement est maintenant une stat ou un critère.
        // Puisque nous n'avons pas la définition de MillAchievement.cresus, nous laissons 
        // un TODO ou nous supposons qu'elle sera gérée dans une classe de stat/critère dédiée.
        if(totalOrs >= 1)
        {
            // TODO: Migrer l'achievement (stat) 'cresus'. 
            // Exemple : playerIn.awardStat(MillStats.CRESUS); 
        }
        
        // 5. Ajout des piles organisées à l'inventaire.
        
        // Deniers restants (moins de 64)
        if (totalDeniers > 0) {
            // PlayerInventory.add(ItemStack) gère l'ajout et le placement dans l'inventaire.
            playerIn.getInventory().add(new ItemStack(MillItems.denier.get(), (int)totalDeniers));
        }
        
        // Argents restants (moins de 64)
        if (totalArgents > 0) {
            playerIn.getInventory().add(new ItemStack(MillItems.denierArgent.get(), (int)totalArgents));
        }
        
        // Ors (peut être plus de 64, PlayerInventory.add gère l'éclatement des piles automatiquement).
        // On éclate manuellement pour garantir des piles de 64, en suivant l'ancienne logique.
        while(totalOrs > 0)
        {
            int amount = (int) Math.min(CONVERSION_RATE, totalOrs);
            
            // PlayerInventory.add() retourne le reste qui n'a pas pu être ajouté. 
            // On utilise add(ItemStack) qui gère l'éclatement automatiquement si la pile dépasse la taille maximale,
            // mais l'ancienne logique semble forcer des piles de 64 (la taille max) même si l'inventaire est plein.
            // On simule l'ancienne boucle simple en ajoutant 64 par 64.
            
            playerIn.getInventory().add(new ItemStack(MillItems.denierOr.get(), amount));
            totalOrs -= amount;
        }
    }
    
    /**
     * Retourne un float aléatoire entre 0.1f et 1.1f.
     * @return Un float aléatoire non-zéro (>= 0.1f).
     */
    public static float getRandomNonzero() { 
        // random.nextFloat() est toujours valide.
        return random.nextFloat() + 0.1f; 
    }
    
    /**
     * Retourne un entier aléatoire pour un genre de Millager.
     * Les valeurs possibles sont : -2, -1, 0.
     * @return -2, -1, ou 0.
     */
    public static int randomizeGender() { 
        // random.nextInt(3) -> random.nextInt(3) est remplacé par random.nextInt(3) de RandomSource.
        return random.nextInt(3) - 2; 
    }
    
    /**
     * Détermine le bloc de "terre" valide correspondant à un bloc donné.
     * La logique de conversion est conservée.
     * @param b Le bloc à vérifier.
     * @param surface Indique si le sol est en surface (true) ou sous terre (false).
     * @return Le bloc de remplacement valide (Dirt, Grass, Sand, Sandstone, Gravel) ou null.
     */
    public static Block getValidGroundBlock(final Block b, final boolean surface) 
    {
        if (b == null) return null;
        
        if (b == Blocks.BEDROCK || b == Blocks.DIRT ||
            b == Blocks.GRASS_BLOCK) { // Remplacement de Blocks.grass par GRASS_BLOCK
            return Blocks.DIRT;
        } else if (b == Blocks.STONE) {
            if (surface) {
                return Blocks.DIRT;
            } else {
                return Blocks.GRASS_BLOCK; // Remplacement par GRASS_BLOCK
            }
        } else if (b == Blocks.GRAVEL) {
            return Blocks.GRAVEL;
        } else if (b == Blocks.SAND) {
            return Blocks.SAND;
        } else if (b == Blocks.SANDSTONE) {
            if (surface) {
                return Blocks.SAND;
            } else {
                return Blocks.SANDSTONE;
            }
        }

        return null;
    }
}