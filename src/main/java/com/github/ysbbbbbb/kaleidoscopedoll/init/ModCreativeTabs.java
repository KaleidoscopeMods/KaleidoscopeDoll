package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class ModCreativeTabs {
    private static final ResourceLocation VANILLA_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_52");
    private static final ResourceLocation PLAYER_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_0");
    private static final ResourceLocation ENTITY_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_5");

    private static final ResourceKey<CreativeModeTab> VANILLA_DOLL_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(KaleidoscopeDoll.MOD_ID, "vanilla_doll"));
    private static final ResourceKey<CreativeModeTab> PLAYER_DOLL_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(KaleidoscopeDoll.MOD_ID, "player_doll"));
    private static final ResourceKey<CreativeModeTab> ENTITY_DOLL_TAB = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
            new ResourceLocation(KaleidoscopeDoll.MOD_ID, "entity_doll"));

    public static void registerTabs() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, VANILLA_DOLL_TAB, FabricItemGroup.builder()
                .title(Component.translatable("item_group.kaleidoscope_doll.vanilla_doll.name"))
                .icon(() -> BuiltInRegistries.ITEM.get(VANILLA_ICON_ID).getDefaultInstance())
                .displayItems((par, output) -> {
                    output.accept(ModItems.DOLL_MACHINE);
                    output.accept(ModItems.PURPLE_DOLL_GIFT_BOX);
                    output.accept(ModItems.GREEN_DOLL_GIFT_BOX);
                    output.accept(ModItems.YELLOW_DOLL_GIFT_BOX);
                    ModRegisterEvent.DOLL_ITEMS.stream()
                            .filter(item -> !SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                            .forEach(output::accept);
                }).build());

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, PLAYER_DOLL_TAB, FabricItemGroup.builder()
                .title(Component.translatable("item_group.kaleidoscope_doll.player_doll.name"))
                .icon(() -> BuiltInRegistries.ITEM.get(PLAYER_ICON_ID).getDefaultInstance())
                .displayItems((par, output) -> {
                    ModRegisterEvent.DOLL_ITEMS.stream()
                            .filter(item -> SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                            .forEach(output::accept);
                }).build());

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ENTITY_DOLL_TAB, FabricItemGroup.builder()
                .title(Component.translatable("item_group.kaleidoscope_doll.entity_doll.name"))
                .icon(() -> BuiltInRegistries.ITEM.get(ENTITY_ICON_ID).getDefaultInstance())
                .displayItems((par, output) -> {
                    DollEntityItem.addCreativeTab(output);
                }).build());
    }
}
