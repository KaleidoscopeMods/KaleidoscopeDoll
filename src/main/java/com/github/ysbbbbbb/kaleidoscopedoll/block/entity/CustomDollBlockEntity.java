package com.github.ysbbbbbb.kaleidoscopedoll.block.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class CustomDollBlockEntity extends BlockEntity {
    private static final String NBT_MODEL_ID = "DollModelId";

    private String modelId = StringUtils.EMPTY;

    public CustomDollBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public CustomDollBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlocks.CUSTOM_DOLL_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString(NBT_MODEL_ID, modelId);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.modelId = tag.getString(NBT_MODEL_ID);
    }

    public void refresh() {
        this.setChanged();
        if (level != null) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
        refresh();
    }
}
