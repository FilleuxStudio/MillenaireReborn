package org.millenaire.common.networking;

import org.millenaire.common.building.PlanIO;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class PacketExportBuilding implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("millenaire", "export_building");
    
    private final BlockPos pos;

    public PacketExportBuilding(BlockPos startPos) {
        this.pos = startPos;
    }

    public PacketExportBuilding(FriendlyByteBuf buf) {
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

    public static void handle(final PacketExportBuilding message, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() != null) {
                PlanIO.exportBuilding(context.player(), message.pos);
            }
        });
    }
}