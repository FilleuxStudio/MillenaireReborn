package org.millenaire;

import java.util.Random;

import org.millenaire.client.gui.MillAchievement;
import org.millenaire.common.items.MillItems;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class CommonUtilities 
{
	public static Random random = new Random();
	
	/**
	 * Organise les devises (denier, argent, or) dans l'inventaire du joueur, 
	 * en convertissant les petites coupures en grandes coupures (64 deniers = 1 argent, 64 argent = 1 or).
	 * @param playerIn Le joueur dont l'inventaire doit être organisé.
	 */
	public static void changeMoney(EntityPlayer playerIn)
	{
		// Initialisation des compteurs de piles de monnaie.
		// La stackSize est mise à 0 au début et sera incrémentée avec les piles trouvées.
		ItemStack denier = new ItemStack(MillItems.denier, 0, 0);
		ItemStack argent = new ItemStack(MillItems.denierArgent, 0, 0);
		ItemStack or = new ItemStack(MillItems.denierOr, 0, 0);
		
		// 1. Collecte de toute la monnaie et suppression de l'inventaire.
		for(int i = 0; i < playerIn.inventory.getSizeInventory(); i++)
		{
			ItemStack stack = playerIn.inventory.getStackInSlot(i);
			if(stack != null)
			{
				if(stack.getItem() == MillItems.denier)
				{
					denier.stackSize = denier.stackSize + stack.stackSize;
					playerIn.inventory.removeStackFromSlot(i);
				}
				else if(stack.getItem() == MillItems.denierArgent) // Utilisation de else if pour l'optimisation
				{
					argent.stackSize = argent.stackSize + stack.stackSize;
					playerIn.inventory.removeStackFromSlot(i);
				}
				else if(stack.getItem() == MillItems.denierOr)
				{
					or.stackSize = or.stackSize + stack.stackSize;
					playerIn.inventory.removeStackFromSlot(i);
				}
			}
		}
		
		// 2. Conversion Denier -> Argent
		argent.stackSize = argent.stackSize + (denier.stackSize / 64);
		denier.stackSize = denier.stackSize % 64; // Le reste devient la pile de deniers.
		
		// 3. Conversion Argent -> Or
		or.stackSize = or.stackSize + (argent.stackSize / 64);
		
		// Vérification pour l'achèvement 'cresus' (si on a au moins 1 or après conversion)
		if(or.stackSize >= 1)
		{
			// Note: La classe MillAchievement doit exister et la stat 'cresus' doit être définie.
			// playerIn.addStat(...) est la méthode standard pour ajouter un achèvement.
			playerIn.addStat(MillAchievement.cresus, 1);
		}

		argent.stackSize = argent.stackSize % 64; // Le reste devient la pile d'argents.
		
		// 4. Ajout des piles organisées à l'inventaire.
		
		// Deniers et Argents (moins de 64 chacun)
		if (denier.stackSize > 0) {
			playerIn.inventory.addItemStackToInventory(denier);
		}
		if (argent.stackSize > 0) {
			playerIn.inventory.addItemStackToInventory(argent);
		}
		
		// Ors (peut être plus de 64, doit être ajouté par piles de 64)
		while(or.stackSize > 64)
		{
			// Crée une nouvelle pile d'or de taille 64 et l'ajoute.
			playerIn.inventory.addItemStackToInventory(new ItemStack(MillItems.denierOr, 64, 0));
			or.stackSize = or.stackSize - 64;
		}
		
		// Ajoute la dernière pile d'or (taille <= 64).
		if (or.stackSize > 0) {
			playerIn.inventory.addItemStackToInventory(or);
		}
	}
	
	/**
	 * Retourne un float aléatoire entre 0.1f et 1.1f.
	 * @return Un float aléatoire non-zéro (>= 0.1f).
	 */
	public static float getRandomNonzero() { 
		// random.nextFloat() retourne entre 0.0 (inclus) et 1.0 (exclus).
		// +0.1f assure un minimum de 0.1f.
		return random.nextFloat() + 0.1f; 
	}
	
	/**
	 * Retourne un entier aléatoire pour un genre de Millager.
	 * Les valeurs possibles sont : -2, -1, 0.
	 * @return -2, -1, ou 0.
	 */
	public static int randomizeGender() { 
		// random.nextInt(3) retourne 0, 1, ou 2.
		// Soustraire 2 donne -2, -1, ou 0.
		return random.nextInt(3) - 2; 
	}
	
	/**
	 * Détermine le bloc de "terre" valide correspondant à un bloc donné.
	 * @param b Le bloc à vérifier.
	 * @param surface Indique si le sol est en surface (true) ou sous terre (false).
	 * @return Le bloc de remplacement valide (Dirt, Grass, Sand, Sandstone, Gravel) ou null.
	 */
	public static Block getValidGroundBlock(final Block b, final boolean surface) 
	{
		if (b == null) return null;
		
		if (b == Blocks.bedrock || b == Blocks.dirt ||
			b == Blocks.grass) {
            return Blocks.dirt; // Le plus générique
		} else if (b == Blocks.stone) {
		    if (surface) {
                return Blocks.dirt; // La pierre en surface est considérée comme la terre (pour planter des choses)
            } else {
                return Blocks.grass; // La pierre sous terre est remplacée par de l'herbe (logique particulière au mod)
            }
        } else if (b == Blocks.gravel) {
		    return Blocks.gravel;
        } else if (b == Blocks.sand) {
		    return Blocks.sand;
        } else if (b == Blocks.sandstone) {
		    if (surface) {
                return Blocks.sand; // Le grès en surface devient du sable
            } else {
                return Blocks.sandstone; // Le grès sous terre reste du grès
            }
        }

		return null; // Si aucun match
	}
}