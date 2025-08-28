package com.github.ysbbbbbb.kaleidoscopedoll.client.gui;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DollButton extends Button {
    private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "textures/gui/computer.png");
    private final ItemStack doll;

    public DollButton(int pX, int pY, ItemStack doll, OnPress onPress) {
        super(pX, pY, 18, 18, Component.empty(), onPress, DEFAULT_NARRATION);
        this.doll = doll;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        graphics.renderFakeItem(this.doll, this.getX() + 1, this.getY() + 1);
        int yTex = 16;
        if (this.isFocused()) {
            yTex = yTex + 18;
        } else if (this.isHovered()) {
            yTex = yTex + 36;
        }
        RenderSystem.enableDepthTest();
        int xTexStart = 177;
        graphics.blit(BG, this.getX(), this.getY(), xTexStart, yTex, 18, 18, 256, 256);
        if (this.isHovered()) {
            Font font = Minecraft.getInstance().font;
            graphics.renderTooltip(font, this.doll, pMouseX, pMouseY);
        }
    }
}
