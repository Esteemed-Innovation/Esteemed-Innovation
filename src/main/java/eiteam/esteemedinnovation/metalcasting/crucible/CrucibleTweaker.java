package eiteam.esteemedinnovation.metalcasting.crucible;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;

import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".Crucible")
public class CrucibleTweaker {
    @ZenMethod
    public static void addBasicLiquid(String name, int r, int g, int b) {
        CrucibleLiquid liquid = new CrucibleLiquid(name, r, g, b);
        CraftTweakerAPI.apply(new AddLiquid(liquid));
    }

    @ZenMethod
    public static void addAlloyLiquid(String name, String liquid1, int amount1, String liquid2, int amount2, int amountOut, int r, int g, int b) {
        CrucibleLiquid crucibleLiquid1 = CrucibleRegistry.getLiquidFromName(liquid1);
        CrucibleLiquid crucibleLiquid2 = CrucibleRegistry.getLiquidFromName(liquid2);

        if (crucibleLiquid1 != null && crucibleLiquid2 != null) {
            CrucibleLiquid out = new CrucibleLiquid(name, r, g, b);
            CrucibleFormula formula = new CrucibleFormula(out, amountOut, crucibleLiquid1, amount1, crucibleLiquid2, amount2);
            CraftTweakerAPI.apply(new AddLiquid(out, formula));
        } else {
            EsteemedInnovation.logger.warn("[EI-MT] One of your liquids is null: " + liquid1 + ", " + liquid2);
        }
    }

    private static class AddLiquid implements IAction {
        private final CrucibleLiquid liquid;
        private final CrucibleFormula formula;

        AddLiquid(CrucibleLiquid liquid, CrucibleFormula formula) {
            this.liquid = liquid;
            this.formula = formula;
        }

        AddLiquid(CrucibleLiquid liquid) {
            this(liquid, null);
        }

        @Override
        public void apply() {
            CrucibleRegistry.registerLiquid(liquid);
            if (formula != null) {
                CrucibleRegistry.registerFormula(formula);
            }
        }

        @Override
        public String describe() {
            return "Adding CrucibleLiquid " + liquid.getName();
        }

    }

