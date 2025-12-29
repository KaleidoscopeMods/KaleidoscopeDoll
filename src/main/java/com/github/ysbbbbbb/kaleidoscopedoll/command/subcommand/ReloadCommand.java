package com.github.ysbbbbbb.kaleidoscopedoll.command.subcommand;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.data.custom.ServerCustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.network.NetworkHandler;
import com.github.ysbbbbbb.kaleidoscopedoll.network.message.CustomDollReloadMessage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;

public class ReloadCommand {
    private static final String RELOAD_NAME = "reload";

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        LiteralArgumentBuilder<CommandSourceStack> reload = LiteralArgumentBuilder.literal(RELOAD_NAME);
        reload.executes(ReloadCommand::reload);
        return reload;
    }

    private static int reload(CommandContext<CommandSourceStack> context) {
        try {
            ServerCustomDollLoader.init();
            // 向所有的客户端发送同步信息
            NetworkHandler.CHANNEL.send(PacketDistributor.ALL.noArg(), new CustomDollReloadMessage());
            context.getSource().sendSuccess(() -> Component.translatable("message.kaleidoscope_doll.reload_sucess"), true);
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to reload custom dolls", e);
            context.getSource().sendFailure(Component.literal(e.getLocalizedMessage()));
        }
        return Command.SINGLE_SUCCESS;
    }
}
