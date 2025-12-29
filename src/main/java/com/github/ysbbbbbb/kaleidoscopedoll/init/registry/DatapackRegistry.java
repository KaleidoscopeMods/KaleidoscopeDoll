package com.github.ysbbbbbb.kaleidoscopedoll.init.registry;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.data.resources.ServerCustomDollReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID)
public class DatapackRegistry {
    @SubscribeEvent
    public static void onAddReloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new ServerCustomDollReloadListener());
    }
}
