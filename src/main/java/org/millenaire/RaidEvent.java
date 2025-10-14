package org.millenaire;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class RaidEvent {
    private static final Map<UUID, RaidInfo> activeRaids = new HashMap<>();
    private static final int RAID_CHECK_INTERVAL = 200; // Toutes les 10 secondes (200 ticks)
    private static int tickCounter = 0;
    
    public RaidEvent() {}
    
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (event.getServer() == null) return;
        
        tickCounter++;
        if (tickCounter < RAID_CHECK_INTERVAL) return;
        tickCounter = 0;
        
        // Vérifier les raids actifs
        Iterator<Map.Entry<UUID, RaidInfo>> iterator = activeRaids.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, RaidInfo> entry = iterator.next();
            RaidInfo raid = entry.getValue();
            
            if (raid.shouldEnd()) {
                endRaid(raid);
                iterator.remove();
            } else {
                updateRaid(raid);
            }
        }
        
        // Vérifier les nouveaux raids potentiels
        if (MillConfig.raidPercentChance > 0 && random.nextInt(100) < MillConfig.raidPercentChance) {
            checkForNewRaids(event.getServer());
        }
    }
    
    private void checkForNewRaids(net.minecraft.server.MinecraftServer server) {
        for (ServerLevel level : server.getAllLevels()) {
            VillageTracker tracker = VillageTracker.get(level);
            
            // Logique simplifiée de déclenchement de raid
            // À compléter avec la logique métier spécifique
            List<org.millenaire.village.Village> villages = getVillagesInDimension(level);
            
            for (org.millenaire.village.Village village : villages) {
                if (shouldStartRaid(village, level)) {
                    startRaid(village, level);
                }
            }
        }
    }
    
    private boolean shouldStartRaid(org.millenaire.village.Village village, ServerLevel level) {
        // Conditions pour déclencher un raid :
        // 1. Le village doit être chargé
        // 2. Doit avoir une certaine richesse
        // 3. Probabilité basée sur la configuration
        // 4. Pas déjà en raid
        
        if (isVillageInRaid(village)) return false;
        
        int raidChance = calculateRaidChance(village);
        return random.nextInt(1000) < raidChance;
    }
    
    private int calculateRaidChance(org.millenaire.village.Village village) {
        int baseChance = MillConfig.raidPercentChance;
        // Ajouter des modificateurs basés sur la richesse, les relations, etc.
        return Math.min(baseChance, 100); // Max 100%
    }
    
    private void startRaid(org.millenaire.village.Village village, ServerLevel level) {
        RaidInfo raid = new RaidInfo(village, level);
        activeRaids.put(raid.getId(), raid);
        
        // Annoncer le raid
        announceRaid(raid);
        
        System.out.println("Raid started on village: " + village.getPos());
    }
    
    private void updateRaid(RaidInfo raid) {
        raid.tick();
        
        // Logique de mise à jour du raid
        // Spawn des bandits, attaques, etc.
        if (raid.getTicks() % 100 == 0) { // Toutes les 5 secondes
            spawnRaiders(raid);
        }
    }
    
    private void spawnRaiders(RaidInfo raid) {
        ServerLevel level = raid.getLevel();
        BlockPos villagePos = raid.getVillage().getPos();
        
        // Spawn des bandits autour du village
        for (int i = 0; i < 2 + random.nextInt(3); i++) {
            BlockPos spawnPos = findSpawnPositionNear(villagePos, level, 50);
            if (spawnPos != null) {
                // spawnBandit(spawnPos, level, raid);
            }
        }
    }
    
    private BlockPos findSpawnPositionNear(BlockPos center, ServerLevel level, int radius) {
        for (int i = 0; i < 10; i++) {
            int x = center.getX() + random.nextInt(radius * 2) - radius;
            int z = center.getZ() + random.nextInt(radius * 2) - radius;
            BlockPos pos = new BlockPos(x, level.getHeight(), z);
            
            if (level.getBlockState(pos.below()).isSolid()) {
                return pos;
            }
        }
        return null;
    }
    
    private void endRaid(RaidInfo raid) {
        // Logique de fin de raid
        announceRaidEnd(raid);
        
        System.out.println("Raid ended on village: " + raid.getVillage().getPos());
    }
    
    private void announceRaid(RaidInfo raid) {
        // Annoncer le raid aux joueurs à proximité
        String message = "Un raid commence sur le village à " + raid.getVillage().getPos().toShortString();
        broadcastMessage(raid.getLevel(), raid.getVillage().getPos(), message, 100);
    }
    
    private void announceRaidEnd(RaidInfo raid) {
        String message = "Le raid sur le village à " + raid.getVillage().getPos().toShortString() + " est terminé";
        broadcastMessage(raid.getLevel(), raid.getVillage().getPos(), message, 100);
    }
    
    private void broadcastMessage(ServerLevel level, BlockPos pos, String message, int radius) {
        Vec3 center = Vec3.atCenterOf(pos);
        level.players().forEach(player -> {
            if (player.distanceToSqr(center) < radius * radius) {
                player.displayClientMessage(net.minecraft.network.chat.Component.literal(message), true);
            }
        });
    }
    
    // Méthodes utilitaires
    private List<org.millenaire.village.Village> getVillagesInDimension(ServerLevel level) {
        // Implémentation simplifiée - à adapter
        return new ArrayList<>();
    }
    
    private boolean isVillageInRaid(org.millenaire.village.Village village) {
        return activeRaids.values().stream()
            .anyMatch(raid -> raid.getVillage().equals(village));
    }
    
    private final Random random = new Random();
    
    // Classe interne pour gérer les informations de raid
    public static class RaidInfo {
        private final UUID id;
        private final org.millenaire.village.Village village;
        private final ServerLevel level;
        private final long startTime;
        private int ticks;
        private int duration; // en ticks
        private int raiderCount;
        
        public RaidInfo(org.millenaire.village.Village village, ServerLevel level) {
            this.id = UUID.randomUUID();
            this.village = village;
            this.level = level;
            this.startTime = System.currentTimeMillis();
            this.ticks = 0;
            this.duration = 12000; // 10 minutes par défaut
            this.raiderCount = 0;
        }
        
        public void tick() {
            ticks++;
        }
        
        public boolean shouldEnd() {
            return ticks >= duration || raiderCount <= 0;
        }
        
        // Getters
        public UUID getId() { return id; }
        public org.millenaire.village.Village getVillage() { return village; }
        public ServerLevel getLevel() { return level; }
        public long getStartTime() { return startTime; }
        public int getTicks() { return ticks; }
        public int getRaiderCount() { return raiderCount; }
        
        public void setRaiderCount(int count) { this.raiderCount = count; }
        public void incrementRaiderCount() { this.raiderCount++; }
        public void decrementRaiderCount() { this.raiderCount = Math.max(0, this.raiderCount - 1); }
    }
    
    public static class RaidEventHandler {
        private final RaidEvent raidEvent = new RaidEvent();
        
        @SubscribeEvent
        public void onServerTick(ServerTickEvent.Post event) {
            raidEvent.onServerTick(event);
        }
        
        public static void register() {
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(new RaidEventHandler());
        }
    }
}