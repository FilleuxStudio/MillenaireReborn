package org.millenaire.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class StoredPosition extends Block {
    public static final EnumProperty<EnumType> VARIANT = EnumProperty.create("variant", EnumType.class);
    
    private boolean showParticles = false;
    private final Vector3f sourceColor = new Vector3f(0.175f, 0.7f, 0.0875f);    // 44820
    private final Vector3f tradeColor = new Vector3f(0.039f, 0.39f, 0.153f);     // 9983
    private final Vector3f pathColor = new Vector3f(1.0f, 0.65f, 0.0f);          // 16766976
    private final Vector3f sleepColor = new Vector3f(0.224f, 0.224f, 0.875f);    // 57538
    private final Vector3f defendColor = new Vector3f(1.0f, 0.0f, 0.0f);         // 16711680
    private final Vector3f hideColor = new Vector3f(0.508f, 0.127f, 0.247f);     // 8323127

    public StoredPosition() {
        super(Properties.of()
            .mapColor(MapColor.NONE)
            .noCollission()
            .noLootTable()
            .strength(-1.0F, 3600000.0F)
            .isViewBlocking((state, level, pos) -> false)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, EnumType.TRADEPOS));
    }

        public StoredPosition(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
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
    public void animateTick(BlockState state, net.minecraft.world.level.Level level, BlockPos pos, RandomSource random) {
        if (showParticles) {
            Vector3f color = getColorForVariant(state.getValue(VARIANT));
            
            if (random.nextFloat() < 0.3f) {
                level.addParticle(new DustParticleOptions(color, 1.0f),
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    0.0D, 0.0D, 0.0D);
            }
        }
    }

    private Vector3f getColorForVariant(EnumType variant) {
        return switch (variant) {
            case TRADEPOS -> tradeColor;
            case SOURCEPOS -> sourceColor;
            case PATHPOS -> pathColor;
            case SLEEPPOS -> sleepColor;
            case DEFENDPOS -> defendColor;
            case HIDEPOS -> hideColor;
        };
    }

    public void setShowParticles(boolean bool) { 
        showParticles = bool; 
    }

    public boolean getShowParticles() { 
        return showParticles; 
    }

    public enum EnumType implements StringRepresentable {
        TRADEPOS("tradepos"),
        SOURCEPOS("sourcepos"),
        PATHPOS("pathpos"),
        HIDEPOS("hidepos"),
        DEFENDPOS("defendpos"),
        SLEEPPOS("sleeppos");
        
        private final String name;

        EnumType(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}