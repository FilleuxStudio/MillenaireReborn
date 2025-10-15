package org.millenaire.common.building;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.millenaire.MillConfig;
import org.millenaire.common.entities.EntityMillVillager;

import java.util.List;

public class BuildingLocation {
    public int minx, maxx, minz, maxz, miny, maxy;
    public int minxMargin, maxxMargin, minyMargin, maxyMargin, minzMargin, maxzMargin;
    public int length, width;
    
    public Direction orientation;
    public BlockPos position;
    
    public List<BlockPos> chestPos;
    public BlockPos tradePos;
    public List<BlockPos> sourcePos;
    public List<BlockPos> craftPos;
    public List<BlockPos> sleepPos;
    public List<BlockPos> hidePos;
    public List<BlockPos> defendPos;
    
    List<EntityMillVillager> residents;
    public List<String> subBuildings;
    
    public BuildingLocation(BuildingPlan plan, BlockPos pos, Direction orientIn) {
        orientation = orientIn;
        position = pos;
        this.computeMargins();
    }
    
    public BuildingLocation(ResourceLocation rl, BlockPos pos, Direction orientIn) {
        this(BuildingTypes.getTypeByID(rl).loadBuilding(), pos, orientIn);
    }
    
    public void computeMargins() {
        minxMargin = minx - MillConfig.minBuildingDistance + 1;
        minzMargin = minz - MillConfig.minBuildingDistance + 1;
        minyMargin = miny - 3;
        maxyMargin = maxy + 1;
        maxxMargin = maxx + MillConfig.minBuildingDistance + 1;
        maxzMargin = maxz + MillConfig.minBuildingDistance + 1;
    }
    
    /*
    public static BuildingLocation fromNBT(CompoundTag nbt) {
        ResourceLocation rl = ResourceLocationUtil.getRL(nbt.getString("planID"));
        BlockPos pos = BlockPos.fromLong(nbt.getLong("pos"));
        Direction fac = Direction.from2DDataValue(nbt.getInt("facing"));
        return new BuildingLocation(rl, pos, fac);
    }
    
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        
        nbt.putString("planID", ResourceLocationUtil.getString(planid));
        nbt.putInt("facing", orientation.get2DDataValue());
        
        return nbt;
    }
    */
}