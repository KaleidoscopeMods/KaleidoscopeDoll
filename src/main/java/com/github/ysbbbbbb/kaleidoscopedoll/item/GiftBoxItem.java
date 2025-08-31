package com.github.ysbbbbbb.kaleidoscopedoll.item;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GiftBoxItem extends BlockItem {
    private static final String NBT_DOLL_ITEMSTACK = "DollItemStack";

    public GiftBoxItem(Block pBlock) {
        super(pBlock, new Item.Properties());
    }

    public static void setDoll(ItemStack doll, ItemStack giftBox) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(NBT_DOLL_ITEMSTACK, doll.save(new CompoundTag()));
        setBlockEntityData(giftBox, ModBlocks.DOLL_GIFT_BOX_BE.get(), compoundTag);
    }
}
