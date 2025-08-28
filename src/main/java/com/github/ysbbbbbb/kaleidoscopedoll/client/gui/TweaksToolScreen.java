package com.github.ysbbbbbb.kaleidoscopedoll.client.gui;

import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.network.NetworkHandler;
import com.github.ysbbbbbb.kaleidoscopedoll.network.message.DollTweakersMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class TweaksToolScreen extends Screen {
    private final int entityId;
    private final Vector3f scale;
    private final Vector3f translation;
    private final Vector2f rotation;

    protected TweaksToolScreen(DollEntity entity) {
        super(Component.literal("Tweaks Tool"));
        this.entityId = entity.getId();
        this.scale = entity.getDisplayScale();
        this.translation = entity.getDisplayTranslation();
        this.rotation = new Vector2f(entity.getXRot(), entity.getYRot());
    }

    public static void openScreen() {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY) {
            return;
        }
        if (hitResult instanceof EntityHitResult result && result.getEntity() instanceof DollEntity doll) {
            Minecraft.getInstance().setScreen(new TweaksToolScreen(doll));
        }
    }

    @Override
    protected void init() {
        int x = (this.width - 380) / 2;
        int y = (this.height - 200) / 2 + 20;

        float scale = 0.1f;
        float translation = 0.1f;
        float rotation = 1f;

        // 缩放
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.scale.x -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.x += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.scale.y -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 22, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.y += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 22, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.scale.z -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 44, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.z += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 44, 20, 20).build());

        // 位移
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.x -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 90, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.x += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 90, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.y -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 112, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.y += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 112, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.z -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 134, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.z += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 134, 20, 20).build());

        // 旋转
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.rotation.x -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.rotation.x += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y, 20, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.rotation.y -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 22, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.rotation.y += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 2 : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 22, 20, 20).build());

        // 重置
        this.addRenderableWidget(Button.builder(Component.translatable("controls.reset"), button -> {
            float yRot = this.getMinecraft().player == null ? 0 : this.getMinecraft().player.getYRot();
            this.scale.set(1);
            this.translation.set(0);
            this.rotation.set(0, yRot + 180);
            this.sendTweaksMessage();
        }).bounds(x + 140, y - 22, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - 380) / 2;
        int y = (this.height - 200) / 2 + 10;

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.scale"), x + 60, y - 2, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 60, y + 17, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 60, y + 39, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 60, y + 61, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.translation"), x + 60, y + 88, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 60, y + 107, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 60, y + 129, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 60, y + 151, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.rotation"), x + 320, y - 2, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 320, y + 17, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 320, y + 39, 0xFFFFFF);
    }

    private void sendTweaksMessage() {
        // 需要对缩放做限制
        NetworkHandler.CHANNEL.sendToServer(new DollTweakersMessage(entityId, scale, translation, rotation));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
