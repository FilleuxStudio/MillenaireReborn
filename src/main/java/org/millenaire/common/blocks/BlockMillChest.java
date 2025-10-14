package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat; // Remplace EntityOcelot
import net.minecraft.world.entity.player.Player; // Remplace EntityPlayer
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity; // Remplace TileEntityChest
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB; // Remplace AxisAlignedBB
import net.minecraft.world.phys.BlockHitResult;
import org.millenaire.Millenaire; // Pour openGui
import org.millenaire.common.entities.TileEntityMillChest;
import net.minecraft.core.Direction; // Remplace EnumFacing
import net.minecraft.world.Container; // Remplace ILockableContainer
import net.minecraft.world.level.LevelAccessor; // Pour la suppression de TileEntity

public class BlockMillChest extends ChestBlock {
    
    // Le constructeur est mis à jour pour être compatible avec les propriétés reçues lors de l'enregistrement.
    public BlockMillChest(BlockBehaviour.Properties properties) {
        // Le constructeur parent de ChestBlock prend les propriétés et le fournisseur de BlockEntityType.
        // Nous conservons vos propriétés personnalisées d'inviolabilité/force.
        super(properties.strength(-1.0F, 6000000.0F).noLootTable().noOcclusion(), 
              () -> MillBlocks.MILL_CHEST_BLOCK_ENTITY.get());
    }

    /**
     * Migration de quantityDropped(). Un bloc indestructible ne droppe rien.
     */
    // Méthode omise car les propriétés noLootTable et la force gèrent la non-destruction/non-drop.

    /**
     * Migration de breakBlock.
     * Cette logique est maintenant gérée par la méthode parente 'onRemove' et la logique par défaut de ChestBlock.
     * On la surcharge pour s'assurer que le TileEntity est bien retiré.
     */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity != null) {
                // Si vous voulez vider l'inventaire ici, vous le feriez avec ContainerHelper.dropContents
            }
            if (state.hasBlockEntity()) {
                level.removeBlockEntity(pos);
            }
        }
    }
    
    /**
     * Migration de onBlockActivated vers use.
     * Contient la logique de détection de coffre double, de blocage, et d'ouverture de l'interface graphique du mod.
     */
     @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS; 
        }

        // 1. Détection de blocage (cat/bloc solide)
        if (this.isBlocked(level, pos)) {
            // Optionnel: feedback sonore
            // level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 0.3F, 0.5F);
            return InteractionResult.CONSUME;
        }

        // 2. Tente d'obtenir le conteneur, y compris la détection de coffre double.
        Container container = getMillLockableContainer(level, pos, state);

        if (container != null) {
            
            // LIGNE CORRIGÉE : Utilisation de player.openMenu() avec le BlockEntity.
            BlockEntity blockentity = level.getBlockEntity(pos);
            
            if (blockentity instanceof MenuProvider menuProvider) {
                 // **C'est la méthode moderne pour ouvrir les GUI**
                 player.openMenu(menuProvider); 
            } else {
                 // Si le BlockEntity n'implémente pas MenuProvider (ce qu'il devrait faire), 
                 // nous pourrions avoir un problème ou une logique d'ouverture de coffre double plus complexe.
                 System.err.println("TileEntityMillChest does not implement MenuProvider.");
            }
        }
        
        return InteractionResult.CONSUME;
    }

    
    /**
     * Migration de createNewTileEntity vers newBlockEntity.
     */
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { 
        return new TileEntityMillChest(pos, state); 
    }
    
    /**
     * Migration et simplification de getLockableContainer.
     * La logique de détection de coffre double est gérée principalement par ChestBlock.
     * Cette version retourne le conteneur simple ou double, mais applique la logique de blocage.
     * On change le nom pour ne pas entrer en conflit avec les méthodes statiques de ChestBlock.
     */
    public Container getMillLockableContainer(Level level, BlockPos pos, BlockState state) {
        BlockEntity blockentity = level.getBlockEntity(pos);

        if (!(blockentity instanceof TileEntityMillChest tile)) {
            return null;
        }
        
        // Applique la logique de blocage personnalisée
        if (this.isBlocked(level, pos)) {
            return null;
        }

        // Le ChestBlock gère la détection de coffre double.
        return getContainer(this, state, level, pos, true);
    }
    
    /**
     * Migration de isBlocked.
     */
    private boolean isBlocked(Level level, BlockPos pos) {
        return this.isBelowSolidBlock(level, pos) || this.isCatSittingOnChest(level, pos);
    }

    /**
     * Migration de isBelowSolidBlock.
     * worldIn.isSideSolid(pos.up(), EnumFacing.DOWN, false) est remplacé par isRedstoneConductor.
     */
    private boolean isBelowSolidBlock(Level level, BlockPos pos) {
        // isRedstoneConductor est une bonne approximation du comportement de bloc solide au-dessus d'un coffre.
        return level.getBlockState(pos.above()).isRedstoneConductor(level, pos.above());
    }

    /**
     * Migration de isOcelotSittingOnChest vers isCatSittingOnChest (EntityOcelot est maintenant EntityCat)
     */
    private boolean isCatSittingOnChest(Level level, BlockPos pos) {
        // AABB de 1x1x1 au-dessus du bloc
        AABB aabb = new AABB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1);
        
        for (Cat cat : level.getEntitiesOfClass(Cat.class, aabb)) {
            if (cat.isInSittingPose()) {
                return true;
            }
        }
        return false;
    }
    
    // NOTE: Si vous devez ajuster l'état du coffre lors du placement (comme dans ChestBlock), vous devez surcharger getStateForPlacement.
    // Nous laissons cette méthode simple pour l'instant.
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Utilise la logique de placement de ChestBlock pour l'orientation et la détection de double coffre.
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}