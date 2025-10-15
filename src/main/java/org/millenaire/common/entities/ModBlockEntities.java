package org.millenaire.common.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "millenaire");
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityVillageStone>> VILLAGE_STONE = 
        BLOCK_ENTITIES.register("village_stone", 
            () -> BlockEntityType.Builder.of(
                TileEntityVillageStone::new, 
                ModBlocks.VILLAGE_STONE.get() // À créer
            ).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillChest>> MILL_CHEST = 
        BLOCK_ENTITIES.register("mill_chest", 
            () -> BlockEntityType.Builder.of(
                TileEntityMillChest::new, 
                ModBlocks.MILL_CHEST.get() // À créer
            ).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillSign>> MILL_SIGN = 
        BLOCK_ENTITIES.register("mill_sign", 
            () -> BlockEntityType.Builder.of(
                TileEntityMillSign::new, 
                ModBlocks.MILL_SIGN.get() // À créer
            ).build(null));
}