package org.millenaire.common.building;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.millenaire.common.CommonUtilities;
import org.millenaire.common.MillCulture;
import org.millenaire.common.VillageGeography;
import org.millenaire.common.blocks.MillBlocks;
import org.millenaire.common.pathing.MillPathNavigate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BuildingPlan {
    public int length;
    public int width;
    public int height;
    public int lengthOffset;
    public int widthOffset;
    public int depth;
    public int areaToClear;
    
    public float minDistance, maxDistance;
    
    public boolean isUpdate = false;
    public boolean isSubBuilding = false;
    
    public List<ResourceCost> resCost;
    public Direction buildingOrientation;
    public String nativeName;
    public String[] maleVillagerType;
    public String[] femaleVillagerType;
    
    public BlockState[][][] buildingArray;
    public List<String> subBuildings;
    public int pathLevel = 0;
    public int pathWidth = 2;
    public boolean rebuildPath = false;

    public BuildingPlan(MillCulture cultureIn, int level) {
        // computeCost(); - to be implemented
    }
    
    public BuildingPlan(int level, int pathLevelIn, BuildingPlan parent) {
        length = parent.length;
        width = parent.width;
        height = parent.height;
        lengthOffset = parent.lengthOffset;
        widthOffset = parent.widthOffset;
        depth = parent.depth;
        areaToClear = parent.areaToClear;
        minDistance = parent.minDistance;
        maxDistance = parent.maxDistance;
        isUpdate = true;
        isSubBuilding = parent.isSubBuilding;
        buildingOrientation = parent.buildingOrientation;
        nativeName = parent.nativeName;
        maleVillagerType = parent.maleVillagerType;
        femaleVillagerType = parent.femaleVillagerType;
        pathWidth = parent.pathWidth;
        
        pathLevel = pathLevelIn;
        if(pathLevel != parent.pathLevel)
            rebuildPath = true;
    }

    public BuildingPlan setLengthWidth(int lenIn, int widIn) {
        this.length = lenIn;
        this.width = widIn;
        
        lengthOffset = (int) Math.floor(length * 0.5);
        widthOffset = (int) Math.floor(width * 0.5);
        
        return this;
    }
    
    public BuildingPlan setHeightDepth(int hiIn, int depIn) {
        this.height = hiIn;
        this.depth = depIn;
        return this;
    }

    public BuildingPlan setArea(int areaIn) {
        this.areaToClear = areaIn;
        return this;
    }
    
    public BuildingPlan setDistance(float minIn, float maxIn) {
        this.minDistance = minIn;
        this.maxDistance = maxIn;
        return this;
    }
    
    public BuildingPlan setSubBuilding(boolean subIn) {
        this.isSubBuilding = subIn;
        return this;
    }
    
    public BuildingPlan setOrientation(Direction orientIn) {
        this.buildingOrientation = orientIn;
        return this;
    }

    public BuildingPlan setNameAndType(String nameIn, String[] maleIn, String[] femaleIn) {
        this.nativeName = nameIn;
        this.maleVillagerType = maleIn;
        this.femaleVillagerType = femaleIn;
        return this;
    }
    
    public BuildingPlan setPlan(BlockState[][][] arrayIn) {
        this.buildingArray = arrayIn;
        computeCost();
        return this;
    }
    
    // Note: Many methods would need complete rewrite for modern Minecraft
    // This is a simplified version showing the migration pattern
    
    private void computeCost() {
        // Cost computation would need complete rewrite for modern item system
        resCost = new ArrayList<>();
    }
    
    private static BlockPos adjustForOrientation(final int x, final int y, final int z, 
            final int xoffset, final int zoffset, final Direction orientation) {
        BlockPos pos = new BlockPos(x, y, z);
        switch (orientation) {
            case SOUTH -> pos = new BlockPos(x + xoffset, y, z + zoffset);
            case WEST -> pos = new BlockPos(x + zoffset, y, z - xoffset);
            case NORTH -> pos = new BlockPos(x - xoffset, y, z - zoffset);
            case EAST -> pos = new BlockPos(x - zoffset, y, z + xoffset);
            default -> {} // Keep original position
        }
        return pos;
    }
    
    public static class LocationReturn {
        public static final int OUTSIDE_RADIUS = 1;
        public static final int LOCATION_CLASH = 2;
        public static final int CONSTRUCTION_FORBIDDEN = 3;
        public static final int WRONG_ALTITUDE = 4;
        public static final int DANGER = 5;
        public static final int NOT_REACHABLE = 6;

        public BuildingLocation location;
        public int errorCode;
        public BlockPos errorPos;

        public LocationReturn(final BuildingLocation l) {
            location = l;
            errorCode = 0;
            errorPos = null;
        }

        public LocationReturn(final int error, final BlockPos pos) {
            location = null;
            errorCode = error;
            errorPos = pos;
        }
    }
    
    public static class ResourceCost {
        int amount;
        ItemStack stack;
        String odString;
        
        public ResourceCost(ItemStack stackIn, int amountIn) {
            amount = amountIn;
            stack = stackIn;
            odString = null;
        }
        
        public ResourceCost(String nameIn, int amountIn) {
            amount = amountIn;
            stack = null;
            odString = nameIn;
        }
        
        public ItemStack getStack() {
            return stack;
        }
        
        public String getString() {
            return odString;
        }
        
        public void add(int amountIn) {
            amount += amountIn;
        }
        
        public int getCost(ItemStack stackIn) {
            // Simplified implementation - would need proper item comparison
            if (ItemStack.isSameItemSameComponents(stack, stackIn)) {
                return amount;
            }
            return 0;
        }
    }
}