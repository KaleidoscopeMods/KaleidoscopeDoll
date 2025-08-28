package com.github.ysbbbbbb.kaleidoscopedoll.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class GiftBoxItem extends BlockItem {
    private static final String NBT_DOLL_ITEMSTACK = "DollItemStack";

    public GiftBoxItem(Block pBlock) {
        super(pBlock, new Item.Properties());
    }

    public static void setDoll(ItemStack doll, ItemStack giftBox, LevelAccessor accessor) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put(NBT_DOLL_ITEMSTACK, doll.save(accessor.registryAccess()));
        giftBox.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(compoundTag));
    }
}
