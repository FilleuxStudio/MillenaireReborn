package org.millenaire; // Nouveau package recommandé pour les données

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

public class RaidTracker extends SavedData
{
    private final static String IDENTITY = "Millenaire.RaidInfo";
    
    // Rendre le constructeur privé pour forcer l'utilisation de la méthode get()
    private RaidTracker() {} // Constructeur par défaut pour computeIfAbsent
    
    // --- Méthodes de Persistance Modernes ---

    /**
     * Nouvelle méthode statique pour charger l'objet à partir du NBT.
     * Note: La signature DOIT inclure HolderLookup.Provider pour SavedData.
     */
    public static RaidTracker load(CompoundTag nbt, HolderLookup.Provider lookupProvider) 
    {
        RaidTracker tracker = new RaidTracker();
        
        // --- LOGIQUE DE LECTURE DU NBT ICI ---
        
        return tracker;
    }

    /**
     * Méthode pour sauvegarder les données dans le NBT.
     */
    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider lookupProvider) 
    {
        // --- LOGIQUE D'ÉCRITURE DU NBT ICI ---
        
        return nbt;
    }

    // --- Accès Moderne aux Données de Monde ---

    /**
     * Récupère le RaidTracker pour le niveau donné, le charge ou le crée si nécessaire.
     */
    public static RaidTracker get(Level world)
    {
        // Les SavedData sont gérées uniquement sur le niveau du serveur (Overworld).
        if (!(world instanceof ServerLevel serverLevel)) {
            throw new IllegalStateException("Attempted to get RaidTracker from a client level.");
        }
        
        // CORRECTION MAJEURE:
        // Utiliser une lambda pour encapsuler RaidTracker::load et passer le HolderLookup.Provider.
        // Cela permet de respecter l'interface fonctionnelle attendue (Function<CompoundTag, T>).
        return serverLevel.getDataStorage().computeIfAbsent(
            (nbt) -> RaidTracker.load(nbt, serverLevel.registryAccess()), // La lambda fournit le CompoundTag et nous y ajoutons le HolderLookup
            RaidTracker::new,                                            // Fournisseur de nouvelle instance
            IDENTITY                                                     // Clé/nom du fichier
        );
    }
}