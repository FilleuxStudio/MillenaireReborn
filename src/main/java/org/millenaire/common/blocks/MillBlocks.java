package org.millenaire.common.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.millenaire.common.entities.TileEntityMillChest;
import org.millenaire.common.entities.TileEntityMillSign;
import org.millenaire.common.entities.TileEntityVillageStone;

import java.util.function.Supplier;

public class MillBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("millenaire");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "millenaire");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("millenaire");

    // Décoratifs
    public static final DeferredBlock<Block> DECORATIVE_STONE = BLOCKS.registerBlock("decorative_stone", 
        BlockDecorativeStone::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F));
    
    public static final DeferredBlock<Block> DECORATIVE_WOOD = BLOCKS.registerBlock("decorative_wood", 
        BlockDecorativeWood::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F));
    
    public static final DeferredBlock<Block> DECORATIVE_EARTH = BLOCKS.registerBlock("decorative_earth", 
        BlockDecorativeEarth::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F));
    
    public static final DeferredBlock<Block> SOD_PLANKS = BLOCKS.registerBlock("sod_planks", 
        BlockDecorativeSodPlank::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 15.0F));
    
    public static final DeferredBlock<Block> EMPTY_SERICULTURE = BLOCKS.registerBlock("empty_sericulture",
        () -> new BlockDecorativeUpdate(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD), 
            DECORATIVE_WOOD.get().defaultBlockState()),
        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD));
    
    public static final DeferredBlock<Block> MUD_BRICK = BLOCKS.registerBlock("mud_brick",
        () -> new BlockDecorativeUpdate(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT), 
            DECORATIVE_EARTH.get().defaultBlockState()),
        BlockBehaviour.Properties.of().mapColor(MapColor.DIRT));

    // Dalles et escaliers
    public static final DeferredBlock<Block> THATCH_SLAB = BLOCKS.registerBlock("thatch_slab",
        () -> new BlockOrientedSlab(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD), THATCH_SLAB.get()),
        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD));
    
    public static final DeferredBlock<Block> THATCH_SLAB_DOUBLE = BLOCKS.registerBlock("thatch_slab_double",
        () -> new BlockOrientedSlab(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD), THATCH_SLAB.get()),
        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD));
    
    public static final DeferredBlock<Block> THATCH_STAIRS = BLOCKS.registerBlock("thatch_stairs",
        () -> new BlockDecorativeOrientedStairs(DECORATIVE_WOOD.get().defaultBlockState(), 
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD)),
        BlockBehaviour.Properties.of().mapColor(MapColor.WOOD));

    // Byzantins
    public static final DeferredBlock<Block> BYZANTINE_TILE = BLOCKS.registerBlock("byzantine_tile",
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)),
        BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    
    public static final DeferredBlock<Block> BYZANTINE_STONE_TILE = BLOCKS.registerBlock("byzantine_stone_tile",
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)),
        BlockBehaviour.Properties.of().mapColor(MapColor.STONE));

    // Cultures
    public static final DeferredBlock<Block> CROP_TURMERIC = BLOCKS.registerBlock("crop_turmeric",
        () -> new BlockMillCrops(false, false, null), // Ajouter la seed appropriée
        BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak());
    
    public static final DeferredBlock<Block> CROP_RICE = BLOCKS.registerBlock("crop_rice",
        () -> new BlockMillCrops(true, false, null),
        BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak());

    // Coffres et signes
    public static final DeferredBlock<Block> MILL_CHEST = BLOCKS.registerBlock("mill_chest",
        BlockMillChest::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(-1.0F, 6000000.0F));
    
    public static final DeferredBlock<Block> MILL_SIGN = BLOCKS.registerBlock("mill_sign",
        BlockMillSign::new, BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noCollission().strength(1.0F));

    // Alchimistes et chemins
    public static final DeferredBlock<Block> ALCHEMISTS = BLOCKS.registerBlock("alchemists",
        BlockAlchemists::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1.0F, 6000000.0F));
    
    public static final DeferredBlock<Block> MILL_PATH = BLOCKS.registerBlock("mill_path",
        BlockMillPath::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F));
    
    public static final DeferredBlock<Block> MILL_PATH_SLAB = BLOCKS.registerBlock("mill_path_slab",
        BlockMillPathSlabHalf::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F));
    
    public static final DeferredBlock<Block> MILL_PATH_SLAB_DOUBLE = BLOCKS.registerBlock("mill_path_slab_double",
        BlockMillPathSlabDouble::new, BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F));

    // Minerais et pierres de village
    public static final DeferredBlock<Block> GALIANITE_ORE = BLOCKS.registerBlock("galianite_ore",
        () -> new BlockMillOre(BlockMillOre.EnumMillOre.GALIANITE),
        BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F));
    
    public static final DeferredBlock<Block> VILLAGE_STONE = BLOCKS.registerBlock("village_stone",
        BlockVillageStone::new, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1.0F, 6000000.0F));

    // Position stockée
    public static final DeferredBlock<Block> STORED_POSITION = BLOCKS.registerBlock("stored_position",
        StoredPosition::new, BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noCollission().noLootTable());

    // Block Entities
    public static final Supplier<BlockEntityType<TileEntityMillChest>> MILL_CHEST_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("mill_chest", 
            () -> BlockEntityType.Builder.of(TileEntityMillChest::new, MILL_CHEST.get()).build(null));
    
    public static final Supplier<BlockEntityType<TileEntityMillSign>> MILL_SIGN_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("mill_sign",
            () -> BlockEntityType.Builder.of(TileEntityMillSign::new, MILL_SIGN.get()).build(null));
    
    public static final Supplier<BlockEntityType<TileEntityVillageStone>> VILLAGE_STONE_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("village_stone",
            () -> BlockEntityType.Builder.of(TileEntityVillageStone::new, VILLAGE_STONE.get()).build(null));

    // Items pour les blocs
    static {
        // Enregistrement automatique des BlockItems pour les blocs simples
        BLOCKS.getEntries().forEach(block -> {
            if (block != STORED_POSITION) { // Exclure les blocs spéciaux
                ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
            }
        });
    }
}