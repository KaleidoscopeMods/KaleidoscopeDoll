package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class ModCreativeTabs extends CreativeModeTab {
    private static final ResourceLocation VANILLA_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_52");
    private static final ResourceLocation PLAYER_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_0");
    public static final CreativeModeTab VANILLA_DOLL_TAB = new ModCreativeTabs("vanilla_doll", VANILLA_ICON_ID);
    public static final CreativeModeTab PLAYER_DOLL_TAB = new ModCreativeTabs("player_doll", PLAYER_ICON_ID);

    private final Component displayName;
    private final ResourceLocation iconId;
    private ItemStack icon;

    public ModCreativeTabs(String tabName, ResourceLocation iconId) {
        super(String.format("kaleidoscope_doll.doll.%s", tabName));
        this.iconId = iconId;
        this.displayName = new TranslatableComponent(String.format("item_group.kaleidoscope_doll.%s.name", tabName));
    }

    @Override
    public ItemStack makeIcon() {
        if (icon == null) {
            icon = ForgeRegistries.ITEMS.getValue(iconId).getDefaultInstance();
        }

        return icon;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }
}