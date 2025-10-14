package org.millenaire;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.Level;
import org.millenaire.common.village.Village;

import java.util.*;

public class VillageTracker extends SavedData {
    private static final String IDENTIFIER = "millenaire_villages";
    
    private final Map<UUID, Village> villages = new HashMap<>();
    
    public VillageTracker() {}
    
    // CORRECTION 1 : La méthode 'save' a déjà la bonne signature.
    @Override
    public CompoundTag save(CompoundTag nbt, net.minecraft.core.HolderLookup.Provider lookupProvider) {
        System.out.println("Village Tracker Writing to NBT");
        
        ListTag villagesList = new ListTag();
        for (Map.Entry<UUID, Village> entry : villages.entrySet()) {
            CompoundTag villageTag = new CompoundTag();
            Village village = entry.getValue();
            
            villageTag.putLong("Pos", village.getPos().asLong());
            villageTag.putUUID("ID", entry.getKey());
            
            villagesList.add(villageTag);
        }
        
        nbt.put("Villages", villagesList);
        return nbt;
    }
    
    // CORRECTION 2 : Ajout de 'lookupProvider' à la signature de 'load'.
    // Cela permet à la méthode de correspondre au BiFunction attendu par SavedData.Factory.
    public static VillageTracker load(CompoundTag nbt, HolderLookup.Provider lookupProvider) {
        System.out.println("Village Tracker reading from NBT");
        VillageTracker tracker = new VillageTracker();
        
        if (nbt.contains("Villages")) {
            ListTag villagesList = nbt.getList("Villages", CompoundTag.TAG_COMPOUND);
            for (int i = 0; i < villagesList.size(); i++) {
                CompoundTag villageTag = villagesList.getCompound(i);
                UUID id = villageTag.getUUID("ID");
                BlockPos pos = BlockPos.of(villageTag.getLong("Pos"));
                
                // NOTE: Dans un vrai mod, vous auriez besoin d'une méthode pour charger les données du Village.
                // Par exemple: Village.load(villageTag, lookupProvider)
                Village village = new Village();
                village.setPos(pos);
                tracker.villages.put(id, village);
            }
        }
        
        return tracker;
    }
    
    public List<Village> getNearVillages(BlockPos pos, int maxDist) {
        List<Village> nearby = new ArrayList<>();
        int maxDistSq = maxDist * maxDist;
        
        for (Village v : villages.values()) {
            if (v.getPos().distSqr(pos) <= maxDistSq) {
                nearby.add(v);
            }
        }
        
        return nearby;
    }
    
    public void registerVillage(UUID id, Village vil) { 
        villages.put(id, vil);
        setDirty();
    }
    
    public void unregisterVillage(UUID id) { 
        villages.remove(id);
        setDirty();
    }
    
    public static VillageTracker get(Level world) {
        if (!(world instanceof ServerLevel serverLevel)) {
            throw new RuntimeException("Attempted to get the village data from a client level");
        }
        
        // CORRECTION 3 : L'appel au Factory est maintenant valide car 'VillageTracker::load' correspond à la signature attendue.
        return serverLevel.getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(VillageTracker::new, VillageTracker::load), 
            IDENTIFIER
        );
    }
}