package com.github.ysbbbbbb.kaleidoscopedoll.item;

import com.github.ysbbbbbb.kaleidoscopedoll.block.IModBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class ModBlockItem<T extends Block & IModBlock> extends BlockItem {
    private final T block;

    public ModBlockItem(T block, Properties properties) {
        super(block, properties);
        this.block = block;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(
        ItemStack stack,
        TooltipContext context,
        TooltipDisplay tooltipDisplay,
        Consumer<Component> tooltipAdder,
        TooltipFlag flag
    ) {
        this.block.appendHoverText(tooltipAdder);
    }
}
