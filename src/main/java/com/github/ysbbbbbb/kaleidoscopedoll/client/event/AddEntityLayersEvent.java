package com.github.ysbbbbbb.kaleidoscopedoll.client.event;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.compat.curios.CuriosCompat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AddEntityLayersEvent {
    @SubscribeEvent
    public static void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        CuriosCompat.addEntityLayers(event);
    }
}
