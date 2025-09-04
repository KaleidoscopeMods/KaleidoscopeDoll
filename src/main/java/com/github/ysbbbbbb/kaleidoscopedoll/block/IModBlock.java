package com.github.ysbbbbbb.kaleidoscopedoll.block;

import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public interface IModBlock {
    void appendHoverText(Consumer<Component> tooltipAdder);
}
