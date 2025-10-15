package org.millenaire.common.items;

import net.minecraft.world.item.*;

public class ItemMillTool {
    // Les ToolMaterial ont été remplacés par Tier
    // Utilisez Tiers.IRON, Tiers.DIAMOND, etc.
    
    public static class ItemMillMace extends SwordItem {
        public ItemMillMace(Tier tier, Properties properties) {
            super(tier, properties);
        }
        
        // La logique d'enchantement automatique doit être gérée différemment
        // Peut-être dans un événement ou dans l'initialisation de l'item
    }
}