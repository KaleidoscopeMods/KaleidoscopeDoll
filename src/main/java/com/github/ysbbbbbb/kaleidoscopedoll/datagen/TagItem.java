package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class TagItem extends ItemTagsProvider {
    /**
     * 原版生物玩偶
     */
    public static final TagKey<Item> VANILLA_DOLLS = itemTagKey("vanilla_dolls");
    /**
     * 玩家玩偶
     */
    public static final TagKey<Item> PLAYER_DOLLS = itemTagKey("player_dolls");
    /**
     * 所有本模组玩偶的 tag，因为方块形态玩偶都使用了不同的注册名
     */
    public static final TagKey<Item> ALL_DOLLS = itemTagKey("all_dolls");
    /**
     * 方块玩偶转换成实体玩偶，合成所需的素材
     */
    public static final TagKey<Item> BLOCK_DOLLS_TO_ENTITY_ITEM = itemTagKey("block_dolls_to_entity_item");

    /**
     * 所有等级的代币
     */
    public static final TagKey<Item> MACHINE_TOKENS = itemTagKey("machine_tokens");
    /**
     * 0 级代币，能够在抽奖机中抽取主世界生物玩偶
     */
    public static final TagKey<Item> TIER_0_MACHINE_TOKENS = itemTagKey("tier_0_machine_tokens");
    /**
     * 1 级代币，能够在抽奖机中抽取下界生物玩偶
     */
    public static final TagKey<Item> TIER_1_MACHINE_TOKENS = itemTagKey("tier_1_machine_tokens");
    /**
     * 2 级代币，能够在抽奖机中抽取末地生物玩偶
     */
    public static final TagKey<Item> TIER_2_MACHINE_TOKENS = itemTagKey("tier_2_machine_tokens");

    /**
     * 0 级玩偶，能够通过 0 级代币在抽奖机中抽取
     */
    public static final TagKey<Item> TIER_0_DOLLS = itemTagKey("tier_0_dolls");
    /**
     * 1 级玩偶，能够通过 1 级代币在抽奖机中抽取
     */
    public static final TagKey<Item> TIER_1_DOLLS = itemTagKey("tier_1_dolls");
    /**
     * 2 级玩偶，能够通过 2 级代币在抽奖机中抽取
     */
    public static final TagKey<Item> TIER_2_DOLLS = itemTagKey("tier_2_dolls");

    public TagItem(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                   CompletableFuture<TagLookup<Block>> blockTags, String modId,
                   @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, modId, existingFileHelper);
    }

    private static TagKey<Item> itemTagKey(String key) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, key));
    }

    private static ResourceKey<Item> dollItem(String key) {
        return ResourceKey.create(Registries.ITEM, new ResourceLocation(KaleidoscopeDoll.MOD_ID, key));
    }

    @Override
    @SuppressWarnings("all")
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(VANILLA_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item)))
                .toArray(Item[]::new));

        this.tag(PLAYER_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item)))
                .toArray(Item[]::new));

        this.tag(ALL_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.toArray(Item[]::new));

        this.tag(BLOCK_DOLLS_TO_ENTITY_ITEM).add(Items.SLIME_BALL, Items.CLAY_BALL);

        this.tag(TIER_0_MACHINE_TOKENS).add(Items.IRON_INGOT);
        this.tag(TIER_1_MACHINE_TOKENS).add(Items.GOLD_INGOT);
        this.tag(TIER_2_MACHINE_TOKENS).add(Items.DIAMOND);
        this.tag(MACHINE_TOKENS).addTag(TIER_0_MACHINE_TOKENS)
                .addTag(TIER_1_MACHINE_TOKENS)
                .addTag(TIER_2_MACHINE_TOKENS);

        List<ResourceKey<Item>> tier1Dolls = Lists.newArrayList(
                dollItem("doll_2"),
                dollItem("doll_10"),
                dollItem("doll_12"),
                dollItem("doll_15"),
                dollItem("doll_16"),
                dollItem("doll_17"),
                dollItem("doll_34"),
                dollItem("doll_35"),
                dollItem("doll_66")
        );
        this.tag(TIER_1_DOLLS).add(tier1Dolls.toArray(new ResourceKey[0]));

        List<ResourceKey<Item>> tier2Dolls = Lists.newArrayList(
                dollItem("doll_3"),
                dollItem("doll_11"),
                dollItem("doll_21")
        );
        this.tag(TIER_2_DOLLS).add(tier2Dolls.toArray(new ResourceKey[0]));

        // 0 级玩偶是除了 1 级和 2 级玩偶之外的所有玩偶
        this.tag(TIER_0_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item)))
                .filter(item -> ForgeRegistries.ITEMS.getResourceKey(item)
                        .map(k -> !tier1Dolls.contains(k) && !tier2Dolls.contains(k)).orElse(false))
                .toArray(Item[]::new));
    }
}
