package me.justahuman.slimefun_essentials.compat.emi.recipes;

import dev.emi.emi.EmiPort;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import me.justahuman.slimefun_essentials.compat.emi.EmiUtils;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.justahuman.slimefun_essentials.utils.Utils;
import net.minecraft.client.gui.tooltip.TooltipComponent;

import java.util.List;

public class GridRecipe extends ProcessRecipe {
    protected final int side;
    public GridRecipe(EmiRecipeCategory category, List<EmiIngredient> inputs, List<EmiStack> outputs, Integer energy, Integer time, Integer speed, String type) {
        super(category, inputs, outputs, null, energy, time, speed);
        
        int length;
        try {
            length = Integer.parseInt(type.substring(type.length() - 1));
        } catch (NumberFormatException ignored) {
            length = 3;
        }
        side = length;
        Utils.fillInputs(inputs, side * side);
    }
    
    @Override
    public int getWidgetsWidth() {
        return (side * TextureUtils.slot + TextureUtils.padding) + (hasEnergy() ? TextureUtils.chargeWidth + TextureUtils.padding : 0) + (TextureUtils.arrowWidth + TextureUtils.padding) + (hasOutputs() ? TextureUtils.bigSlot * this.outputs.size() : 0);
    }
    
    @Override
    public int getWidgetsHeight() {
        return (side * TextureUtils.slot);
    }
    
    @Override
    public void addWidgets(WidgetHolder widgets) {
        int offsetX = TextureUtils.padding;
        int offsetY = TextureUtils.padding;
        final int offsetYCharge = (getDisplayHeight() - TextureUtils.chargeHeight) / 2;
        final int offsetYArrow = (getDisplayHeight() - TextureUtils.arrowHeight) / 2;
        final int offsetYBig = (getDisplayHeight() - TextureUtils.bigSlot) / 2;
    
        // Display Energy
        if (hasEnergy() && hasOutputs()) {
            addEnergyDisplay(widgets, offsetX, offsetYCharge);
            offsetX += TextureUtils.chargeWidth + TextureUtils.padding;
        }
        
        int i = 0;
        for (int y = 1; y <= side; y++) {
            for (int x = 1; x <= side; x++) {
                widgets.addSlot(inputs.get(i), offsetX, offsetY);
                offsetX += TextureUtils.slot;
                i++;
            }
            offsetX = TextureUtils.padding;
            offsetY += TextureUtils.slot;
        }
        offsetX += TextureUtils.slot * side + TextureUtils.padding;
    
        // Display Time
        if (hasTime()) {
            final int sfTicks = Math.max(1, this.time / 10 / (hasSpeed() ? this.speed : 1));
            final int millis =  sfTicks * 500;
            widgets.addFillingArrow(offsetX, offsetYArrow, millis).tooltip((mx, my) -> List.of(TooltipComponent.of(EmiPort.ordered(EmiPort.translatable("slimefun_essentials.recipe.time", EmiUtils.numberFormat.format(sfTicks / 2f), EmiUtils.numberFormat.format(sfTicks * 10))))));
        } else {
            widgets.addTexture(EmiTexture.EMPTY_ARROW, offsetX, offsetYArrow);
        }
        offsetX += TextureUtils.arrowWidth + TextureUtils.padding;
    
        // Display Outputs
        if (hasOutputs()) {
            for (EmiStack output : this.outputs) {
                widgets.addSlot(output, offsetX, offsetYBig).output(true);
                offsetX += TextureUtils.bigSlot + TextureUtils.padding;
            }
        } else if (hasEnergy()) {
            addEnergyDisplay(widgets, offsetX, offsetYCharge);
        }
    }
}
