package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeDoll.MOD_ID);

    public static DeferredItem<Item> DOLL_ICON = ITEMS.register("doll_icon", () -> new Item(new Item.Properties()));
    public static DeferredItem<Item> DOLL_MACHINE = ITEMS.register("doll_machine", () -> new BlockItem(ModBlocks.DOLL_MACHINE.get(), new Item.Properties()));
    public static DeferredItem<Item> PURPLE_DOLL_GIFT_BOX = ITEMS.register("purple_doll_gift_box", () -> new BlockItem(ModBlocks.PURPLE_DOLL_GIFT_BOX.get(), new Item.Properties()));
    public static DeferredItem<Item> GREEN_DOLL_GIFT_BOX = ITEMS.register("green_doll_gift_box", () -> new BlockItem(ModBlocks.GREEN_DOLL_GIFT_BOX.get(), new Item.Properties()));
    public static DeferredItem<Item> YELLOW_DOLL_GIFT_BOX = ITEMS.register("yellow_doll_gift_box", () -> new BlockItem(ModBlocks.YELLOW_DOLL_GIFT_BOX.get(), new Item.Properties()));
}
