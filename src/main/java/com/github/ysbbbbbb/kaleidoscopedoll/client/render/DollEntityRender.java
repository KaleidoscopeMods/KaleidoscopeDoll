package com.github.ysbbbbbb.kaleidoscopedoll.client.render;

import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.client.resources.CustomDollReloadListener;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.apache.commons.lang3.StringUtils;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class DollEntityRender extends EntityRenderer<DollEntity> {
    private static final ResourceLocation EMPTY = new ResourceLocation("minecraft", "textures/misc/empty.png");

    public DollEntityRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DollEntity dollEntity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        @Nullable BlockState blockState = dollEntity.getDisplayBlockState();
        String customDollId = dollEntity.getCustomDollId();

        // 优先判断是不是自定义玩偶
        if (StringUtils.isBlank(customDollId) && (blockState == null || blockState.isAir())) {
            return;
        }

        poseStack.pushPose();

        // 应用位移变换
        Vector3f translation = dollEntity.getDisplayTranslation();
        poseStack.translate(translation.x, translation.y, translation.z);

        // 应用 Y 轴旋转（基于实体的 yaw）
        Entity vehicle = dollEntity.getVehicle();
        if (vehicle != null) {
            float vehicleYaw = Mth.lerp(partialTick, vehicle.yRotO, vehicle.getYRot());
            poseStack.mulPose(Axis.YP.rotationDegrees(-vehicleYaw));
        } else {
            entityYaw = Mth.lerp(partialTick, dollEntity.yRotO, dollEntity.getYRot());
            poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw));
        }
        float pitchRadians = Mth.lerp(partialTick, dollEntity.xRotO, dollEntity.getXRot());
        poseStack.mulPose(Axis.XP.rotationDegrees(pitchRadians));

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

        // 渲染逻辑
        if (!StringUtils.isBlank(customDollId)) {
            renderCustom(customDollId, poseStack, bufferSource, packedLight);
        } else if (blockState != null && !blockState.isAir()) {
            renderBlock(dollEntity, poseStack, bufferSource, blockState);
        }

        poseStack.popPose();
        super.render(dollEntity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private static void renderCustom(String modelId, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Model model;
        ResourceLocation texture;

        if (StringUtils.isBlank(modelId)) {
            model = CustomDollReloadListener.DFAULT_DOLL_MODEL;
            texture = CustomDollReloadListener.DEFAULT_TEXTURE_ID;
        } else {
            model = CustomDollLoader.getModel(modelId);
            if (model == null) {
                model = CustomDollReloadListener.DFAULT_DOLL_MODEL;
                texture = CustomDollReloadListener.DEFAULT_TEXTURE_ID;
            } else {
                texture = CustomDollLoader.getTexture(modelId);
                if (texture == null) {
                    texture = MissingTextureAtlasSprite.getLocation();
                }
            }
        }

        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180));

        VertexConsumer buffer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void renderBlock(DollEntity dollEntity, PoseStack poseStack, MultiBufferSource bufferSource, BlockState blockState) {
        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();
        Level level = dollEntity.level();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.cutout());
        blockRenderer.renderBatched(blockState, dollEntity.blockPosition(), level, poseStack, buffer, false, level.random, ModelData.EMPTY, RenderType.cutout());
    }

    @Override
    public ResourceLocation getTextureLocation(DollEntity dollEntity) {
        return EMPTY;
    }
}
