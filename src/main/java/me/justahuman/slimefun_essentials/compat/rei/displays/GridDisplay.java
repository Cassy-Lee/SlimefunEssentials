package me.justahuman.slimefun_essentials.compat.rei.displays;

import me.justahuman.slimefun_essentials.api.OffsetBuilder;
import me.justahuman.slimefun_essentials.client.SlimefunRecipeCategory;
import me.justahuman.slimefun_essentials.client.SlimefunRecipe;
import me.justahuman.slimefun_essentials.compat.rei.ReiUtils;
import me.justahuman.slimefun_essentials.utils.TextureUtils;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import net.minecraft.item.ItemStack;

import java.util.List;

public class GridDisplay extends ProcessDisplay {
    protected final int side;
    public GridDisplay(SlimefunRecipeCategory slimefunRecipeCategory, SlimefunRecipe slimefunRecipe, int side) {
        super(slimefunRecipeCategory, slimefunRecipe);

        this.side = side;
        ReiUtils.fillEntries(this.inputs, side * side);
    }

    @Override
    public List<Widget> setupDisplay(OffsetBuilder offsets, List<Widget> widgets, Rectangle bounds, ItemStack icon) {
        offsets.setY(calculateYOffset(slimefunRecipe, TextureUtils.SLOT.size(getDrawMode()) * 3) + offsets.minY());

        // Display Energy
        addEnergyWithCheck(widgets, offsets);

        int i = 0;
        for (int y = 1; y <= this.side; y++) {
            for (int x = 1; x <= this.side; x++) {
                addSlot(widgets, this.inputs.get(i), offsets.getX(), offsets.getY());
                offsets.x().addSlot(false);
                i++;
            }
            offsets.x().subtract(TextureUtils.SLOT.size(getDrawMode()) * this.side);
            offsets.y().addSlot(false);
        }
        offsets.x().add(TextureUtils.SLOT.size(getDrawMode()) * this.side).addPadding();

        // Display Arrow
        addArrow(widgets, offsets);

        // Display Outputs
        addOutputsOrEnergy(widgets, offsets);
        return widgets;
    }
}
