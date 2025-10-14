package org.millenaire.data; // Recommandé: mettre les classes de données dans un package dédié

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.core.registries.BuiltInRegistries; // Nouvelle classe de registre
import net.minecraft.world.entity.Entity; // Import conservé pour l'initialisation si nécessaire, mais obsolète ici
import net.minecraft.world.level.Level; // Remplacement de net.minecraft.world.World

import java.util.HashMap;
import java.util.Map;
import java.util.Set; // Remplacement de getKeySet()

public class PlayerTracker {

    // Clé unique pour enregistrer les données dans le NBT persistant du joueur
    private final static String IDENTIFIER_KEY = "Millenaire_CropKnowledge";
    
    // Remplacement de l'ancien identifiant de IExtendedEntityProperties pour la section des propriétés persistantes
    private final static String PERSISTED_DATA_TAG = "ForgeData";

    private final Map<Item, Boolean> playerCropKnowledge = new HashMap<>();

    public PlayerTracker() { 
        // Constructeur public pour la création d'une nouvelle instance de données
    }

    /**
     * Récupère le PlayerTracker (ou crée/charge) à partir des données persistantes du joueur.
     * Cette méthode remplace l'ancien PlayerTracker.get(player).
     */
    public static PlayerTracker get(Player player) {
        // Accède au tag NBT persistant qui survit aux changements de dimension et aux reconnexions.
        CompoundTag persistentData = player.getPersistentData().getCompound(PERSISTED_DATA_TAG);
        CompoundTag dataTag = persistentData.getCompound(IDENTIFIER_KEY);

        PlayerTracker tracker = new PlayerTracker();
        
        // Charge les données à partir du NBT
        tracker.loadNBTData(dataTag);

        return tracker;
    }

    /**
     * Sauvegarde les données du PlayerTracker dans le NBT persistant du joueur.
     * Cette méthode doit être appelée lors des événements PlayerEvent.SaveToFile et PlayerEvent.PlayerLoggedOut.
     */
    public void save(Player player) {
        // 1. Crée le tag NBT à sauvegarder
        CompoundTag dataTag = new CompoundTag();
        this.saveNBTData(dataTag);

        // 2. Accède au tag persistant du joueur
        CompoundTag persistentData = player.getPersistentData().getCompound(PERSISTED_DATA_TAG);
        
        // 3. Place les données dans l'emplacement correct
        persistentData.put(IDENTIFIER_KEY, dataTag);
        
        // 4. Met à jour le tag persistant principal du joueur (important si le tag PERSISTED_DATA_TAG était vide)
        player.getPersistentData().put(PERSISTED_DATA_TAG, persistentData);
    }
    
    // --- Méthodes de Persistance (Mise à jour NBT et Registre) ---

    // Remplacement de l'ancienne implémentation IExtendedEntityProperties.saveNBTData
    public void saveNBTData(CompoundTag compound)
    {
        CompoundTag cropKnowledge = new CompoundTag();

        for(Map.Entry<Item, Boolean> entry : playerCropKnowledge.entrySet()) {
            Item item = entry.getKey();
            
            // Méthode moderne pour obtenir l'ID textuel d'un Item (ResourceLocation)
            ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(item);

            // Vérifie que l'item n'est pas null et n'est pas de l'air avant de sauvegarder
            if (itemKey != null && !itemKey.toString().equals("minecraft:air")) {
                cropKnowledge.putBoolean(itemKey.toString(), entry.getValue());
            }
        }

        compound.put("cropKnowledge", cropKnowledge);
    }

    // Remplacement de l'ancienne implémentation IExtendedEntityProperties.loadNBTData
    public void loadNBTData(CompoundTag compound)
    {
        playerCropKnowledge.clear();

        // Utilisation de .getCompound() au lieu de .getCompoundTag()
        CompoundTag cropKnowledge = compound.getCompound("cropKnowledge");
        
        // Utilisation de .getAllKeys() au lieu de .getKeySet()
        Set<String> keys = cropKnowledge.getAllKeys();
        
        for(String s : keys) {
            ResourceLocation itemRL = new ResourceLocation(s);
            // Méthode moderne pour obtenir un Item à partir de son ID textuel
            Item i = BuiltInRegistries.ITEM.get(itemRL); 
            
            if (i != null && i != net.minecraft.world.item.Items.AIR) {
                boolean canPlant = cropKnowledge.getBoolean(s);
                playerCropKnowledge.put(i, canPlant);
            }
        }
    }
    
    // --- Méthodes Métier ---
    
    public void setCanUseCrop(Item cropIn, boolean canUse) { 
        playerCropKnowledge.put(cropIn, canUse); 
    }

    public boolean canPlayerUseCrop(Item cropIn) {
        // Utilisation de getOrDefault pour simplifier et gérer le cas où la clé est absente
        return playerCropKnowledge.getOrDefault(cropIn, false); 
    }
    
    /** * Remplacement de l'ancienne méthode init() de IExtendedEntityProperties.
     * Cette méthode n'est plus appelée directement.
     */
    public void init(Entity entity, Level world)
    {
        // La logique d'initialisation (si nécessaire) devrait être gérée dans un événement PlayerEvent.PlayerLoggedIn.
    }
}