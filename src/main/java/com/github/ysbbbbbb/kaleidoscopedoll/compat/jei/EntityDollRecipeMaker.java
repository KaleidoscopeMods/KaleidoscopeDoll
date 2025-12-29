package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.data.custom.ServerCustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.CustomDollItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.ModIds;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

public class EntityDollRecipeMaker {
    private static final String GROUP = "jei.doll.entity";

    public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
        List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();

        // 普通玩偶
        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (!(item instanceof DollItem dollItem)) {
                return;
            }

            Ingredient inputDoll = Ingredient.of(item);
            Ingredient blockToEntityItem = Ingredient.of(TagItem.BLOCK_DOLLS_TO_ENTITY_ITEM);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, inputDoll, blockToEntityItem);

            BlockState dollState = dollItem.getBlock().defaultBlockState();
            ItemStack output = DollEntityItem.createItemWithBlockState(dollState);

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, GROUP + "." + key.getPath());
            ShapelessRecipe recipe = new ShapelessRecipe(GROUP, CraftingBookCategory.MISC, output, inputs);

            recipes.add(new RecipeHolder<>(id, recipe));
        });

        // 自定义玩偶
        ServerCustomDollLoader.getModels().forEach(id -> {
            ItemStack stack = ModItems.CUSTOM_DOLL.get().getDefaultInstance();
            CustomDollItem.setModelId(stack, id);

            Ingredient inputDoll = Ingredient.of(stack);
            Ingredient blockToEntityItem = Ingredient.of(TagItem.BLOCK_DOLLS_TO_ENTITY_ITEM);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, inputDoll, blockToEntityItem);

            ItemStack output = DollEntityItem.createItemWithCustomDollId(id);

            // 将 id 转换为 md5 字符串
            String hashId = md5Hex(id);
            ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, GROUP + ".custom." + hashId);
            ShapelessRecipe recipe = new ShapelessRecipe(GROUP, CraftingBookCategory.MISC, output, inputs);

            recipes.add(new RecipeHolder<>(recipeId, recipe));
        });

        return recipes;
    }

    private static String md5Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // 不太可能发生，回退到随机 UUID
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    private EntityDollRecipeMaker() {
    }
}
