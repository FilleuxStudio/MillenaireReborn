package org.millenaire.common.networking;

import org.millenaire.common.building.PlanIO;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketImportBuilding implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("millenaire", "import_building");
    
    private final BlockPos pos;

    public PacketImportBuilding(BlockPos startPos) {
        this.pos = startPos;
    }

    public PacketImportBuilding(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(final PacketImportBuilding message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                PlanIO.importBuilding(context.player(), message.pos);
            }
        });
    }
}