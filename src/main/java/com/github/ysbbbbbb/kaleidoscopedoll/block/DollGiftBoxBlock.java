package com.github.ysbbbbbb.kaleidoscopedoll.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DollGiftBoxBlock extends HorizontalDirectionalBlock implements IModBlock {
    private static final MapCodec<DollGiftBoxBlock> CODEC = simpleCodec(DollGiftBoxBlock::new);
    private static final VoxelShape SHAPE = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 15.0d, 15.0d);

    public DollGiftBoxBlock(Properties properties) {
        super(properties.instrument(NoteBlockInstrument.BASEDRUM)
                .sound(SoundType.WOOL)
                .strength(0f, 10f)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level instanceof ServerLevel serverLevel) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            serverLevel.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 50, 1, 1, 1, 0.2);
        }
        level.playSound(null, pos, SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.BLOCKS, 0.5f, 2f);
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public void appendHoverText(Consumer<Component> tooltipAdder) {
        tooltipAdder.accept(Component.translatable("tooltip.kaleidoscope_doll.doll_gift_box").withStyle(ChatFormatting.DARK_GRAY));
    }
}
