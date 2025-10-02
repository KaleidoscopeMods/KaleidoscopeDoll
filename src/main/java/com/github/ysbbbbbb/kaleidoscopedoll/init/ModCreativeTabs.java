package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.AUTHOR_DOLLS;
import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KaleidoscopeDoll.MOD_ID);
    private static final ResourceLocation VANILLA_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_52");
    private static final ResourceLocation AUTHOR_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_0");
    private static final ResourceLocation PLAYER_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "computer");
    private static final ResourceLocation ENTITY_ICON_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_5");

    public static RegistryObject<CreativeModeTab> VANILLA_DOLL_TAB = TABS.register("vanilla_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.vanilla_doll.name"))
            .icon(() -> ForgeRegistries.ITEMS.getValue(VANILLA_ICON_ID).getDefaultInstance())
            .displayItems((par, output) -> {
                output.accept(ModItems.DOLL_MACHINE.get());
                output.accept(ModItems.COMPUTER.get());
                output.accept(ModItems.TWEAKS_TOOL.get());
                output.accept(ModItems.PURPLE_DOLL_GIFT_BOX.get());
                output.accept(ModItems.GREEN_DOLL_GIFT_BOX.get());
                output.accept(ModItems.YELLOW_DOLL_GIFT_BOX.get());
                ModRegisterEvent.DOLL_ITEMS.stream()
                        .filter(item -> !SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item)))
                        .forEach(output::accept);
            }).build());

    public static RegistryObject<CreativeModeTab> PLAYER_DOLL_TAB = TABS.register("player_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.player_doll.name"))
            .icon(() -> ForgeRegistries.ITEMS.getValue(PLAYER_ICON_ID).getDefaultInstance())
            .withTabsBefore(VANILLA_DOLL_TAB.getId())
            .displayItems((par, output) -> {
                ModRegisterEvent.DOLL_ITEMS.stream()
                        .filter(item -> SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item))
                                        && !AUTHOR_DOLLS.contains(ForgeRegistries.ITEMS.getKey(item)))
                        .forEach(output::accept);
            }).build());

    public static RegistryObject<CreativeModeTab> AUTHOR_DOLL_TAB = TABS.register("author_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.author_doll.name"))
            .icon(() -> ForgeRegistries.ITEMS.getValue(AUTHOR_ICON_ID).getDefaultInstance())
            .withTabsBefore(VANILLA_DOLL_TAB.getId())
            .displayItems((par, output) -> {
                ModRegisterEvent.DOLL_ITEMS.stream()
                        .filter(item -> AUTHOR_DOLLS.contains(ForgeRegistries.ITEMS.getKey(item)))
                        .forEach(output::accept);
            }).build());

    public static RegistryObject<CreativeModeTab> ENTITY_DOLL_TAB = TABS.register("entity_doll", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group.kaleidoscope_doll.entity_doll.name"))
            .icon(() -> ForgeRegistries.ITEMS.getValue(ENTITY_ICON_ID).getDefaultInstance())
            .withTabsBefore(VANILLA_DOLL_TAB.getId())
            .displayItems((par, output) -> {
                DollEntityItem.addCreativeTab(output);
            }).build());
}
