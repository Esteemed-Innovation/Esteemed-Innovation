package flaxbeard.steamcraft.common.item;

import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.ICrucibleMold;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemIngotMold extends Item implements ICrucibleMold {

    private ResourceLocation icon = new ResourceLocation("steamcraft:textures/models/moldIngot.png");

    public ItemIngotMold() {
        this.maxStackSize = 1;
    }

    @Override
    public ResourceLocation getBlockTexture() {
        return icon;
    }

    @Override
    public boolean canUseOn(CrucibleLiquid liquid) {
        return (liquid.ingot != null);
    }

    @Override
    public ItemStack getItemFromLiquid(CrucibleLiquid liquid) {
        ItemStack newStack = liquid.ingot.copy();
        newStack.stackSize = 1;
        return newStack;
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid) {
        return 9;
    }
}
