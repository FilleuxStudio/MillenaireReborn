package org.millenaire;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.entities.MillEntities;
import org.millenaire.common.items.MillItems;

import java.util.ArrayList;
import java.util.List;

@Mod(Millenaire.MODID)
public class Millenaire {
    public static final String MODID = "millenaire";
    public static final String NAME = "Millénaire";
    
    public static Millenaire instance;
    public static List<net.minecraft.world.level.block.Block> forbiddenBlocks = new ArrayList<>();
    
    // Système d'enregistrement NeoForge
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    // Onglet créatif Millénaire
    public static final DeferredRegister.Data<CreativeModeTab> TAB_MILLENAIRE = CREATIVE_TABS.register(
        "millenaire_tab", 
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.millenaire"))
            .icon(() -> new ItemStack(MillBlocks.DECORATIVE_STONE.get()))
            .displayItems((params, output) -> {
                // === BLOCS DÉCORATIFS ===
                // Pierre Décorative
                output.accept(MillBlocks.DECORATIVE_STONE.get());
                
                // Bois Décoratif  
                output.accept(MillBlocks.DECORATIVE_WOOD.get());
                
                // Terre Décorative
                output.accept(MillBlocks.DECORATIVE_EARTH.get());
                
                // Planches de Gazon
                output.accept(MillBlocks.SOD_PLANKS.get());
                
                // Sériculture et Briques
                output.accept(MillBlocks.EMPTY_SERICULTURE.get());
                output.accept(MillBlocks.MUD_BRICK.get());
                
                // === ARCHITECTURE ===
                // Chaume
                output.accept(MillBlocks.THATCH_SLAB.get());
                output.accept(MillBlocks.THATCH_STAIRS.get());
                
                // Style Byzantin
                output.accept(MillBlocks.BYZANTINE_TILE.get());
                output.accept(MillBlocks.BYZANTINE_STONE_TILE.get());
                output.accept(MillBlocks.BYZANTINE_TILE_SLAB.get());
                output.accept(MillBlocks.BYZANTINE_TILE_STAIRS.get());
                
                // Sculpture Inuit
                output.accept(MillBlocks.INUIT_CARVING.get());
                
                // Mur de papier
                output.accept(MillBlocks.PAPER_WALL.get());
                
                // Cadres en bois
                output.accept(MillBlocks.PLAIN_TIMBER_FRAME.get());
                output.accept(MillBlocks.CROSS_TIMBER_FRAME.get());
                
                // === BLOCS FONCTIONNELS ===
                // Coffre Millénaire
                output.accept(MillBlocks.MILL_CHEST.get());
                
                // Panneau Millénaire
                output.accept(MillBlocks.MILL_SIGN.get());
                
                // Pierre d'Alchimiste
                output.accept(MillBlocks.ALCHEMISTS.get());
                
                // Système de chemins
                output.accept(MillBlocks.MILL_PATH.get());
                output.accept(MillBlocks.MILL_PATH_SLAB.get());
                
                // Minerai
                output.accept(MillBlocks.GALIANITE_ORE.get());
                output.accept(MillBlocks.GALIANITE_BLOCK.get());
                
                // Pierre de Village
                output.accept(MillBlocks.VILLAGE_STONE.get());
                
                // Position Stockée (pour le debug)
                output.accept(MillBlocks.STORED_POSITION.get());
                
                // Mur de terre
                output.accept(MillBlocks.DIRT_WALL.get());
                
                // Briques cuites et séchées
                output.accept(MillBlocks.COOKED_BRICK.get());
                output.accept(MillBlocks.DRIED_BRICK.get());
                
                // Ornement en or
                output.accept(MillBlocks.GOLD_ORNAMENT.get());
                
                // === CULTURES ===
                output.accept(MillBlocks.CROP_TURMERIC.get());
                output.accept(MillBlocks.CROP_RICE.get());
                output.accept(MillBlocks.CROP_MAIZE.get());
                output.accept(MillBlocks.CROP_GRAPE_VINE.get());
                
                // === ITEMS ===
                // Monnaie
                output.accept(MillItems.DENIER.get());
                output.accept(MillItems.DENIER_ARGENT.get());
                output.accept(MillItems.DENIER_OR.get());
                
                // Nourriture
                output.accept(MillItems.GRAPES.get());
                output.accept(MillItems.MAIZE.get());
                output.accept(MillItems.RICE.get());
                output.accept(MillItems.TURMERIC.get());
                output.accept(MillItems.CALVA.get());
                output.accept(MillItems.CIDER.get());
                output.accept(MillItems.WINE.get());
                
                // Parchemins
                output.accept(MillItems.PARCHMENT_ALL.get());
                output.accept(MillItems.PARCHMENT_VILLAGER.get());
                output.accept(MillItems.PARCHMENT_BUILDING.get());
                output.accept(MillItems.PARCHMENT_ITEM.get());
                
                // Outils et armes
                output.accept(MillItems.NORMAN_SWORD.get());
                output.accept(MillItems.NORMAN_AXE.get());
                output.accept(MillItems.NORMAN_PICKAXE.get());
                output.accept(MillItems.NORMAN_SHOVEL.get());
                output.accept(MillItems.NORMAN_HOE.get());
                
                // Armures
                output.accept(MillItems.NORMAN_HELMET.get());
                output.accept(MillItems.NORMAN_CHESTPLATE.get());
                output.accept(MillItems.NORMAN_LEGGINGS.get());
                output.accept(MillItems.NORMAN_BOOTS.get());
                
                // Amulettes et objets spéciaux
                output.accept(MillItems.AMULET_VISHNU.get());
                output.accept(MillItems.AMULET_YGGDRASIL.get());
                output.accept(MillItems.AMULET_ALCHEMIST.get());
                output.accept(MillItems.AMULET_SKOLL_HATI.get());
                
                // Baguettes
                output.accept(MillItems.WAND_SUMMONING.get());
                output.accept(MillItems.WAND_NEGATION.get());
                output.accept(MillItems.WAND_CREATIVE.get());
                
                // Divers
                output.accept(MillItems.MILL_PURSE.get());
                output.accept(MillItems.GALIANITE_DUST.get());
                output.accept(MillItems.UNKNOWN_POWDER.get());
                output.accept(MillItems.TUNING_FORK.get());
            })
            .build()
    );

