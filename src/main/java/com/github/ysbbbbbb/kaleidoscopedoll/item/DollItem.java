package com.github.ysbbbbbb.kaleidoscopedoll.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class DollItem extends BlockItem {
    private final String langKey;

    public DollItem(Block block, String langKey) {
        super(block, new Item.Properties().equipmentSlot((entity, item) -> EquipmentSlot.HEAD));
        this.langKey = "tooltip.kaleidoscope_doll.doll." + langKey;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("block.kaleidoscope_doll.doll");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(this.langKey).withStyle(ChatFormatting.DARK_GRAY));
    }
}
