package org.millenaire.common.building;

import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import org.millenaire.common.MillCulture;
import org.millenaire.common.util.ItemRateWrapper;
import org.millenaire.common.util.ResourceLocationUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildingTypes {
    
    private static Map<ResourceLocation, BuildingType> buildingCache = new HashMap<>();

    public static void cacheBuildingTypes(MillCulture culture) {
        try {
            InputStream is = MillCulture.class.getClassLoader().getResourceAsStream(
                "assets/millenaire/cultures/" + culture.cultureName.toLowerCase() + "/buildings/buildings.json");
            if (is != null) {
                String[] buildings = new Gson().fromJson(new InputStreamReader(is), String[].class);
                
                for(String building : buildings) {
                    ResourceLocation loc = ResourceLocation.parse(building);
                    InputStream file = MillCulture.class.getClassLoader().getResourceAsStream(
                        "assets/millenaire/cultures/" + loc.getNamespace() + "/buildings/" + loc.getPath() + ".json");
                    if (file != null) {
                        BuildingType type = new Gson().fromJson(new InputStreamReader(file), BuildingType.class);
                        buildingCache.put(loc, type);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static BuildingType getTypeByID(ResourceLocation rl) { 
        return buildingCache.get(rl); 
    }
    
    public static BuildingType getTypeFromProject(BuildingProject proj) {
        return buildingCache.get(ResourceLocationUtil.getRL(proj.ID));
    }
    
    public static Map<ResourceLocation, BuildingType> getCache() { 
        return buildingCache; 
    }
    
    public static class BuildingType {
        private String identifier;
        protected List<ItemRateWrapper> itemrates = new ArrayList<>();
        
        public BuildingType() {}
        
        public BuildingType(ResourceLocation cultureandname) { 
            identifier = ResourceLocationUtil.getString(cultureandname); 
        }
        
        public BuildingPlan loadBuilding() {
            ResourceLocation s = ResourceLocationUtil.getRL(identifier);
            try {
                // This would need to be adapted to work with modern PlanIO
                return null; // Placeholder
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}