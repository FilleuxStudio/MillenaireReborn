package org.millenaire; // Changement de package recommandé

import java.util.ArrayList;
import java.util.List;

import org.millenaire.common.blocks.MillBlocks; // Assurez-vous que MillBlocks est dans le package commun
import org.millenaire.common.building.BuildingLocation;

import net.minecraft.core.BlockPos; // Remplacement de net.minecraft.util.BlockPos
import net.minecraft.server.level.ServerLevel; // Utilisation de ServerLevel au lieu de World pour les opérations côté serveur
import net.minecraft.world.level.Level; // Remplacement de net.minecraft.world.World
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks; // Remplacement de net.minecraft.init.Blocks
import net.minecraft.world.level.block.FenceBlock; // Remplacement de BlockFence
import net.minecraft.world.level.block.LiquidBlock; // Remplacement de BlockLiquid
import net.minecraft.world.level.block.SlabBlock; // Remplacement de BlockSlab
import net.minecraft.world.level.block.StairBlock; // Remplacement de BlockStairs
import net.minecraft.world.level.block.WallBlock; // Remplacement de BlockWall
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk; // Remplacement de net.minecraft.world.chunk.Chunk

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

	public Level world; // Changement de World à Level

	private int lastUpdatedX, lastUpdatedZ;

	private int updateCounter;
	
	public VillageGeography()
	{
		
	}
	
	private void createWorldInfo(final List<BuildingLocation> locations, final BuildingLocation blIP, final int pstartX, final int pstartZ, final int endX, final int endZ)
	{
		// ... (Le calcul des dimensions reste le même)
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
	 * Met à jour pour les classes de blocs modernes de Minecraft.
	 */
	private static boolean isForbiddenBlockForConstruction(final Block block)
	{
		// Utilisation des classes de blocs modernes et des Blocks.XXX
		return block == Blocks.WATER || block == Blocks.FLOWING_WATER || block == Blocks.ICE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA 
			|| block == Blocks.OAK_PLANKS || block == Blocks.COBBLESTONE || block == Blocks.BRICK_BLOCK || block == Blocks.CHEST 
			|| block == Blocks.GLASS || block == Blocks.STONE_BRICKS || block == Blocks.PRISMARINE
			|| block instanceof WallBlock || block instanceof FenceBlock // Utilisation des classes modernes
			|| block == MillBlocks.DECORATIVE_EARTH.get() || block == MillBlocks.DECORATIVE_STONE.get() || block == MillBlocks.DECORATIVE_WOOD.get() // Accès via .get() pour DeferredBlock
			|| block == MillBlocks.BYZANTINE_TILE.get() || block == MillBlocks.BYZANTINE_TILE_SLAB.get() 
			|| block == MillBlocks.BYZANTINE_STONE_TILE.get() || block == MillBlocks.PAPER_WALL.get() || block == MillBlocks.EMPTY_SERICULTURE.get() 
			// Ajouté: Les blocs SlabBlock et StairBlock sont des classes de base modernes
			|| block instanceof SlabBlock || block instanceof StairBlock
			|| block instanceof LiquidBlock; // Remplacement de BlockLiquid
	}
	
	private void registerBuildingLocation(final BuildingLocation bl) 
	{
		buildingLocations.add(bl);

		final int sx = Math.max(bl.minxMargin - mapStartX, 0);
		final int sz = Math.max(bl.minzMargin - mapStartZ, 0);
		final int ex = Math.min(bl.maxxMargin - mapStartX, length + 1);
		final int ez = Math.min(bl.maxzMargin - mapStartZ, width + 1);

		for (int i = sx; i < ex; i++) 
		{
			for (int j = sz; j < ez; j++) 
			{
				buildingLoc[i][j] = true;
			}
		}
	}
	
	public boolean update(final Level world, final List<BuildingLocation> locations, final BuildingLocation blIP, final BlockPos center, final int radius)
	{
		this.world = world;
		this.yBaseline = center.getY();
		locationIP = blIP;

		if (buildingLocations != null && buildingLocations.size() > 0 && buildingLocations.size() == locations.size()) 
		{
			buildingLocations = locations;
			updateNextChunk();
			return false; // Assurez-vous qu'une valeur de retour est présente
		} 
		
		int startX = center.getX(), startZ = center.getZ(), endX = center.getX(), endZ = center.getZ(); 
		
		for (final BuildingLocation location : locations) {
			if (location != null) {
				if (location.position.getX() - location.length / 2 < startX) {
					startX = location.position.getX() - location.length / 2;
				}
				if (location.position.getX() + location.length / 2 > endX) {
					endX = location.position.getX() + location.length / 2;
				}
				if (location.position.getZ() - location.width / 2 < startZ) {
					startZ = location.position.getZ() - location.width / 2;
				}
				if (location.position.getZ() + location.width / 2 > endZ) {
					endZ = location.position.getZ() + location.width / 2;
				}
			}
		}
		
		if (blIP != null) {
			if (blIP.position.getX() - blIP.length / 2 < startX) {
				startX = blIP.position.getX() - blIP.length / 2;
			}
			if (blIP.position.getX() + blIP.length / 2 > endX) {
				endX = blIP.position.getX() + blIP.length / 2;
			}
			if (blIP.position.getZ() - blIP.width / 2 < startZ) {
				startZ = blIP.position.getZ() - blIP.width / 2;
			}
			if (blIP.position.getZ() + blIP.width / 2 > endZ) {
				endZ = blIP.position.getZ() + blIP.width / 2;
			}
		}
		
		startX = Math.min(startX - BUILDING_MARGIN, center.getX() - radius - MAP_MARGIN);
		startZ = Math.min(startZ - BUILDING_MARGIN, center.getZ() - radius - MAP_MARGIN);
		endX = Math.max(endX + BUILDING_MARGIN, center.getX() + radius + MAP_MARGIN);
		endZ = Math.max(endZ + BUILDING_MARGIN, center.getZ() + radius + MAP_MARGIN);

		createWorldInfo(locations, blIP, startX, startZ, endX, endZ);
		return true;
	}

	private void updateChunk(final int x, final int z)
	{
		final int realX = mapStartX + x;
		final int realZ = mapStartZ + z;
		
		// S'assurer que le niveau est un ServerLevel pour l'accès aux chunks
		if (!(world instanceof ServerLevel serverLevel)) {
			return;
		}

		// Utilisation de .getChunk(x, z) pour obtenir le LevelChunk
		final LevelChunk chunk = serverLevel.getChunk(realX >> 4, realZ >> 4);
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				final int xcoord = x + i;
				final int zcoord = z + j;
				
				// Assurez-vous de gérer les débordements de tableau si xcoord ou zcoord sont hors limites (bien que le calcul des boucles devrait l'éviter)
				if (xcoord < length && zcoord < width) {
					
					// Ancienne méthode topGround = (short)chunk.getTopGroundBlock(i, j); est remplacée par le calcul du bloc de surface
					// C'est une simplification pour la migration, la logique exacte de 'topGround' peut varier.
					// Nous utilisons getHighestNonAirBlock pour trouver une hauteur de construction
					BlockPos topPos = chunk.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(realX + i, 0, realZ + j));

					topGround[xcoord][zcoord] = (short) topPos.getY();
					constructionHeight[xcoord][zcoord] = (short) (topPos.getY() + 1);
					
					// Le reste de la logique doit être mis à jour pour utiliser BlockState au lieu de Block/ID
					BlockState state = serverLevel.getBlockState(new BlockPos(realX + i, topPos.getY(), realZ + j));

					danger[xcoord][zcoord] = isDangerousBlock(state); // Appel à la nouvelle méthode isDangerousBlock
					water[xcoord][zcoord] = isBlockWater(state);
					tree[xcoord][zcoord] = isBlockTree(state.getBlock());
					topAdjusted[xcoord][zcoord] = !isBlockIdGround(state.getBlock());

					// Logique originale simplifiée de spaceAbove et constructionHeight
					// Nécessite plus de contexte pour une migration parfaite, mais voici la structure de base:

					int tempHeight = (int) constructionHeight[xcoord][zcoord];
					int yMax = world.getMaxBuildHeight();

					int space = 0;
					for (int y = tempHeight; y < yMax; y++) {
						if (!serverLevel.getBlockState(new BlockPos(realX + i, y, realZ + j)).isAir()) {
							break;
						}
						space++;
					}
					spaceAbove[xcoord][zcoord] = (short)space;

					// Le reste de la logique est inchangé
					if (!buildingLoc[xcoord][zcoord]) {
						buildingForbidden[xcoord][zcoord] = isForbiddenBlockForConstruction(state.getBlock());
					} else {
						buildingForbidden[xcoord][zcoord] = false;
					}
					
					if (yBaseline > topGround[xcoord][zcoord] + VALIDHEIGHTDIFF || yBaseline < topGround[xcoord][zcoord] - VALIDHEIGHTDIFF) {
						canBuild[xcoord][zcoord] = false;
					} else {
						canBuild[xcoord][zcoord] = !buildingForbidden[xcoord][zcoord];
					}
				}
			}
		}
	}
	
	private void updateNextChunk()
	{
		// ... (Mise à jour du compteur inchangée)
		updateCounter = (updateCounter + 1) % frequency;

		if (updateCounter != 0) {
			return;
		}

		lastUpdatedX++;
		if (lastUpdatedX * 16 >= length) {
			lastUpdatedX = 0;
			lastUpdatedZ++;
		}

		if (lastUpdatedZ * 16 >= width) {
			lastUpdatedZ = 0;
		}

		// Dans un environnement moderne, le threading doit être géré avec soin. 
		// Il est fortement DÉCONSEILLÉ d'utiliser Thread.start() pour interagir avec le Level.
		// Le code de modding moderne utilise des tâches asynchrones soumises au serveur 
		// ou exécute le travail directement sur le tick (comme RaidEvent.onServerTick) si le temps le permet.
		// Pour la migration, si vous DEVEZ conserver la logique multi-threadée, elle doit être protégée 
		// et ne pas accéder directement au monde. 
		// Cependant, comme la logique est dans un thread séparé dans l'original, je la conserve 
		// avec des avertissements sur la sécurité dans les mods modernes.
		final UpdateThread thread = new UpdateThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.x = lastUpdatedX << 4;
		thread.z = lastUpdatedZ << 4;

		thread.start();
	}
	
	/**
	 * Classe interne pour le multithreading, fortement déconseillée pour l'accès direct aux données de monde.
	 * DOIT être exécutée uniquement si VillageGeography est sur le côté serveur.
	 */
	private class UpdateThread extends Thread
	{
		public int x, z;
		
		@Override
		public void run()
		{
			if (world.isClientSide) {
				return; // Ne jamais mettre à jour côté client si cela impacte l'état du monde
			}
			updateChunk(x, z);
		}
	}


    private static boolean isBlockIdGround(final Block b)
	{
        // Remplacement des Blocks.XXX
        return (b == Blocks.BEDROCK || b == Blocks.CLAY || b == Blocks.DIRT ||
                b == Blocks.GRASS_BLOCK || b == Blocks.GRAVEL || b == Blocks.OBSIDIAN ||
                b == Blocks.SAND || b == Blocks.FARMLAND);
	}

    // NOUVELLE MÉTHODE (simplification de l'ancien isBlockIdGroundOrCeiling)
    private static boolean isBlockGroundOrCeiling(final Block b)
	{
        // Remplacement des Blocks.XXX
		return (b == Blocks.STONE || b == Blocks.SANDSTONE);
	}
	
	// Utilise la nouvelle méthode BlockState.isSolid() ou une vérification personnalisée
	private static boolean isBlockSolid(Block block)
	{
		// isFullCube() est souvent remplacé par isSolidRender ou isCollisionShapeFullBlock
		// On utilise une heuristique basée sur les propriétés de base des blocs pleins
		return block.defaultBlockState().canOcclude(); // Utilisation d'une propriété de BlockState
	}

	// NOUVELLE MÉTHODE: Vérification si le bloc est dangereux (comme la lave ou la TNT)
	private static boolean isDangerousBlock(BlockState state)
	{
		Block block = state.getBlock();
		return block == Blocks.LAVA || block == Blocks.FLOWING_LAVA || block == Blocks.MAGMA_BLOCK || block == Blocks.TNT;
	}

	// NOUVELLE MÉTHODE: Vérification si le bloc est de l'eau
	private static boolean isBlockWater(BlockState state)
	{
		return state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER;
	}

	// NOUVELLE MÉTHODE: Vérification si le bloc est un arbre (à compléter selon les besoins du mod)
	private static boolean isBlockTree(Block block)
	{
		return block == Blocks.OAK_LOG || block == Blocks.SPRUCE_LOG || block == Blocks.BIRCH_LOG || block == Blocks.JUNGLE_LOG;
	}
}