package com.github.ysbbbbbb.kaleidoscopedoll.block.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!dollItemStack.isEmpty()) {
            tag.put(NBT_DOLL_ITEMSTACK, dollItemStack.save(registries));
        }
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(NBT_DOLL_ITEMSTACK)) {
            CompoundTag compound = tag.getCompound(NBT_DOLL_ITEMSTACK);
            dollItemStack = ItemStack.parse(registries, compound).orElse(ItemStack.EMPTY);
        }
    }

    public ItemStack getDollItemStack() {
        return dollItemStack.copyWithCount(1);
    }
}