    @ZenMethod
    public static void removeLiquid(String name) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(name);
        if (liquid != null) {
            CraftTweakerAPI.apply(new RemoveLiquid(liquid));
        } else {
            EsteemedInnovation.logger.warn("[EI-MT] Could not remove non-existant liquid " + name);
        }
    }

    private static class RemoveLiquid implements IAction {
        private final CrucibleLiquid liquid;

        RemoveLiquid(CrucibleLiquid liquid) {
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            CrucibleRegistry.removeLiquid(liquid);
        }

        @Override
        public String describe() {
            return "Removing CrucibleLiquid " + liquid.getName();
        }

    }

    @ZenMethod
    public static void addMeltRecipe(IItemStack item, String liquidName, int amountOut) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        ItemStack stack = CraftTweakerMC.getItemStack(item);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new AddMeltRecipe(stack, liquid, amountOut));
        }
    }

    @ZenMethod
    public static void addOreMeltRecipe(String dict, String liquidName, int amountOut) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new AddMeltRecipe(dict, liquid, amountOut));
        }
    }

    @ZenMethod
    public static void addToolMeltRecipe(IItemStack itemstack, String liquidName, int amountOut) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        Item item = CraftTweakerMC.getItemStack(itemstack).getItem();
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new AddMeltRecipe(item, liquid, amountOut));
        }
    }

    private static class AddMeltRecipe implements IAction {
        private final Object in;
        private final CrucibleLiquid liquid;
        private final int amountOut;

        AddMeltRecipe(Object in, CrucibleLiquid liquid, int amountOut) {
            this.in = in;
            this.liquid = liquid;
            this.amountOut = amountOut;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                CrucibleRegistry.registerMeltRecipe(item, meta, liquid, amountOut);
            } else if (in instanceof Item) {
                CrucibleRegistry.registerMeltRecipeTool((Item) in, liquid, amountOut);
            } else if (in instanceof String) {
                CrucibleRegistry.registerMeltRecipeOreDict((String) in, liquid, amountOut);
            }
        }

        @Override
        public String describe() {
            return "Adding Crucible melt recipe for " + liquid.getName();
        }
    }

    @ZenMethod
    public static void removeMeltRecipe(IItemStack input, String output) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(output);
        ItemStack stack = CraftTweakerMC.getItemStack(input);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + output);
        } else {
            CraftTweakerAPI.apply(new RemoveMeltRecipe(stack, liquid));
        }
    }

    @ZenMethod
    public static void removeOreMeltRecipe(String dict, String liquidName) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new RemoveMeltRecipe(dict, liquid));
        }
    }

    @ZenMethod
    public static void removeToolMeltRecipe(IItemStack itemstack, String liquidName) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        Item item = CraftTweakerMC.getItemStack(itemstack).getItem();
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new RemoveMeltRecipe(item, liquid));
        }
    }

    private static class RemoveMeltRecipe implements IAction {
        private final Object in;
        private final CrucibleLiquid liquid;

        public RemoveMeltRecipe(Object in, CrucibleLiquid liquid) {
            this.in = in;
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                CrucibleRegistry.removeMeltRecipe(item, meta, liquid);
            } else if (in instanceof Item) {
                CrucibleRegistry.removeMeltRecipeTool((Item) in, liquid);
            } else if (in instanceof String) {
                CrucibleRegistry.removeMeltRecipeOreDict((String) in, liquid);
            }
        }

        @Override
        public String describe() {
            return "Removing melting recipe for " + liquid.getName();
        }
    }

    @ZenMethod
    public static void addDunkRecipe(IItemStack in, String inLiq, int liquidAmount, IItemStack out) {
        ItemStack inStack = CraftTweakerMC.getItemStack(in);
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(inLiq);
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + inLiq);
        } else {
            CraftTweakerAPI.apply(new AddDunkRecipe(inStack, liquid, liquidAmount, outStack));
        }
    }

    @ZenMethod
    public static void addOreDunkRecipe(String dict, String liq, int amount, IItemStack out) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liq);
        ItemStack outStack = CraftTweakerMC.getItemStack(out);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liq);
        } else {
            CraftTweakerAPI.apply(new AddDunkRecipe(dict, liquid, amount, outStack));
        }
    }

    private static class AddDunkRecipe implements IAction {
        private final Object in;
        private final CrucibleLiquid liquid;
        private final int liquidAmount;
        private final ItemStack out;

        public AddDunkRecipe(Object in, CrucibleLiquid liquid, int liquidAmount, ItemStack out) {
            this.in = in;
            this.liquid = liquid;
            this.liquidAmount = liquidAmount;
            this.out = out;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                CrucibleRegistry.registerDunkRecipe(item, meta, liquid, liquidAmount, out);
            } else if (in instanceof String) {
                CrucibleRegistry.registerOreDictDunkRecipe((String) in, liquid, liquidAmount, out);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Adding dunk recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.getName();
            } else if (in instanceof String) {
                return "Adding dunk recipe for " + in + " and " + liquid.getName();
            }
            return null;
        }
    }

    @ZenMethod
    public static void removeDunkRecipe(IItemStack in, String liquidName) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        ItemStack stack = CraftTweakerMC.getItemStack(in);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new RemoveDunkRecipe(stack, liquid));
        }
    }

    @ZenMethod
    public static void removeOreDunkRecipe(String dict, String liquidName) {
        CrucibleLiquid liquid = CrucibleRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            EsteemedInnovation.logger.warn("[EI-MT] Could not find liquid " + liquidName);
        } else {
            CraftTweakerAPI.apply(new RemoveDunkRecipe(dict, liquid));
        }
    }

    private static class RemoveDunkRecipe implements IAction {
        private final Object in;
        private final CrucibleLiquid liquid;

        public RemoveDunkRecipe(Object in, CrucibleLiquid liquid) {
            this.in = in;
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                CrucibleRegistry.removeDunkRecipe(item, meta, liquid);
            } else if (in instanceof String) {
                CrucibleRegistry.removeOreDictDunkRecipe((String) in, liquid);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Removing dunking recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.getName();
            } else if (in instanceof String) {
                return "Removing dunking recipe for " + in + " and " + liquid.getName();
            }
            return null;
        }

    }
}
