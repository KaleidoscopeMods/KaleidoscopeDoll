package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCreativeTabs extends CreativeModeTab {
    public static final CreativeModeTab TAB = new ModCreativeTabs();
    private static final ResourceLocation ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_52");

    private final Component displayName;
    private ItemStack icon;

    public ModCreativeTabs() {
        super("kaleidoscope_doll.doll");
        this.displayName = new TranslatableComponent("item_group.kaleidoscope_doll.doll.name");
    }

    @Override
    public ItemStack makeIcon() {
        if (icon == null) {
            icon = ForgeRegistries.ITEMS.getValue(ICON_ID).getDefaultInstance();
        }

        return icon;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }
}
