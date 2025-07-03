package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class AdvancementGenerator extends FabricAdvancementProvider {
    protected AdvancementGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> saver) {
        AdvancementHolder root = make(BuiltInRegistries.ITEM.get(id("doll_52")), "root")
                .requirements(AdvancementRequirements.Strategy.OR)
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/root").toString());

        AdvancementHolder dollMachine = make(ModItems.DOLL_MACHINE, "doll_machine").parent(root)
                .addCriterion("doll_machine", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DOLL_MACHINE))
                .save(saver, id("doll/doll_machine").toString());

        make(ModItems.PURPLE_DOLL_GIFT_BOX, "purple_doll_gift_box").parent(dollMachine)
                .addCriterion("purple_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PURPLE_DOLL_GIFT_BOX))
                .save(saver, id("doll/purple_doll_gift_box").toString());
        make(ModItems.GREEN_DOLL_GIFT_BOX, "green_doll_gift_box").parent(dollMachine)
                .addCriterion("green_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_DOLL_GIFT_BOX))
                .save(saver, id("doll/green_doll_gift_box").toString());
        AdvancementHolder yellowGiftBox = make(ModItems.YELLOW_DOLL_GIFT_BOX, "yellow_doll_gift_box").parent(dollMachine)
                .addCriterion("yellow_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.YELLOW_DOLL_GIFT_BOX))
                .save(saver, id("doll/yellow_doll_gift_box").toString());

        make(BuiltInRegistries.ITEM.get(id("doll_0")), "credits").parent(root)
                .addCriterion("credits", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/credits").toString());

        Advancement.Builder fullCollection = makeChallenge(BuiltInRegistries.ITEM.get(id("doll_5")), "full_collection").parent(yellowGiftBox)
                .requirements(AdvancementRequirements.Strategy.AND);
        ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                .forEach(item -> fullCollection.addCriterion(BuiltInRegistries.ITEM.getKey(item).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item)));
        fullCollection.rewards(AdvancementRewards.Builder.experience(100))
                .save(saver, id("doll/full_collection").toString());
    }

    private static Advancement.Builder make(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "textures/advancement/background.png"),
                AdvancementType.TASK, false, false, false);
    }

    private static Advancement.Builder makeChallenge(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "textures/advancement/background.png"),
                AdvancementType.CHALLENGE, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, id);
    }
}
