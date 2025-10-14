package org.millenaire.common.networking;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class PacketRegistration {
    
    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.0");
        
        // Packets envoyés du client vers le serveur
        registrar.playToServer(
            MillPacket.ID, 
            MillPacket::new, 
            MillPacket::handle
        );
        
        registrar.playToServer(
            PacketExportBuilding.ID, 
            PacketExportBuilding::new, 
            PacketExportBuilding::handle
        );
        
        registrar.playToServer(
            PacketImportBuilding.ID, 
            PacketImportBuilding::new, 
            PacketImportBuilding::handle
        );
        
        // Packets envoyés du serveur vers le client
        registrar.playToClient(
            PacketSayTranslatedMessage.ID, 
            PacketSayTranslatedMessage::new, 
            PacketSayTranslatedMessage::handle
        );
    }
}