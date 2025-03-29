package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public final class ModItems {
    public static final Item DOLL_MACHINE = new BlockItem(ModBlocks.DOLL_MACHINE, new Item.Properties());
    public static final Item PURPLE_DOLL_GIFT_BOX = new BlockItem(ModBlocks.PURPLE_DOLL_GIFT_BOX, new Item.Properties());
    public static final Item GREEN_DOLL_GIFT_BOX = new BlockItem(ModBlocks.GREEN_DOLL_GIFT_BOX, new Item.Properties());
    public static final Item YELLOW_DOLL_GIFT_BOX = new BlockItem(ModBlocks.YELLOW_DOLL_GIFT_BOX, new Item.Properties());
    public static final Item DOLL_ICON = new Item(new Item.Properties());

    public static void registerItems() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_machine"), DOLL_MACHINE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, "purple_doll_gift_box"), PURPLE_DOLL_GIFT_BOX);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, "green_doll_gift_box"), GREEN_DOLL_GIFT_BOX);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, "yellow_doll_gift_box"), YELLOW_DOLL_GIFT_BOX);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_icon"), DOLL_ICON);
    }
}
