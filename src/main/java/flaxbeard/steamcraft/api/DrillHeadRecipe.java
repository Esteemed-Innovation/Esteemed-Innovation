package flaxbeard.steamcraft.api;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.MutablePair;
import flaxbeard.steamcraft.item.tool.steam.ItemDrillHeadUpgrade;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;
import flaxbeard.steamcraft.misc.OreDictHelper;

import java.util.ArrayList;
import java.util.Map;

public class DrillHeadRecipe extends ShapedOreRecipe {
    public DrillHeadRecipe(Item result, Object... recipe) {
        super(result, recipe);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack result = super.getCraftingResult(inv);
        ItemDrillHeadUpgrade upgrade = (ItemDrillHeadUpgrade) result.getItem();
        int numIronPlates = 0;
        int numNuggets = 0;
        int numIngots = 0;
        int numOthers = 0;

        ArrayList<String> materialSafetyNet = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null || stack.getItem() == null) {
                continue;
            }
            FMLLog.info(stack.toString());

            if (OreDictHelper.plateSteamcraftIrons.contains(MutablePair.of(stack.getItem(), stack.getItemDamage()))) {
                numIronPlates += 1;
                continue;
            }

            for (Map.Entry<String, MutablePair<ArrayList<ItemStack>, ArrayList<ItemStack>>> entry : DrillHeadMaterial.materialOres.entrySet()) {
                String material = entry.getKey();
                ArrayList<ItemStack> ingots = entry.getValue().left;
                ArrayList<ItemStack> nuggets = entry.getValue().right;
                for (ItemStack ingot : ingots) {
                    if (ingot.getItem() == stack.getItem()) {
                        numIngots += 1;
                        materialSafetyNet.add(material);
                    }
                }
                for (ItemStack nugget : nuggets) {
                    if (nugget.getItem() == stack.getItem()) {
                        numNuggets += 1;
                        materialSafetyNet.add(material);
                    }
                }
            }

            for (Map.Entry<String, ArrayList<ItemStack>> entry : DrillHeadMaterial.nonStandardMaterials.entrySet()) {
                String material = entry.getKey();
                ArrayList<ItemStack> list = entry.getValue();
                for (ItemStack other : list) {
                    if (other.getItem() == stack.getItem()) {
                        numOthers += 1;
                        materialSafetyNet.add(material);
                    }
                }
            }
        }

        FMLLog.info("%s plates, %s others, %s nuggets, and %s ingots", numIronPlates, numOthers, numNuggets, numIngots);

        if (!isNetSafe(materialSafetyNet) || numIronPlates != 3) {
            return null;
        }

        String mat = materialSafetyNet.get(0);

        if ((numOthers == 4 && numNuggets == 0 && numIngots == 0) ||
          (numOthers == 0 && numNuggets == 1 && numIngots == 3)) {
//            NBTTagCompound nbt = new NBTTagCompound();
//            nbt.setString("material", mat);
//            result.setTagCompound(nbt);
            upgrade.setMyMaterial(result, mat);
            return result;
        }
        return null;
    }

    /**
     * Checks whether every value in the safety net is equal to the first one.
     * @param ary The ArrayList safety net.
     * @return Whether it is all equal strings or not.
     */
    private boolean isNetSafe(ArrayList<String> ary) {
        for (String s : ary) {
            if (!s.equals(ary.get(0))) {
                return false;
            }
        }
        return true;
    }
}
