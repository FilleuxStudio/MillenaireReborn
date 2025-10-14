package org.millenaire.common.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRateWrapper {

    private String itemid;
    private int stacksize;
    private int meta;
    private int ticks;
    
    public ItemRateWrapper() {
        
    }
    
    public ItemRateWrapper(ResourceLocation item, int size, int meta, int rate) {
        this.itemid = ResourceLocationUtil.getString(item);
        this.stacksize = size;
        this.meta = meta;
        this.ticks = rate;
    }
    
    public ItemStack create() {
        Item item = ResourceLocationUtil.getItem(ResourceLocationUtil.getRL(itemid));
        return new ItemStack(item, stacksize);
        // Note: Le paramètre 'meta' (damage value) n'est plus utilisé dans les nouvelles versions
        // Si vous avez besoin de spécifier des composants NBT, utilisez ItemStack.Builder
    }
    
    // Getters pour l'accès aux données
    public String getItemId() { return itemid; }
    public int getStackSize() { return stacksize; }
    public int getMeta() { return meta; }
    public int getTicks() { return ticks; }
    
    // Setters si nécessaire
    public void setItemId(String itemid) { this.itemid = itemid; }
    public void setStackSize(int stacksize) { this.stacksize = stacksize; }
    public void setMeta(int meta) { this.meta = meta; }
    public void setTicks(int ticks) { this.ticks = ticks; }
}