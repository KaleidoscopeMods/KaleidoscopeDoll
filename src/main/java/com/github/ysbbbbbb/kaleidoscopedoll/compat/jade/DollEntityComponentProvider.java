package com.github.ysbbbbbb.kaleidoscopedoll.compat.jade;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum DollEntityComponentProvider implements IEntityComponentProvider {
    INSTANCE;

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_entity");

    @Override
    public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig pluginConfig) {
        if (!(accessor.getEntity() instanceof DollEntity dollEntity)) {
            return;
        }
        Block block = dollEntity.getDisplayBlockState().getBlock();
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        String vanillaDesc = ModRegisterEvent.VANILLA_TOOLTIPS.getOrDefault(key, "vanilla");
        String specialDesc = ModRegisterEvent.SPECIAL_TOOLTIPS.getOrDefault(key, vanillaDesc);
        tooltip.add(Component.translatable("tooltip.kaleidoscope_doll.doll." + specialDesc).withStyle(ChatFormatting.AQUA));
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
