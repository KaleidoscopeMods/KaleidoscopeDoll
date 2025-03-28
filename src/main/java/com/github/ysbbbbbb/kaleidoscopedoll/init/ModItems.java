package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, KaleidoscopeDoll.MOD_ID);

    public static RegistryObject<Item> DOLL_ICON = ITEMS.register("doll_icon", () -> new Item(new Item.Properties()));
    public static RegistryObject<Item> DOLL_MACHINE = ITEMS.register("doll_machine", () -> new BlockItem(ModBlocks.DOLL_MACHINE.get(), new Item.Properties()));
    public static RegistryObject<Item> PURPLE_DOLL_GIFT_BOX = ITEMS.register("purple_doll_gift_box", () -> new BlockItem(ModBlocks.PURPLE_DOLL_GIFT_BOX.get(), new Item.Properties()));
    public static RegistryObject<Item> GREEN_DOLL_GIFT_BOX = ITEMS.register("green_doll_gift_box", () -> new BlockItem(ModBlocks.GREEN_DOLL_GIFT_BOX.get(), new Item.Properties()));
    public static RegistryObject<Item> YELLOW_DOLL_GIFT_BOX = ITEMS.register("yellow_doll_gift_box", () -> new BlockItem(ModBlocks.YELLOW_DOLL_GIFT_BOX.get(), new Item.Properties()));
}
