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
    private static final int MAX_DOLL_COUNT = 200;

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
        registerSpecialTooltips("doll_113", "sponsors_1ye_kul");
        registerSpecialTooltips("doll_114", "sponsors_aleph_2");
        registerSpecialTooltips("doll_115", "sponsors_apomona");
        registerSpecialTooltips("doll_116", "sponsors_atkingqvq");
        registerSpecialTooltips("doll_117", "sponsors_bestshaaku");
        registerSpecialTooltips("doll_118", "sponsors_blackannin");
        registerSpecialTooltips("doll_119", "sponsors_ca1_t0u");
        registerSpecialTooltips("doll_120", "sponsors_catbug");
        registerSpecialTooltips("doll_121", "sponsors_catjoy6493");
        registerSpecialTooltips("doll_122", "sponsors_catlikesleep1160");
        registerSpecialTooltips("doll_123", "sponsors_cdrsimon");
        registerSpecialTooltips("doll_124", "sponsors_celia4300");
        registerSpecialTooltips("doll_125", "sponsors_coppercuo");
        registerSpecialTooltips("doll_126", "sponsors_creamtea2542");
        registerSpecialTooltips("doll_127", "sponsors_cxzkkaa");
        registerSpecialTooltips("doll_128", "sponsors_dafeimao");
        registerSpecialTooltips("doll_129", "sponsors_dazhuo1");
        registerSpecialTooltips("doll_130", "sponsors_dongfang_qian");
        registerSpecialTooltips("doll_131", "sponsors_eat_grass_");
        registerSpecialTooltips("doll_132", "sponsors_echoing_phantom");
        registerSpecialTooltips("doll_133", "sponsors_elex7310");
        registerSpecialTooltips("doll_134", "sponsors_entropy_yeah");
        registerSpecialTooltips("doll_135", "sponsors_eteroo44");
        registerSpecialTooltips("doll_136", "sponsors_fishinblack");
        registerSpecialTooltips("doll_137", "sponsors_foxxie");
        registerSpecialTooltips("doll_138", "sponsors_gfboy");
        registerSpecialTooltips("doll_139", "sponsors_gouzi_jun");
        registerSpecialTooltips("doll_140", "sponsors_gzq777");
        registerSpecialTooltips("doll_141", "sponsors_g_papy");
        registerSpecialTooltips("doll_142", "sponsors_hardypuro");
        registerSpecialTooltips("doll_143", "sponsors_hinijikata");
        registerSpecialTooltips("doll_144", "sponsors_ike_sonata");
        registerSpecialTooltips("doll_145", "sponsors_inky_mad");
        registerSpecialTooltips("doll_146", "sponsors_jg_aurora");
        registerSpecialTooltips("doll_147", "sponsors_jia1chen");
        registerSpecialTooltips("doll_148", "sponsors_jiaqihuang");
        registerSpecialTooltips("doll_149", "sponsors_jijiwawa");
        registerSpecialTooltips("doll_150", "sponsors_jm_wuyan");
        registerSpecialTooltips("doll_151", "sponsors_kaixiaoli_li");
        registerSpecialTooltips("doll_152", "sponsors_kaltsiit");
        registerSpecialTooltips("doll_153", "sponsors_kal_crane");
        registerSpecialTooltips("doll_154", "sponsors_kelvin");
        registerSpecialTooltips("doll_155", "sponsors_kuiper_hooligans");
        registerSpecialTooltips("doll_156", "sponsors_lantehall");
        registerSpecialTooltips("doll_157", "sponsors_legitchunk");
        registerSpecialTooltips("doll_158", "sponsors_limfx");
        registerSpecialTooltips("doll_159", "sponsors_lingjinzi");
        registerSpecialTooltips("doll_160", "sponsors_lingrengui");
        registerSpecialTooltips("doll_161", "sponsors_ling_e_iswolf");
        registerSpecialTooltips("doll_162", "sponsors_liulian");
        registerSpecialTooltips("doll_163", "sponsors_long_shu");
        registerSpecialTooltips("doll_164", "sponsors_luohammer");
        registerSpecialTooltips("doll_165", "sponsors_marguarita");
        registerSpecialTooltips("doll_166", "sponsors_mingqijiang");
        registerSpecialTooltips("doll_167", "sponsors_miya");
        registerSpecialTooltips("doll_168", "sponsors_mochcanl");
        registerSpecialTooltips("doll_169", "sponsors_mtraptrix");
        registerSpecialTooltips("doll_170", "sponsors_mu_ni_dian");
        registerSpecialTooltips("doll_171", "sponsors_nigella");
        registerSpecialTooltips("doll_172", "sponsors_nina_yxj");
        registerSpecialTooltips("doll_173", "sponsors_nuperaki");
        registerSpecialTooltips("doll_174", "sponsors_panta_lone");
        registerSpecialTooltips("doll_175", "sponsors_q821");
        registerSpecialTooltips("doll_176", "sponsors_qi_qingm");
        registerSpecialTooltips("doll_177", "sponsors_qxiaochai");
        registerSpecialTooltips("doll_178", "sponsors_revisorytoast22");
        registerSpecialTooltips("doll_179", "sponsors_saelens16");
        registerSpecialTooltips("doll_180", "sponsors_shiinanomashiro");
        registerSpecialTooltips("doll_181", "sponsors_shovei_");
        registerSpecialTooltips("doll_182", "sponsors_thirteny13");
        registerSpecialTooltips("doll_183", "sponsors_thunder50bmg");
        registerSpecialTooltips("doll_184", "sponsors_trance0203");
        registerSpecialTooltips("doll_185", "sponsors_usoko");
        registerSpecialTooltips("doll_186", "sponsors_xian__yue");
        registerSpecialTooltips("doll_187", "sponsors_xin_yucll");
        registerSpecialTooltips("doll_188", "sponsors_xlrm");
        registerSpecialTooltips("doll_189", "sponsors_xuanyou_awa");
        registerSpecialTooltips("doll_190", "sponsors_xvangezi");
        registerSpecialTooltips("doll_191", "sponsors_yili_olen");
        registerSpecialTooltips("doll_192", "sponsors_yuan_fe");
        registerSpecialTooltips("doll_193", "sponsors_yuroli73");
        registerSpecialTooltips("doll_194", "sponsors__aorta_");
        registerSpecialTooltips("doll_195", "sponsors__haman_");
        registerSpecialTooltips("doll_196", "sponsors_kelvin_0");
        registerSpecialTooltips("doll_197", "sponsors_sfshuofang");
        registerSpecialTooltips("doll_198", "sponsors_xvp1563");
        registerSpecialTooltips("doll_199", "sponsors_swamp_puppy");
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
