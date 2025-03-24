package com.github.ysbbbbbb.kaleidoscopedoll.compat.curios;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.CuriosApi;

public class CuriosCompatInner {

    @OnlyIn(Dist.CLIENT)
    static void registerRenderer(EntityRenderersEvent.AddLayers event) {
        if (event.getSkin("default") instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, event.getContext().getItemInHandRenderer()));
        }
        if (event.getSkin("slim") instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, event.getContext().getItemInHandRenderer()));
        }
    }

    static void registerDollItemPredicate() {
        CuriosApi.registerCurioPredicate(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_item"),
                slotResult -> slotResult.stack().getItem() instanceof DollItem);

    }
}