    public Millenaire(IEventBus modEventBus) {
        instance = this;
        
        // Enregistrement de la configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MillConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MillConfig.SERVER_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MillConfig.COMMON_SPEC);
        
        // Enregistrement des systèmes
        MillBlocks.BLOCKS.register(modEventBus);
        MillBlocks.BLOCK_ENTITIES.register(modEventBus);
        MillItems.ITEMS.register(modEventBus);
        MillEntities.ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        
        // Événements
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
        
        // Initialisation des cultures
        MillCulture.preinitialize();
        
        System.out.println("Millénaire mod initialisé avec succès pour NeoForge 1.21.10");
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Initialisation après que tous les registres sont prêts
        event.enqueueWork(() -> {
            // Code d'initialisation thread-safe
            setForbiddenBlocks();
            registerVillagerPOIs();
            setupNetworking();
            
            // Enregistrement des recettes
            registerRecipes();
            
            // Initialisation des trackers côté serveur
            if (event.getSide().isServer()) {
                VillageTracker.initializeServer();
                PlayerTracker.initializeServer();
            }
        });
    }
    
    private void onServerStarting(ServerStartingEvent event) {
        // Enregistrement des commandes
        event.getServer().getCommands().getDispatcher().register(MillCommand.getCommand());
        
        System.out.println("Millénaire server starting - Commandes initialisées");
    }
    
    private void setForbiddenBlocks() {
        // Récupération des blocs interdits depuis la configuration
        String forbiddenBlocksConfig = MillConfig.forbiddenBlocks();
        if (forbiddenBlocksConfig != null && !forbiddenBlocksConfig.isEmpty()) {
            String[] blockNames = forbiddenBlocksConfig.split(",");
            for (String blockName : blockNames) {
                String trimmedName = blockName.trim();
                if (!trimmedName.isEmpty()) {
                    // Logique pour trouver le bloc dans le registre
                    // Note: Cette logique devra être adaptée selon ton système de recherche de blocs
                    System.out.println("Bloc interdit configuré: " + trimmedName);
                    
                    // Exemple de recherche (à adapter):
                    /*
                    ResourceLocation blockId = ResourceLocation.tryParse(trimmedName);
                    if (blockId != null) {
                        Block block = BuiltInRegistries.BLOCK.get(blockId);
                        if (block != Blocks.AIR) {
                            forbiddenBlocks.add(block);
                        }
                    }
                    */
                }
            }
        }
        System.out.println("Chargement des blocs interdits terminé. " + forbiddenBlocks.size() + " blocs configurés.");
    }
    
    private void registerVillagerPOIs() {
        // Enregistrement des Points of Interest pour les villageois Millénaire
        // Cette méthode sera implémentée lorsque le système d'entités sera migré
        System.out.println("Enregistrement des POIs pour les villageois Millénaire");
        
        // Exemple d'enregistrement (à adapter):
        /*
        POIRegister.register("millenaire:chief", 
            MillBlocks.VILLAGE_STONE.get(), 
            1, // max tickets
            1  // valid range
        );
        */
    }
    
    private void setupNetworking() {
        // Configuration du système de networking
        // Cette méthode sera implémentée lorsque le système de networking sera migré
        System.out.println("Configuration du networking Millénaire");
        
        // Exemple (à adapter):
        /*
        NETWORK = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
        );
        */
    }
    
    private void registerRecipes() {
        // Enregistrement des recettes de craft et de fusion
        // À implémenter avec le nouveau système de données
        System.out.println("Enregistrement des recettes Millénaire");
        
        // Les recettes sont maintenant gérées via JSON dans data/millenaire/recipes/
    }
    
    // Méthode utilitaire pour obtenir l'instance du mod
    public static Millenaire getInstance() {
        return instance;
    }
    
    // Méthode pour accéder au mod ID
    public static String getModId() {
        return MODID;
    }
    
    // Méthode pour vérifier si un bloc est interdit
    public static boolean isBlockForbidden(net.minecraft.world.level.block.Block block) {
        return forbiddenBlocks.contains(block);
    }
}