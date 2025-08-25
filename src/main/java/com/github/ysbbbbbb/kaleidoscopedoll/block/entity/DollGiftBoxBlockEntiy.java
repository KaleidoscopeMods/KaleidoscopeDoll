package com.github.ysbbbbbb.kaleidoscopedoll.block.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class DollGiftBoxBlockEntiy extends BlockEntity {
    private static final String NBT_DOLL_ITEMSTACK = "DollItemStack";

    private ItemStack dollItemStack = ItemStack.EMPTY;

    public DollGiftBoxBlockEntiy(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public DollGiftBoxBlockEntiy(BlockPos pos, BlockState state) {
        this(ModBlocks.DOLL_GIFT_BOX_BE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!dollItemStack.isEmpty()) {
            tag.put(NBT_DOLL_ITEMSTACK, dollItemStack.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(NBT_DOLL_ITEMSTACK)) {
            dollItemStack = ItemStack.of(tag.getCompound(NBT_DOLL_ITEMSTACK));
        }
    }

    public ItemStack getDollItemStack() {
        return dollItemStack.copyWithCount(1);
    }
}
