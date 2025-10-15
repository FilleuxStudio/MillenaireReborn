package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.millenaire.MillCulture;
import org.millenaire.VillagerType;
import org.millenaire.common.blocks.BlockVillageStone;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TileEntityVillageStone extends BlockEntity {
    private List<EntityMillVillager> currentVillagers = new ArrayList<>();
    
    public String culture = "biome";
    public boolean randomVillage = true;
    public MillCulture.VillageType villageType;
    public String villageName;
    public boolean willExplode = false;
    private UUID villageID;
    public int testVar = 0;

    public TileEntityVillageStone(BlockPos pos, BlockState state) {
        super(ModBlockEntities.VILLAGE_STONE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        
        if (level != null && !level.isClientSide() && level.getBlockState(worldPosition).getBlock() instanceof BlockVillageStone) {
            initializeVillage();
        }
    }

    private void initializeVillage() {
        if (culture.equalsIgnoreCase("biome") && level != null) {
            // Logique de détermination de la culture par biome
            culture = "norman"; // Temporaire pour les tests
        }

        try {
            MillCulture millCulture = MillCulture.getCulture(culture);
            if (randomVillage) {
                villageType = millCulture.getRandomVillageType();
            } else {
                villageType = millCulture.getVillageType(villageName);
            }
            
            villageName = villageType.getVillageName();
            
            // Création du village - à adapter
            // Village v = Village.createVillage(this.getBlockPos(), level, villageType, millCulture);
            // v.setupVillage();
            
        } catch (Exception ex) {
            System.err.println("Erreur lors de la création du village");
            ex.printStackTrace();
        }
    }

    public EntityMillVillager createVillager(int villagerID) {
        if (level instanceof ServerLevel serverLevel) {
            return createVillager(serverLevel, MillCulture.getCulture(culture), villagerID);
        }
        return null;
    }

    public EntityMillVillager createVillager(ServerLevel worldIn, MillCulture cultureIn, int villagerID) {
        // Logique de création de villageois adaptée...
        // Similaire à l'original mais avec les nouvelles APIs
        return null;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        // Sauvegarde NBT
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        // Chargement NBT
    }
}