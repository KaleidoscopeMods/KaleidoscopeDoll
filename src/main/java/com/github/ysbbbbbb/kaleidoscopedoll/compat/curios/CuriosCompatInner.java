package com.github.ysbbbbbb.kaleidoscopedoll.compat.curios;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompatInner {
    @OnlyIn(Dist.CLIENT)
    static void registerRenderer(EntityRenderersEvent.AddLayers event) {
        if (event.getSkin(PlayerSkin.Model.WIDE) instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, event.getContext().getItemInHandRenderer()));
        }
        if (event.getSkin(PlayerSkin.Model.SLIM) instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, event.getContext().getItemInHandRenderer()));
        }
    }

    static void registerDollItemPredicate() {
        CuriosApi.registerCurioPredicate(ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_item"), slotResult -> {
            ItemStack item = slotResult.stack();
            if (item.getItem() instanceof DollItem) {
                return true;
            }
            return item.is(ModItems.CUSTOM_DOLL.get());
        });
    }
}
