package com.github.ysbbbbbb.kaleidoscopedoll.client.gui;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DollButton extends ImageButton {
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "textures/gui/computer.png");
    private final ItemStack doll;

    public DollButton(int pX, int pY, ItemStack doll, OnPress onPress) {
        super(pX, pY, 18, 18, 177, 16, 0, BG, onPress);
        this.doll = doll;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        graphics.renderFakeItem(this.doll, this.getX() + 1, this.getY() + 1);
        int yTex = this.yTexStart;
        if (this.isFocused()) {
            yTex = yTex + 18;
        } else if (this.isHovered()) {
            yTex = yTex + 36;
        }
        RenderSystem.enableDepthTest();
        graphics.blit(BG, this.getX(), this.getY(), this.xTexStart, yTex, 18, 18, 256, 256);
        if (this.isHovered()) {
            Font font = Minecraft.getInstance().font;
            graphics.renderTooltip(font, this.doll, pMouseX, pMouseY);
        }
    }
}
