package com.github.ysbbbbbb.kaleidoscopedoll.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModEntities;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModSounds;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class DollEntity extends Entity {
    private static final EntityDataAccessor<BlockState> DATA_BLOCK_STATE = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.BLOCK_STATE);
    private static final EntityDataAccessor<Vector3f> DATA_SCALE = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> DATA_TRANSLATION = SynchedEntityData.defineId(DollEntity.class, EntityDataSerializers.VECTOR3);

    public static final String TAG_BLOCK_STATE = "doll_block_state";
    private static final String TAG_SCALE = "doll_scale";
    private static final String TAG_TRANSLATION = "doll_translation";

    private boolean inThrowing = false;
    private long bounceTime = 0;

    public DollEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
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

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        long time = this.bounceTime - System.currentTimeMillis();
        if (time > 0) {
            return InteractionResult.PASS;
        }
        this.bounceTime = System.currentTimeMillis() + 500;
        if (player.level() instanceof ServerLevel serverLevel) {
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

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
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
            return false;
        }
    }

    private void dropItem(@Nullable Entity pBrokenEntity) {
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
    public double getMyRidingOffset() {
        if (this.getVehicle() instanceof Phantom) {
            return 0.125;
        }
        return 0.3125;
    }

    @Override
    public @Nullable ItemStack getPickResult() {
        return DollEntityItem.createItemWithEntity(this);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_BLOCK_STATE, Blocks.AIR.defaultBlockState());
        this.entityData.define(DATA_SCALE, new Vector3f(1.0f));
        this.entityData.define(DATA_TRANSLATION, new Vector3f());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains(TAG_BLOCK_STATE)) {
            HolderLookup<Block> lookup = this.level().holderLookup(Registries.BLOCK);
            setDisplayBlockState(NbtUtils.readBlockState(lookup, tag.getCompound(TAG_BLOCK_STATE)));
        }
        if (tag.contains(TAG_SCALE)) {
            setDisplayScale(readVector3f(tag.getCompound(TAG_SCALE)));
        }
        if (tag.contains(TAG_TRANSLATION)) {
            setDisplayTranslation(readVector3f(tag.getCompound(TAG_TRANSLATION)));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        BlockState blockState = getDisplayBlockState();
        if (blockState != Blocks.AIR.defaultBlockState()) {
            tag.put(TAG_BLOCK_STATE, NbtUtils.writeBlockState(blockState));
        }
        tag.put(TAG_SCALE, writeVector3f(getDisplayScale()));
        tag.put(TAG_TRANSLATION, writeVector3f(getDisplayTranslation()));
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
        this.entityData.set(DATA_SCALE, scale);
    }

    public Vector3f getDisplayTranslation() {
        return this.entityData.get(DATA_TRANSLATION);
    }

    public void setDisplayTranslation(Vector3f translation) {
        this.entityData.set(DATA_TRANSLATION, translation);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setInThrowing(boolean inThrowing) {
        this.inThrowing = inThrowing;
    }

    public long getBounceTime() {
        return bounceTime;
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

        // 检查与玩偶碰撞的实体
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(0.2), e -> e != this && e.isAlive() && e.isPickable())) {
            // 计算从玩偶到目标实体的方向向量
            Vec3 knockbackDirection = this.getDeltaMovement().normalize();

            // 击退强度基于玩偶的速度
            double knockbackStrength = Math.min(dollSpeed * 0.4, 1) * GeneralConfig.DOLL_KNOCKBACK_FORCE.get();

            // 计算击退向量，保持一定的垂直分量
            Vec3 knockbackVector = new Vec3(
                    knockbackDirection.x * knockbackStrength,
                    Math.max(0.2, knockbackDirection.y * knockbackStrength * 0.5),
                    knockbackDirection.z * knockbackStrength
            );

            // 应用击退效果
            entity.setDeltaMovement(entity.getDeltaMovement().add(knockbackVector));
            entity.hasImpulse = true;

            // 生成击中粒子效果
            if (this.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CRIT,
                        entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                        5, 0.2, 0.2, 0.2, 0.1);
            }
        }
    }
}
