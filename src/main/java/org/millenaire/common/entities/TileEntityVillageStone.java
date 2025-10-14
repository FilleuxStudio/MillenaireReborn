package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.millenaire.common.CommonUtilities;
import org.millenaire.common.MillConfig;
import org.millenaire.common.MillCulture;
import org.millenaire.common.VillagerType;
import org.millenaire.common.blocks.MillBlockEntities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntityVillageStone extends BlockEntity {
    private List<EntityMillVillager> currentVillagers = new ArrayList<>();

    // Control Value. Changed when using wandSummon, if left as 'biome' when onLoad called, culture decided by biome.
    public String culture = "biome";
    public boolean randomVillage = true;
    public MillCulture.VillageType villageType;
    public String villageName;
    public boolean willExplode = false;
    private UUID villageID;

    public int testVar = 0;

    public TileEntityVillageStone(BlockPos pos, BlockState state) {
        super(MillBlockEntities.VILLAGE_STONE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        
        Level world = this.getLevel();
        BlockPos pos = this.getBlockPos();
        
        if (world != null && !world.isClientSide()) { // server only
            if (culture.equalsIgnoreCase("biome")) {
                if (world.getBiome(pos).value() != null) {
                    // Do awesome stuff and set culture. Below is simply for testing.
                    System.out.println("Village Culture being set by biome");
                    culture = "norman";
                }
            }

            try {
                MillCulture cultureObj = MillCulture.getCulture(culture);
                if (randomVillage) {
                    villageType = cultureObj.getRandomVillageType();
                } else {
                    villageType = cultureObj.getVillageType(villageName);
                }

                villageName = villageType.getVillageName();
                
                // Village v = Village.createVillage(this.getBlockPos(), world, villageType, MillCulture.getCulture(culture));
                // v.setupVillage();

                if (MillConfig.villageAnnouncement) {
                    if (!world.isClientSide()) {
                        for (Player player : world.players()) {
                            player.sendSystemMessage(Component.literal(culture + " village " + villageName + " discovered at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()));
                        }
                    }
                }

                if (!world.isClientSide()) {
                    System.out.println(culture + " village " + villageName + " created at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());
                }
                
                // Building initialization code here
                
            } catch (Exception ex) {
                System.err.println("Something went catastrophically wrong creating this village");
                ex.printStackTrace();
            }
        }
    }

    public EntityMillVillager createVillager(Level worldIn, MillCulture cultureIn, int villagerID) {
        VillagerType currentVillagerType;
        int currentGender;

        if (villagerID == 0) {
            int balance = 0;
            villagerID = (int) CommonUtilities.getRandomNonzero();
            boolean checkAgain = false;

            for (EntityMillVillager currentVillager : currentVillagers) {
                if (currentVillager.getGender() == 0) {
                    balance++;
                } else {
                    balance--;
                }

                if (villagerID == currentVillager.villagerID) {
                    villagerID = (int) CommonUtilities.getRandomNonzero();
                    checkAgain = true;
                }
            }
            
            while (checkAgain) {
                checkAgain = false;
                for (EntityMillVillager currentVillager : currentVillagers) {
                    if (villagerID == currentVillager.villagerID) {
                        villagerID = (int) CommonUtilities.getRandomNonzero();
                        checkAgain = true;
                    }
                }
            }

            balance += CommonUtilities.randomizeGender();

            if (balance < 0) {
                currentGender = 0;
                currentVillagerType = cultureIn.getChildType(0);
            } else {
                currentGender = 1;
                currentVillagerType = cultureIn.getChildType(1);
            }

            EntityMillVillager newVillager = new EntityMillVillager(worldIn, villagerID, cultureIn);
            newVillager.setTypeAndGender(currentVillagerType, currentGender);

            return newVillager;
        } else {
            for (EntityMillVillager currentVillager : currentVillagers) {
                if (villagerID == currentVillager.villagerID) {
                    return currentVillager;
                }
            }

            System.err.println("Attempted to create nonspecific Villager.");
        }

        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        // Save village data
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        // Load village data
    }
}