package org.millenaire.common.worldgen;

import org.millenaire.MillConfig;
import org.millenaire.VillageTracker;
import org.millenaire.common.blocks.MillBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.neoforged.neoforge.common.world.ModifiableBiomeSource;
import net.neoforged.neoforge.event.level.ChunkEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Random;

@EventBusSubscriber(modid = "millenaire", bus = EventBusSubscriber.Bus.GAME)
public class VillageGenerator {

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel world) {
            // Génération basée sur le chargement des chunks
            generateInChunk(world, event.getChunk().getPos().x, event.getChunk().getPos().z);
        }
    }

    private static void generateInChunk(ServerLevel world, int chunkX, int chunkZ) {
        if (world.dimension() == Level.OVERWORLD) {
            BlockPos pos = new BlockPos(chunkX * 16, 0, chunkZ * 16);
            
            try {
                generateVillageAt(world.getRandom(), world.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, pos), world);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Attempt to generate the village
     */
    private static boolean generateVillageAt(Random rand, BlockPos pos, ServerLevel world) {
        // Vérifier la configuration
        if (!MillConfig.generateVillages && !MillConfig.generateLoneBuildings) {
            return false;
        }

        // Vérifier la distance du spawn
        BlockPos spawnPos = world.getSharedSpawnPos();
        if (spawnPos.distSqr(pos) < MillConfig.spawnDistance * MillConfig.spawnDistance) {
            return false;
        }

        // Vérifier s'il y a des villages proches
        if (!VillageTracker.get(world).getNearVillages(pos, MillConfig.minVillageDistance).isEmpty()) {
            return false;
        }

        // Vérifier le biome (exemple)
        Holder<Biome> biome = world.getBiome(pos);
        if (!isSuitableBiome(biome)) {
            return false;
        }

        // Générer le village stone avec une certaine probabilité
        if (rand.nextInt(50) == 1) {
            // Vérifier que le chunk est chargé
            if (world.hasChunk(pos.getX() >> 4, pos.getZ() >> 4)) {
                world.setBlockAndUpdate(pos, MillBlocks.VILLAGE_STONE.get().defaultBlockState());
                return true;
            }
        }

        return false;
    }

    /**
     * Vérifie si le biome est approprié pour la génération de villages
     */
    private static boolean isSuitableBiome(Holder<Biome> biome) {
        // Implémente ta logique de vérification de biome ici
        // Par exemple, exclure les océans, rivières, etc.
        ResourceLocation biomeName = biome.unwrapKey().map(ResourceKey::location).orElse(null);
        
        if (biomeName != null) {
            String path = biomeName.getPath();
            // Exclure certains biomes inappropriés
            return !path.contains("ocean") && 
                   !path.contains("river") && 
                   !path.contains("beach") &&
                   !path.contains("deep");
        }
        
        return true;
    }

    /**
     * Alternative: Système de génération plus avancé avec placement de structures
     */
    public static void generateVillageStructure(ServerLevel world, BlockPos centerPos, Random random) {
        // Logique de génération de structure de village plus complexe
        // Peut être appelée depuis d'autres systèmes de génération
        
        // Exemple: générer des bâtiments autour du village stone
        for (int i = 0; i < 5; i++) {
            int offsetX = random.nextInt(32) - 16;
            int offsetZ = random.nextInt(32) - 16;
            BlockPos buildingPos = centerPos.offset(offsetX, 0, offsetZ);
            buildingPos = world.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.WORLD_SURFACE, buildingPos);
            
            // Placer un bâtiment de test
            // world.setBlockAndUpdate(buildingPos, MillBlocks.TEST_BUILDING.get().defaultBlockState());
        }
    }
}