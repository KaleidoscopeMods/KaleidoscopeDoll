package com.github.ysbbbbbb.kaleidoscopedoll.block.entity;

import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.util.Collections;
import java.util.List;

public class DollMachineBlockEntity extends BlockEntity {
    private static final String NBT_KEY_TIER = "Tier";
    private static final String NBT_KEY_AVAILABLE_DOLLS = "AvailableDolls";

    private Int2ObjectMap<List<ResourceLocation>> availableDolls = new Int2ObjectOpenHashMap<>();
    private int tier = 0;

    public DollMachineBlockEntity(BlockEntityType<?> entityType, BlockPos pos, BlockState state) {
        super(entityType, pos, state);
    }

    public DollMachineBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlocks.DOLL_MACHINE_BE.get(), pos, state);
    }

    public void onTokensClicked(ItemStack itemStack, Level level) {
        if (itemStack.is(TagItem.TIER_0_MACHINE_TOKENS)) {
            tier = 0;
        } else if (itemStack.is(TagItem.TIER_1_MACHINE_TOKENS)) {
            tier = 1;
        } else if (itemStack.is(TagItem.TIER_2_MACHINE_TOKENS)) {
            tier = 2;
        }
        List<ResourceLocation> list = availableDolls.getOrDefault(tier, Lists.newArrayList());
        if (list.isEmpty()) {
            if (tier == 0) {
                shuffleDolls(TagItem.TIER_0_DOLLS, list);
            } else if (tier == 1) {
                shuffleDolls(TagItem.TIER_1_DOLLS, list);
            } else if (tier == 2) {
                shuffleDolls(TagItem.TIER_2_DOLLS, list);
            }
        }
        itemStack.shrink(1);
        this.setChanged();
    }

    public ItemStack onFinishLottery() {
        ItemStack output = ItemStack.EMPTY;
        List<ResourceLocation> list = availableDolls.getOrDefault(tier, Lists.newArrayList());
        if (!list.isEmpty()) {
            // 取出第一个
            ResourceLocation doll = list.remove(0);
            Item item = ForgeRegistries.ITEMS.getValue(doll);
            if (item != null) {
                output = new ItemStack(item);
            }
            availableDolls.put(tier, list);
            this.setChanged();
        }
        return output;
    }

    private void shuffleDolls(TagKey<Item> tagKey, List<ResourceLocation> list) {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        if (tags == null) {
            return;
        }
        tags.getTag(tagKey).stream().forEach(tag -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(tag);
            if (key != null) {
                list.add(key);
            }
        });
        Collections.shuffle(list);
        availableDolls.put(tier, list);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(NBT_KEY_TIER, tier);

        ListTag dollsTag = new ListTag();
        for (int i = 0; i < availableDolls.size(); i++) {
            List<ResourceLocation> dolls = availableDolls.get(i);
            ListTag listTag = new ListTag();
            for (ResourceLocation doll : dolls) {
                listTag.add(StringTag.valueOf(doll.toString()));
            }
            dollsTag.add(listTag);
        }
        tag.put(NBT_KEY_AVAILABLE_DOLLS, dollsTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.contains(NBT_KEY_TIER)) {
            tier = tag.getInt(NBT_KEY_TIER);
        }

        availableDolls.clear();
        if (tag.contains(NBT_KEY_AVAILABLE_DOLLS)) {
            ListTag dollsTag = tag.getList(NBT_KEY_AVAILABLE_DOLLS, Tag.TAG_COMPOUND);
            for (int i = 0; i < dollsTag.size(); i++) {
                ListTag listTag = dollsTag.getList(i);
                List<ResourceLocation> dolls = listTag.stream()
                        .filter(nbt -> nbt instanceof StringTag)
                        .map(nbt -> new ResourceLocation(nbt.getAsString()))
                        .toList();
                availableDolls.put(i, dolls);
            }
        }
    }

    public int getTier() {
        return tier;
    }
}
