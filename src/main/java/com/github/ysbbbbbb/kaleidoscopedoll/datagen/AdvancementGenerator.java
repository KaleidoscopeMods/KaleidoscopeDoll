package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class AdvancementGenerator extends FabricAdvancementProvider {
    protected AdvancementGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> saver) {
        Advancement root = make(BuiltInRegistries.ITEM.get(id("doll_52")), "root")
                .requirements(RequirementsStrategy.OR)
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/root").toString());

        Advancement dollMachine = make(ModItems.DOLL_MACHINE, "doll_machine").parent(root)
                .addCriterion("doll_machine", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DOLL_MACHINE))
                .save(saver, id("doll/doll_machine").toString());

        make(ModItems.PURPLE_DOLL_GIFT_BOX, "purple_doll_gift_box").parent(dollMachine)
                .addCriterion("purple_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PURPLE_DOLL_GIFT_BOX))
                .save(saver, id("doll/purple_doll_gift_box").toString());
        make(ModItems.GREEN_DOLL_GIFT_BOX, "green_doll_gift_box").parent(dollMachine)
                .addCriterion("green_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_DOLL_GIFT_BOX))
                .save(saver, id("doll/green_doll_gift_box").toString());
        Advancement yellowGiftBox = make(ModItems.YELLOW_DOLL_GIFT_BOX, "yellow_doll_gift_box").parent(dollMachine)
                .addCriterion("yellow_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.YELLOW_DOLL_GIFT_BOX))
                .save(saver, id("doll/yellow_doll_gift_box").toString());

        make(BuiltInRegistries.ITEM.get(id("doll_0")), "credits").parent(root)
                .addCriterion("credits", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/credits").toString());

        Advancement.Builder fullCollection = makeChallenge(BuiltInRegistries.ITEM.get(id("doll_5")), "full_collection").parent(yellowGiftBox)
                .requirements(RequirementsStrategy.AND);
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
                new ResourceLocation(KaleidoscopeDoll.MOD_ID, "textures/advancement/background.png"),
                FrameType.TASK, false, false, false);
    }

    private static Advancement.Builder makeChallenge(ItemLike item, String key) {
        MutableComponent title = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.title", key));
        MutableComponent desc = Component.translatable(String.format("advancements.kaleidoscope_doll.doll.%s.description", key));

        return Advancement.Builder.advancement().display(item, title, desc,
                new ResourceLocation(KaleidoscopeDoll.MOD_ID, "textures/advancement/background.png"),
                FrameType.CHALLENGE, true, true, false);
    }

    private static ResourceLocation id(String id) {
        return new ResourceLocation(KaleidoscopeDoll.MOD_ID, id);
    }
}
