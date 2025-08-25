package com.github.ysbbbbbb.kaleidoscopedoll.client.event;

import com.github.ysbbbbbb.kaleidoscopedoll.client.gui.ComputerMenuScreen;
import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvent {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent evt) {
        evt.enqueueWork(() -> MenuScreens.register(ComputerMenu.TYPE, ComputerMenuScreen::new));
    }
}
