package flaxbeard.steamcraft.integration.tinkers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.library.tools.ToolCore;
import tconstruct.modifiers.tools.ModBoolean;

import java.util.Arrays;

public class ModiferSteam extends ModBoolean {
    public int modifiersRequired = 1; // LALALALA totally not hidden
                                      // IguanaTweaks Support LALALALA

    public ModiferSteam(ItemStack[] tank) {
        super(tank, 18, "Steam", "\u00a7e", "");
    }

    @Override
    protected boolean canModify(ItemStack tool, ItemStack[] input) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");

        if (tags.getInteger("Modifiers") > 0 && !tags.getBoolean(key))
            return false; // Will fail if the modifier is false or the tag
                          // doesn't exist
        if (Arrays.asList(((ToolCore) tool.getItem()).getTraits()).contains("ammo"))
            return false;
        return true;
    }

    @Override
    public void modify(ItemStack[] input, ItemStack tool) {
        NBTTagCompound tags = tool.getTagCompound();

        if (!tags.hasKey(key)) {
            int modifiers = tags.getCompoundTag("InfiTool").getInteger("Modifiers");
            modifiers -= modifiersRequired;
            tags.getCompoundTag("InfiTool").setInteger("Modifiers", modifiers);
            addModifierTip(tool, "\u00a7eSteam");
        }

        tags.getCompoundTag("InfiTool").setBoolean(key, true);
    }
}