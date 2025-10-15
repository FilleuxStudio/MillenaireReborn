package org.millenaire;

import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.entities.EntityMillVillager;
import org.millenaire.common.worldgen.VillageGenerator;
import org.millenaire.client.gui.MillAchievement;
import org.millenaire.client.gui.MillGuiHandler;
import org.millenaire.common.items.MillItems;
import org.millenaire.common.networking.MillPacket;
import org.millenaire.common.networking.PacketExportBuilding;
import org.millenaire.common.networking.PacketImportBuilding;
import org.millenaire.common.networking.PacketSayTranslatedMessage;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mod(Millenaire.MODID)
public class Millenaire {
    public static final String MODID = "millenaire";
    public static final String NAME = "Mill\u00e9naire";
    public static final String VERSION = "7.0.0";

    public static boolean isServer = true;
    
    public List<net.minecraft.world.level.block.Block> forbiddenBlocks;
    
    public static Millenaire instance;
    
    // Deferred Register pour le Creative Tab
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    
    public static final Supplier<CreativeModeTab> TAB_MILLENAIRE = CREATIVE_MODE_TABS.register("mill_tab", 
        () -> CreativeModeTab.builder()
            .title(net.minecraft.network.chat.Component.translatable("itemGroup.mill_tab"))
            .icon(() -> new ItemStack(MillItems.denierOr.get()))
            .displayItems((parameters, output) -> {
                // Ici vous ajouterez les items à afficher dans le tab
            })
            .build());
    
    public Millenaire(IEventBus modEventBus) {
        instance = this;
        
        // Enregistrement des deferred registers
        CREATIVE_MODE_TABS.register(modEventBus);
        
        // Enregistrement des événements
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerPackets);
        
        // Pré-initialisation
        MillConfig.preinitialize();
        NeoForge.EVENT_BUS.register(new RaidEvent.RaidEventHandler());
        
        setForbiddenBlocks();
        
        // Initialisation des blocks et items
        MillBlocks.preinitialize();
        MillItems.preinitialize();
        EntityMillVillager.preinitialize();
        MillCulture.preinitialize();
        MillAchievement.preinitialize();
        
        // Configuration côté client
        if (FMLEnvironment.dist.isClient()) {
            MillBlocks.prerender();
            MillItems.prerender();
            EntityMillVillager.prerender();
            MillConfig.eventRegister(modEventBus);
            isServer = false;
        }
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        // Enregistrement du générateur de villages
        net.neoforged.neoforge.registries.GameData.registerWorldGenerator(
            new VillageGenerator(), 1000
        );
        
        // Render côté client
        if (FMLEnvironment.dist.isClient()) {
            MillBlocks.render();
            MillItems.render();
        }
    }
    
    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID)
            .versioned("1.0")
            .optional();
        
        registrar.playToServer(
            MillPacket.TYPE, 
            MillPacket.STREAM_CODEC,
            (payload, context) -> MillPacket.PacketHandlerOnServer.handle(payload, context)
        );
        
        registrar.playToServer(
            PacketImportBuilding.TYPE,
            PacketImportBuilding.STREAM_CODEC,
            (payload, context) -> PacketImportBuilding.Handler.handle(payload, context)
        );
        
        registrar.playToClient(
            PacketSayTranslatedMessage.TYPE,
            PacketSayTranslatedMessage.STREAM_CODEC,
            (payload, context) -> PacketSayTranslatedMessage.Handler.handle(payload, context)
        );
        
        registrar.playToServer(
            PacketExportBuilding.TYPE,
            PacketExportBuilding.STREAM_CODEC,
            (payload, context) -> PacketExportBuilding.Handler.handle(payload, context)
        );
    }
    
    private void setForbiddenBlocks() {
        String parsing = MillConfig.forbiddenBlocks.substring(11);
        forbiddenBlocks = new ArrayList<>();
        
        for (final String name : parsing.split(", |,")) {
            ResourceLocation resourceLocation = ResourceLocation.tryParse(name);
            if (resourceLocation != null) {
                net.minecraft.core.RegistryAccess registries = ServerLifecycleHooks.getCurrentServer().registryAccess();
                net.minecraft.core.registries.BuiltInRegistries.BLOCK.getOptional(resourceLocation)
                    .ifPresent(forbiddenBlocks::add);
            }
        }
    }
    
    // Méthode pour enregistrer les commandes (à appeler ailleurs)
    public static void registerCommands(net.neoforged.neoforge.server.command.ConfigCommand registration) {
        // L'enregistrement des commandes se fait maintenant via l'événement RegisterCommands
        NeoForge.EVENT_BUS.addListener((net.neoforged.neoforge.event.RegisterCommandsEvent event) -> {
            event.getDispatcher().register(
                net.minecraft.commands.Commands.literal("millenaire")
                    .then(MillCommand.register())
            );
        });
    }
}