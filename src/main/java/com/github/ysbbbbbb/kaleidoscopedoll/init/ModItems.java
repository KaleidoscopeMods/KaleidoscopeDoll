package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.IModBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.item.ModBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Objects;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KaleidoscopeDoll.MOD_ID);

    public static DeferredItem<Item> DOLL_ICON = ITEMS.registerSimpleItem("doll_icon");
    public static DeferredItem<BlockItem> DOLL_MACHINE = ModItems.registerModBlockItem(ModBlocks.DOLL_MACHINE);
    public static DeferredItem<BlockItem> PURPLE_DOLL_GIFT_BOX = ModItems.registerModBlockItem(ModBlocks.PURPLE_DOLL_GIFT_BOX);
    public static DeferredItem<BlockItem> GREEN_DOLL_GIFT_BOX = ModItems.registerModBlockItem(ModBlocks.GREEN_DOLL_GIFT_BOX);
    public static DeferredItem<BlockItem> YELLOW_DOLL_GIFT_BOX = ModItems.registerModBlockItem(ModBlocks.YELLOW_DOLL_GIFT_BOX);

    public static <T extends Block & IModBlock> DeferredItem<BlockItem> registerModBlockItem(DeferredHolder<Block, T> block) {
        return ModItems.ITEMS.register(
            Objects.requireNonNull(block.getKey()).location().getPath(),
            (key) -> new ModBlockItem<>(
                block.value(), new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, key))
                .useBlockDescriptionPrefix()
            )
        );
    }
}
