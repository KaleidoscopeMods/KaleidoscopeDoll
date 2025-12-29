package com.github.ysbbbbbb.kaleidoscopedoll.item;

import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.CustomDollBlockEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.client.render.CustomDollItemRender;
import com.github.ysbbbbbb.kaleidoscopedoll.data.custom.ServerCustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class CustomDollItem extends BlockItem {
    private static final String NBT_MODEL_ID = "DollModelId";

    public CustomDollItem(Block block) {
        super(block, new Item.Properties());
    }

    @Nullable
    public static String getModelId(ItemStack doll) {
        if (doll.getTag() != null && doll.getTag().contains(NBT_MODEL_ID)) {
            return doll.getTag().getString(NBT_MODEL_ID);
        }
        return null;
    }

    public static void setModelId(ItemStack doll, String modelId) {
        doll.getOrCreateTag().putString(NBT_MODEL_ID, modelId);
    }

    public static void addCreativeTab(CreativeModeTab.Output output) {
        output.accept(new ItemStack(ModItems.CUSTOM_DOLL.get()));
        ServerCustomDollLoader.getModels().forEach(modelId -> {
            ItemStack dollStack = new ItemStack(ModItems.CUSTOM_DOLL.get());
            setModelId(dollStack, modelId);
            output.accept(dollStack);
        });
    }

    public static ItemStack getDefaultItemStack() {
        ItemStack dollStack = new ItemStack(ModItems.CUSTOM_DOLL.get());
        ServerCustomDollLoader.getModels().stream().findFirst().ifPresent(modelId -> {
            setModelId(dollStack, modelId);
        });
        return dollStack;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        if (!level.isClientSide && !result && player != null) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CustomDollBlockEntity customDollBlockEntity) {
                String modelId = getModelId(stack);
                if (modelId != null) {
                    customDollBlockEntity.setModelId(modelId);
                }
            }
        }
        return result;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private CustomDollItemRender render = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                Minecraft minecraft = Minecraft.getInstance();
                if (render == null) {
                    render = new CustomDollItemRender(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
                }
                return render;
            }
        });
    }

    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.HEAD;
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("block.kaleidoscope_doll.doll");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        String modelId = getModelId(stack);
        if (modelId != null) {
            String locale = Minecraft.getInstance().getLanguageManager().getSelected();
            // 按照换行进行分割
            String[] lines = CustomDollLoader.getLanguage(locale, modelId).split("\n");
            for (String line : lines) {
                list.add(Component.literal(line).withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            list.add(Component.translatable("tooltip.kaleidoscope_doll.custom.unknown").withStyle(ChatFormatting.DARK_GRAY));
        }
    }
}
