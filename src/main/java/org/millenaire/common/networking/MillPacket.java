package org.millenaire.common.networking;

import org.millenaire.common.blocks.BlockVillageStone;
import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.items.MillItems;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MillPacket implements CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("millenaire", "mill_packet");
    
    private final int eventID;

    public MillPacket(int eventID) {
        this.eventID = eventID;
    }

    public MillPacket(FriendlyByteBuf buf) {
        this.eventID = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(eventID);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public int getID() {
        return eventID;
    }

    public static void handle(final MillPacket message, final IPayloadContext context) {
        context.enqueueWork(() -> processMessage(message, context));
    }

    private static void processMessage(MillPacket message, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayer sendingPlayer)) {
            return;
        }

        if (message.getID() == 2) {
            ItemStack heldItem = sendingPlayer.getMainHandItem();
            if (heldItem.getItem() != MillItems.WAND_NEGATION.get()) {
                System.err.println("Player not holding Wand of Negation when attempting to delete Village");
            } else {
                Level world = sendingPlayer.level();
                CompoundTag nbt = heldItem.getTag();
                int posX = nbt.getInt("X");
                int posY = nbt.getInt("Y");
                int posZ = nbt.getInt("Z");
                
                if (world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock() instanceof BlockVillageStone villStone) {
                    villStone.negate(world, new BlockPos(posX, posY, posZ), sendingPlayer);
                }
            }
        }
        
        if (message.getID() == 3) {
            ItemStack heldItem = sendingPlayer.getMainHandItem();
            if (heldItem.getItem() != MillItems.WAND_NEGATION.get()) {
                System.err.println("Player not holding Wand of Negation when attempting to delete Villager");
            } else {
                Level world = sendingPlayer.level();
                CompoundTag nbt = heldItem.getTag();
                int id = nbt.getInt("ID");
                
                var entity = world.getEntity(id);
                if (entity != null) {
                    world.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 0.0F, Level.ExplosionInteraction.NONE);
                    world.playSound(null, entity.blockPosition(), net.minecraft.sounds.SoundEvents.PLAYER_HURT, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.4F);
                    entity.discard();
                }
            }
        }
        
        if (message.getID() == 4) {
            ItemStack heldItem = sendingPlayer.getMainHandItem();
            if (heldItem.getItem() != MillItems.WAND_SUMMONING.get()) {
                System.err.println("Player not holding Wand of Summoning when attempting to create Village");
            } else {
                Level world = sendingPlayer.level();
                CompoundTag nbt = heldItem.getTag();
                int posX = nbt.getInt("X");
                int posY = nbt.getInt("Y");
                int posZ = nbt.getInt("Z");
                
                world.setBlockAndUpdate(new BlockPos(posX, posY, posZ), MillBlocks.VILLAGE_STONE.get().defaultBlockState());
            }
        }
    }
}