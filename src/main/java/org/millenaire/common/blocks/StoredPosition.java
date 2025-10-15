package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class StoredPosition extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);
    private boolean showParticles = false;

    private final Vector3f sourceColor = new Vector3f(0.175f, 0.7f, 0.087f); // Vert
    private final Vector3f tradeColor = new Vector3f(0.039f, 0.39f, 0.98f); // Bleu
    private final Vector3f pathColor = new Vector3f(1.0f, 0.98f, 0.0f); // Jaune
    private final Vector3f sleepColor = new Vector3f(0.22f, 0.45f, 0.88f); // Bleu clair
    private final Vector3f defendColor = new Vector3f(1.0f, 0.0f, 0.0f); // Rouge
    private final Vector3f hideColor = new Vector3f(0.5f, 0.25f, 0.0f); // Marron

    public StoredPosition(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.TRADE_POS));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (showParticles) {
            Vector3f color = switch (state.getValue(VARIANT)) {
                case TRADE_POS -> tradeColor;
                case SOURCE_POS -> sourceColor;
                case PATH_POS -> pathColor;
                case SLEEP_POS -> sleepColor;
                case DEFEND_POS -> defendColor;
                case HIDE_POS -> hideColor;
            };

            level.addParticle(new DustParticleOptions(color, 1.0F), 
                pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return showParticles ? Shapes.block() : Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }

    public void setShowParticles(boolean bool) { 
        showParticles = bool; 
    }
    
    public boolean getShowParticles() { 
        return showParticles; 
    }

    public enum EnumType implements StringRepresentable {
        TRADE_POS("trade_pos"),
        SOURCE_POS("source_pos"),
        PATH_POS("path_pos"),
        HIDE_POS("hide_pos"),
        DEFEND_POS("defend_pos"),
        SLEEP_POS("sleep_pos");

        private final String name;

        EnumType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}