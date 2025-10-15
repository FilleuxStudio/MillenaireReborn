package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.millenaire.MillCulture;
import org.millenaire.VillagerType;
import org.millenaire.common.entities.ai.EntityAIGateOpen;
import org.millenaire.common.pathing.MillPathNavigate;

import javax.annotation.Nullable;
import java.util.List;

public class EntityMillVillager extends PathfinderMob {
    // Synched Data Parameters
    private static final EntityDataAccessor<String> DATA_TEXTURE = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_AGE = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GENDER = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_NAME = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);

    public int villagerID;
    private MillCulture culture;
    private VillagerType type;
    
    private boolean isVillagerSleeping = false;
    public boolean isPlayerInteracting = false;
    
    private final SimpleContainer villagerInventory = new SimpleContainer(16);

    public EntityMillVillager(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public EntityMillVillager(Level level, int id, MillCulture culture) {
        this(EntityType.VILLAGER, level); // Utilisation temporaire du villageois vanilla
        this.villagerID = id;
        this.culture = culture;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.55D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TEXTURE, "texture");
        builder.define(DATA_NAME, "name");
        builder.define(DATA_AGE, 0);
        builder.define(DATA_GENDER, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        // this.goalSelector.addGoal(1, new EntityAIGateOpen(this, true)); // À adapter
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3.0F, 0.5F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityMillVillager.class, 6.0F));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 0.6D));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new MillPathNavigate(this, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.setCanPickUpLoot(false);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public EntityMillVillager setTypeAndGender(VillagerType typeIn, int genderIn) {
        this.type = typeIn;
        this.getEntityData().set(DATA_GENDER, genderIn);
        if (typeIn != null) {
            this.getEntityData().set(DATA_TEXTURE, typeIn.getTexture());
        }
        return this;
    }

    public void setChild() {
        this.getEntityData().set(DATA_AGE, 1);
    }

    public void setName(String nameIn) {
        this.getEntityData().set(DATA_NAME, nameIn);
    }

    public String getTexture() {
        return this.getEntityData().get(DATA_TEXTURE);
    }

    public int getGender() {
        return this.getEntityData().get(DATA_GENDER);
    }

    public String getName() {
        return this.getEntityData().get(DATA_NAME);
    }

    public VillagerType getVillagerType() {
        return type;
    }

    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_AGE) > 0;
    }

    public boolean canBeLeashed() {
        return false;
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        Containers.dropContents(this.level(), this.blockPosition(), this.villagerInventory);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (type != null) {
            if (type.hireCost > 0) {
                this.isPlayerInteracting = true;
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
            if (type.isChief) {
                this.isPlayerInteracting = true;
                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isVillagerSleeping || this.isPlayerInteracting;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        
        if (isPlayerInteracting && this.level() instanceof ServerLevel serverLevel) {
            List<Player> playersNear = serverLevel.getEntitiesOfClass(Player.class, 
                new AABB(this.getX() - 5, this.getY() - 1, this.getZ() - 5, 
                         this.getX() + 5, this.getY() + 1, this.getZ() + 5));
            
            if (playersNear.isEmpty()) {
                isPlayerInteracting = false;
            }
        }

        if (isVillagerSleeping) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
    }

    public void addAdditionalSaveData(CompoundTag compound, HolderLookup.Provider registries) {
        super.addAdditionalSaveData(compound);
        compound.putInt("villagerID", villagerID);
        if (culture != null) {
            compound.putString("culture", culture.cultureName);
        }
        compound.putInt("gender", this.getEntityData().get(DATA_GENDER));
        if (type != null) {
            compound.putString("villagerType", type.id);
        }
        compound.putBoolean("sleeping", isVillagerSleeping);
        compound.putString("texture", this.getEntityData().get(DATA_TEXTURE));
        compound.putInt("age", this.getEntityData().get(DATA_AGE));
        compound.putString("name", this.getEntityData().get(DATA_NAME));
        
        ListTag inventoryList = new ListTag();
        for (int i = 0; i < this.villagerInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.villagerInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                itemstack.save(registries, itemTag);
                inventoryList.add(itemTag);
            }
        }
        compound.put("Inventory", inventoryList);
    }

    public void readAdditionalSaveData(CompoundTag compound, HolderLookup.Provider registries) {
        super.readAdditionalSaveData(compound);
        this.villagerID = compound.getInt("villagerID");
        try {
            this.culture = MillCulture.getCulture(compound.getString("culture"));
        } catch (Exception ex) {
            System.err.println("Villager failed to read from NBT correctly");
            ex.printStackTrace();
        }
        
        this.getEntityData().set(DATA_GENDER, compound.getInt("gender"));
        if (culture != null && compound.contains("villagerType")) {
            this.type = culture.getVillagerType(compound.getString("villagerType"));
        }
        this.isVillagerSleeping = compound.getBoolean("sleeping");
        this.getEntityData().set(DATA_TEXTURE, compound.getString("texture"));
        this.getEntityData().set(DATA_AGE, compound.getInt("age"));
        this.getEntityData().set(DATA_NAME, compound.getString("name"));
        
        if (compound.contains("Inventory", Tag.TAG_LIST)) {
            ListTag inventoryList = compound.getList("Inventory", Tag.TAG_COMPOUND);
            for (int i = 0; i < inventoryList.size(); ++i) {
                CompoundTag itemTag = inventoryList.getCompound(i);
                int slot = itemTag.getByte("Slot") & 255;
                if (slot >= 0 && slot < this.villagerInventory.getContainerSize()) {
                    ItemStack itemstack = ItemStack.parse(registries, itemTag).orElse(ItemStack.EMPTY);
                    this.villagerInventory.setItem(slot, itemstack);
                }
            }
        }
    }
}