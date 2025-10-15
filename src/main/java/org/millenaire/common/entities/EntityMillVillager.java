package org.millenaire.entities;

import java.util.List;

import org.millenaire.MillCulture;
import org.millenaire.Millenaire;
import org.millenaire.VillagerType;
import org.millenaire.ai.EntityAIGateOpen;
import org.millenaire.client.gui.MillAchievement;
import org.millenaire.client.rendering.RenderMillVillager;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;

public class EntityMillVillager extends PathfinderMob {
    
    // EntityDataAccessors remplacent DataWatcher
    private static final EntityDataAccessor<String> DATA_TEXTURE = 
        SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_AGE = 
        SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GENDER = 
        SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_NAME = 
        SynchedEntityData.defineId(EntityMillVillager.class, EntityDataSerializers.STRING);

    public int villagerID;
    private MillCulture culture;
    private VillagerType type;

    private boolean isVillagerSleeping = false;
    public boolean isPlayerInteracting = false;
    
    private SimpleContainer villagerInventory;
    
    public EntityMillVillager(EntityType<? extends EntityMillVillager> entityType, Level level) {
        super(entityType, level);
        this.villagerInventory = new SimpleContainer(16);
        this.fireImmune();
    }

    public EntityMillVillager(EntityType<? extends EntityMillVillager> entityType, Level level, 
                             int idIn, MillCulture cultureIn) {
        super(entityType, level);
        this.villagerID = idIn;
        this.culture = cultureIn;
        this.villagerInventory = new SimpleContainer(16);
        this.fireImmune();
    }
    
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(1, new EntityAIGateOpen(this, true));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 3.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, EntityMillVillager.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.6D));
    }
    
    @Override
    protected PathNavigation createNavigation(Level level) {
        return new MillPathNavigate(this, level);
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.55D)
            .add(Attributes.MAX_HEALTH, 20.0D);
    }
    
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TEXTURE, "texture");
        builder.define(DATA_NAME, "name");
        builder.define(DATA_AGE, 0); // 0 = Adult
        builder.define(DATA_GENDER, 0); // 0 = male, 1 = female, 2 = Sym Female
    }
    
    public EntityMillVillager setTypeAndGender(VillagerType typeIn, int genderIn) {
        this.type = typeIn;
        this.entityData.set(DATA_GENDER, genderIn);
        this.entityData.set(DATA_TEXTURE, type.getTexture());
        return this;
    }
    
    public void setChild() { 
        this.entityData.set(DATA_AGE, 1); 
    }
    
    public void setVillagerName(String nameIn) { 
        this.entityData.set(DATA_NAME, nameIn); 
    }
    
    public String getTexture() { 
        return this.entityData.get(DATA_TEXTURE); 
    }
    
    public int getGender() { 
        return this.entityData.get(DATA_GENDER); 
    }
    
    public String getVillagerName() { 
        return this.entityData.get(DATA_NAME); 
    }

    public VillagerType getVillagerType() { 
        return type; 
    }
    
    @Override
    public boolean isBaby() { 
        return this.entityData.get(DATA_AGE) > 0; 
    }
    
    @Override
    public boolean canBeLeashed() { 
        return false; 
    }
    
    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource damageSource, boolean recentlyHit) {
        super.dropCustomDeathLoot(level, damageSource, recentlyHit);
        for (int i = 0; i < this.villagerInventory.getContainerSize(); i++) {
            ItemStack stack = this.villagerInventory.getItem(i);
            if (!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }
    }
    
    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        // Contrôle ce que fait le villageois quand il rencontre un item au sol
        // À implémenter selon la logique du mod
    }
    
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; // Les villageois ne despawn jamais
    }
    
    @Override
    protected int getExperienceReward(ServerLevel level) {
        // return type.expGiven;
        return super.getExperienceReward(level);
    }
    
    @Override
    public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effect) {
        return true;
    }
    
    public boolean interact(Player playerIn) {
        if (!this.level().isClientSide) {
            playerIn.awardStat(MillAchievement.firstContact);
            
            if (type.hireCost > 0) {
                this.isPlayerInteracting = true;
                // playerIn.openMenu(...) pour ouvrir le GUI
                return true;
            }
            
            if (type.isChief) {
                this.isPlayerInteracting = true;
                // playerIn.openMenu(...) pour ouvrir le GUI du chef
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean isImmobile() {
        return !this.isAlive() || this.isVillagerSleeping || this.isPlayerInteracting;
    }
    
    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();

        if (isVillagerSleeping) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
    }
    
    @Override
    public void tick() {
        if (!this.isAlive()) {
            super.tick();
            return;
        }
        
        if (isPlayerInteracting) {
            List<Player> playersNear = this.level().getEntitiesOfClass(
                Player.class, 
                new AABB(
                    this.getX() - 5, this.getY() - 1, this.getZ() - 5,
                    this.getX() + 5, this.getY() + 1, this.getZ() + 5
                )
            );
            
            if (playersNear.isEmpty()) {
                isPlayerInteracting = false;
            }
        }

        super.tick();
    }
    
    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("villagerID", villagerID);
        nbt.putString("culture", culture != null ? culture.cultureName : "");
        nbt.putInt("gender", this.entityData.get(DATA_GENDER));
        nbt.putString("villagerType", type != null ? type.id : "");
        nbt.putBoolean("sleeping", isVillagerSleeping);
        
        nbt.putString("texture", this.entityData.get(DATA_TEXTURE));
        nbt.putInt("age", this.entityData.get(DATA_AGE));
        nbt.putString("name", this.entityData.get(DATA_NAME));
        
        ListTag inventoryList = new ListTag();
        for (int i = 0; i < this.villagerInventory.getContainerSize(); i++) {
            ItemStack stack = this.villagerInventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                inventoryList.add(stack.save(this.registryAccess(), itemTag));
            }
        }
        nbt.put("Inventory", inventoryList);
    }
    
    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        villagerID = nbt.getInt("villagerID");
        
        try {
            String cultureName = nbt.getString("culture");
            if (!cultureName.isEmpty()) {
                culture = MillCulture.getCulture(cultureName);
            }
        } catch (Exception ex) {
            System.err.println("Villager failed to read culture from NBT");
            ex.printStackTrace();
        }
        
        if (culture != null) {
            this.entityData.set(DATA_GENDER, nbt.getInt("gender"));
            String typeId = nbt.getString("villagerType");
            if (!typeId.isEmpty()) {
                type = culture.getVillagerType(typeId);
            }
        }
        
        isVillagerSleeping = nbt.getBoolean("sleeping");
        
        this.entityData.set(DATA_TEXTURE, nbt.getString("texture"));
        this.entityData.set(DATA_AGE, nbt.getInt("age"));
        this.entityData.set(DATA_NAME, nbt.getString("name"));
        
        ListTag inventoryList = nbt.getList("Inventory", 10);
        for (int i = 0; i < inventoryList.size(); i++) {
            CompoundTag itemTag = inventoryList.getCompound(i);
            int slot = itemTag.getByte("Slot") & 255;
            ItemStack stack = ItemStack.parseOptional(this.registryAccess(), itemTag);
            if (slot >= 0 && slot < this.villagerInventory.getContainerSize()) {
                this.villagerInventory.setItem(slot, stack);
            }
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + ": " + getVillagerName() + 
               "/" + this.villagerID + "/" + level();
    }
    
    // Méthode pour l'enregistrement des attributs (appelée lors du setup)
    public static void registerAttributes(EntityAttributeCreationEvent event, EntityType<EntityMillVillager> entityType) {
        event.put(entityType, createAttributes().build());
    }
    
    // Méthode pour l'enregistrement du renderer (appelée lors du setup client)
    public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event, EntityType<EntityMillVillager> entityType) {
        event.registerEntityRenderer(entityType, RenderMillVillager::new);
    }
}