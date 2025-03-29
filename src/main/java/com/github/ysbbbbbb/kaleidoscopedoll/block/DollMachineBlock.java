package com.github.ysbbbbbb.kaleidoscopedoll.block;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class DollMachineBlock extends HorizontalDirectionalBlock {
    private static final MapCodec<DollMachineBlock> CODEC = simpleCodec(prop -> new DollMachineBlock());
    private static final VoxelShape SHAPE_UPPER = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 8.0d, 15.0d);
    private static final VoxelShape SHAPE = Block.box(1.0d, 0.0d, 1.0d, 15.0d, 16.0d, 15.0d);
    private static final BooleanProperty LOTTERY_IN_PROGRESS = BooleanProperty.create("lottery_in_progress");
    private static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    private static final TagKey<Item> DOLL_MACHINE_TOKENS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_machine_tokens"));

    public DollMachineBlock() {
        super(Properties.of().ignitedByLava()
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
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
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
        return super.playerWillDestroy(level, pos, state, player);
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
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        if (stack.is(DOLL_MACHINE_TOKENS) && !blockstate.getValue(LOTTERY_IN_PROGRESS)) {
            int x = pos.getX();
            int y = blockstate.getValue(HALF) == DoubleBlockHalf.LOWER ? pos.getY() : pos.getY() - 1;
            int z = pos.getZ();
            stack.shrink(1);
            world.setBlockAndUpdate(pos, blockstate.setValue(LOTTERY_IN_PROGRESS, true));
            world.playSound(null, pos, SoundEvents.LARGE_AMETHYST_BUD_PLACE, SoundSource.BLOCKS, 4f, 2f);
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.WAX_ON, x + 0.5, y + 1, z + 0.5, 8, 0.5, 0.5, 0.5, 0.2);
                serverLevel.sendParticles(ParticleTypes.WAX_OFF, x + 0.5, y + 1, z + 0.5, 8, 0.5, 0.5, 0.5, 0.2);
                world.scheduleTick(pos, blockstate.getBlock(), 60);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, blockstate, world, pos, entity, hand, hit);
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
    public void appendHoverText(ItemStack pStack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag pFlag) {
        tooltip.add(Component.translatable("tooltip.kaleidoscope_doll.doll_machine").withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}