package com.github.ysbbbbbb.kaleidoscopedoll.client.render;

import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Vector3f;

public class DollEntityRender extends EntityRenderer<DollEntity> {
    private static final ResourceLocation EMPTY = new ResourceLocation("minecraft", "textures/misc/empty.png");

    public DollEntityRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DollEntity dollEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        BlockState blockState = dollEntity.getDisplayBlockState();
        if (blockState == null || blockState.isAir()) {
            return;
        }

        poseStack.pushPose();

        // 应用位移变换
        Vector3f translation = dollEntity.getDisplayTranslation();
        poseStack.translate(translation.x, translation.y, translation.z);

        // 应用 Y 轴旋转（基于实体的 yaw）
        float pitchRadians = (float) Math.toRadians(-dollEntity.getXRot());
        if (dollEntity.getVehicle() != null) {
            float vehicleYaw = dollEntity.getVehicle().getVisualRotationYInDegrees();
            poseStack.mulPose(Axis.YP.rotationDegrees(-vehicleYaw));
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
        }
        poseStack.mulPose(Axis.XP.rotation(pitchRadians));

        // 应用缩放变换
        Vector3f scale = dollEntity.getDisplayScale();
        poseStack.scale(scale.x, scale.y, scale.z);

        // 如果是弹跳时间，那么应用弹跳效果
        long time = dollEntity.getBounceTime() - System.currentTimeMillis();
        if (time > 0) {
            // 阻尼系数 - 控制弹跳衰减速度
            float dampingFactor = 0.6f;
            // 弹跳频率 - 控制弹跳次数
            float frequency = 6f;
            // 压缩强度
            float bounceStrength = 0.6f;

            // 计算弹跳进度 (0-1) 500ms持续时间
            float bounceProgress = 1.0f - ((float) time / 500.0f);
            // 使用指数衰减函数模拟阻尼
            float dampedAmplitude = (float) Math.exp(-dampingFactor * bounceProgress * 8.0f);
            // 压缩变形效果 - 模拟弹性物体的形变
            float compressionIntensity = dampedAmplitude * bounceStrength;

            // 当接触地面时（弹跳到最低点），Y轴压缩，X/Z轴扩张
            float groundContact = (float) Math.max(0, -Math.sin(bounceProgress * frequency * Math.PI));
            float scaleY = 1.0f - groundContact * compressionIntensity;
            float scaleXZ = 1.0f + groundContact * compressionIntensity;

            poseStack.scale(scaleXZ, scaleY, scaleXZ);
        }

        // 将方块中心对齐到实体位置
        poseStack.translate(-0.5, 0, -0.5);

        // 渲染方块，使用 renderBatched 方法
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        Level level = dollEntity.level();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());
        blockRenderer.renderBatched(blockState, dollEntity.blockPosition(), level, poseStack, buffer, false, level.random, ModelData.EMPTY, RenderType.cutout());

        poseStack.popPose();
        super.render(dollEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DollEntity dollEntity) {
        return EMPTY;
    }
}
