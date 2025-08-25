package com.github.ysbbbbbb.kaleidoscopedoll.block;

import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.DollMachineBlockEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.GiftBoxItem;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DollMachineBlock extends HorizontalDirectionalBlock implements EntityBlock {
    private static final VoxelShape SHAPE_UPPER = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 8.0d, 15.0d);
    private static final VoxelShape SHAPE = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 16.0d, 15.0d);
    private static final BooleanProperty LOTTERY_IN_PROGRESS = BooleanProperty.create("lottery_in_progress");
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public DollMachineBlock() {
        super(BlockBehaviour.Properties.of().ignitedByLava()
                .instrument(NoteBlockInstrument.BASS)
                .sound(SoundType.COPPER)
                .strength(8f, 10f)
                .lightLevel(s -> 2)
                .pushReaction(PushReaction.BLOCK)
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
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            DoubleBlockHalf value = state.getValue(HALF);
            if (value == DoubleBlockHalf.UPPER) {
                BlockPos below = pos.below();
                BlockState belowState = level.getBlockState(below);
                if (belowState.is(state.getBlock()) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER) {
                    BlockState blockState = belowState.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();
                    level.setBlock(below, blockState, Block.UPDATE_SUPPRESS_DROPS | Block.UPDATE_ALL);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, below, Block.getId(belowState));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
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
        ItemStack itemInHand = entity.getItemInHand(hand);
        if (itemInHand.is(TagItem.MACHINE_TOKENS) && !blockstate.getValue(LOTTERY_IN_PROGRESS)) {
            int x = pos.getX();
            int y = blockstate.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.getY() : pos.getY() - 1;
            int z = pos.getZ();

            // 触发 te
            BlockPos tePos = new BlockPos(x, y, z);
            if (world.getBlockEntity(tePos) instanceof DollMachineBlockEntity te) {
                te.onTokensClicked(itemInHand, world);
            }

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
        int y = blockstate.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.getY() : pos.getY() - 1;
        int z = pos.getZ();

        world.sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), x + 0.5, y + 1.8, z + 0.5,
                10, 0.5, 0.5, 0.5, 0.5);
        world.playSound(null, x, y, z, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1f, 1f);
        world.setBlockAndUpdate(pos, blockstate.setValue(LOTTERY_IN_PROGRESS, false));

        // 触发 te
        BlockPos tePos = new BlockPos(x, y, z);
        ItemStack result = ItemStack.EMPTY;
        int tier = 0;
        if (world.getBlockEntity(tePos) instanceof DollMachineBlockEntity te) {
            tier = te.getTier();
            result = te.onFinishLottery();
        }
        if (result.isEmpty()) {
            return;
        }

        Item item;
        if (tier == 0) {
            item = ModItems.GREEN_DOLL_GIFT_BOX.get();
        } else if (tier == 1) {
            item = ModItems.YELLOW_DOLL_GIFT_BOX.get();
        } else {
            item = ModItems.PURPLE_DOLL_GIFT_BOX.get();
        }

        ItemStack gift = new ItemStack(item);
        GiftBoxItem.setDoll(result, gift);
        ItemEntity itemEntity = new ItemEntity(world, x + 0.5, y + 1.8, z + 0.5, gift);
        itemEntity.setPickUpDelay(10);
        world.addFreshEntity(itemEntity);
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        if (pState.getValue(HALF) == DoubleBlockHalf.LOWER) {
            return new DollMachineBlockEntity(pPos, pState);
        }
        return null;
    }
}
