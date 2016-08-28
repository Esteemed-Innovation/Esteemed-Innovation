package eiteam.esteemedinnovation.api;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.commons.lang3.tuple.MutablePair;
import eiteam.esteemedinnovation.item.tool.steam.ItemDrillHeadUpgrade;
import eiteam.esteemedinnovation.misc.DrillHeadMaterial;
import eiteam.esteemedinnovation.misc.OreDictHelper;

import java.util.ArrayList;
import java.util.List;
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
            Item item = stack.getItem();
            int meta = stack.getItemDamage();

            if (OreDictHelper.thinIronPlates.contains(MutablePair.of(stack.getItem(), stack.getItemDamage()))) {
                numIronPlates += 1;
                continue;
            }

            for (Map.Entry<String, MutablePair<List<ItemStack>, List<ItemStack>>> entry : DrillHeadMaterial.materialOres.entrySet()) {
                String material = entry.getKey();
                List<ItemStack> ingots = entry.getValue().left;
                List<ItemStack> nuggets = entry.getValue().right;
                for (ItemStack ingot : ingots) {
                    if (ingot.getItem() == item && ingot.getItemDamage() == meta) {
                        numIngots += 1;
                        materialSafetyNet.add(material);
                    }
                }
                for (ItemStack nugget : nuggets) {
                    if (nugget.getItem() == item && nugget.getItemDamage() == meta) {
                        numNuggets += 1;
                        materialSafetyNet.add(material);
                    }
                }
            }

            for (Map.Entry<String, List<ItemStack>> entry : DrillHeadMaterial.nonStandardMaterials.entrySet()) {
                String material = entry.getKey();
                List<ItemStack> list = entry.getValue();
                for (ItemStack other : list) {
                    if (other.getItem() == item && other.getItemDamage() == meta) {
                        numOthers += 1;
                        materialSafetyNet.add(material);
                    }
                }
            }
        }

        if (!isNetSafe(materialSafetyNet) || numIronPlates != 3) {
            return null;
        }

        String mat = materialSafetyNet.get(0);

        if ((numOthers == 4 && numNuggets == 0 && numIngots == 0) ||
          (numOthers == 0 && numNuggets == 1 && numIngots == 3)) {
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
