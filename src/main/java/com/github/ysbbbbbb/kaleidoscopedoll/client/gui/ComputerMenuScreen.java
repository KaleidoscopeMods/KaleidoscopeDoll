package com.github.ysbbbbbb.kaleidoscopedoll.client.gui;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import net.minecraftforge.registries.tags.ITagManager;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class ComputerMenuScreen extends AbstractContainerScreen<ComputerMenu> {
    private static final ResourceLocation BG = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "textures/gui/computer.png");
    private static final int MAX_DOLLS_PER_PAGE = 32;

    private List<Item> dolls = Lists.newArrayList();
    private int currentPage = 0;
    private float scrollOffs;
    private boolean scrolling;

    private EditBox searchField;

    public ComputerMenuScreen(ComputerMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 208;
        this.getDolls();
    }

    private void getDolls() {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        if (tags == null) {
            return;
        }
        this.dolls.addAll(tags.getTag(TagItem.PLAYER_DOLLS).stream().toList());
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        this.initSearch();
        this.initDollButtons();
    }

    private void initDollButtons() {
        ITagManager<Item> tags = ForgeRegistries.ITEMS.tags();
        if (tags == null) {
            return;
        }
        ITag<Item> tag = tags.getTag(TagItem.PLAYER_DOLLS);
        String searchText = this.searchField.getValue().toLowerCase(Locale.ENGLISH);
        this.dolls.clear();
        if (StringUtils.isEmpty(searchText)) {
            this.dolls.addAll(tag.stream().toList());
        } else {
            this.dolls.addAll(tag.stream()
                    .filter(item -> doSearch(item, searchText))
                    .toList());
        }
        int start = currentPage * MAX_DOLLS_PER_PAGE;
        int end = Math.min(start + MAX_DOLLS_PER_PAGE, this.dolls.size());
        List<Item> dollsToShow = this.dolls.subList(start, end);
        int xPos = this.leftPos + 7;
        int yPos = this.topPos + 17;
        int xOffset = 18;
        int yOffset = 18;
        for (int i = 0; i < dollsToShow.size(); i++) {
            Item item = dollsToShow.get(i);
            int x = xPos + (i % 8) * xOffset;
            int y = yPos + (i / 8) * yOffset;
            this.addRenderableWidget(new DollButton(x, y, item.getDefaultInstance(), button -> {
                button.setFocused(!button.isFocused());
                if (this.minecraft == null || this.minecraft.gameMode == null) {
                    return;
                }
                // 通过物品 ID 传递合成信息
                this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, Item.getId(item));
            }));
        }
    }

    private boolean doSearch(Item item, String searchText) {
        // 可以直接搜索物品 id 或者文本描述
        ResourceLocation id = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));
        if (id.toString().contains(searchText)) {
            return true;
        }
        String key = SPECIAL_TOOLTIPS.get(id);
        if (key == null) {
            return false;
        }
        // 语言文件的 key 也可作为搜索判断
        if (key.contains(searchText)) {
            return true;
        }
        return I18n.get("tooltip.kaleidoscope_doll.doll." + key).contains(searchText);
    }

    private void initSearch() {
        String perText = "";
        boolean focus = false;
        if (searchField != null) {
            perText = searchField.getValue();
            focus = searchField.isFocused();
        }
        searchField = new EditBox(getMinecraft().font, leftPos + 8, topPos + 102, 85, 16, Component.literal("Doll Search Box"));
        searchField.setValue(perText);
        searchField.setBordered(false);
        searchField.setMaxLength(32);
        searchField.setTextColor(0x39ff56);
        searchField.setFocused(focus);
        searchField.moveCursorToEnd();
        this.addWidget(this.searchField);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        graphics.drawString(this.font, Component.translatable("block.kaleidoscope_doll.computer"),
                this.titleLabelX, this.titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        renderBackground(graphics);
        int posX = this.leftPos;
        int posY = (this.height - this.imageHeight) / 2;
        graphics.blit(BG, posX, posY, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y, float partialTicks) {
        super.render(graphics, x, y, partialTicks);

        int uOffset = this.isScrollBarActive() ? 0 : 14;
        int offset = (int) (57 * this.scrollOffs);
        graphics.blit(BG, this.leftPos + 155, this.topPos + 17 + offset,
                177 + uOffset, 0, 12, 15);

        this.searchField.render(graphics, x, y, partialTicks);
        if (!this.searchField.isFocused() && this.searchField.getValue().isEmpty()) {
            graphics.drawString(font, Component.translatable("gui.kaleidoscope_doll.computer.search")
                            .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC),
                    this.leftPos + 9, this.topPos + 102, 0xFFFFFF, false);
        }

        this.renderTooltip(graphics, x, y);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String value = this.searchField.getValue();
        super.resize(minecraft, width, height);
        this.searchField.setValue(value);
    }

    @Override
    protected void containerTick() {
        this.searchField.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.searchField.mouseClicked(mouseX, mouseY, button)) {
            this.setFocused(this.searchField);
            return true;
        } else {
            this.searchField.setFocused(false);
        }

        this.scrolling = false;
        double scrollX = this.leftPos + 155;
        double scrollY = this.topPos + 17;
        if (scrollX <= mouseX && mouseX < (scrollX + 12) && scrollY <= mouseY && mouseY < (scrollY + 72)) {
            this.scrolling = true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            float yOffset = (float) mouseY - this.topPos - 17;
            this.scrollOffs = Mth.clamp(yOffset / 57, 0F, 1F);
            this.currentPage = (int) ((this.scrollOffs * this.getOffscreenRows()) + 0.5);
            this.init();
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive()) {
            int offscreenRows = this.getOffscreenRows();
            float scrollAmount = (float) delta / offscreenRows;
            this.scrollOffs = Mth.clamp(this.scrollOffs - scrollAmount, 0.0F, 1.0F);
            this.currentPage = (int) ((this.scrollOffs * offscreenRows) + 0.5);
            this.init();
        }
        return true;
    }

    private int getOffscreenRows() {
        return (this.dolls.size() + MAX_DOLLS_PER_PAGE - 1) / MAX_DOLLS_PER_PAGE - 1;
    }

    private boolean isScrollBarActive() {
        return this.dolls.size() > MAX_DOLLS_PER_PAGE;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        String preText = this.searchField.getValue();
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
            if (!Objects.equals(preText, this.searchField.getValue())) {
                currentPage = 0;
                this.init();
            }
            return true;
        } else {
            return this.searchField.isFocused()
                   && this.searchField.isVisible()
                   && keyCode != InputConstants.KEY_ESCAPE
                   || super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchField == null) {
            return false;
        }
        String perText = this.searchField.getValue();
        if (this.searchField.charTyped(codePoint, modifiers)) {
            if (!Objects.equals(perText, this.searchField.getValue())) {
                currentPage = 0;
                this.init();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void insertText(String text, boolean overwrite) {
        if (overwrite) {
            this.searchField.setValue(text);
        } else {
            this.searchField.insertText(text);
        }
    }
}
