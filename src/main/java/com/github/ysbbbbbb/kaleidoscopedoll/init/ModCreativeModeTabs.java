package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCreativeModeTabs extends CreativeModeTab {
    public static final CreativeModeTab TAB = new ModCreativeModeTabs();
    private static final ResourceLocation ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_52");

    private final Component displayName;
    private ItemStack icon;

    public ModCreativeModeTabs() {
        super("kaleidoscope_doll.doll");
        this.displayName = Component.translatable("item_group.kaleidoscope_doll.doll.name");
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
