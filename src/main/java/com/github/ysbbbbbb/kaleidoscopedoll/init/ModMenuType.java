package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuType {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(Registries.MENU, KaleidoscopeDoll.MOD_ID);

    public static final Supplier<MenuType<ComputerMenu>> COMPUTER_CONTAINER = CONTAINER_TYPE.register("computer", () -> ComputerMenu.TYPE);
}
