package org.millenaire;

import net.minecraft.nbt.CompoundTag; // NBTTagCompound → CompoundTag
import net.minecraft.world.level.saveddata.SavedData; // WorldSavedData → SavedData

public class RaidTracker extends SavedData {
    private static final String IDENTIFIER = "millenaire_raid_info";
    
    public RaidTracker() {}
    
    public static RaidTracker create() {
        return new RaidTracker();
    }
    
    @Override
    public CompoundTag save(CompoundTag nbt) { // writeToNBT → save
        // Logique de sauvegarde
        return nbt;
    }
    
    public static RaidTracker load(CompoundTag nbt) { // readFromNBT → load
        RaidTracker data = new RaidTracker();
        // Logique de chargement
        return data;
    }
    
    public static RaidTracker get(net.minecraft.world.level.Level world) {
        return world.getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(RaidTracker::create, RaidTracker::load), 
            IDENTIFIER
        );
    }
}