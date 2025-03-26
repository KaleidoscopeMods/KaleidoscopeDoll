package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@Mod.EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegisterEvent {
    public static final Map<ResourceLocation, DollBlock> DOLL_BLOCKS = Maps.newHashMap();
    public static final Map<ResourceLocation, String> SPECIAL_TOOLTIPS = Maps.newHashMap();
    public static final Set<Item> DOLL_ITEMS = Sets.newLinkedHashSet();
    private static final int MAX_DOLL_COUNT = 95;

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
        registerSpecialTooltips("doll_78", "sponsors_aoisake");
        registerSpecialTooltips("doll_79", "sponsors_cpcian39");
        registerSpecialTooltips("doll_80", "sponsors_swordman_live");
        registerSpecialTooltips("doll_81", "sponsors_tclimuareotin");
        registerSpecialTooltips("doll_82", "sponsors_taste_puppy");
        registerSpecialTooltips("doll_83", "sponsors_poetry_chicken");
        registerSpecialTooltips("doll_84", "sponsors_fudge_666");
        registerSpecialTooltips("doll_85", "sponsors_chair341");
        registerSpecialTooltips("doll_86", "sponsors_dumnheint");
        registerSpecialTooltips("doll_87", "sponsors_lilunli");
        registerSpecialTooltips("doll_88", "sponsors_psbastards");
        registerSpecialTooltips("doll_89", "sponsors_pysgas");
        registerSpecialTooltips("doll_90", "sponsors_redflashier");
        registerSpecialTooltips("doll_91", "sponsors_rsob123");
        registerSpecialTooltips("doll_92", "sponsors_solareclipseg");
        registerSpecialTooltips("doll_93", "sponsors_zombiec1107");
        registerSpecialTooltips("doll_94", "sponsors__moepus_");
    }

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        registerAllSpecialTooltips();
        // 批量注册玩偶
        if (event.getRegistryKey().equals(ForgeRegistries.BLOCKS.getRegistryKey())) {
            IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
                ResourceLocation name = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_" + i);
                DollBlock block = new DollBlock();
                DOLL_BLOCKS.put(name, block);
                event.register(ForgeRegistries.BLOCKS.getRegistryKey(), name, () -> block);
            });
        }
        if (event.getRegistryKey().equals(ForgeRegistries.ITEMS.getRegistryKey())) {
            IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
                ResourceLocation name = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_" + i);
                DollBlock block = DOLL_BLOCKS.get(name);
                Item item = new DollItem(block, SPECIAL_TOOLTIPS.getOrDefault(name, "vanilla"));
                DOLL_ITEMS.add(item);
                event.register(ForgeRegistries.ITEMS.getRegistryKey(), name, () -> item);
            });
        }
    }

    private static void registerSpecialTooltips(String name, String tooltip) {
        ResourceLocation id = new ResourceLocation(KaleidoscopeDoll.MOD_ID, name);
        SPECIAL_TOOLTIPS.put(id, tooltip);
    }
}
