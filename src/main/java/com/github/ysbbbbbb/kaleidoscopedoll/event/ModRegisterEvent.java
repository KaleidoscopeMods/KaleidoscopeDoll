package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegisterEvent {
    public static final Map<ResourceLocation, DollBlock> DOLL_BLOCKS = Maps.newHashMap();
    public static final Map<ResourceLocation, String> SPECIAL_TOOLTIPS = Maps.newHashMap();
    public static final Set<Item> DOLL_ITEMS = Sets.newLinkedHashSet();
    private static final int MAX_DOLL_COUNT = 78;

    private static void registerAllSpecialTooltips() {
        registerSpecialTooltips("doll_0", "author_ysbb");
        registerSpecialTooltips("doll_1", "author_tartaric_acid");
        registerSpecialTooltips("doll_67", "author_abert_cat");
        registerSpecialTooltips("doll_68", "author_cr_019");

        registerSpecialTooltips("doll_69", "sponsors_guriformes");
        registerSpecialTooltips("doll_70", "sponsors_kupurrra");
        registerSpecialTooltips("doll_71", "sponsors_tanyeng");
        registerSpecialTooltips("doll_72", "sponsors_airsamafurry");
        registerSpecialTooltips("doll_73", "sponsors_corleonejing");
        registerSpecialTooltips("doll_74", "sponsors_kuriyamayasura");
        registerSpecialTooltips("doll_75", "sponsors_miomilost");
        registerSpecialTooltips("doll_76", "sponsors_nekonymph");
        registerSpecialTooltips("doll_77", "sponsors_puerkimiko");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        registerAllSpecialTooltips();
        // 批量注册玩偶
        IForgeRegistry<Block> blockRegister = event.getRegistry();
        IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
            ResourceLocation name = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_" + i);
            DollBlock block = new DollBlock();
            block.setRegistryName(name);
            DOLL_BLOCKS.put(name, block);
            blockRegister.register(block);
        });
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        registerAllSpecialTooltips();
        // 批量注册玩偶
        IForgeRegistry<Item> blockItemRegister = event.getRegistry();
        IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
            ResourceLocation name = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_" + i);
            DollBlock block = DOLL_BLOCKS.get(name);
            Item item = new DollItem(block, SPECIAL_TOOLTIPS.getOrDefault(name, "vanilla"));
            item.setRegistryName(name);
            DOLL_ITEMS.add(item);
            blockItemRegister.register(item);
        });
    }

    private static void registerSpecialTooltips(String name, String tooltip) {
        ResourceLocation id = new ResourceLocation(KaleidoscopeDoll.MOD_ID, name);
        SPECIAL_TOOLTIPS.put(id, tooltip);
    }
}
