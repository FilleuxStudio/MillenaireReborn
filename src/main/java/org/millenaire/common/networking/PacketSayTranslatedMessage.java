package org.millenaire.common.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketSayTranslatedMessage implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("millenaire", "translated_message");
    
    private final String message;

    public PacketSayTranslatedMessage(String message) {
        this.message = message;
    }

    public PacketSayTranslatedMessage(FriendlyByteBuf buf) {
        this.message = buf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(final PacketSayTranslatedMessage message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable(message.message));
            }
        });
    }
}