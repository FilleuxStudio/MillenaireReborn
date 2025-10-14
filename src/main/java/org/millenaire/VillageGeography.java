package org.millenaire;

import java.util.ArrayList;
import java.util.List;

import org.millenaire.comme.blocks.MillBlocks;
import org.millenaire.building.BuildingLocation;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockWall;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class VillageGeography 
{
	private static final int MAP_MARGIN = 10;
	private static final int BUILDING_MARGIN = 5;
	private static final int VALIDHEIGHTDIFF = 10;

	public int length = 0;
	public int width = 0;
	private int chunkStartX = 0, chunkStartZ = 0;
	public int mapStartX = 0, mapStartZ = 0;
	private int yBaseline = 0;

	private short[][] topGround;
	public short[][] constructionHeight;
	private short[][] spaceAbove;
	
	public boolean[][] danger;
	public boolean[][] buildingForbidden;
	public boolean[][] canBuild;

	public boolean[][] buildingLoc;

	private boolean[][] water;
	private boolean[][] tree;
	
	public boolean[][] buildTested = null;

	private boolean[][] topAdjusted;

	public boolean[][] path;

	private int frequency = 10;
	private List<BuildingLocation> buildingLocations = new ArrayList<BuildingLocation>();
	private BuildingLocation locationIP;

	public int nbLoc = 0;

	public World world;

	private int lastUpdatedX, lastUpdatedZ;

	private int updateCounter;
	
	public VillageGeography()
	{
		// Constructeur par défaut
	}
	
	private void createWorldInfo(final List<BuildingLocation> locations, final BuildingLocation blIP, final int pstartX, final int pstartZ, final int endX, final int endZ)
	{
		// Calcule les coordonnées du monde en coordonnées de la carte/tableau.
		chunkStartX = pstartX >> 4;
		chunkStartZ = pstartZ >> 4;
		mapStartX = chunkStartX << 4;
		mapStartZ = chunkStartZ << 4;

		length = ((endX >> 4) + 1 << 4) - mapStartX;
		width = ((endZ >> 4) + 1 << 4) - mapStartZ;

		frequency = (int) Math.max(1000 * 1.0 / (length * width / 256), 10);

		if (frequency == 0) 
		{
			System.err.println("Null frequency in createWorldInfo.");
		}

		// Initialisation des tableaux de données
		topGround = new short[length][width];
		constructionHeight = new short[length][width];
		spaceAbove = new short[length][width];
		danger = new boolean[length][width];
		buildingLoc = new boolean[length][width];
		buildingForbidden = new boolean[length][width];
		canBuild = new boolean[length][width];
		buildTested = new boolean[length][width];
		water = new boolean[length][width];
		tree = new boolean[length][width];
		path = new boolean[length][width];
		topAdjusted = new boolean[length][width];

		buildingLocations = new ArrayList<>();

		for (int i = 0; i < length; i++) 
		{
			for (int j = 0; j < width; j++) 
			{
				buildingLoc[i][j] = false;
				canBuild[i][j] = false;
			}
		}

		for (final BuildingLocation location : locations) 
		{
			registerBuildingLocation(location);
		}

		locationIP = blIP;
		if (locationIP != null) 
		{
			registerBuildingLocation(locationIP);
		}

		// Mise à jour de toutes les chunks initiales
		for (int i = 0; i < length; i += 16) 
		{
			for (int j = 0; j < width; j += 16) 
			{
				updateChunk(i, j);
			}
		}
		lastUpdatedX = 0;
		lastUpdatedZ = 0;
	}

	/**
	 * Vérifie si un bloc est interdit pour la construction.
	 * @param block Le bloc à vérifier.
	 * @return true si le bloc est interdit, false sinon.
	 */
	private static boolean isForbiddenBlockForConstruction(final Block block)
	{
		// Ajout de vérification de nullité pour un code plus robuste.
		if (block == null) return false;
		
		return block == Blocks.water || block == Blocks.flowing_water || block == Blocks.ice || block == Blocks.flowing_lava || block == Blocks.lava || block == Blocks.planks || block == Blocks.cobblestone || block == Blocks.brick_block || block == Blocks.chest || block == Blocks.glass || block == Blocks.stonebrick || block == Blocks.prismarine
				|| block instanceof BlockWall || block instanceof BlockFence || block == MillBlocks.blockDecorativeEarth || block == MillBlocks.blockDecorativeStone || block == MillBlocks.blockDecorativeWood || block == MillBlocks.byzantineTile || block == MillBlocks.byzantineTileSlab || block == MillBlocks.byzantineStoneTile || block == MillBlocks.paperWall || block == MillBlocks.emptySericulture;
	}
	
	private void registerBuildingLocation(final BuildingLocation bl) 
	{
		// S'assurer que la liste n'est pas nulle, bien qu'elle soit initialisée dans createWorldInfo.
		if (buildingLocations == null) {
			buildingLocations = new ArrayList<>();
		}
		
		buildingLocations.add(bl);
		nbLoc++; // Ajout du compteur de localisation

		// Calcule les limites de la zone de la construction dans les coordonnées de la carte/tableau.
		final int sx = Math.max(bl.minxMargin - mapStartX, 0);
		final int sz = Math.max(bl.minzMargin - mapStartZ, 0);
		// Utilise 'length - 1' et 'width - 1' pour ne pas dépasser les limites du tableau.
		final int ex = Math.min(bl.maxxMargin - mapStartX, length);
		final int ez = Math.min(bl.maxzMargin - mapStartZ, width); 

		for (int i = sx; i < ex; i++) 
		{
			for (int j = sz; j < ez; j++) 
			{
				// Marque la zone comme étant occupée par une construction.
				if (i >= 0 && i < length && j >= 0 && j < width) {
					buildingLoc[i][j] = true;
				}
			}
		}
	}
	
	/**
	 * Met à jour les informations géographiques du village.
	 * @param world Le monde actuel.
	 * @param locations La liste des emplacements de construction existants.
	 * @param blIP L'emplacement de la construction en cours (In Progress).
	 * @param center La position centrale du village.
	 * @param radius Le rayon maximal du village.
	 * @return true si la carte géographique a été réinitialisée, false sinon.
	 */
	public boolean update(final World world, final List<BuildingLocation> locations, final BuildingLocation blIP, final BlockPos center, final int radius)
	{
		this.world = world;
		this.yBaseline = center.getY();
		locationIP = blIP;

		// Si les emplacements n'ont pas changé en nombre, on se contente de mettre à jour la prochaine chunk.
		if (buildingLocations != null && buildingLocations.size() > 0 && buildingLocations.size() == locations.size() && locationIP == blIP) 
		{
			buildingLocations = locations; // Met à jour la référence de la liste des emplacements
			updateNextChunk();
			return false;
		}

		// Sinon, recalcule la zone couverte par le village.
		int startX = center.getX(), startZ = center.getZ(), endX = center.getX(), endZ = center.getZ();

		// Étend la zone pour inclure tous les emplacements de construction.
		for (final BuildingLocation location : locations) 
		{
			if (location != null) 
			{
				startX = Math.min(startX, location.position.getX() - location.length / 2);
				endX = Math.max(endX, location.position.getX() + location.length / 2);
				startZ = Math.min(startZ, location.position.getZ() - location.width / 2);
				endZ = Math.max(endZ, location.position.getZ() + location.width / 2);
			}
		}

		if (blIP != null) 
		{
			startX = Math.min(startX, blIP.position.getX() - blIP.length / 2);
			endX = Math.max(endX, blIP.position.getX() + blIP.length / 2);
			startZ = Math.min(startZ, blIP.position.getZ() - blIP.width / 2);
			endZ = Math.max(endZ, blIP.position.getZ() + blIP.width / 2);
		}

		// Ajout des marges (BUILDING_MARGIN, radius, MAP_MARGIN)
		startX = Math.min(startX - BUILDING_MARGIN, center.getX() - radius - MAP_MARGIN);
		startZ = Math.min(startZ - BUILDING_MARGIN, center.getZ() - radius - MAP_MARGIN);
		endX = Math.max(endX + BUILDING_MARGIN, center.getX() + radius + MAP_MARGIN);
		endZ = Math.max(endZ + BUILDING_MARGIN, center.getZ() + radius + MAP_MARGIN);

		// Calcule les nouvelles dimensions de la carte
		final int chunkStartXTemp = startX >> 4;
		final int chunkStartZTemp = startZ >> 4;
		final int mapStartXTemp = chunkStartXTemp << 4;
		final int mapStartZTemp = chunkStartZTemp << 4;
		final int lengthTemp = ((endX >> 4) + 1 << 4) - mapStartXTemp;
		final int widthTemp = ((endZ >> 4) + 1 << 4) - mapStartZTemp;

		// Si la taille ou l'origine de la carte a changé, on la recrée entièrement.
		if (lengthTemp != length || widthTemp != width || mapStartXTemp != mapStartX || mapStartZTemp != mapStartZ) 
		{
			createWorldInfo(locations, blIP, startX, startZ, endX, endZ);
			return true;
		} 
		else 
		{
			// Sinon, on met à jour uniquement la liste des emplacements et la prochaine chunk.
			buildingLocations = new ArrayList<>();
			for (final BuildingLocation location : locations) 
			{
				registerBuildingLocation(location);
			}
			if (blIP != null) { // Assure que blIP est aussi mis à jour si non inclus dans locations
				registerBuildingLocation(blIP);
			}

			updateNextChunk();
			return false;
		}
	}
	
	/**
	 * Met à jour les informations géographiques pour une chunk (16x16) spécifique.
	 * @param startX La coordonnée X de début de la chunk (dans les coordonnées de la carte/tableau, 0 à length-1).
	 * @param startZ La coordonnée Z de début de la chunk (dans les coordonnées de la carte/tableau, 0 à width-1).
	 */
	private void updateChunk(final int startX, final int startZ) 
	{
		// Assure que les 9 chunks environnantes sont chargées pour les opérations aux bords.
		for (int i = -1; i < 2; i++) 
		{
			for (int j = -1; j < 2; j++) 
			{
				if (!world.getChunkProvider().chunkExists((startX + mapStartX >> 4) + i, (startZ + mapStartZ >> 4) + j))
				{
					// La méthode provideChunk va charger ou générer la chunk.
					world.getChunkProvider().provideChunk((startX + mapStartX >> 4) + i, (startZ + mapStartZ >> 4) + j);
				}
			}
		}

		// Récupère la chunk centrale.
		final Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(startX + mapStartX, yBaseline ,startZ + mapStartZ));

		// Parcours des 16x16 blocs de la chunk.
		for (int i = 0; i < 16; i++) 
		{
			for (int j = 0; j < 16; j++) 
			{
				final short miny = (short) Math.max(yBaseline - 25, 1);
				final short maxy = (short) Math.min(yBaseline + 25, 255);

				final int mx = i + startX; // Coordonnée X dans le tableau de la carte
				final int mz = j + startZ; // Coordonnée Z dans le tableau de la carte

				// S'assurer que nous sommes dans les limites du tableau.
				if (mx < 0 || mx >= length || mz < 0 || mz >= width) {
					continue;
				}

				// Réinitialisation des flags pour ce bloc
				canBuild[mx][mz] = false;
				buildingForbidden[mx][mz] = false;
				water[mx][mz] = false;
				topAdjusted[mx][mz] = false;
				danger[mx][mz] = false; // Réinitialisé ici avant la détection de lave

				short y = maxy;
				Block block;

				// --- 1. Détermination de constructionHeight (hauteur du plafond souterrain/sol) ---
				short ceilingSize = 0;
				// Le bloc au niveau maxy
				Block tblock = chunk.getBlock(i, y, j);

				// Descend jusqu'à trouver un bloc 'ground' ou une grande cavité (ceilingSize > 3).
				while (y >= miny && !isBlockIdGround(tblock)) 
				{
					if (isBlockIdGroundOrCeiling(tblock)) // Blocs "sol ou plafond" (stone, sandstone)
					{
						ceilingSize++;
					}
					else
					{
						ceilingSize = 0;
					}

					y--;

					if (ceilingSize > 3) // S'arrête si un plafond de plus de 3 blocs est trouvé.
					{
						break;
					}

					if (y >= miny) {
						tblock = chunk.getBlock(i, y, j);
					} else {
						tblock = null;
					}
				}

				constructionHeight[mx][mz] = y; // Hauteur du bloc sous le point trouvé.

				// --- 2. Détermination de constructionHeight (ajustement vers le haut) ---
				
				boolean heightDone = false;
				short lastLiquid = -1;

				if (y <= maxy && y > 1) 
				{
					// Le bloc au niveau y (potentiel sol/plafond)
					block = chunk.getBlock(i, y, j);
				}
				else
				{
					block = null;
				}

				boolean onground = true; // Utilisé pour savoir si on a traversé de l'eau.
				
				// Monte jusqu'à ce qu'un bloc non solide et non liquide soit trouvé.
				while (block != null && (isBlockSolid(block) || block instanceof BlockLiquid || !onground)) 
				{
					if (block == Blocks.log) 
					{
						heightDone = true; // Le bois (arbre) compte comme un sol mais on s'arrête là.
					} 
					else if (!heightDone) // Tout ce qui est solide (sauf bois) fait monter la constructionHeight.
					{
						constructionHeight[mx][mz]++;
					} 
					else 
					{
						heightDone = true;
					}

					if (isForbiddenBlockForConstruction(block)) 
					{
						buildingForbidden[mx][mz] = true;
					}

					if (block instanceof BlockLiquid) 
					{
						onground = false; // On est dans l'eau
						lastLiquid = y;
					} 
					else if (isBlockSolid(block)) 
					{
						onground = true; // On a touché un bloc solide après de l'eau
					}

					y++;

					if (y <= maxy && y > 1) 
					{
						block = chunk.getBlock(i, y, j);
					} 
					else 
					{
						block = null;
					}
				}

				// Si la recherche s'est arrêtée parce qu'on était encore dans l'eau, on revient au niveau de l'eau.
				if (!onground)
				{
					y = lastLiquid;
				}

				// On continue de monter pour trouver le premier bloc non-solide avec un bloc non-solide au-dessus.
				while (y <= maxy && y > 1 && !(!isBlockSolid(chunk.getBlock(i, y, j)) && !isBlockSolid(chunk.getBlock(i, y + 1, j)))) 
				{
					y++;
				}

				y = (short) Math.max(1, y); // Assure y >= 1

				topGround[mx][mz] = y; // La hauteur du sol (air au-dessus)
				spaceAbove[mx][mz] = 0;

				// --- 3. Détermination de spaceAbove, danger, water, tree, path, canBuild ---

				final Block soilBlock = chunk.getBlock(i, y - 1, j); // Le bloc immédiatement sous topGround
				block = chunk.getBlock(i, y, j); // Le bloc à topGround (doit être l'air/eau, etc.)

                water[mx][mz] = (block == Blocks.flowing_water || block == Blocks.water);

				tree[mx][mz] = (soilBlock == Blocks.log);

				path[mx][mz] = (soilBlock == MillBlocks.blockMillPath || soilBlock == MillBlocks.blockMillPathSlab || soilBlock == MillBlocks.blockMillPathSlabDouble);

				boolean blocked = false;

				// Si le bloc n'est pas une barrière/mur, n'est pas solide, et n'est pas de l'eau courante/source
				if (!(soilBlock instanceof BlockFence) && !(soilBlock instanceof BlockWall) && !isBlockSolid(block) && block != Blocks.flowing_water && soilBlock != Blocks.water) 
				{
					spaceAbove[mx][mz] = 1; // Au moins un bloc d'espace libre
				} 
				else 
				{
					blocked = true;
				}

				if (block == Blocks.flowing_lava || block == Blocks.lava) 
				{
					danger[mx][mz] = true;
				} 
				else 
				{
					// Vérifie si le bloc actuel ou le sol est dans la liste des blocs interdits de Millenaire (Millenaire.instance.forbiddenBlocks)
					// Le code original semble avoir une erreur logique ici, écrasant la valeur. On corrige pour une vérification OU.
					for (final Block forbiddenBlock : Millenaire.instance.forbiddenBlocks) 
					{
						// Si le bloc à 'y' ou le bloc à 'y-1' (sol) est un bloc interdit, marque comme danger.
                        if (forbiddenBlock == block || forbiddenBlock == soilBlock) {
							danger[mx][mz] = true;
							break;
						}
					}
				}

				// Détermine si on peut construire à cette position (hauteur valide, pas de danger, pas de construction existante).
				if (!danger[mx][mz] && !buildingLoc[mx][mz]) 
				{
					// La hauteur de construction (généralement le sol) doit être proche de la baseline.
					if (constructionHeight[mx][mz] > yBaseline - VALIDHEIGHTDIFF && constructionHeight[mx][mz] < yBaseline + VALIDHEIGHTDIFF) 
					{
						canBuild[mx][mz] = true;
					}
				}

				// Mise à jour finale du flag buildingForbidden à la hauteur du sol (topGround).
				buildingForbidden[mx][mz] = isForbiddenBlockForConstruction(block);

				// Continue de monter pour compter l'espace au-dessus (jusqu'à maxy ou 3 blocs max).
				y++;

				while (y < maxy && y > 0) 
				{
					block = chunk.getBlock(i, y, j);

					// Si non bloqué et moins de 3 blocs d'espace et non solide.
					if (!blocked && spaceAbove[mx][mz] < 3 && !isBlockSolid(block)) 
					{
						spaceAbove[mx][mz]++;
					} 
					else 
					{
						blocked = true;
					}

					// Mise à jour du flag buildingForbidden pour tout bloc au-dessus.
                    if (isForbiddenBlockForConstruction(block)) {
						buildingForbidden[mx][mz] = true;
					}

					y++;
				}

				// Finalisation: Si un bloc est interdit, on ne peut pas construire.
                if (buildingForbidden[mx][mz]) {
					canBuild[mx][mz] = false;
				}
			}
		}

		// --- 4. Correction des Trous (Gap Filling) ---
		
		boolean gapFilled = true;

		// Première passe: trous d'un bloc de large
		while (gapFilled) 
		{
			gapFilled = false;
			// Parcours légèrement élargi (-5 à 21) pour les bords de chunk
			for (int i = -5; i < 21; i++) 
			{
				for (int j = -5; j < 21; j++) 
				{
					final int mx = i + startX;
					final int mz = j + startZ;
					
					// S'assurer d'être dans les limites de la carte pour les accès [mx][mz]
					if (mx < 0 || mx >= length || mz < 0 || mz >= width) {
						continue;
					}

					// Gap en X
					if (mz >= 0 && mz < width) 
					{
						// Assure que les voisins existent
						if (mx > 1 && mx < length - 1) 
						{
							// Différence de niveau entre les bords < 2 ET le niveau actuel est 2 blocs au-dessus des deux bords
							if (Math.abs(topGround[mx - 1][mz] - topGround[mx + 1][mz]) < 2 && (topGround[mx - 1][mz] + 2 < topGround[mx][mz] || topGround[mx + 1][mz] + 2 < topGround[mx][mz])) 
							{
								final short ntg = topGround[mx - 1][mz];
								// Vérification des blocs solides aux niveaux ntg, ntg-1, ntg-2, ntg+1, ntg+2, ntg+3
								final boolean samesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg, startZ + mapStartZ + j)).getBlock());
								final boolean belowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 1, startZ + mapStartZ + j)).getBlock());
								final boolean below2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 2, startZ + mapStartZ + j)).getBlock());
								final boolean abovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 1, startZ + mapStartZ + j)).getBlock());
								final boolean above2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 2, startZ + mapStartZ + j)).getBlock());
								final boolean above3solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 3, startZ + mapStartZ + j)).getBlock());

								// 1. Niveau ntg (niveau du bord gauche)
								if (Math.abs(topGround[mx - 1][mz] - topGround[mx + 1][mz]) < 2 && belowsolid && !samesolid && !abovesolid) 
								{
									topGround[mx][mz] = ntg;
									spaceAbove[mx][mz] = (short) (!above2solid ? 3 : 2);
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								} 
								// 2. Niveau ntg - 1 (un bloc sous le bord gauche) - Assumes bord droit <= bord gauche
								else if (topGround[mx + 1][mz] <= topGround[mx - 1][mz] && below2solid && !belowsolid && !samesolid && !abovesolid) 
								{
									topGround[mx][mz] = (short) (ntg - 1);
									spaceAbove[mx][mz] = (short) (!abovesolid ? 3 : 2); // Le niveau 'ntg' est 'abovesolid' si le sol est ntg-1.
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								} 
								// 3. Niveau ntg + 1 (un bloc au-dessus du bord gauche) - Assumes bord droit >= bord gauche
								else if (topGround[mx + 1][mz] >= topGround[mx - 1][mz] && samesolid && !abovesolid && !above2solid) 
								{
									topGround[mx][mz] = (short) (ntg + 1);
									spaceAbove[mx][mz] = (short) (!above3solid ? 3 : 2);
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								}
							}
						}
					}
					
					// Gap en Z (logique similaire)
					if (mx >= 0 && mx < length) 
					{
						// Assure que les voisins existent
						if (mz > 1 && mz < width - 1) 
						{
							if (Math.abs(topGround[mx][mz - 1] - topGround[mx][mz + 1]) < 3 && (topGround[mx][mz - 1] + 2 < topGround[mx][mz] || topGround[mx][mz + 1] + 2 < topGround[mx][mz])) 
							{
								final short ntg = topGround[mx][mz - 1];
								final boolean samesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg, startZ + mapStartZ + j)).getBlock());
								final boolean belowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 1, startZ + mapStartZ + j)).getBlock());
								final boolean below2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 2, startZ + mapStartZ + j)).getBlock());
								final boolean abovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 1, startZ + mapStartZ + j)).getBlock());
								final boolean above2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 2, startZ + mapStartZ + j)).getBlock());
								final boolean above3solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 3, startZ + mapStartZ + j)).getBlock());

								// 1. Niveau ntg
								if (Math.abs(topGround[mx][mz - 1] - topGround[mx][mz + 1]) < 2 && belowsolid && !samesolid && !abovesolid) 
								{
									topGround[mx][mz] = ntg;
									spaceAbove[mx][mz] = (short) (!above2solid ? 3 : 2);
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								} 
								// 2. Niveau ntg - 1 - Assumes bord droit <= bord gauche
								else if (topGround[mx][mz + 1] <= topGround[mx][mz - 1] && below2solid && !belowsolid && !samesolid && !abovesolid) 
								{
									topGround[mx][mz] = (short) (ntg - 1);
									spaceAbove[mx][mz] = (short) (!abovesolid ? 3 : 2);
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								} 
								// 3. Niveau ntg + 1 - Assumes bord droit >= bord gauche
								else if (topGround[mx][mz + 1] >= topGround[mx][mz - 1] && samesolid && !abovesolid && !above2solid) 
								{
									topGround[mx][mz] = (short) (ntg + 1);
									spaceAbove[mx][mz] = (short) (!above3solid ? 3 : 2);
									gapFilled = true;
									topAdjusted[mx][mz] = true;
								}
							}
						}
					}
				}
			}

			/*
			 * Deuxième passe: Trous de deux blocs de large, sur le même niveau.
			 */
			for (int i = -5; i < 21; i++) 
			{
				for (int j = -5; j < 21; j++) 
				{
					final int mx = i + startX;
					final int mz = j + startZ;

					// S'assurer d'être dans les limites de la carte pour les accès [mx][mz] et [mx+1][mz]
					if (mx < 0 || mx >= length || mz < 0 || mz >= width) {
						continue;
					}

					// Gap de deux blocs en X
					if (mz >= 0 && mz < width) {
						if (mx > 1 && mx < length - 2) {
							// Bords à 2 blocs d'écart sont au même niveau et ce niveau est sous le sol actuel.
							if (topGround[mx - 1][mz] == topGround[mx + 2][mz] && topGround[mx - 1][mz] < topGround[mx][mz] && topGround[mx - 1][mz] < topGround[mx + 1][mz]) 
							{
								final short ntg = topGround[mx - 1][mz];
								
								// Blocs dans la première colonne (mx)
								final boolean samesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg, startZ + mapStartZ + j)).getBlock());
								final boolean belowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 1, startZ + mapStartZ + j)).getBlock());
								final boolean abovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 1, startZ + mapStartZ + j)).getBlock());
								final boolean above2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 2, startZ + mapStartZ + j)).getBlock());

								// Blocs dans la deuxième colonne (mx+1)
								final boolean nextsamesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i + 1, ntg, startZ + mapStartZ + j)).getBlock());
								final boolean nextbelowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i + 1, ntg - 1, startZ + mapStartZ + j)).getBlock());
								final boolean nextabovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i + 1, ntg + 1, startZ + mapStartZ + j)).getBlock());
								final boolean nextabove2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i + 1, ntg + 2, startZ + mapStartZ + j)).getBlock());

								// check if same level works (solide en ntg-1 pour les deux, air en ntg et ntg+1 pour les deux)
								if (belowsolid && nextbelowsolid && !samesolid && !nextsamesolid && !abovesolid && !nextabovesolid) 
								{
									topGround[mx][mz] = ntg;
									
									// S'assurer que [mx+1][mz] est dans les limites avant d'y accéder.
									if (mx + 1 < length) {
										topGround[mx + 1][mz] = ntg;
										spaceAbove[mx + 1][mz] = (short) (!nextabove2solid ? 3 : 2);
									}
									
									spaceAbove[mx][mz] = (short) (!above2solid ? 3 : 2);

									gapFilled = true;
									topAdjusted[mx][mz] = true; // On marque seulement le premier comme ajusté
								}
							}
						}
					}
					
					// Gap de deux blocs en Z
					if (mx >= 0 && mx < length) 
					{
						if (mz > 1 && mz < width - 2) 
						{
							// Bords à 2 blocs d'écart sont au même niveau et ce niveau est sous le sol actuel.
							if (topGround[mx][mz - 1] == topGround[mx][mz + 2] && topGround[mx][mz - 1] < topGround[mx][mz] && topGround[mx][mz - 1] < topGround[mx][mz + 1]) 
							{
								final short ntg = topGround[mx][mz - 1];
								
								// Blocs dans la première colonne (mz)
								final boolean samesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg, startZ + mapStartZ + j)).getBlock());
								final boolean belowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 1, startZ + mapStartZ + j)).getBlock());
								final boolean abovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 1, startZ + mapStartZ + j)).getBlock());
								final boolean above2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 2, startZ + mapStartZ + j)).getBlock());

								// Blocs dans la deuxième colonne (mz+1)
								final boolean nextsamesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg, startZ + mapStartZ + j + 1)).getBlock());
								final boolean nextbelowsolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg - 1, startZ + mapStartZ + j + 1)).getBlock());
								final boolean nextabovesolid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 1, startZ + mapStartZ + j + 1)).getBlock());
								final boolean nextabove2solid = isBlockSolid(world.getBlockState(new BlockPos(startX + mapStartX + i, ntg + 2, startZ + mapStartZ + j + 1)).getBlock());

								// check if same level works
								if (belowsolid && nextbelowsolid && !samesolid && !nextsamesolid && !abovesolid && !nextabovesolid) {
									topGround[mx][mz] = ntg;
									
									// S'assurer que [mx][mz+1] est dans les limites avant d'y accéder.
									if (mz + 1 < width) {
										topGround[mx][mz + 1] = ntg;
										spaceAbove[mx][mz + 1] = (short) (!nextabove2solid ? 3 : 2);
									}
									
									spaceAbove[mx][mz] = (short) (!above2solid ? 3 : 2);

									gapFilled = true;
									topAdjusted[mx][mz] = true; // On marque seulement le premier comme ajusté
								}
							}
						}
					}
				}
			}
		}

		// --- 5. Finalisation (Suppression de l'espace au-dessus près du danger) ---
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {

				final int mx = i + startX;
				final int mz = j + startZ;

				// S'assurer d'être dans les limites
				if (mx < 0 || mx >= length || mz < 0 || mz >= width) {
					continue;
				}

				if (danger[mx][mz]) {
					// Si danger, on supprime l'espace au-dessus dans une zone 5x5 centrée.
					for (int k = -2; k < 3; k++) {
						for (int l = -2; l < 3; l++) {
							final int nx = mx + k;
							final int nz = mz + l;
							// Le code original semble avoir une erreur ici, la boucle ne fait rien.
							// L'intention est probablement de mettre spaceAbove à 0 pour tous les blocs voisins.
							if (nx >= 0 && nx < length && nz >= 0 && nz < width) {
								spaceAbove[nx][nz] = 0;
							}
						}
					}
					// On doit aussi mettre spaceAbove à 0 pour le point de danger lui-même.
					spaceAbove[mx][mz] = 0;
				}
			}
		}
	}
	
	/**
	 * Détermine la prochaine chunk à mettre à jour et lance un thread.
	 */
	private void updateNextChunk()
	{

		updateCounter = (updateCounter + 1) % frequency;

		if (updateCounter != 0) {
			return; // N'update pas à chaque tick, mais avec une fréquence calculée.
		}

		// Sélection de la prochaine chunk.
		lastUpdatedX++;
		if (lastUpdatedX * 16 >= length) {
			lastUpdatedX = 0;
			lastUpdatedZ++;
		}

		if (lastUpdatedZ * 16 >= width) {
			lastUpdatedZ = 0;
		}

		// Lancement du thread pour mettre à jour la chunk en arrière-plan.
		final UpdateThread thread = new UpdateThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.x = lastUpdatedX << 4; // Coordonnée de début X de la chunk (dans le tableau)
		thread.z = lastUpdatedZ << 4; // Coordonnée de début Z de la chunk (dans le tableau)

		thread.start();
	}

    /**
	 * Vérifie si le bloc est considéré comme du 'sol' (naturel).
	 * @param b Le bloc à vérifier.
	 * @return true si c'est un bloc de sol, false sinon.
	 */
    private static boolean isBlockIdGround(final Block b)
	{
		if (b == null) return false;
		
        return (b == Blocks.bedrock || b == Blocks.clay || b == Blocks.dirt ||
                b == Blocks.grass || b == Blocks.gravel || b == Blocks.obsidian ||
                b == Blocks.sand || b == Blocks.farmland);
	}

    /**
	 * Vérifie si le bloc est considéré comme du 'sol ou plafond' (matériaux durs).
	 * @param b Le bloc à vérifier.
	 * @return true si c'est un bloc de sol ou plafond, false sinon.
	 */
    private static boolean isBlockIdGroundOrCeiling(final Block b)
	{
		if (b == null) return false;
		
		return (b == Blocks.stone || b == Blocks.sandstone);
	}
	
	/**
	 * Vérifie si le bloc est considéré comme 'solide' (bloque le mouvement).
	 * @param block Le bloc à vérifier.
	 * @return true si le bloc est solide, false sinon.
	 */
	private static boolean isBlockSolid(Block block)
	{
		if (block == null) return false;
		
		// isFullCube est une méthode de Block. La logique est correcte.
		return block.isFullCube() || block == Blocks.glass || block == Blocks.glass_pane || block instanceof BlockSlab || block instanceof BlockStairs || block instanceof BlockFence || block instanceof BlockWall || block == MillBlocks.paperWall;
	}
	
	//////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	/**
	 * Thread interne pour mettre à jour une chunk en arrière-plan.
	 */
	public class UpdateThread extends Thread 
	{
		int x; // Coordonnée X de la chunk (dans le tableau)
		int z; // Coordonnée Z de la chunk (dans le tableau)

		@Override
		public void run() { 
			// Ajout d'une vérification de nullité pour le monde (robustesse)
			if (world != null) {
				updateChunk(x, z); 
			}
		}
	}
}