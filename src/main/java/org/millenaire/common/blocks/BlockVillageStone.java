package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import org.millenaire.common.entities.TileEntityVillageStone;

import com.mojang.serialization.MapCodec;

import javax.annotation.Nullable;

public class BlockVillageStone extends BaseEntityBlock {

    public BlockVillageStone() {
        super(Properties.of()
            .mapColor(MapColor.STONE)
            // setBlockUnbreakable() -> strength(-1.0F) et setResistance(6000000.0F) -> strength(..., 6000000.0F)
            .strength(-1.0F, 6000000.0F)
            .noLootTable() // quantityDropped = 0
        );
    }

     public BlockVillageStone(BlockBehaviour.Properties properties) {
        super(properties);
        
        // Toutes les propriétés sont maintenant passées par MillBlocks
        // Vous n'avez plus besoin d'appeler Properties.of()... dans cette classe.
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        // Remplace getRenderType() { return 3; }
        return RenderShape.MODEL;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            // Migration de ChatComponentText et playerIn.addChatMessage
            player.displayClientMessage(Component.literal("The Village name almost seems to shimmer in the twilight"), true);
        }

        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te) {
            if (te.testVar < 16) {
                te.testVar++;
            } else {
                te.testVar = 0;
            }
            // Important pour la persistance des données du TileEntity
            te.setChanged(); 
        }

        return InteractionResult.SUCCESS;
    }

    public void negate(Level level, BlockPos pos, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te) {
            te.willExplode = true;
            
            // Migration de worldIn.scheduleUpdate
            level.scheduleTick(pos, this, 60); 
            
            // Migration de "portal.portal" vers un événement sonore moderne
            level.playSound(null, pos, SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, 0.01F);
            te.setChanged(); 
        } else {
            System.err.println("Negation failed. BlockEntity not loaded correctly.");
        }
    }

    
    // Migration de public void updateTick(...)
    public void tick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TileEntityVillageStone te && te.willExplode) {
            
            // Suppression du bloc avant l'explosion
            level.removeBlock(pos, false); 
            
            // CORRECTION: Explosion migrée. L'ancienne version utilisait 2.0F et 'true' (pour le feu).
            // La signature correcte pour recréer une explosion avec feu sans entité spécifique est :
            // level.explode(source, x, y, z, power, causesFire, interaction)
            level.explode(null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 2.0F, true, Level.ExplosionInteraction.BLOCK);
            
            // PrimedTnt est inutile ici car vous ne le faites pas apparaître, vous l'utilisiez 
            // uniquement pour déterminer le type d'explosion dans l'ancien code.
        }
    }

    @Nullable
    @Override
    // Migration de public TileEntity createNewTileEntity(...)
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityVillageStone(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'codec'");
    }
    
    // CORRECTION: Suppression de l'override de getTicker. 
    // La logique de tick est déjà gérée par le block.tick planifié (level.scheduleTick).
    // Cette méthode n'est nécessaire que pour les mises à jour continues du BlockEntity.
}