package com.github.ysbbbbbb.kaleidoscopedoll.compat.curios;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompatInner {
    @OnlyIn(Dist.CLIENT)
    static void registerRenderer(EntityRenderersEvent.AddLayers event) {
        ItemInHandRenderer itemInHandRenderer = Minecraft.getInstance().getItemInHandRenderer();

        if (event.getSkin("default") instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, itemInHandRenderer));
        }
        if (event.getSkin("slim") instanceof PlayerRenderer playerRenderer) {
            playerRenderer.addLayer(new DollItemRenderer<>(playerRenderer, itemInHandRenderer));
        }
    }

    static void registerHeadSlot() {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().cosmetic().build());
    }
}
