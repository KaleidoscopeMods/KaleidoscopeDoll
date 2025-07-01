package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class ModJEIPlugins implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "jei");

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ModItems.DOLL_ENTITY_ITEM.get(), new EntityDollSubtype());
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, EntityDollRecipeMaker.createRecipes());
    }

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }
}
