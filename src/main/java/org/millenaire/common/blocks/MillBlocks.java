package org.millenaire.common.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder; // Utilisation de DeferredHolder pour la clarté

import org.millenaire.common.entities.TileEntityMillChest;
import org.millenaire.common.entities.TileEntityMillSign;
import org.millenaire.common.entities.TileEntityVillageStone;

import java.util.function.Supplier;

public class MillBlocks {
    // Les DeferredRegister doivent être publics et statiques (Correct)
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("millenaire");
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "millenaire");
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems("millenaire");

    /**
     * Helper pour enregistrer le bloc ET son BlockItem standard.
     * Les blocs utilisant cette méthode DOIVENT utiliser un BlockItem simple.
     */
    private static <T extends Block> DeferredHolder<Block, T> registerBlockWithSimpleItem(final String name, final Supplier<T> blockSupplier) {
        DeferredHolder<Block, T> block = BLOCKS.register(name, blockSupplier);
        // Enregistre l'ItemBlock correspondant
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    // --- Décoratifs ---
    
    // CORRECTION : S'assure que BlockDecorativeStone::new reçoit les propriétés via le Supplier.
    public static final DeferredHolder<Block, Block> DECORATIVE_STONE = registerBlockWithSimpleItem("decorative_stone", 
        () -> new BlockDecorativeStone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F)));
    
    public static final DeferredHolder<Block, Block> DECORATIVE_WOOD = registerBlockWithSimpleItem("decorative_wood", 
        () -> new BlockDecorativeWood(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F)));
    
    public static final DeferredHolder<Block, Block> DECORATIVE_EARTH = registerBlockWithSimpleItem("decorative_earth", 
        () -> new BlockDecorativeEarth(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F)));
    
    public static final DeferredHolder<Block, Block> SOD_PLANKS = registerBlockWithSimpleItem("sod_planks", 
        () -> new BlockDecorativeSodPlank(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F, 15.0F)));
    
    // BlockDecorativeUpdate nécessite la BlockState par défaut en argument.
    public static final DeferredHolder<Block, Block> EMPTY_SERICULTURE = registerBlockWithSimpleItem("empty_sericulture",
        () -> new BlockDecorativeUpdate(
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F),
            DECORATIVE_WOOD.get().defaultBlockState())
    );
    
    public static final DeferredHolder<Block, Block> MUD_BRICK = registerBlockWithSimpleItem("mud_brick",
        () -> new BlockDecorativeUpdate(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F),
            DECORATIVE_EARTH.get().defaultBlockState())
    );

    // --- Dalles et escaliers ---
    // NOTE : Ces blocs utilisaient des ItemBlock personnalisés dans la 1.12.2  (ItemOrientedSlab). 
    // J'utilise le BlockItem standard ici, mais vous devrez migrer la logique personnalisée d'ItemOrientedSlab si elle est critique.

    // THATCH SLAB (Half)
    public static final DeferredHolder<Block, Block> THATCH_SLAB = BLOCKS.register("thatch_slab",
        () -> new BlockOrientedSlab(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F), THATCH_SLAB.get())
    );
    // THATCH SLAB DOUBLE (Full) - N'a pas d'ItemBlock.
    public static final DeferredHolder<Block, Block> THATCH_SLAB_DOUBLE = BLOCKS.register("thatch_slab_double",
        () -> new BlockOrientedSlab(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F), THATCH_SLAB.get())
    );
    // Enregistrement de l'ItemBlock pour le THATCH_SLAB
    public static final DeferredHolder<Item, Item> THATCH_SLAB_ITEM = ITEMS.register("thatch_slab", 
        () -> new BlockItem(THATCH_SLAB.get(), new Item.Properties())
    ); 

    public static final DeferredHolder<Block, Block> THATCH_STAIRS = registerBlockWithSimpleItem("thatch_stairs",
        () -> new BlockDecorativeOrientedStairs(
            DECORATIVE_WOOD.get().defaultBlockState(), 
            BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F))
    );

    // --- Byzantins ---

    public static final DeferredHolder<Block, Block> BYZANTINE_TILE = registerBlockWithSimpleItem("byzantine_tile",
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F))
    );
    
    public static final DeferredHolder<Block, Block> BYZANTINE_STONE_TILE = registerBlockWithSimpleItem("byzantine_stone_tile",
        () -> new BlockDecorativeOriented(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(1.5F, 6.0F))
    );

    // --- Cultures ---
    // CORRECTION : Les cultures n'ont pas d'ItemBlock (n'utilisent pas registerBlockWithSimpleItem).
    // NOTE : BlockMillCrops(boolean, boolean, Item)  – Le troisième argument doit être la graine (seed item).
    public static final DeferredHolder<Block, Block> CROP_TURMERIC = BLOCKS.register("crop_turmeric",
        () -> new BlockMillCrops(false, false, null) // 'null' doit être remplacé par le Supplier de la graine
    );

    public static final DeferredHolder<Block, Block> CROP_RICE = BLOCKS.register("crop_rice",
        () -> new BlockMillCrops(true, false, null) // 'null' doit être remplacé par le Supplier de la graine
    );

    // --- Coffres et signes ---
    
    public static final DeferredHolder<Block, Block> MILL_CHEST = registerBlockWithSimpleItem("mill_chest",
        () -> new BlockMillChest(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(-1.0F, 6000000.0F))
    );
    
    public static final DeferredHolder<Block, Block> MILL_SIGN = registerBlockWithSimpleItem("mill_sign",
        () -> new BlockMillSign(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noCollission().strength(1.0F))
    );

    // --- Alchimistes et chemins ---

    public static final DeferredHolder<Block, Block> ALCHEMISTS = registerBlockWithSimpleItem("alchemists",
        () -> new BlockAlchemists(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1.0F, 6000000.0F))
    );
    
    // NOTE : Les chemins utilisaient un ItemMillPath personnalisé, on gère le bloc et l'item séparément.
    public static final DeferredHolder<Block, Block> MILL_PATH = BLOCKS.register("mill_path",
        () -> new BlockMillPath(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F))
    );
    public static final DeferredHolder<Item, Item> MILL_PATH_ITEM = ITEMS.register("mill_path", 
        // L'ItemBlock personnalisé doit être migré ici, je laisse BlockItem standard pour la compilation.
        () -> new BlockItem(MILL_PATH.get(), new Item.Properties())
    );

    public static final DeferredHolder<Block, Block> MILL_PATH_SLAB = BLOCKS.register("mill_path_slab",
        () -> new BlockMillPathSlabHalf(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F))
    );
    public static final DeferredHolder<Block, Block> MILL_PATH_SLAB_DOUBLE = BLOCKS.register("mill_path_slab_double",
        () -> new BlockMillPathSlabDouble(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F))
    );
    public static final DeferredHolder<Item, Item> MILL_PATH_SLAB_ITEM = ITEMS.register("mill_path_slab", 
        // L'ItemBlock personnalisé doit être migré ici.
        () -> new BlockItem(MILL_PATH_SLAB.get(), new Item.Properties())
    );

    // --- Minerais et pierres de village ---
    
    // CORRECTION : Ajuste le constructeur pour inclure les propriétés.
    public static final DeferredHolder<Block, Block> GALIANITE_ORE = registerBlockWithSimpleItem("galianite_ore",
        () -> new BlockMillOre(BlockMillOre.EnumMillOre.GALIANITE, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F))
    );
    
    public static final DeferredHolder<Block, Block> VILLAGE_STONE = registerBlockWithSimpleItem("village_stone",
        () -> new BlockVillageStone(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(-1.0F, 6000000.0F))
    );

    // --- Position stockée ---
    // Pas d'ItemBlock pour ce bloc (conformément à l'ancien code ).
    public static final DeferredHolder<Block, Block> STORED_POSITION = BLOCKS.register("stored_position",
        () -> new StoredPosition(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noCollission().noLootTable())
    );

    // --- Block Entities ---
    // Les types de retour ont été ajustés pour utiliser DeferredHolder et correspondre à la nouvelle API.
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillChest>> MILL_CHEST_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("mill_chest", 
            () -> BlockEntityType.Builder.of(TileEntityMillChest::new, MILL_CHEST.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityMillSign>> MILL_SIGN_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("mill_sign",
            () -> BlockEntityType.Builder.of(TileEntityMillSign::new, MILL_SIGN.get()).build(null));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEntityVillageStone>> VILLAGE_STONE_BLOCK_ENTITY = 
        BLOCK_ENTITIES.register("village_stone",
            () -> BlockEntityType.Builder.of(TileEntityVillageStone::new, VILLAGE_STONE.get()).build(null));
    
    // Le bloc 'static' de fin a été retiré, sa logique étant déplacée dans registerBlockWithSimpleItem.
}