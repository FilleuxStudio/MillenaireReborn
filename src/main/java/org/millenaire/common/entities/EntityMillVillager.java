package org.millenaire.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.millenaire.common.MillCulture;
import org.millenaire.common.Millenaire;
import org.millenaire.common.VillagerType;
import org.millenaire.common.entities.ai.EntityAIGateOpen;

import java.util.List;

public class EntityMillVillager extends PathfinderMob {
    private static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> AGE = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> GENDER = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> NAME = SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);

    public int villagerID;
    private MillCulture culture;
    private VillagerType type;
    
    private boolean isVillagerSleeping = false;
    public boolean isPlayerInteracting = false;
    
    private final SimpleContainer villagerInventory;
    
    public EntityMillVillager(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.villagerInventory = new SimpleContainer(16);
        this.setPersistenceRequired();
    }

    public EntityMillVillager(Level level, int idIn, MillCulture cultureIn) {
        this(MillEntities.MILL_VILLAGER.get(), level);
        villagerID = idIn;
        culture = cultureIn;
        this.villagerInventory = new SimpleContainer(16);
        this.setPersistenceRequired();
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.55D)
                .add(Attributes.MAX_HEALTH, 20.0D);
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new EntityAIGateOpen(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3.0F, 0.5F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityMillVillager.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.6D));
    }
    
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TEXTURE, "texture");
        this.entityData.define(NAME, "name");
        this.entityData.define(AGE, 0);
        this.entityData.define(GENDER, 0);
    }
    
    public EntityMillVillager setTypeAndGender(VillagerType typeIn, int genderIn) {
        this.type = typeIn;
        this.entityData.set(GENDER, genderIn);
        this.entityData.set(TEXTURE, typeIn.getTexture());
        return this;
    }
    
    public void setChild() { 
        this.entityData.set(AGE, 1); 
    }
    
    public void setName(String nameIn) { 
        this.entityData.set(NAME, nameIn); 
    }
    
    public String getTexture() { 
        return this.entityData.get(TEXTURE); 
    }
    
    public int getGender() { 
        return this.entityData.get(GENDER); 
    }
    
    public String getName() { 
        return this.entityData.get(NAME); 
    }

    public VillagerType getVillagerType() { 
        return type; 
    }
    
    @Override
    public boolean isBaby() { 
        return (this.entityData.get(AGE) > 0); 
    }
    
    @Override
    public boolean canBeLeashed() { 
        return false; 
    }
    
    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.level().isClientSide) {
            ContainerHelper.dropContents(this.level(), this.blockPosition(), this.villagerInventory);
        }
    }
    
    @Override
    protected void pickUpItem(org.minecraft.world.entity.item.ItemEntity itemEntity) {
        // Custom pickup logic can be implemented here
    }
    
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Custom damage handling can be implemented here
        return super.hurt(source, amount);
    }
    
    public void faceDirection() {
        // Face an Entity or specific BlockPos when we want them to
    }
    
    @Override
    public int getExperienceReward() {
        // return villagertype.expgiven;
        return super.getExperienceReward();
    }
    
    @Override
    public boolean interact(Player player) {
        // Achievement system removed in 1.21, need alternative
        // player.awardStat(MillAchievement.FIRST_CONTACT);
        
        if (type != null && type.hireCost > 0) {
            this.isPlayerInteracting = true;
            if (player instanceof ServerPlayer serverPlayer) {
                // Open GUI - need to implement custom menu system
                // serverPlayer.openMenu(...);
            }
            return true;
        }
        
        if (type != null && type.isChief) {
            this.isPlayerInteracting = true;
            if (player instanceof ServerPlayer serverPlayer) {
                // Open GUI - need to implement custom menu system
                // serverPlayer.openMenu(...);
            }
            return true;
        }
        
        return false;
    }
    
    @Override
    protected boolean isImmobile() {
        return this.getHealth() <= 0 || this.isVillagerSleeping || this.isPlayerInteracting;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();

        if (isVillagerSleeping) {
            this.setDeltaMovement(0, 0, 0);
        }
    }
    
    @Override
    public void tick() {
        if (this.isDeadOrDying()) {
            super.tick();
            return;
        }
        
        if (isPlayerInteracting) {
            List<Player> playersNear = this.level().getEntitiesOfClass(Player.class, 
                new AABB(getX() - 5, getY() - 1, getZ() - 5, getX() + 5, getY() + 1, getZ() + 5));
            
            if (playersNear.isEmpty()) {
                isPlayerInteracting = false;
            }
        }

        // Add custom AI logic here
        
        super.tick();
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("villagerID", villagerID);
        if (culture != null) {
            compound.putString("culture", culture.cultureName);
        }
        compound.putInt("gender", this.entityData.get(GENDER));
        if (type != null) {
            compound.putString("villagerType", type.id);
        }
        compound.putBoolean("sleeping", isVillagerSleeping);
        
        compound.putString("texture", this.entityData.get(TEXTURE));
        compound.putInt("age", this.entityData.get(AGE));
        compound.putString("name", this.entityData.get(NAME));
        
        ListTag nbttaglist = new ListTag();
        for (int i = 0; i < this.villagerInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.villagerInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                nbttaglist.add(itemstack.save(new CompoundTag()));
            }
        }
        compound.put("Inventory", nbttaglist);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        villagerID = compound.getInt("villagerID");
        try {
            culture = MillCulture.getCulture(compound.getString("culture"));
        } catch (Exception ex) {
            System.err.println("Villager failed to read from NBT correctly");
            ex.printStackTrace();
        }
        
        if (culture != null) {
            this.entityData.set(GENDER, compound.getInt("gender"));
            type = culture.getVillagerType(compound.getString("villagerType"));
            isVillagerSleeping = compound.getBoolean("sleeping");
            
            this.entityData.set(TEXTURE, compound.getString("texture"));
            this.entityData.set(AGE, compound.getInt("age"));
            this.entityData.set(NAME, compound.getString("name"));
            
            if (compound.contains("Inventory", 9)) {
                ListTag nbttaglist = compound.getList("Inventory", 10);
                for (int i = 0; i < nbttaglist.size(); ++i) {
                    ItemStack itemstack = ItemStack.of(nbttaglist.getCompound(i));
                    if (!itemstack.isEmpty()) {
                        this.villagerInventory.addItem(itemstack);
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + ": " + getName() + "/" + this.villagerID + "/" + level();
    }
    
    private void updateHired() {
        // find target (base this on stance, change stance in onInteract)
        // pathFind to entity you want to attack (or following player)
        // handle doors and fence gates
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }
}