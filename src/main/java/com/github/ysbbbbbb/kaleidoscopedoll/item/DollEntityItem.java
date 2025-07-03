package com.github.ysbbbbbb.kaleidoscopedoll.item;

import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModDataComponent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModEntities;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity.TAG_BLOCK_STATE;

public class DollEntityItem extends Item {
    private static final String TAG_DOLL_ENTITY = "doll_entity";
    private static final int THROW_DURATION = 5;

    public DollEntityItem() {
        super(new Properties());
    }

    public static ItemStack createItemWithEntity(DollEntity entity) {
        ItemStack stack = new ItemStack(ModItems.DOLL_ENTITY_ITEM);
        saveDollEntity(stack, entity);
        return stack;
    }

    public static ItemStack createItemWithBlockState(BlockState state) {
        ItemStack stack = new ItemStack(ModItems.DOLL_ENTITY_ITEM);
        CompoundTag entityTag = new CompoundTag();
        entityTag.put(TAG_BLOCK_STATE, NbtUtils.writeBlockState(state));
        stack.set(ModDataComponent.TAG_DOLL_ENTITY, entityTag);
        return stack;
    }

    public static void saveDollEntity(ItemStack stack, DollEntity entity) {
        if (!stack.is(ModItems.DOLL_ENTITY_ITEM)) {
            return;
        }

        CompoundTag entityTag = new CompoundTag();
        entity.addAdditionalSaveData(entityTag);

        stack.set(ModDataComponent.TAG_DOLL_ENTITY, entityTag);
    }

    public static Block getBlockFromItemStack(ItemStack stack) {
        if (!stack.is(ModItems.DOLL_ENTITY_ITEM)) {
            return ModBlocks.PURPLE_DOLL_GIFT_BOX;
        }

        CompoundTag entityTag = stack.get(ModDataComponent.TAG_DOLL_ENTITY);
        if (entityTag != null && entityTag.contains(TAG_BLOCK_STATE)) {
            CompoundTag compound = entityTag.getCompound(TAG_BLOCK_STATE);
            HolderLookup<Block> lookup = BuiltInRegistries.BLOCK.asLookup();
            return NbtUtils.readBlockState(lookup, compound).getBlock();
        }

        return ModBlocks.PURPLE_DOLL_GIFT_BOX;
    }

    public static DollEntity getDollEntity(Level level, ItemStack stack) {
        if (!stack.is(ModItems.DOLL_ENTITY_ITEM)) {
            return new DollEntity(ModEntities.DOLL, level);
        }
        CompoundTag entityTag = stack.get(ModDataComponent.TAG_DOLL_ENTITY);
        if (entityTag == null) {
            return new DollEntity(ModEntities.DOLL, level);
        }
        DollEntity entity = new DollEntity(ModEntities.DOLL, level);
        entity.load(entityTag);
        return entity;
    }

    public static boolean hasEntityData(ItemStack stack) {
        return stack.has(ModDataComponent.TAG_DOLL_ENTITY);
    }

    public static void clearEntityData(ItemStack stack) {
        stack.remove(ModDataComponent.TAG_DOLL_ENTITY);
    }

    public static void addCreativeTab(CreativeModeTab.Output output) {
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> output.accept(createItemWithBlockState(block.defaultBlockState())));
    }

    private boolean mayPlace(Player pPlayer, Direction pDirection, ItemStack pItemStack, BlockPos pPos) {
        return !pPlayer.level().isOutsideBuildHeight(pPos) && pPlayer.mayUseItemAt(pPos, pDirection, pItemStack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // 如果玩家蹲下，则进入投掷模式
        if (player.isShiftKeyDown() && GeneralConfig.DOLL_CAN_BE_THROWN.get()) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }

        // 否则返回通过，允许正常的右键放置
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        // 计算使用时间
        int useTime = this.getUseDuration(stack, entity) - timeLeft;

        // 如果使用时间达到要求，则投掷
        if (useTime >= THROW_DURATION && GeneralConfig.DOLL_CAN_BE_THROWN.get()) {
            int strength = useTime - THROW_DURATION;
            throwDollEntity(level, entity, stack, strength);
        }
    }

    private void throwDollEntity(Level level, LivingEntity entity, ItemStack stack, int strength) {
        if (!level.isClientSide) {
            // 从物品获取或创建实体
            DollEntity dollEntity = getDollEntity(level, stack);
            dollEntity.setInThrowing(true);

            // 如果实体的 BlockState 为空气，设置为紫色礼盒
            if (dollEntity.getDisplayBlockState().isAir()) {
                dollEntity.setDisplayBlockState(ModBlocks.PURPLE_DOLL_GIFT_BOX.defaultBlockState());
            }

            // 设置实体的初始位置（在玩家眼部高度）
            Vec3 playerEyePos = entity.getEyePosition();
            dollEntity.setPos(playerEyePos.x, playerEyePos.y - 0.1, playerEyePos.z);

            // 获取玩家的视线方向
            Vec3 lookAngle = entity.getLookAngle();

            // 设置投掷速度
            double throwPower = Mth.clamp(strength * 0.1, 0.1, 3);
            dollEntity.setDeltaMovement(lookAngle.x * throwPower, lookAngle.y * throwPower, lookAngle.z * throwPower);

            // 设置实体的旋转角度
            dollEntity.setYRot(entity.getYRot() - 180);

            // 播放投掷音效
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SNOWBALL_THROW,
                    SoundSource.PLAYERS, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

            // 添加实体到世界
            level.addFreshEntity(dollEntity);

            if (!(entity instanceof Player player) || !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos clickedPos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();

        if (player == null || !this.mayPlace(player, clickedFace, stack, clickedPos)) {
            return InteractionResult.FAIL;
        }

        // 如果玩家蹲下，不进行放置，让投掷逻辑处理
        if (player.isShiftKeyDown()) {
            return InteractionResult.PASS;
        }

        // 检查位置是否可以放置实体
        BlockPos spawnPos = clickedPos.relative(clickedFace);
        Vec3 spawnLocation = Vec3.atBottomCenterOf(spawnPos);

        // 从物品获取或创建实体
        DollEntity dollEntity = getDollEntity(level, stack);

        // 如果实体的BlockState为空气，设置为紫色礼盒
        if (dollEntity.getDisplayBlockState().isAir()) {
            dollEntity.setDisplayBlockState(ModBlocks.PURPLE_DOLL_GIFT_BOX.defaultBlockState());
        }

        // 设置实体位置和朝向
        dollEntity.setPos(spawnLocation.x, spawnLocation.y, spawnLocation.z);
        dollEntity.setYRot(player.getYRot() - 180);

        // 生成实体到世界
        if (dollEntity.canSurvives()) {
            if (!level.isClientSide) {
                dollEntity.playSound(SoundEvents.WOOL_PLACE, 1.0F, 1.0F);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, dollEntity.position());
                level.addFreshEntity(dollEntity);
            }
            // 消耗物品
            stack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltips, TooltipFlag isAdvanced) {
        tooltips.add(Component.translatable("item.kaleidoscopedoll.doll_entity_item.throw.tooltip").withStyle(ChatFormatting.GRAY));
        tooltips.add(Component.translatable("item.kaleidoscopedoll.doll_entity_item.place.tooltip").withStyle(ChatFormatting.GRAY));
    }
}
