package com.github.ysbbbbbb.kaleidoscopedoll.init.registry;

import com.github.ysbbbbbb.kaleidoscopedoll.command.RootCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class CommandRegistry {
    @SubscribeEvent
    public static void onServerStaring(RegisterCommandsEvent event) {
        RootCommand.register(event.getDispatcher());
    }
}