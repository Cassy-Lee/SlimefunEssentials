package me.justahuman.slimefun_essentials.compat.jei;

import me.justahuman.slimefun_essentials.client.ResourceLoader;
import me.justahuman.slimefun_essentials.client.SlimefunCategory;
import me.justahuman.slimefun_essentials.client.SlimefunItemStack;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.jei.categories.AncientAltarCategory;
import me.justahuman.slimefun_essentials.compat.jei.categories.GridCategory;
import me.justahuman.slimefun_essentials.compat.jei.categories.ProcessCategory;
import me.justahuman.slimefun_essentials.compat.jei.categories.ReactorCategory;
import me.justahuman.slimefun_essentials.compat.jei.categories.SmelteryCategory;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRuntimeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.library.load.registration.SubtypeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JeiIntegration implements IModPlugin {
    public static final JeiRecipeInterpreter RECIPE_INTERPRETER = new JeiRecipeInterpreter();

    @Override
    @NotNull
    public Identifier getPluginUid() {
        return Utils.newIdentifier("jei_integration");
    }

    @Override
    public void registerRuntime(IRuntimeRegistration registration) {
        registration.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, ResourceLoader.getSlimefunItems().values().stream().map(SlimefunItemStack::itemStack).toList());
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        if (!Utils.shouldFunction() || !(registration instanceof SubtypeRegistration subtypeRegistration)) {
            return;
        }

        for (SlimefunItemStack slimefunItemStack : ResourceLoader.getSlimefunItems().values()) {
            registration.registerSubtypeInterpreter(slimefunItemStack.itemStack().getItem(),
                    new SlimefunIdInterpreter(subtypeRegistration.getInterpreters().get(VanillaTypes.ITEM_STACK, slimefunItemStack.itemStack()).orElse(null)));
        }
    }
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (!Utils.shouldFunction()) {
            return;
        }

        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        for (SlimefunCategory slimefunCategory : SlimefunCategory.getSlimefunCategories().values()) {
            registration.addRecipeCategories(getJeiCategory(guiHelper, slimefunCategory, ResourceLoader.getSlimefunItems().get(slimefunCategory.id()).itemStack()));
        }
    }
    
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (!Utils.shouldFunction()) {
            return;
        }

        for (SlimefunCategory slimefunCategory : SlimefunCategory.getSlimefunCategories().values()) {
            registration.addRecipes(RecipeType.create(Utils.ID, slimefunCategory.id().toLowerCase(), SlimefunRecipe.class), slimefunCategory.recipes());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (!Utils.shouldFunction()) {
            return;
        }

        for (SlimefunCategory slimefunCategory : SlimefunCategory.getSlimefunCategories().values()) {
            registration.addRecipeCatalyst(ResourceLoader.getSlimefunItems().get(slimefunCategory.id()).itemStack(), RecipeType.create(Utils.ID, slimefunCategory.id().toLowerCase(), SlimefunRecipe.class));
        }
    }

    public static IRecipeCategory<SlimefunRecipe> getJeiCategory(IGuiHelper guiHelper, SlimefunCategory slimefunCategory, ItemStack catalyst) {
        final String type = slimefunCategory.type();
        if (type.equals("ancient_altar")) {
            return new AncientAltarCategory(guiHelper, slimefunCategory, catalyst);
        } else if (type.equals("smeltery")) {
            return new SmelteryCategory(guiHelper, slimefunCategory, catalyst);
        } else if (type.equals("reactor")) {
            return new ReactorCategory(guiHelper, slimefunCategory, catalyst);
        } else if (type.contains("grid")) {
            return new GridCategory(guiHelper, slimefunCategory, catalyst, TextureUtils.getSideSafe(type));
        } else {
            return new ProcessCategory(guiHelper, slimefunCategory, catalyst);
        }
    }
}
