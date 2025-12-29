package com.github.ysbbbbbb.kaleidoscopedoll.client.render;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.CustomDollItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.apache.commons.lang3.StringUtils;
import org.joml.Matrix4f;

import java.util.concurrent.TimeUnit;

public class DollEntityItemRender extends BlockEntityWithoutLevelRenderer {
    private final Cache<ItemStack, ItemStack> dollCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.SECONDS).build();
    private final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "textures/item/doll_entity_item_bg.png");

    public DollEntityItemRender(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack poseStack,
                             MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        Level world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }

        // 从物品获取玩偶实体
        ItemStack dollShowItem = dollCache.getIfPresent(itemStackIn);
        if (dollShowItem == null) {
            DollEntity entity = DollEntityItem.getDollEntity(world, itemStackIn);

            // 先检查是否是自定义玩偶
            String dollId = entity.getCustomDollId();
            if (StringUtils.isNotBlank(dollId)) {
                dollShowItem = new ItemStack(ModItems.CUSTOM_DOLL.get());
                CustomDollItem.setModelId(dollShowItem, dollId);
            } else {
                Block displayBlock = entity.getDisplayBlockState().getBlock();
                if (displayBlock == Blocks.AIR) {
                    dollShowItem = new ItemStack(ModItems.PURPLE_DOLL_GIFT_BOX.get());
                } else {
                    dollShowItem = new ItemStack(displayBlock);
                }
            }

            dollCache.put(itemStackIn, dollShowItem);
        }

        // GUI 内渲染背景，从而方便区分实体版玩偶和方块版玩偶
        if (transformType == ItemDisplayContext.GUI) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0);

            // 绑定背景纹理并渲染
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, BG);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            // 创建矩阵并渲染背景四边形
            Matrix4f matrix = poseStack.last().pose();
            BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            float size = 0.5f;
            bufferBuilder.addVertex(matrix, -size, -size, -0.001f).setUv(0, 1);
            bufferBuilder.addVertex(matrix, size, -size, -0.001f).setUv(1, 1);
            bufferBuilder.addVertex(matrix, size, size, -0.001f).setUv(1, 0);
            bufferBuilder.addVertex(matrix, -size, size, -0.001f).setUv(0, 0);

            BufferUploader.drawWithShader(bufferBuilder.buildOrThrow());
            RenderSystem.disableBlend();

            poseStack.popPose();
        }

        // 渲染物品
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        if (transformType == ItemDisplayContext.GUI) {
            poseStack.scale(0.75f, 0.75f, 0.75f);
        }
        poseStack.translate(0.5, 0.5, 0.5);
        itemRenderer.renderStatic(dollShowItem, transformType, combinedLight, combinedOverlay, poseStack, bufferSource, world, 0);
        poseStack.popPose();
    }
}
