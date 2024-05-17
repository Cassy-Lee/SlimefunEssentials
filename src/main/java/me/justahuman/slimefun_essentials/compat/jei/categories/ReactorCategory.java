package me.justahuman.slimefun_essentials.compat.jei.categories;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.jei.JeiIntegration;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReactorCategory extends ProcessCategory {
    public ReactorCategory(IGuiHelper guiHelper, SlimefunRecipeCategory slimefunRecipeCategory) {
        super(Type.REACTOR, guiHelper, slimefunRecipeCategory);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SlimefunRecipe recipe, IFocusGroup focuses) {
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe), calculateYOffset(this.slimefunRecipeCategory, recipe));
        recipe.fillInputs(4);

        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(0));
        offsets.y().addSlot(false);
        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(0));
        offsets.y().addSlot(false);
        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(0));
        offsets.x().addSlot();

        offsets.x().addArrow();

        if (recipe.hasOutputs()) {
            JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.OUTPUT, offsets.getX() + 5, offsets.getY() + 5), recipe.outputs().get(0));
        }

        if (recipe.hasEnergy()) {
            offsets.x().add(recipe.hasOutputs() ? TextureUtils.LARGE_SLOT.size() : TextureUtils.ENERGY.width()).addPadding();
        }

        offsets.x().addArrow();
        offsets.y().subtract(TextureUtils.SLOT.size() * 2);

        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(1));
        offsets.y().addSlot(false);
        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(2));
        offsets.y().addSlot(false);
        JeiIntegration.RECIPE_INTERPRETER.addIngredients(builder.addSlot(RecipeIngredientRole.INPUT, offsets.getX() + 1, offsets.getY() + 1), recipe.inputs().get(3));
    }

    @Override
    public void draw(SlimefunRecipe recipe, IRecipeSlotsView recipeSlotsView, DrawContext graphics, double mouseX, double mouseY) {
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe), calculateYOffset(this.slimefunRecipeCategory, recipe));
        recipe.fillInputs(4);

        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
        offsets.x().addSlot();

        addFillingArrow(graphics, offsets.getX(), offsets.getY(), recipe.sfTicks(), false);
        offsets.x().addArrow();

        if (recipe.hasOutputs()) {
            TextureUtils.LARGE_SLOT.draw(graphics, offsets.getX(), offsets.getY());
        }

        if (recipe.hasEnergy()) {
            addEnergy(graphics, offsets.getX() + (recipe.hasOutputs() ? (TextureUtils.LARGE_SLOT.size() - TextureUtils.ENERGY.width()) / 2 : 0), offsets.getY() + (recipe.hasOutputs() ? - TextureUtils.ENERGY.height() - TextureUtils.PADDING : TextureUtils.PADDING), recipe.energy() < 0);
            offsets.x().add(recipe.hasOutputs() ? TextureUtils.LARGE_SLOT.size() : TextureUtils.ENERGY.width()).addPadding();
        }

        addFillingArrow(graphics, offsets.getX(), offsets.getY(), recipe.sfTicks(), true);
        offsets.x().addArrow();
        offsets.y().subtract(TextureUtils.SLOT.size() * 2);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
        offsets.y().addSlot(false);
        TextureUtils.SLOT.draw(graphics, offsets.getX(), offsets.getY());
    }

    @NotNull
    @Override
    public List<Text> getTooltipStrings(SlimefunRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        final List<Text> tooltips = new ArrayList<>();
        final OffsetBuilder offsets = new OffsetBuilder(this, recipe, calculateXOffset(this.slimefunRecipeCategory, recipe), calculateYOffset(this.slimefunRecipeCategory, recipe));

        offsets.y().addSlot(false).addSlot(false);
        offsets.x().addSlot();

        if (tooltipActive(mouseX, mouseY, offsets.getX(), offsets.getY(), TextureUtils.ARROW)) {
            tooltips.add(timeTooltip(recipe));
        }
        offsets.x().addArrow();

        if (recipe.hasEnergy()) {
            if (tooltipActive(mouseX, mouseY, offsets.getX() + (recipe.hasOutputs() ? (TextureUtils.LARGE_SLOT.size() - TextureUtils.ENERGY.width()) / 2 : 0), offsets.getY() + (recipe.hasOutputs() ? - TextureUtils.ENERGY.height() - TextureUtils.PADDING : TextureUtils.PADDING), TextureUtils.ENERGY)) {
                tooltips.add(energyTooltip(recipe));
            }
            offsets.x().add(recipe.hasOutputs() ? TextureUtils.LARGE_SLOT.size() : TextureUtils.ENERGY.width()).addPadding();
        }

        if (tooltipActive(mouseX, mouseY, offsets.getX(), offsets.getY(), TextureUtils.ARROW)) {
            tooltips.add(timeTooltip(recipe));
        }
        return tooltips;
    }
}
