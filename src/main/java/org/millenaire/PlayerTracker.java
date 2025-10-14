package org.millenaire;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerTracker implements INBTSerializable<CompoundTag> {
    private static final String IDENTIFIER = "millenaire_player_info";
    
    private final Player player;
    private Map<Item, Boolean> playerCropKnowledge = new HashMap<>();

    public PlayerTracker(Player player) { 
        this.player = player; 
    }

    public static void register(Player player) {
        player.registerAdditionalSavedData(new PlayerTracker(player));
    }

    public static PlayerTracker get(Player player) {
        return player.getAdditionalSavedData().computeIfAbsent(
            () -> new PlayerTracker(player), IDENTIFIER
        );
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag compound = new CompoundTag();
        CompoundTag cropKnowledge = new CompoundTag();

        for (Map.Entry<Item, Boolean> entry : playerCropKnowledge.entrySet()) {
            String key = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(entry.getKey()).toString();
            cropKnowledge.putBoolean(key, entry.getValue());
        }

        compound.put("cropKnowledge", cropKnowledge);
        return compound;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag compound) {
        if (compound.contains("cropKnowledge")) {
            CompoundTag cropKnowledge = compound.getCompound("cropKnowledge");
            for (String key : cropKnowledge.getAllKeys()) {
                Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse(key));
                if (item != null) {
                    boolean canPlant = cropKnowledge.getBoolean(key);
                    playerCropKnowledge.put(item, canPlant);
                }
            }
        }
    }
    
    public void setCanUseCrop(Item cropIn, boolean canUse) { 
        playerCropKnowledge.put(cropIn, canUse); 
    }

    public boolean canPlayerUseCrop(Item cropIn) {
        return playerCropKnowledge.getOrDefault(cropIn, false);
    }
}