package org.millenaire.common.blocks;

import org.millenaire.Millenaire;
import org.millenaire.common.entities.TileEntityMillChest;
import org.millenaire.common.entities.TileEntityMillSign;
import org.millenaire.common.entities.TileEntityVillageStone;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class MillBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Millenaire.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Millenaire.MODID);
    
    // Thatch blocks - CORRIGÉ
    public static final DeferredBlock<Block> THATCH_SLAB = BLOCKS.register("thatch_slab", 
        () -> new BlockDecorativeOrientedSlabHalf(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)));
    
    public static final DeferredBlock<Block> THATCH_SLAB_DOUBLE = BLOCKS.register("thatch_slab_double", 
        () -> new BlockDecorativeOrientedSlabDouble(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)));
    
    // Byzantine blocks - CORRIGÉ
    public static final DeferredBlock<Block> BYZANTINE_TILE_SLAB = BLOCKS.register("byzantine_tile_slab", 
        () -> new BlockDecorativeOrientedSlabHalf(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));
    
    public static final DeferredBlock<Block> BYZANTINE_TILE_SLAB_DOUBLE = BLOCKS.register("byzantine_tile_slab_double", 
        () -> new BlockDecorativeOrientedSlabDouble(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));

    // Path blocks - CORRIGÉ  
    public static final DeferredBlock<Block> BLOCK_MILL_PATH_SLAB = BLOCKS.register("block_mill_path_slab", 
        () -> new BlockMillPathSlabHalf(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.6F, 0.6F)
            .sound(SoundType.GRAVEL)));
    
    public static final DeferredBlock<Block> BLOCK_MILL_PATH_SLAB_DOUBLE = BLOCKS.register("block_mill_path_slab_double", 
        () -> new BlockMillPathSlabDouble(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.6F, 0.6F)
            .sound(SoundType.GRAVEL)));

    // Sign - CORRIGÉ
    public static final DeferredBlock<Block> BLOCK_MILL_SIGN = BLOCKS.register("block_mill_sign", 
        () -> new BlockMillSign(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(-1.0F, 3600000.0F)
            .sound(SoundType.WOOD)
            .noOcclusion()
            .noLootTable()));

    // Les autres blocs restent inchangés...
    public static final DeferredBlock<Block> BLOCK_DECORATIVE_STONE = BLOCKS.register("block_decorative_stone", 
        () -> new BlockDecorativeStone(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));
    
    public static final DeferredBlock<Block> BLOCK_DECORATIVE_WOOD = BLOCKS.register("block_decorative_wood", 
        () -> new BlockDecorativeWood(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD)));
    
    public static final DeferredBlock<Block> BLOCK_DECORATIVE_EARTH = BLOCKS.register("block_decorative_earth", 
        () -> new BlockDecorativeEarth(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.5F, 0.5F)
            .sound(SoundType.GRAVEL)));
    
    public static final DeferredBlock<Block> BLOCK_SOD_PLANKS = BLOCKS.register("block_sod_plank", 
        () -> new BlockDecorativeSodPlank(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 15.0F)
            .sound(SoundType.WOOD)));
    
    public static final DeferredBlock<Block> EMPTY_SERICULTURE = BLOCKS.register("empty_sericulture", 
        () -> new BlockDecorativeUpdate(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0F, 3.0F)
            .sound(SoundType.WOOD), 
            () -> BLOCK_DECORATIVE_WOOD.get().defaultBlockState()));
    
    public static final DeferredBlock<Block> MUD_BRICK = BLOCKS.register("mud_brick", 
        () -> new BlockDecorativeUpdate(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.5F, 0.5F)
            .sound(SoundType.GRAVEL), 
            () -> BLOCK_DECORATIVE_EARTH.get().defaultBlockState()));
    
    public static final DeferredBlock<Block> THATCH_STAIRS = BLOCKS.register("thatch_stairs", 
        () -> new BlockDecorativeOrientedStairs(() -> BLOCK_DECORATIVE_WOOD.get().defaultBlockState(), 
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.WOOD)
                .strength(2.0F, 3.0F)
                .sound(SoundType.WOOD)));
    
    public static final DeferredBlock<Block> BYZANTINE_TILE = BLOCKS.register("byzantine_tile", 
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));
    
    public static final DeferredBlock<Block> BYZANTINE_STONE_TILE = BLOCKS.register("byzantine_stone_tile", 
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> BYZANTINE_TILE_STAIRS = BLOCKS.register("byzantine_tile_stairs", 
        () -> new BlockDecorativeOrientedStairs(() -> BYZANTINE_STONE_TILE.get().defaultBlockState(), 
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .strength(2.0F, 6.0F)
                .sound(SoundType.STONE)));
    
    public static final DeferredBlock<Block> PAPER_WALL = BLOCKS.register("paper_wall", 
        () -> new BlockDecorativePane(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOL)
            .strength(0.8F, 1.0F)
            .sound(SoundType.WOOL)
            .noOcclusion()));
    
    public static final DeferredBlock<Block> BLOCK_CARVING = BLOCKS.register("inuit_carving", 
        () -> new BlockDecorativeCarving(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)
            .noOcclusion()));
    
    public static final DeferredBlock<Block> CROP_TURMERIC = BLOCKS.register("crop_turmeric", 
        () -> new BlockMillCrops(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP), false, false));
    
    public static final DeferredBlock<Block> CROP_RICE = BLOCKS.register("crop_rice", 
        () -> new BlockMillCrops(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP), true, false));
    
    public static final DeferredBlock<Block> CROP_MAIZE = BLOCKS.register("crop_maize", 
        () -> new BlockMillCrops(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP), false, true));
    
    public static final DeferredBlock<Block> CROP_GRAPE_VINE = BLOCKS.register("crop_grape_vine", 
        () -> new BlockMillCrops(BlockBehaviour.Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .randomTicks()
            .instabreak()
            .sound(SoundType.CROP), false, false));
    
    public static final DeferredBlock<Block> BLOCK_MILL_CHEST = BLOCKS.register("block_mill_chest", 
        () -> new BlockMillChest(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(-1.0F, 3600000.0F)
            .sound(SoundType.WOOD)
            .noLootTable()));
    
    public static final DeferredBlock<Block> BLOCK_ALCHEMISTS = BLOCKS.register("block_alchemists", 
        () -> new BlockAlchemists(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.STONE)));
    
    public static final DeferredBlock<Block> BLOCK_MILL_PATH = BLOCKS.register("block_mill_path", 
        () -> new BlockMillPath(BlockBehaviour.Properties.of()
            .mapColor(MapColor.DIRT)
            .strength(0.6F, 0.6F)
            .sound(SoundType.GRAVEL)));
    
    public static final DeferredBlock<Block> GALIANITE_ORE = BLOCKS.register("galianite_ore", 
        () -> new BlockMillOre(BlockMillOre.EnumMillOre.GALIANITE, BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0F, 3.0F)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()));
    
    public static final DeferredBlock<Block> VILLAGE_STONE = BLOCKS.register("village_stone", 
        () -> new BlockVillageStone(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(-1.0F, 3600000.0F)
            .sound(SoundType.STONE)
            .noLootTable()));
    
    public static final DeferredBlock<Block> STORED_POSITION = BLOCKS.register("stored_position", 
        () -> new StoredPosition(BlockBehaviour.Properties.of()
            .mapColor(MapColor.NONE)
            .noCollission()
            .instabreak()
            .sound(SoundType.EMPTY)
            .noLootTable()
            .pushReaction(PushReaction.BLOCK)));
    
    // Block Entities
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillChest>> MILL_CHEST_ENTITY = 
        BLOCK_ENTITIES.register("tile_entity_mill_chest", 
            () -> BlockEntityType.Builder.of(TileEntityMillChest::new, BLOCK_MILL_CHEST.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillSign>> MILL_SIGN_ENTITY = 
        BLOCK_ENTITIES.register("tile_entity_mill_sign", 
            () -> BlockEntityType.Builder.of(TileEntityMillSign::new, BLOCK_MILL_SIGN.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityVillageStone>> VILLAGE_STONE_ENTITY = 
        BLOCK_ENTITIES.register("tile_entity_village_stone", 
            () -> BlockEntityType.Builder.of(TileEntityVillageStone::new, VILLAGE_STONE.get()).build(null));
    
    public static void preinitialize(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
    
    // Les autres méthodes restent inchangées...
    public static void recipes() {
        // Les recettes seront gérées via datagen
    }
    
    public static void prerender() {
        // Géré via JSON
    }
    
    public static void render() {
        // Géré via JSON
    }
}