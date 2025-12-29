package com.github.ysbbbbbb.kaleidoscopedoll.inventory;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.data.custom.ServerCustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.CustomDollItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

public class ComputerMenu extends AbstractContainerMenu {
    public static final MenuType<ComputerMenu> TYPE = IForgeMenuType.create((windowId, inv, data) -> new ComputerMenu(windowId, inv));
    private static final ResourceLocation OUTPUT_SLOT = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "slot/computer_output_slot");
    private final ItemStackHandler input = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(TagItem.COMPUTER_TOKENS);
        }
    };
    private final ItemStackHandler output = new ItemStackHandler() {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }
    };

    public ComputerMenu(int id, Inventory inventory) {
        super(TYPE, id);

        // 输入槽
        this.addSlot(new SlotItemHandler(input, 0, 108, 98) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                // 清空输出槽
                output.setStackInSlot(0, ItemStack.EMPTY);
            }
        });

        // 输出槽
        this.addSlot(new SlotItemHandler(output, 0, 151, 98) {
            @Override
            public void onTake(Player player, ItemStack stack) {
                super.onTake(player, stack);
                ItemStack inputStack = input.getStackInSlot(0);
                if (!inputStack.isEmpty()) {
                    // 输入槽物品数量减 1
                    inputStack.shrink(1);
                    input.setStackInSlot(0, inputStack);
                }
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, OUTPUT_SLOT);
            }
        });

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 184));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }
    }

    public boolean clickDollButton(ItemStack doll) {
        ItemStack stack = this.input.getStackInSlot(0);
        // 防止作弊，必须检查一次
        if (stack.is(TagItem.COMPUTER_TOKENS)) {
            // 要么是玩家玩偶
            if (doll.is(TagItem.PLAYER_DOLLS)) {
                this.output.setStackInSlot(0, doll);
                return true;
            }

            // 要么是自定义玩偶
            if (doll.is(TagItem.CUSTOM_DOLLS)) {
                // 自定义的 ID 必须在服务端存在
                String id = CustomDollItem.getModelId(doll);
                if (StringUtils.isBlank(id) || !ServerCustomDollLoader.getModels().contains(id)) {
                    return false;
                }
                this.output.setStackInSlot(0, doll);
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotItem = slot.getItem();
            itemStack = slotItem.copy();
            if (index < 2) {
                if (!this.moveItemStackTo(slotItem, 2, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
                // 点击的就是输出槽，并且转移成功了，此时输入槽扣除一个
                if (index == 1) {
                    ItemStack inputStack = input.getStackInSlot(0);
                    if (!inputStack.isEmpty()) {
                        // 输入槽物品数量减 1
                        inputStack.shrink(1);
                        input.setStackInSlot(0, inputStack);
                    }
                }
            } else if (!this.moveItemStackTo(slotItem, 0, 2, true)) {
                return ItemStack.EMPTY;
            }

            if (slotItem.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        ItemHandlerHelper.giveItemToPlayer(player, input.getStackInSlot(0));
    }
}
