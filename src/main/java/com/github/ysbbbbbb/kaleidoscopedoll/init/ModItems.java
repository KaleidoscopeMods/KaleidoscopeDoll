package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeDoll.MOD_ID);

    public static DeferredItem<Item> DOLL_ICON = ITEMS.registerSimpleItem("doll_icon");
    public static DeferredItem<BlockItem> DOLL_MACHINE = ITEMS.registerSimpleBlockItem(ModBlocks.DOLL_MACHINE);
    public static DeferredItem<BlockItem> PURPLE_DOLL_GIFT_BOX = ITEMS.registerSimpleBlockItem(ModBlocks.PURPLE_DOLL_GIFT_BOX);
    public static DeferredItem<BlockItem> GREEN_DOLL_GIFT_BOX = ITEMS.registerSimpleBlockItem(ModBlocks.GREEN_DOLL_GIFT_BOX);
    public static DeferredItem<BlockItem> YELLOW_DOLL_GIFT_BOX = ITEMS.registerSimpleBlockItem(ModBlocks.YELLOW_DOLL_GIFT_BOX);
}
