package com.github.ysbbbbbb.kaleidoscopedoll.block;

import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DollMachineBlock extends HorizontalDirectionalBlock {
    private static final VoxelShape SHAPE_UPPER = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 8.0d, 15.0d);
    private static final VoxelShape SHAPE = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 16.0d, 15.0d);
    private static final BooleanProperty LOTTERY_IN_PROGRESS = BooleanProperty.create("lottery_in_progress");
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public DollMachineBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE)
                .sound(SoundType.COPPER)
                .strength(8f, 10f)
                .lightLevel(s -> 2)
                .noOcclusion());
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LOTTERY_IN_PROGRESS, false)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        DoubleBlockHalf blockHalf = state.getValue(HALF);
        if (facing.getAxis() == Direction.Axis.Y && blockHalf == DoubleBlockHalf.LOWER == (facing == Direction.UP)) {
            boolean condition = facingState.is(this) && facingState.getValue(HALF) != blockHalf;
            return condition ? state.setValue(FACING, facingState.getValue(FACING)).setValue(LOTTERY_IN_PROGRESS, facingState.getValue(LOTTERY_IN_PROGRESS)) : Blocks.AIR.defaultBlockState();
        } else {
            boolean condition = blockHalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !state.canSurvive(level, currentPos);
            return condition ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, level, currentPos, facingPos);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        if (clickedPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(clickedPos.above()).canBeReplaced(context)) {
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity entity, ItemStack pStack) {
        pLevel.setBlock(pPos.above(), pState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        BlockState blockstate = pLevel.getBlockState(blockpos);
        return pState.getValue(HALF) == DoubleBlockHalf.LOWER ? blockstate.isFaceSturdy(pLevel, blockpos, Direction.UP) : blockstate.is(this);
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        if (entity.getItemInHand(hand).is(TagItem.DOLL_MACHINE_TOKENS) && !blockstate.getValue(LOTTERY_IN_PROGRESS)) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            entity.getItemInHand(hand).shrink(1);
            world.setBlockAndUpdate(pos, blockstate.setValue(LOTTERY_IN_PROGRESS, true));
            world.playSound(null, pos, SoundEvents.LARGE_AMETHYST_BUD_PLACE, SoundSource.BLOCKS, 4f, 2f);
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.WAX_ON, x + 0.5, y + 1, z + 0.5, 8, 0.5, 0.5, 0.5, 0.2);
                serverLevel.sendParticles(ParticleTypes.WAX_OFF, x + 0.5, y + 1, z + 0.5, 8, 0.5, 0.5, 0.5, 0.2);
                world.scheduleTick(pos, blockstate.getBlock(), 60);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(blockstate, world, pos, entity, hand, hit);
    }

    @Override
    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        if (blockstate.getValue(LOTTERY_IN_PROGRESS)) {
            dropGiftBox(world, blockstate, pos, random);
        }
    }

    public void dropGiftBox(ServerLevel world, BlockState blockstate, BlockPos pos, RandomSource random) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        world.sendParticles(ParticleTypes.ANGRY_VILLAGER.getType(), x, y + 1.8, z, 10, 0.1, 0.1, 0.1, 0);
        world.playSound(null, x, y, z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        world.setBlockAndUpdate(pos, blockstate.setValue(LOTTERY_IN_PROGRESS, false));

        int yellowCount = GeneralConfig.YELLOW_DOLL_GIFT_BOX_WEIGHT.get();
        int greenCount = yellowCount + GeneralConfig.GREEN_DOLL_GIFT_BOX_WEIGHT.get();
        int totalCount = greenCount + GeneralConfig.PURPLE_DOLL_GIFT_BOX_WEIGHT.get();
        int count = random.nextInt(0, totalCount);

        ItemEntity item;
        if (count <= yellowCount) {
            item = new ItemEntity(world, x + 0.5, y + 1.8, z + 0.5, ModItems.YELLOW_DOLL_GIFT_BOX.get().getDefaultInstance());
        } else if (count <= greenCount) {
            item = new ItemEntity(world, x + 0.5, y + 1.8, z + 0.5, ModItems.GREEN_DOLL_GIFT_BOX.get().getDefaultInstance());
        } else {
            item = new ItemEntity(world, x + 0.5, y + 1.8, z + 0.5, ModItems.PURPLE_DOLL_GIFT_BOX.get().getDefaultInstance());
        }
        item.setPickUpDelay(10);
        world.addFreshEntity(item);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (blockState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return SHAPE;
        } else {
            return SHAPE_UPPER;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LOTTERY_IN_PROGRESS, HALF);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> tooltip, TooltipFlag pFlag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_doll.doll_machine").withStyle(ChatFormatting.DARK_GRAY));
    }
}
