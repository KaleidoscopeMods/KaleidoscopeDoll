package com.github.ysbbbbbb.kaleidoscopedoll.client.event;

import com.github.ysbbbbbb.kaleidoscopedoll.client.gui.ComputerMenuScreen;
import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void clientSetup(RegisterMenuScreensEvent event) {
        event.register(ComputerMenu.TYPE, ComputerMenuScreen::new);
    }
}
