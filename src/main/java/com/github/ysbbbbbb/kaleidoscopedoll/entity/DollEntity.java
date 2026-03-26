package com.github.ysbbbbbb.kaleidoscopedoll.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.client.gui.TweaksToolScreen;
import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModEntities;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Rotations;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.fluids.FluidType;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class DollEntity extends Entity {
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Vector3f> DATA_SCALE = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DATA_TRANSLATION = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);

    private static final EntityDataAccessor<ItemStack> DATA_HOLD_ITEM = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Vector3f> DATA_ITEM_SCALE = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DATA_ITEM_TRANSLATION = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DATA_ITEM_ROTATION = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);

    private static final EntityDataAccessor<String> CUSTOM_DOLL_ID = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.STRING);

    public static final String TAG_BLOCK_STATE = "doll_block_state";
    public static final String TAG_CUSTOM_DOLL_ID = "custom_doll_id";

    private static final String TAG_SCALE = "doll_scale";
    private static final String TAG_TRANSLATION = "doll_translation";
    private static final String TAG_DROP_FROM_PHANTOM = "drop_from_phantom";
    private static final String TAG_DROP_FROM_PHANTOM_TIME = "drop_from_phantom_time";

    private static final String TAG_HOLD_ITEM = "hold_item";
    private static final String TAG_ITEM_SCALE = "item_scale";
    private static final String TAG_ITEM_TRANSLATION = "item_translation";
    private static final String TAG_ITEM_ROTATION = "item_rotation";

    private boolean inThrowing = false;
    private long bounceTime = 0;
    /**
     * 存取物品CD
     */
    private long takeCD = 0;

    /**
     * 用来标记是否是从幻翼上掉下来的，如果玩家x分钟内没有捡起，则自然消失
     */
    private boolean dropFromPhantom = false;
    /**
     * 计时器，单位 tick
     */
    private int dropFromPhantomTick = 0;
    /**
     * 用于碰撞击退的计数器，防止无限制左脚踩右脚上天
     */
    private int knockbackCount = 0;
    private int lastKnockbackTick = 0;

    public DollEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.refreshDimensions();
    }

    public DollEntity(Level level, double x, double y, double z, float yaw) {
        this(ModEntities.DOLL.get(), level);
        this.setPos(x, y, z);
        this.setYRot(yaw);
    }

    @Override
    public void tick() {
        // 调用父类的基础 tick 逻辑
        super.tick();

        // 先判断存活
        // 没有骑在幻翼上时才会计数
        if (this.dropFromPhantom && GeneralConfig.PHANTOM_DOLL_EXIST_TICKS.get() > 0
            && !(this.getVehicle() instanceof Phantom)) {
            this.dropFromPhantomTick++;
            if (this.dropFromPhantomTick >= GeneralConfig.PHANTOM_DOLL_EXIST_TICKS.get()) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.POOF, this.getRandomX(1), this.getRandomY(), this.getRandomZ(1),
                            20, 0.2, 0.2, 0.2, 0.02);
                }
                this.discard();
                return;
            }
        }

        if (this.onGround() || this.isInWater() || this.isInLava()) {
            // 如果在地面、水中或岩浆中，重置丢出状态
            this.inThrowing = false;
        } else if (this.getVehicle() == null && this.getPassengers().isEmpty() && this.getDeltaMovement().length() > 0.5) {
            // 如果没有乘坐其他实体且有足够速度，附加 360 度托马斯大回旋
            float rotationSpeed = Mth.randomBetween(level().random, 3, 5);
            // 随机方向
            float yRotSpeed = this.getUUID().getLeastSignificantBits() % 2 == 0 ? rotationSpeed : -rotationSpeed;
            float xRotSpeed = this.getUUID().getMostSignificantBits() % 2 == 0 ? rotationSpeed : -rotationSpeed;
            this.setYRot((this.getYRot() + yRotSpeed) % 360);
            this.setXRot((this.getXRot() + xRotSpeed) % 360);
        }

        if (this.inThrowing && this.level() instanceof ServerLevel serverLevel && GeneralConfig.DOLL_THROW_PARTICLE_EFFECT.get()) {
            // 渲染丢出去的拖尾粒子
            serverLevel.sendParticles(ParticleTypes.GLOW, this.getX(), this.getY() + 0.25, this.getZ(),
                    3, 0.1, 0.1, 0.1, 0.2);
        }

        // 碰撞击退检测
        // 仅在 2 tick 后开始检测，避免初始时触碰到丢玩偶的玩家
        if (tickCount > 2 && GeneralConfig.DOLL_CAN_KNOCKBACK_ENTITIES.get()) {
            this.checkCollisionKnockback();
        }

        // 记录上一刻的位置（用于插值渲染）
        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();

        // 获取当前位置的最高流体类型和运动向量
        FluidType fluidType = this.getMaxHeightFluidType();
        Vec3 movement = this.getDeltaMovement();
        // 流体影响高度
        double fluidHeight = 1.0E-5;

        // 流体中的运动逻辑
        if (!fluidType.isAir() && this.getFluidTypeHeight(fluidType) > fluidHeight) {
            double frictionFactor = 0;
            double floatingFactor = 0;
            if (GeneralConfig.DOLL_AFFECTED_BY_WATER.get()) {
                // 流体摩擦阻力，岩浆阻力更大
                frictionFactor = this.isInLava() ? 0.9 : 0.95;
                // 轻微上浮力
                floatingFactor = movement.y < 0.06 ? 5.0E-4 : 0;
            }
            this.setDeltaMovement(movement.x * frictionFactor, movement.y + floatingFactor, movement.z * frictionFactor);
        } else if (!this.isNoGravity()) {
            // 不在流体中且有重力时，应用重力
            double gravity = GeneralConfig.DOLL_AFFECTED_BY_GRAVITY.get() ? 0.04 : 0;
            this.setDeltaMovement(movement.add(0, -gravity, 0));
        }

        // 移动和摩擦力处理
        // 不在地面、有速度或每 4 tick 强制更新
        if (!this.onGround() || this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-5 || (this.tickCount + this.getId()) % 4 == 0) {
            // 执行移动
            this.move(MoverType.SELF, this.getDeltaMovement());
            // 默认摩擦系数
            double frictionFactor = 0.98;
            if (this.onGround()) {
                // 在地面时使用地面方块的摩擦系数
                BlockPos groundPos = getBlockPosBelowThatAffectsMyMovement();
                frictionFactor = this.level().getBlockState(groundPos).getFriction(level(), groundPos, this) * 0.98;
            }
            // 应用摩擦力（水平和垂直分别处理）
            this.setDeltaMovement(this.getDeltaMovement().multiply(frictionFactor, 0.98, frictionFactor));
            // 落地弹跳效果
            if (this.onGround()) {
                Vec3 bounceMovement = this.getDeltaMovement();
                if (bounceMovement.y < 0) {
                    // Y 速度反向减半
                    this.setDeltaMovement(bounceMovement.multiply(1, -0.5, 1));
                }
            }
        }

        // 水流推动检测
        if (GeneralConfig.DOLL_AFFECTED_BY_WATER.get()) {
            this.hasImpulse |= this.updateInWaterStateAndDoFluidPushing();
        }

        // 服务端检查运动变化（用于网络同步）
        if (!this.level().isClientSide) {
            double movementDelta = this.getDeltaMovement().subtract(movement).lengthSqr();
            if (movementDelta > 0.01D) {
                // 标记需要同步给客户端
                this.hasImpulse = true;
            }
        }
    }

    public boolean canSurvives() {
        if (!this.level().noCollision(this)) {
            return false;
        }
        return this.level().getEntities(this, this.getBoundingBox(), e -> e instanceof DollEntity).isEmpty();
    }

    private void noise(ServerLevel serverLevel) {
        RandomSource randomSource = serverLevel.getRandom();
        float pitch = 0.75f + randomSource.nextFloat() * 0.5f;
        this.playSound(ModSounds.DUCK_TOY.get(), 1, pitch);

        Vec3 notePos = this.position().add(
                randomSource.nextFloat() / 2 - 0.25,
                1 + randomSource.nextFloat() / 5,
                randomSource.nextFloat() / 2 - 0.25
        );
        float color = randomSource.nextInt(4) / 24.0F;
        serverLevel.sendParticles(ParticleTypes.NOTE, notePos.x(), notePos.y(), notePos.z(), 0, color, 0, 0, 1);
    }

    private boolean bounce() {
        long time = this.bounceTime - System.currentTimeMillis();
        if (time > 0)
            return false;
        this.bounceTime = System.currentTimeMillis() + 500;
        return true;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(ModItems.TWEAKS_TOOL.get())) {
            if (level().isClientSide && FMLEnvironment.dist == Dist.CLIENT) {
                // FIXME 临时解决方案，直接打开界面
                TweaksToolScreen.openScreen();
            }
            return InteractionResult.SUCCESS;
        }
        else if (getHoldItem().isEmpty() && player.isCrouching() && !itemStack.isEmpty()) {
            long takeCD = this.takeCD - System.currentTimeMillis();
            if (takeCD > 0) {return InteractionResult.PASS;}
            var a = itemStack.copy();
            var b = itemStack.copy();
            a.setCount(1);
            b.setCount(itemStack.getCount() - 1);
            setHoldItem(a);
            player.setItemInHand(hand, b);
            this.takeCD = System.currentTimeMillis() + 500;
            return InteractionResult.SUCCESS;
        }

        if (!getHoldItem().isEmpty() && player.isCrouching() && itemStack.isEmpty()) {
            long takeCD = this.takeCD - System.currentTimeMillis();
            if (takeCD > 0) {return InteractionResult.PASS;}
            player.setItemInHand(hand, getHoldItem().copy());
            setHoldItem(ItemStack.EMPTY);
            this.takeCD = System.currentTimeMillis() + 500;
            return InteractionResult.SUCCESS;
        }

        if (!bounce()) {
            return InteractionResult.PASS;
        };
        if (player.level() instanceof ServerLevel serverLevel) {
            noise(serverLevel);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public BlockPos getBlockPosBelowThatAffectsMyMovement() {
        return this.getOnPos(0.999999F);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return pDistance < 128 * 128;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            // 必须是玩家直接造成的伤害才能打掉
            if (!this.isRemoved() && !this.level().isClientSide && source.getDirectEntity() instanceof Player) {
                this.kill();
                this.markHurt();
                this.dropItem(source.getEntity());
                return true;
            }
            else if (this.level() instanceof ServerLevel serverLevel) {
                if (!bounce()) {
                    return false;
                };
                noise(serverLevel);
            }
            return false;
        }
    }

    private void dropItem(@Nullable Entity pBrokenEntity) {
        if (!getHoldItem().isEmpty()) {
            this.spawnAtLocation(getHoldItem().copy());
            setHoldItem(ItemStack.EMPTY);
        }
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.playSound(SoundEvents.WOOL_BREAK, 1.0F, 1.0F);
            if (pBrokenEntity instanceof Player player) {
                if (player.getAbilities().instabuild) {
                    return;
                }
            }
            ItemStack dollItem = DollEntityItem.createItemWithEntity(this);
            this.spawnAtLocation(dollItem);
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof Phantom phantom) {
            this.setXRot(-phantom.getXRot());
        }
    }

    @Override
    @NotNull
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        if (this.getVehicle() instanceof Phantom) {
            return new Vec3(0, 0.625, 0);
        }
        return new Vec3(0, 0.8125, 0);
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return DollEntityItem.createItemWithEntity(this);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_BLOCK_STATE, Blocks.AIR.defaultBlockState());
        builder.define(DATA_SCALE, new Vector3f(1.0f));
        builder.define(DATA_TRANSLATION, new Vector3f());
        builder.define(CUSTOM_DOLL_ID, StringUtils.EMPTY);
        builder.define(DATA_HOLD_ITEM, ItemStack.EMPTY);
        builder.define(DATA_ITEM_SCALE, new Vector3f(1.0f));
        builder.define(DATA_ITEM_TRANSLATION, new Vector3f());
        builder.define(DATA_ITEM_ROTATION, new Vector3f());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_SCALE.equals(pKey) || DATA_ITEM_SCALE.equals(pKey)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(pKey);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains(TAG_BLOCK_STATE)) {
            HolderLookup<Block> lookup = this.level().holderLookup(Registries.BLOCK);
            setDisplayBlockState(NbtUtils.readBlockState(lookup, tag.getCompound(TAG_BLOCK_STATE)));
        }
        if (tag.contains(TAG_CUSTOM_DOLL_ID)) {
            setCustomDollId(tag.getString(TAG_CUSTOM_DOLL_ID));
        }
        if (tag.contains(TAG_SCALE)) {
            setDisplayScale(readVector3f(tag.getCompound(TAG_SCALE)));
        }
        if (tag.contains(TAG_TRANSLATION)) {
            setDisplayTranslation(readVector3f(tag.getCompound(TAG_TRANSLATION)));
        }
        if (tag.contains(TAG_DROP_FROM_PHANTOM)) {
            this.dropFromPhantom = tag.getBoolean(TAG_DROP_FROM_PHANTOM);
        }
        if (tag.contains(TAG_DROP_FROM_PHANTOM_TIME)) {
            this.dropFromPhantomTick = tag.getInt(TAG_DROP_FROM_PHANTOM_TIME);
        }
        if (tag.contains(TAG_HOLD_ITEM)) {
            setHoldItem(ItemStack.parseOptional(this.registryAccess(), tag.getCompound(TAG_HOLD_ITEM)));
        }
        if (tag.contains(TAG_ITEM_SCALE)) {
            setItemScale(readVector3f(tag.getCompound(TAG_ITEM_SCALE)));
        }
        if (tag.contains(TAG_ITEM_TRANSLATION)) {
            setItemTranslation(readVector3f(tag.getCompound(TAG_ITEM_TRANSLATION)));
        }
        if (tag.contains(TAG_ITEM_ROTATION)) {
            setItemRotation(readVector3f(tag.getCompound(TAG_ITEM_TRANSLATION)));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        BlockState blockState = getDisplayBlockState();
        if (blockState != Blocks.AIR.defaultBlockState()) {
            tag.put(TAG_BLOCK_STATE, NbtUtils.writeBlockState(blockState));
        }
        if (StringUtils.isNotBlank(getCustomDollId())) {
            tag.putString(TAG_CUSTOM_DOLL_ID, getCustomDollId());
        }
        tag.put(TAG_SCALE, writeVector3f(getDisplayScale()));
        tag.put(TAG_TRANSLATION, writeVector3f(getDisplayTranslation()));
        tag.putBoolean(TAG_DROP_FROM_PHANTOM, this.dropFromPhantom);
        tag.putInt(TAG_DROP_FROM_PHANTOM_TIME, this.dropFromPhantomTick);
        if (!getHoldItem().isEmpty()) {
            tag.put(TAG_HOLD_ITEM, getHoldItem().save(this.registryAccess()));
        }
        tag.put(TAG_ITEM_SCALE, writeVector3f(getItemScale()));
        tag.put(TAG_ITEM_TRANSLATION, writeVector3f(getItemTranslation()));
        tag.put(TAG_ITEM_ROTATION, writeVector3f(getItemRotation()));
    }

    public void removePhantomRecord(CompoundTag tag) {
        tag.remove(TAG_DROP_FROM_PHANTOM);
        tag.remove(TAG_DROP_FROM_PHANTOM_TIME);
    }

    private Vector3f readVector3f(CompoundTag tag) {
        return new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
    }

    private CompoundTag writeVector3f(Vector3f vector) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", vector.x);
        tag.putFloat("y", vector.y);
        tag.putFloat("z", vector.z);
        return tag;
    }

    public BlockState getDisplayBlockState() {
        return this.entityData.get(DATA_BLOCK_STATE);
    }

    public void setDisplayBlockState(BlockState blockState) {
        this.entityData.set(DATA_BLOCK_STATE, blockState);
    }

    public Vector3f getDisplayScale() {
        return this.entityData.get(DATA_SCALE);
    }

    public void setDisplayScale(Vector3f scale) {
        this.entityData.set(DATA_SCALE, scale, true);
    }

    public Vector3f getDisplayTranslation() {
        return this.entityData.get(DATA_TRANSLATION);
    }

    public void setDisplayTranslation(Vector3f translation) {
        this.entityData.set(DATA_TRANSLATION, translation, true);
    }

    public ItemStack getHoldItem() {
        return this.entityData.get(DATA_HOLD_ITEM);
    }

    public void setHoldItem(ItemStack holdItem) {
        this.entityData.set(DATA_HOLD_ITEM, holdItem.copy());
    }

    public Vector3f getItemScale() {
        return this.entityData.get(DATA_ITEM_SCALE);
    }

    public void setItemScale(Vector3f scale) {
        this.entityData.set(DATA_ITEM_SCALE, scale, true);
    }

    public Vector3f getItemTranslation() {
        return this.entityData.get(DATA_ITEM_TRANSLATION);
    }

    public void setItemTranslation(Vector3f translation) {
        this.entityData.set(DATA_ITEM_TRANSLATION, translation, true);
    }

    public Vector3f getItemRotation() {
        return this.entityData.get(DATA_ITEM_ROTATION);
    }

    public void setItemRotation(Vector3f rotations) {
        this.entityData.set(DATA_ITEM_ROTATION, rotations, true);
    }

    public void setCustomDollId(String customDollId) {
        this.entityData.set(CUSTOM_DOLL_ID, customDollId);
    }

    public String getCustomDollId() {
        return this.entityData.get(CUSTOM_DOLL_ID);
    }

    public void setInThrowing(boolean inThrowing) {
        this.inThrowing = inThrowing;
    }

    public long getBounceTime() {
        return bounceTime;
    }

    public void setDropFromPhantom(boolean dropFromPhantom) {
        this.dropFromPhantom = dropFromPhantom;
    }

    @Override
    public void setRot(float yRot, float xRot) {
        super.setRot(yRot, xRot);
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        Vector3f displayScale = this.getDisplayScale();
        EntityDimensions dimensions = super.getDimensions(pose);
        float width = Math.max(Math.abs(displayScale.x), Math.abs(displayScale.z));
        float height = Math.abs(displayScale.y);
        return dimensions.scale(width, height);
    }

    private void checkCollisionKnockback() {
        // 仅在服务器端进行击退处理
        if (this.level().isClientSide) {
            return;
        }

        // 获取玩偶当前速度
        Vec3 dollVelocity = this.getDeltaMovement();
        double dollSpeed = dollVelocity.length();

        // 只有当玩偶速度达到 0.25 以上时才进行击退检测
        if (dollSpeed < 0.25) {
            return;
        }

        // 每次击退后增加计数器，最多允许击退 5 次，之后需要玩偶停下来重置
        if (this.knockbackCount >= 5) {
            // 记录最后的碰撞 tick，用来重置计数器
            if (this.tickCount - this.lastKnockbackTick > 20) {
                this.knockbackCount = 0;
            }
            this.lastKnockbackTick = this.tickCount;
            return;
        }

        // 检查与玩偶碰撞的实体
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(0.2),
                e -> e != this && e.isAlive() && e.isPickable())) {
            // 计算从玩偶到目标实体的方向向量
            Vec3 knockbackDirection = this.getDeltaMovement().normalize();

            // 击退强度基于玩偶的速度和玩偶的大小
            // 玩偶越大击退效果越差
            double sizeNumber = 0.75 / Math.max(this.getBbWidth(), this.getBbHeight());
            double knockbackStrength = Math.min(dollSpeed * 0.4, 1)
                                       * Math.min(sizeNumber * sizeNumber, 1)
                                       * GeneralConfig.DOLL_KNOCKBACK_FORCE.get();

            // 计算击退向量，保持一定的垂直分量
            Vec3 knockbackVector = new Vec3(
                    knockbackDirection.x * knockbackStrength,
                    Math.max(0.2, knockbackDirection.y * knockbackStrength * 0.5),
                    knockbackDirection.z * knockbackStrength
            );

            // 应用击退效果
            Vec3 speed = entity.getDeltaMovement().add(knockbackVector);
            // 限制最大速度，防止过快，我们依据玩偶大小做最大速度限制
            double maxSpeed = Math.min(sizeNumber * sizeNumber, 1);
            if (speed.length() > maxSpeed) {
                speed = speed.normalize().scale(maxSpeed);
            }

            entity.setDeltaMovement(speed);
            entity.hasImpulse = true;
            this.knockbackCount++;

            // 生成击中粒子效果
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CRIT,
                        entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                        5, 0.2, 0.2, 0.2, 0.1);
            }
        }
    }
}
