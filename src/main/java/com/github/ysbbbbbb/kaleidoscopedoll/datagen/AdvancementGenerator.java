package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class AdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = make(ModItems.DOLL_ICON.get(), "root")
                .requirements(RequirementsStrategy.OR)
                .addCriterion("tick", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/root"), existingFileHelper);

        Advancement dollMachine = make(ModItems.DOLL_MACHINE.get(), "doll_machine").parent(root)
                .addCriterion("doll_machine", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.DOLL_MACHINE.get()))
                .save(saver, id("doll/doll_machine"), existingFileHelper);

        make(ModItems.PURPLE_DOLL_GIFT_BOX.get(), "purple_doll_gift_box").parent(dollMachine)
                .addCriterion("purple_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.PURPLE_DOLL_GIFT_BOX.get()))
                .save(saver, id("doll/purple_doll_gift_box"), existingFileHelper);
        make(ModItems.GREEN_DOLL_GIFT_BOX.get(), "green_doll_gift_box").parent(dollMachine)
                .addCriterion("green_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GREEN_DOLL_GIFT_BOX.get()))
                .save(saver, id("doll/green_doll_gift_box"), existingFileHelper);
        Advancement yellowGiftBox = make(ModItems.YELLOW_DOLL_GIFT_BOX.get(), "yellow_doll_gift_box").parent(dollMachine)
                .addCriterion("yellow_doll_gift_box", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.YELLOW_DOLL_GIFT_BOX.get()))
                .save(saver, id("doll/yellow_doll_gift_box"), existingFileHelper);

        make(ForgeRegistries.ITEMS.getValue(id("doll_0")), "credits").parent(root)
                .addCriterion("credits", PlayerTrigger.TriggerInstance.tick())
                .save(saver, id("doll/credits"), existingFileHelper);

        Advancement.Builder fullCollection = makeChallenge(ForgeRegistries.ITEMS.getValue(id("doll_61")), "full_collection").parent(yellowGiftBox)
                .requirements(RequirementsStrategy.AND);
        ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(ForgeRegistries.ITEMS.getKey(item)))
                .forEach(item -> fullCollection.addCriterion(ForgeRegistries.ITEMS.getKey(item).getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item)));
        fullCollection.rewards(AdvancementRewards.Builder.experience(100))
                .save(saver, id("doll/full_collection"), existingFileHelper);
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
