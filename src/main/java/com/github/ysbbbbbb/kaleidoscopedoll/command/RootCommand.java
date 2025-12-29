package com.github.ysbbbbbb.kaleidoscopedoll.command;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.command.subcommand.ReloadCommand;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class RootCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(KaleidoscopeDoll.MOD_ID)
                .requires((source -> source.hasPermission(2)));
        root.then(ReloadCommand.get());
        dispatcher.register(root);
    }
}
