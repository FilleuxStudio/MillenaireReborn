package org.millenaire;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class MillConfig {
    // Configuration values
    public static ModConfigSpec.BooleanValue LEARN_LANGUAGES;
    public static ModConfigSpec.BooleanValue VILLAGE_ANNOUNCEMENT;
    public static ModConfigSpec.BooleanValue DISPLAY_NAMES;
    public static ModConfigSpec.IntValue NAME_DISTANCE;
    public static ModConfigSpec.IntValue DIALOGUE_DISTANCE;
    
    public static ModConfigSpec.BooleanValue GENERATE_VILLAGES;
    public static ModConfigSpec.BooleanValue GENERATE_LONE_BUILDINGS;
    public static ModConfigSpec.IntValue MIN_VILLAGE_DISTANCE;
    public static ModConfigSpec.IntValue MIN_LONE_DISTANCE;
    public static ModConfigSpec.IntValue MIN_VILLAGE_LONE_DISTANCE;
    public static ModConfigSpec.IntValue SPAWN_DISTANCE;
    
    public static ModConfigSpec.IntValue LOADED_RADIUS;
    public static ModConfigSpec.IntValue MIN_BUILDING_DISTANCE;
    public static ModConfigSpec.IntValue MAX_CHILDREN;
    public static ModConfigSpec.BooleanValue BUILD_PATHS;
    public static ModConfigSpec.IntValue VILLAGE_RELATION_DISTANCE;
    public static ModConfigSpec.IntValue BANDIT_RAID_DISTANCE;
    public static ModConfigSpec.IntValue RAID_PERCENT_CHANCE;
    public static ModConfigSpec.ConfigValue<String> FORBIDDEN_BLOCKS;
    
    // Static references to the actual values for backward compatibility
    public static boolean learnLanguages;
    public static boolean villageAnnouncement;
    public static boolean displayNames;
    public static int nameDistance;
    public static int dialogueDistance;
    
    public static boolean generateVillages;
    public static boolean generateLoneBuildings;
    public static int minVillageDistance;
    public static int minLoneDistance;
    public static int minVillageLoneDistance;
    public static int spawnDistance;
    
    public static int loadedRadius;
    public static int minBuildingDistance;
    public static int maxChildren;
    public static boolean buildPaths;
    public static int villageRelationDistance;
    public static int banditRaidDistance;
    public static int raidPercentChance;
    public static String forbiddenBlocks;
    
    // Config specification
    public static final ModConfigSpec SPEC;
    
    static {
        ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
        
        // UI Options
        BUILDER.comment("UI Options").push("uioptions");
        
        LEARN_LANGUAGES = BUILDER
            .comment("Whether players can learn languages over time")
            .define("learnLanguages", true);
        
        VILLAGE_ANNOUNCEMENT = BUILDER
            .comment("Whether villages announce when they have new trades")
            .define("villageAnnouncementRecipe", false);
        
        DISPLAY_NAMES = BUILDER
            .comment("Whether to display villager names above their heads")
            .define("displayNames", true);
        
        NAME_DISTANCE = BUILDER
            .comment("Maximum distance to display villager names")
            .defineInRange("nameDistance", 20, 1, 100);
        
        DIALOGUE_DISTANCE = BUILDER
            .comment("Maximum distance to start a dialogue")
            .defineInRange("dialogueDistance", 5, 1, 50);
        
        BUILDER.pop();
        
        // World Generation
        BUILDER.comment("World Generation").push("worldgen");
        
        GENERATE_VILLAGES = BUILDER
            .comment("Whether to generate villages")
            .define("generateVillages", true);
        
        GENERATE_LONE_BUILDINGS = BUILDER
            .comment("Whether to generate lone buildings")
            .define("generateLoneBuildings", true);
        
        MIN_VILLAGE_DISTANCE = BUILDER
            .comment("Minimum distance between villages")
            .defineInRange("minVillageDistance", 600, 100, 5000);
        
        MIN_LONE_DISTANCE = BUILDER
            .comment("Minimum distance between lone buildings")
            .defineInRange("minLoneDistance", 600, 100, 5000);
        
        MIN_VILLAGE_LONE_DISTANCE = BUILDER
            .comment("Minimum distance between villages and lone buildings")
            .defineInRange("minVillageLoneDistance", 300, 50, 2500);
        
        SPAWN_DISTANCE = BUILDER
            .comment("Distance from spawn where villages generate")
            .defineInRange("spawnDistance", 200, 0, 1000);
        
        BUILDER.pop();
        
        // Village Behavior
        BUILDER.comment("Village Behavior").push("villagebehavior");
        
        LOADED_RADIUS = BUILDER
            .comment("Radius around player where villages are active")
            .defineInRange("loadedRadius", 200, 50, 1000);
        
        MIN_BUILDING_DISTANCE = BUILDER
            .comment("Minimum distance between buildings")
            .defineInRange("minBuildingDistance", 2, 1, 10);
        
        MAX_CHILDREN = BUILDER
            .comment("Maximum children per village")
            .defineInRange("maxChildren", 10, 1, 50);
        
        BUILD_PATHS = BUILDER
            .comment("Whether villages build paths")
            .define("buildPaths", true);
        
        VILLAGE_RELATION_DISTANCE = BUILDER
            .comment("Distance for village relations")
            .defineInRange("villageRelationDistance", 2000, 500, 10000);
        
        BANDIT_RAID_DISTANCE = BUILDER
            .comment("Distance for bandit raids")
            .defineInRange("banditRaidDistance", 1500, 500, 10000);
        
        RAID_PERCENT_CHANCE = BUILDER
            .comment("Chance of raids")
            .defineInRange("raidPercentChance", 20, 0, 100);
        
        FORBIDDEN_BLOCKS = BUILDER
            .comment("List of forbidden blocks")
            .define("forbiddenBlocks", "forbidden: ");
        
        BUILDER.pop();
        
        SPEC = BUILDER.build();
    }
    
    public static void preinitialize() {
        // Load config values
        loadConfig();
    }
    
    public static void eventRegister(IEventBus modEventBus) {
        // Register config events
        modEventBus.addListener(MillConfig::onConfigLoad);
        modEventBus.addListener(MillConfig::onConfigReload);
    }
    
    public static void loadConfig() {
        // Map the ModConfigSpec values to the static fields for backward compatibility
        learnLanguages = LEARN_LANGUAGES.get();
        villageAnnouncement = VILLAGE_ANNOUNCEMENT.get();
        displayNames = DISPLAY_NAMES.get();
        nameDistance = NAME_DISTANCE.get();
        dialogueDistance = DIALOGUE_DISTANCE.get();
        
        generateVillages = GENERATE_VILLAGES.get();
        generateLoneBuildings = GENERATE_LONE_BUILDINGS.get();
        minVillageDistance = MIN_VILLAGE_DISTANCE.get();
        minLoneDistance = MIN_LONE_DISTANCE.get();
        minVillageLoneDistance = MIN_VILLAGE_LONE_DISTANCE.get();
        spawnDistance = SPAWN_DISTANCE.get();
        
        loadedRadius = LOADED_RADIUS.get();
        minBuildingDistance = MIN_BUILDING_DISTANCE.get();
        maxChildren = MAX_CHILDREN.get();
        buildPaths = BUILD_PATHS.get();
        villageRelationDistance = VILLAGE_RELATION_DISTANCE.get();
        banditRaidDistance = BANDIT_RAID_DISTANCE.get();
        raidPercentChance = RAID_PERCENT_CHANCE.get();
        forbiddenBlocks = FORBIDDEN_BLOCKS.get();
    }
    
    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(Millenaire.MODID)) {
            loadConfig();
            System.out.println("Reloaded Millenaire Config");
        }
    }
    
    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(Millenaire.MODID)) {
            loadConfig();
            System.out.println("Loaded Millenaire Config");
        }
    }
}