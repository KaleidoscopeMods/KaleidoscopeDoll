package com.github.ysbbbbbb.kaleidoscopedoll.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;
import static com.github.ysbbbbbb.kaleidoscopedoll.init.ModCreativeTabs.PLAYER_DOLL_TAB;
import static com.github.ysbbbbbb.kaleidoscopedoll.init.ModCreativeTabs.VANILLA_DOLL_TAB;

public class DollItem extends BlockItem {
    private final String langKey;

    public DollItem(Block block, String langKey) {
        super(block, new Item.Properties());
        this.langKey = "tooltip.kaleidoscope_doll.doll." + langKey;
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(Component.translatable(this.langKey).withStyle(ChatFormatting.DARK_GRAY));
    }

    @Override
    public void fillItemCategory(CreativeModeTab creativeModeTab, NonNullList<ItemStack> itemStacks) {
        ResourceLocation resourceLocation = ForgeRegistries.ITEMS.getKey(this);
        if (SPECIAL_TOOLTIPS.containsKey(resourceLocation) && creativeModeTab.equals(PLAYER_DOLL_TAB)) {
            this.getBlock().fillItemCategory(creativeModeTab, itemStacks);
        } else if (!SPECIAL_TOOLTIPS.containsKey(resourceLocation) && creativeModeTab.equals(VANILLA_DOLL_TAB)){
            this.getBlock().fillItemCategory(creativeModeTab, itemStacks);
        }
    }
}
