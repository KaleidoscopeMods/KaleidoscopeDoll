package com.github.ysbbbbbb.kaleidoscopedoll.compat.curios;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

/**
 * 依据原版{@link CustomHeadLayer CustomHeadLayer}书写而来，仅供玩家渲染布偶使用
 */
public class DollItemRenderer<T extends LivingEntity, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;

    public DollItemRenderer(RenderLayerParent<T, M> renderer, ItemInHandRenderer itemInHandRenderer) {
        super(renderer);
        this.itemInHandRenderer = itemInHandRenderer;
    }

    public static void translateToHead(PoseStack poseStack) {
        poseStack.translate(0.0F, -0.25F, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        poseStack.scale(0.625F, -0.625F, -0.625F);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        CuriosApi.getCuriosInventory(livingEntity)
                .ifPresent(handler -> handler.getCurios().forEach((id, stacksHandler) -> {
                    if (stacksHandler.isVisible() && "head".equals(stacksHandler.getIdentifier())) {
                        IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                        IDynamicStackHandler cosmeticStacksHandler = stacksHandler.getCosmeticStacks();

                        for (int i = 0; i < stackHandler.getSlots(); i++) {
                            ItemStack stack = cosmeticStacksHandler.getStackInSlot(i);
                            NonNullList<Boolean> renderStates = stacksHandler.getRenders();
                            boolean renderable = renderStates.size() > i && renderStates.get(i);

                            if (stack.isEmpty() && renderable) {
                                stack = stackHandler.getStackInSlot(i);
                            }

                            if (!stack.isEmpty()) {
                                rendererDollItem(stack, livingEntity, poseStack, bufferIn, packedLightIn);
                            }
                        }
                    }
                }));
    }

    private <T extends LivingEntity, M extends EntityModel<T>> void rendererDollItem(ItemStack itemStack, T livingEntity, PoseStack poseStack, MultiBufferSource multiBufferSource, int light) {
        if (itemStack.getItem() instanceof DollItem || itemStack.is(ModItems.CUSTOM_DOLL.get())) {
            poseStack.pushPose();
            poseStack.scale(1.0f, 1.0f, 1.0f);
            this.getParentModel().getHead().translateAndRotate(poseStack);
            translateToHead(poseStack);
            this.itemInHandRenderer.renderItem(livingEntity, itemStack, ItemDisplayContext.HEAD, false, poseStack, multiBufferSource, light);
            poseStack.popPose();
        }
    }
}
