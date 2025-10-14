package org.millenaire;

import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MillConfig {
    // Configuration séparée en Client/Server/Common comme recommandé par NeoForge
    public static final Client CLIENT;
    public static final Server SERVER; 
    public static final Common COMMON;
    
    public static final ModConfigSpec CLIENT_SPEC;
    public static final ModConfigSpec SERVER_SPEC;
    public static final ModConfigSpec COMMON_SPEC;
    
    static {
        final Pair<Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(Client::new);
        final Pair<Server, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(Server::new);
        final Pair<Common, ModConfigSpec> commonSpecPair = new ModConfigSpec.Builder().configure(Common::new);
        
        CLIENT = clientSpecPair.getLeft();
        SERVER = serverSpecPair.getLeft();
        COMMON = commonSpecPair.getLeft();
        CLIENT_SPEC = clientSpecPair.getRight();
        SERVER_SPEC = serverSpecPair.getRight();
        COMMON_SPEC = commonSpecPair.getRight();
    }
    
    // Catégories pour organisation
    public static final String CATEGORY_UI = "uiOptions";
    public static final String CATEGORY_WORLDGEN = "worldGen";
    public static final String CATEGORY_VILLAGE = "villageBehavior";
    
    public static class Client {
        // UI Options
        public final ModConfigSpec.BooleanValue learnLanguages;
        public final ModConfigSpec.BooleanValue displayNames;
        public final ModConfigSpec.IntValue nameDistance;
        public final ModConfigSpec.IntValue dialogueDistance;
        
        public Client(ModConfigSpec.Builder builder) {
            builder.comment("User Interface options").push(CATEGORY_UI);
            
            this.learnLanguages = builder
                .comment("Enable language learning system")
                .translation("config.millenaire.learn_languages")
                .define("learnLanguages", true);
                
            this.displayNames = builder
                .comment("Display villager names above their heads")
                .translation("config.millenaire.display_names")
                .define("displayNames", true);
                
            this.nameDistance = builder
                .comment("Maximum distance to display villager names")
                .translation("config.millenaire.name_distance")
                .defineInRange("nameDistance", 20, 1, 100);
                
            this.dialogueDistance = builder
                .comment("Maximum distance for dialogue interaction")
                .translation("config.millenaire.dialogue_distance")
                .defineInRange("dialogueDistance", 5, 1, 50);
                
            builder.pop();
        }
    }
    
    public static class Server {
        // World Generation
        public final ModConfigSpec.BooleanValue generateVillages;
        public final ModConfigSpec.BooleanValue generateLoneBuildings;
        public final ModConfigSpec.IntValue minVillageDistance;
        public final ModConfigSpec.IntValue spawnDistance;
        
        public Server(ModConfigSpec.Builder builder) {
            builder.comment("World generation options").push(CATEGORY_WORLDGEN);
            
            this.generateVillages = builder
                .comment("Generate Millenaire villages in the world")
                .translation("config.millenaire.generate_villages")
                .define("generateVillages", true);
                
            this.generateLoneBuildings = builder
                .comment("Generate lone buildings in the world")
                .translation("config.millenaire.generate_lone_buildings")
                .define("generateLoneBuildings", true);
                
            this.minVillageDistance = builder
                .comment("Minimum distance between villages (in blocks)")
                .translation("config.millenaire.min_village_distance")
                .defineInRange("minVillageDistance", 600, 100, 5000);
                
            this.spawnDistance = builder
                .comment("Distance from world spawn where villages won't generate (in blocks)")
                .translation("config.millenaire.spawn_distance")
                .defineInRange("spawnDistance", 500, 100, 5000);
                
            builder.pop();
        }
    }
    
    public static class Common {
        // Village Behavior
        public final ModConfigSpec.BooleanValue villageAnnouncement;
        public final ModConfigSpec.IntValue loadedRadius;
        public final ModConfigSpec.IntValue maxChildren;
        public final ModConfigSpec.BooleanValue buildPaths;
        public final ModConfigSpec.ConfigValue<String> forbiddenBlocks;
        
        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Village behavior options").push(CATEGORY_VILLAGE);
            
            this.villageAnnouncement = builder
                .comment("Announce village discoveries in chat")
                .translation("config.millenaire.village_announcement")
                .define("villageAnnouncement", false);
                
            this.loadedRadius = builder
                .comment("Radius around player where villages are loaded (in blocks)")
                .translation("config.millenaire.loaded_radius")
                .defineInRange("loadedRadius", 200, 50, 1000);
                
            this.maxChildren = builder
                .comment("Maximum number of children per village")
                .translation("config.millenaire.max_children")
                .defineInRange("maxChildren", 10, 1, 50);
                
            this.buildPaths = builder
                .comment("Allow villages to build paths between buildings")
                .translation("config.millenaire.build_paths")
                .define("buildPaths", true);
                
            this.forbiddenBlocks = builder
                .comment("List of blocks that villagers cannot use for construction (comma-separated)")
                .translation("config.millenaire.forbidden_blocks")
                .define("forbiddenBlocks", "minecraft:bedrock,minecraft:barrier");
                
            builder.pop();
        }
    }
    
    // Méthodes d'accès statiques pour compatibilité avec ton code existant
    public static boolean learnLanguages() {
        return CLIENT.learnLanguages.get();
    }
    
    public static boolean villageAnnouncement() {
        return COMMON.villageAnnouncement.get();
    }
    
    public static boolean displayNames() {
        return CLIENT.displayNames.get();
    }
    
    public static int nameDistance() {
        return CLIENT.nameDistance.get();
    }
    
    public static int dialogueDistance() {
        return CLIENT.dialogueDistance.get();
    }
    
    public static boolean generateVillages() {
        return SERVER.generateVillages.get();
    }
    
    public static boolean generateLoneBuildings() {
        return SERVER.generateLoneBuildings.get();
    }
    
    public static int minVillageDistance() {
        return SERVER.minVillageDistance.get();
    }
    
    public static int spawnDistance() {
        return SERVER.spawnDistance.get();
    }
    
    public static int loadedRadius() {
        return COMMON.loadedRadius.get();
    }
    
    public static int maxChildren() {
        return COMMON.maxChildren.get();
    }
    
    public static boolean buildPaths() {
        return COMMON.buildPaths.get();
    }
    
    public static String forbiddenBlocks() {
        return COMMON.forbiddenBlocks.get();
    }
    
    // Méthode d'initialisation
    public static void preinitialize() {
        // L'enregistrement se fait maintenant dans la classe principale du mod
        // Les valeurs sont chargées automatiquement par NeoForge
    }
}