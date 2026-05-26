package com.github.ysbbbbbb.kaleidoscopedoll.client.gui;

import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.network.NetworkHandler;
import com.github.ysbbbbbb.kaleidoscopedoll.network.message.DollTweakersMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
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
    private final Vector3f itemScale;
    private final Vector3f itemTranslation;
    private final Vector3f itemRotation;

    protected TweaksToolScreen(DollEntity entity) {
        super(Component.literal("Tweaks Tool"));
        this.entityId = entity.getId();
        this.scale = entity.getDisplayScale();
        this.translation = entity.getDisplayTranslation();
        this.rotation = new Vector2f(entity.getXRot(), entity.getYRot());
        this.itemScale = entity.getItemScale();
        this.itemTranslation = entity.getItemTranslation();
        this.itemRotation = entity.getItemRotation();
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
            this.scale.x -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.x += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.scale.y -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 20, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.y += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 20, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.scale.z -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 40, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.scale.z += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 40, 16, 16).build());

        // 位移
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.x -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 70, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.x += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 70, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.y -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 90, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.y += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 90, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.translation.z -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 110, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.translation.z += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 110, 16, 16).build());

        // 旋转
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.rotation.x -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 140, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.rotation.x += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 140, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.rotation.y -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 25, y + 160, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.rotation.y += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 75, y + 160, 16, 16).build());

        // 物品缩放
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemScale.x -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemScale.x += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemScale.y -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 20, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemScale.y += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 20, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemScale.z -= (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 40, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemScale.z += (scale * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 40, 16, 16).build());

        // 物品位移
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemTranslation.x -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 70, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemTranslation.x += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 70, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemTranslation.y -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 90, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemTranslation.y += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 90, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemTranslation.z -= (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 110, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemTranslation.z += (translation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 110, 16, 16).build());

        // 物品旋转
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemRotation.x -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 140, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemRotation.x += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 140, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemRotation.y -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 160, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemRotation.y += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 160, 16, 16).build());

        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            this.itemRotation.z -= (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 285, y + 180, 16, 16).build());
        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            this.itemRotation.z += (rotation * (Screen.hasShiftDown() ? 10 : 1) * (Screen.hasControlDown() ? 0.1f : 1));
            this.sendTweaksMessage();
        }).bounds(x + 335, y + 180, 16, 16).build());

        // 重置
        this.addRenderableWidget(Button.builder(Component.translatable("controls.reset"), button -> {
            float yRot = this.getMinecraft().player == null ? 0 : this.getMinecraft().player.getYRot();
            this.scale.set(1);
            this.translation.set(0);
            this.rotation.set(0, yRot + 180);
            this.itemScale.set(1);
            this.itemTranslation.set(0);
            this.itemRotation.set(0, 0, 0);
            this.sendTweaksMessage();
        }).bounds(x + 140, y - 22, 100, 16).build());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int x = (this.width - 380) / 2;
        int y = (this.height - 200) / 2 + 10;

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.help"), x + 190, y - 35, 0xFFFFFF);


        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.total_scale"), x + 59, y - 2, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 59, y + 15, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 59, y + 35, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 59, y + 55, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.total_translation"), x + 59, y + 68, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 59, y + 85, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 59, y + 105, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 59, y + 125, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.total_rotation"), x + 59, y + 138, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 59, y + 155, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 59, y + 175, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.item_scale"), x + 319, y - 2, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 319, y + 15, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 319, y + 35, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 319, y + 55, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.item_translation"), x + 319, y + 68, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 319, y + 85, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 319, y + 105, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 319, y + 125, 0xFFFFFF);

        graphics.drawCenteredString(font, Component.translatable("gui.kaleidoscope_doll.tweaks_tool.item_rotation"), x + 319, y + 138, 0xFFFFFF);
        graphics.drawCenteredString(font, "X", x + 319, y + 155, 0xFFFFFF);
        graphics.drawCenteredString(font, "Y", x + 319, y + 175, 0xFFFFFF);
        graphics.drawCenteredString(font, "Z", x + 319, y + 195, 0xFFFFFF);
    }

    private void sendTweaksMessage() {
        CompoundTag itemDisplay = new CompoundTag();
        itemDisplay.put("item_scale", writeVector3f(itemScale));
        itemDisplay.put("item_translation", writeVector3f(itemTranslation));
        itemDisplay.put("item_rotation", writeVector3f(itemRotation));

        // 需要对缩放做限制
        NetworkHandler.CHANNEL.sendToServer(new DollTweakersMessage(entityId, scale, translation, rotation, itemDisplay));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private CompoundTag writeVector3f(Vector3f vector) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x", vector.x);
        tag.putFloat("y", vector.y);
        tag.putFloat("z", vector.z);
        return tag;
    }
}
