package org.millenaire.common.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ResourceLocationUtil {

    public static ResourceLocation getRL(String rl) { 
        return ResourceLocation.parse(rl); 
    }
    
    public static String getString(ResourceLocation rl) { 
        return rl.getNamespace() + ":" + rl.getPath(); 
    }
    
    public static Item getItem(ResourceLocation rl) { 
        return BuiltInRegistries.ITEM.get(rl);
    }
}