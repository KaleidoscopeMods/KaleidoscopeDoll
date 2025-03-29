package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModRegisterEvent {
    public static final Map<ResourceLocation, DollBlock> DOLL_BLOCKS = Maps.newHashMap();
    public static final Map<ResourceLocation, String> SPECIAL_TOOLTIPS = Maps.newHashMap();
    public static final Set<Item> DOLL_ITEMS = Sets.newLinkedHashSet();
    private static final int MAX_DOLL_COUNT = 113;

    private static void registerAllSpecialTooltips() {
        registerSpecialTooltips("doll_0", "author_ysbb");
        registerSpecialTooltips("doll_1", "author_tartaric_acid");
        registerSpecialTooltips("doll_67", "author_abert_cat");
        registerSpecialTooltips("doll_68", "author_cr_019");

        registerSpecialTooltips("doll_69", "sponsors_guriformes");
        registerSpecialTooltips("doll_70", "sponsors_passer1ne");
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
        registerSpecialTooltips("doll_95", "sponsors_ackrf");
        registerSpecialTooltips("doll_96", "sponsors_chuanchengzi");
        registerSpecialTooltips("doll_97", "sponsors_deltaex001");
        registerSpecialTooltips("doll_98", "sponsors_elyasrosedale");
        registerSpecialTooltips("doll_99", "sponsors_faustduck");
        registerSpecialTooltips("doll_100", "sponsors_lixu223");
        registerSpecialTooltips("doll_101", "sponsors_kupurrra");
        registerSpecialTooltips("doll_102", "sponsors_thjnd");
        registerSpecialTooltips("doll_103", "sponsors_variant_dk");
        registerSpecialTooltips("doll_104", "sponsors_yuxiaowan");
        registerSpecialTooltips("doll_105", "sponsors_bigpear5231");
        registerSpecialTooltips("doll_106", "sponsors_idlersnacl");
        registerSpecialTooltips("doll_107", "sponsors_kekmc123");
        registerSpecialTooltips("doll_108", "sponsors_kizunahovene");
        registerSpecialTooltips("doll_109", "sponsors_linx");
        registerSpecialTooltips("doll_110", "sponsors_linx_su");
        registerSpecialTooltips("doll_111", "sponsors_wyx0714");
        registerSpecialTooltips("doll_112", "sponsors_xheya_3u3");
    }

    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        registerAllSpecialTooltips();
        // 批量注册玩偶
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
                ResourceLocation name = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_" + i);
                ResourceKey<Block> id = ResourceKey.create(Registries.BLOCK, name);
                DollBlock block = new DollBlock(BlockBehaviour.Properties.of().setId(id));
                DOLL_BLOCKS.put(name, block);
                event.register(Registries.BLOCK, name, () -> block);
            });
        }
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            IntStream.range(0, MAX_DOLL_COUNT).forEach(i -> {
                ResourceLocation name = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_" + i);
                ResourceKey<Item> id = ResourceKey.create(Registries.ITEM, name);
                DollBlock block = DOLL_BLOCKS.get(name);
                Item item = new DollItem(block, new Item.Properties().setId(id), SPECIAL_TOOLTIPS.getOrDefault(name, "vanilla"));
                DOLL_ITEMS.add(item);
                event.register(Registries.ITEM, name, () -> item);
            });
        }
    }

    private static void registerSpecialTooltips(String name, String tooltip) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, name);
        SPECIAL_TOOLTIPS.put(id, tooltip);
    }
}
