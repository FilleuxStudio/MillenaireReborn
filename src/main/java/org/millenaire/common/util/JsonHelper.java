package org.millenaire.common.util;

import org.millenaire.MillCulture;

/**
 * Wrapper for the JSON parser
 */
public class JsonHelper {

    public static class VillageTypes {
        
        public VillageTypes() {
            
        }
        
        public VillageTypes(MillCulture.VillageType[] types) { 
            this.types = types; 
        }
        
        public MillCulture.VillageType[] types;
        
        public void setTypes(MillCulture.VillageType[] types) { 
            this.types = types; 
        }
        
        public MillCulture.VillageType[] getTypes() { 
            return types; 
        }
    }
}