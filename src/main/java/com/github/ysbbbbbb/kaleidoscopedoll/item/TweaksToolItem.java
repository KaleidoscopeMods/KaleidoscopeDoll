package com.github.ysbbbbbb.kaleidoscopedoll.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TweaksToolItem extends Item {
    public TweaksToolItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(Component.translatable("tooltip.kaleidoscope_doll.tweaks_tool.1").withStyle(ChatFormatting.DARK_GRAY));
        tooltipComponents.add(Component.translatable("tooltip.kaleidoscope_doll.tweaks_tool.2").withStyle(ChatFormatting.DARK_GRAY));
    }
}
