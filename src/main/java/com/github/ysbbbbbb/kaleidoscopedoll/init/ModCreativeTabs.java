package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;


public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeDoll.MOD_ID);
    private static final ResourceLocation VANILLA_ICON_ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_52");
    private static final ResourceLocation PLAYER_ICON_ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_0");

    public static Supplier<CreativeModeTab> VANILLA_DOLL_TAB = TABS.register("vanilla_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.vanilla_doll.name"))
            .icon(() -> BuiltInRegistries.ITEM.get(VANILLA_ICON_ID).getDefaultInstance())
            .displayItems((par, output) -> {
                output.accept(ModItems.DOLL_MACHINE.get());
                output.accept(ModItems.PURPLE_DOLL_GIFT_BOX.get());
                output.accept(ModItems.GREEN_DOLL_GIFT_BOX.get());
                output.accept(ModItems.YELLOW_DOLL_GIFT_BOX.get());
                ModRegisterEvent.DOLL_ITEMS.stream()
                        .filter(item -> !SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                        .forEach(output::accept);
            }).build());

    public static Supplier<CreativeModeTab> PLAYER_DOLL_TAB = TABS.register("player_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.player_doll.name"))
            .icon(() -> BuiltInRegistries.ITEM.get(PLAYER_ICON_ID).getDefaultInstance())
            .displayItems((par, output) -> {
                ModRegisterEvent.DOLL_ITEMS.stream()
                        .filter(item -> SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                        .forEach(output::accept);
            }).build());
}
