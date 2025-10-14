package org.millenaire.common.village;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.millenaire.MillCulture;
import org.millenaire.MillCulture.VillageType;
import org.millenaire.VillageGeography;
import org.millenaire.VillageTracker;
import org.millenaire.common.building.BuildingLocation;
import org.millenaire.common.building.BuildingPlan;
import org.millenaire.common.building.BuildingProject;
import org.millenaire.common.building.PlanIO;
import org.millenaire.common.entities.EntityMillVillager;
import org.millenaire.common.pathing.MillPathNavigate;
import org.millenaire.common.util.ResourceLocationUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Village {

    private UUID uuid;
    private BlockPos mainBlock;
    private VillageGeography geo;
    private VillageType type;
    private MillCulture culture;
    private Level world;
    private BuildingLocation[] buildings;
    
    private Village(BlockPos b, Level worldIn, VillageType typeIn, MillCulture cultureIn) {
        this.setPos(b);
        this.uuid = UUID.randomUUID();
        this.world = worldIn;
        this.type = typeIn;
        this.culture = cultureIn;
        this.geo = new VillageGeography();
        this.geo.update(world, new ArrayList<BuildingLocation>(), null, mainBlock, 64);
    }
    
    /**
     * FOR USE BY VILLAGE TRACKER ONLY
     */
    @Deprecated
    public Village() {}
    
    /**
     * FOR USE BY VILLAGE TRACKER ONLY
     */
    @Deprecated
    public void setPos(BlockPos pos) { mainBlock = pos; }
    
    public VillageType getType() { return type; }

    public UUID getUUID() { return uuid; }
    
    public BlockPos getPos() { return mainBlock; }
    
    public boolean setupVillage() {
        try {
            for(BuildingProject proj : type.startingBuildings) {
                BuildingPlan p = PlanIO.loadSchematic(PlanIO.getBuildingTag(ResourceLocationUtil.getRL(proj.ID).getPath(), culture, true), culture, proj.lvl);
                
                EntityMillVillager v = new EntityMillVillager(world, 100100, culture);
                v.setPos(mainBlock.getX(), mainBlock.getY(), mainBlock.getZ());
                v.setTypeAndGender(MillCulture.normanCulture.getVillagerType("normanKnight"), 1);
                world.addFreshEntity(v);
                
                BuildingLocation loc = p.findBuildingLocation(geo, new MillPathNavigate(v, world), mainBlock, 64, new Random(), p.buildingOrientation);
                PlanIO.placeBuilding(p, loc, world);
            }
            return true;
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static Village createVillage(BlockPos VSPos, Level world, VillageType typeIn, MillCulture cultureIn) {
        Village v = new Village(VSPos, world, typeIn, cultureIn);
        VillageTracker.get(world).registerVillage(v.getUUID(), v);
        return v; 
    }
    
    // Getters et setters supplémentaires si nécessaire
    public VillageGeography getGeography() { return geo; }
    public void setGeography(VillageGeography geo) { this.geo = geo; }
    
    public MillCulture getCulture() { return culture; }
    public void setCulture(MillCulture culture) { this.culture = culture; }
    
    public Level getWorld() { return world; }
    public void setWorld(Level world) { this.world = world; }
    
    public BuildingLocation[] getBuildings() { return buildings; }
    public void setBuildings(BuildingLocation[] buildings) { this.buildings = buildings; }
}