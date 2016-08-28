package eiteam.esteemedinnovation.init.misc.integration.crafttweaker;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.CrucibleFormula;
import eiteam.esteemedinnovation.api.CrucibleLiquid;
import eiteam.esteemedinnovation.api.GeneralRegistry;

import minetweaker.IUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods." + EsteemedInnovation.MOD_ID + ".Crucible")
public class CrucibleTweaker {
    @ZenMethod
    public static void addBasicLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, int r, int g, int b) {
        ItemStack iStack = MineTweakerMC.getItemStack(ingot);
        ItemStack pStack = MineTweakerMC.getItemStack(plate);
        ItemStack nStack = MineTweakerMC.getItemStack(nugget);
        CrucibleLiquid liquid = new CrucibleLiquid(name, iStack, pStack, nStack, null, r, g, b);
        MineTweakerAPI.apply(new AddLiquid(liquid));
    }

    @ZenMethod
    public static void addAlloyLiquid(String name, IItemStack ingot, IItemStack plate, IItemStack nugget, String liquid1, int amount1, String liquid2, int amount2, int amountOut, int r, int g, int b) {
        ItemStack iStack = MineTweakerMC.getItemStack(ingot);
        ItemStack pStack = MineTweakerMC.getItemStack(plate);
        ItemStack nStack = MineTweakerMC.getItemStack(nugget);
        CrucibleLiquid crucibleLiquid1 = GeneralRegistry.getLiquidFromName(liquid1);
        CrucibleLiquid crucibleLiquid2 = GeneralRegistry.getLiquidFromName(liquid2);

        if (crucibleLiquid1 != null && crucibleLiquid2 != null) {
            CrucibleFormula formula = new CrucibleFormula(crucibleLiquid1, amount1, crucibleLiquid2, amount2, amountOut);
            CrucibleLiquid out = new CrucibleLiquid(name, iStack, pStack, nStack, formula, r, g, b);
            MineTweakerAPI.apply(new AddLiquid(out));
        } else {
            FMLLog.warning("[EI-MT] One of your liquids is null: " + liquid1 + ", " + liquid2);
        }
    }

    private static class AddLiquid implements IUndoableAction {
        private final CrucibleLiquid liquid;
        public AddLiquid(CrucibleLiquid liquid) {
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            GeneralRegistry.registerLiquid(liquid);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GeneralRegistry.removeLiquid(liquid);
        }

        @Override
        public String describe() {
            return "Adding CrucibleLiquid " + liquid.name;
        }

        @Override
        public String describeUndo() {
            return "Removing CrucibleLiquid " + liquid.name;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeLiquid(String name) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(name);
        if (liquid != null) {
            MineTweakerAPI.apply(new RemoveLiquid(liquid));
        } else {
            FMLLog.warning("[EI-MT] Could not remove non-existant liquid " + name);
        }
    }

    private static class RemoveLiquid implements IUndoableAction {
        private final CrucibleLiquid liquid;

        public RemoveLiquid(CrucibleLiquid liquid) {
            this.liquid = liquid;
        }

        @Override
        public void apply() {
            GeneralRegistry.removeLiquid(liquid);
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            GeneralRegistry.registerLiquid(liquid);
        }

        @Override
        public String describe() {
            return "Removing CrucibleLiquid " + liquid.name;
        }

        @Override
        public String describeUndo() {
            return "Adding CrucibleLiquid " + liquid.name;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void addMeltRecipe(IItemStack item, String liquidName, int amountOut) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        ItemStack stack = MineTweakerMC.getItemStack(item);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new AddMeltRecipe(stack, liquid, amountOut));
        }
    }

    @ZenMethod
    public static void addOreMeltRecipe(String dict, String liquidName, int amountOut) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new AddMeltRecipe(dict, liquid, amountOut));
        }
    }

    @ZenMethod
    public static void addToolMeltRecipe(IItemStack itemstack, String liquidName, int amountOut) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        Item item = MineTweakerMC.getItemStack(itemstack).getItem();
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new AddMeltRecipe(item, liquid, amountOut));
        }
    }

    private static class AddMeltRecipe implements IUndoableAction {
        private final Object in;
        private final CrucibleLiquid liquid;
        private final int amountOut;

        public AddMeltRecipe(Object in, CrucibleLiquid liquid, int amountOut) {
            this.in = in;
            this.liquid = liquid;
            this.amountOut = amountOut;
        }

        @Override
        public void apply() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                GeneralRegistry.registerMeltRecipe(item, meta, liquid, amountOut);
            } else if (in instanceof Item) {
                GeneralRegistry.registerMeltRecipeTool((Item) in, liquid, amountOut);
            } else if (in instanceof String) {
                GeneralRegistry.registerMeltRecipeOreDict((String) in, liquid, amountOut);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                GeneralRegistry.removeMeltRecipe(item, meta, liquid);
            } else if (in instanceof Item) {
                GeneralRegistry.removeMeltRecipeTool((Item) in, liquid);
            } else if (in instanceof String) {
                GeneralRegistry.removeMeltRecipeOreDict((String) in, liquid);
            }
        }

        @Override
        public String describe() {
            return "Adding Crucible melt recipe for " + liquid.name;
        }

        @Override
        public String describeUndo() {
            return "Removing Crucible melt recipe for " + liquid.name;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeMeltRecipe(IItemStack input, String output) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(output);
        ItemStack stack = MineTweakerMC.getItemStack(input);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + output);
        } else {
            MineTweakerAPI.apply(new RemoveMeltRecipe(stack, liquid));
        }
    }

    @ZenMethod
    public static void removeOreMeltRecipe(String dict, String liquidName) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new RemoveMeltRecipe(dict, liquid));
        }
    }

    @ZenMethod
    public static void removeToolMeltRecipe(IItemStack itemstack, String liquidName) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        Item item = MineTweakerMC.getItemStack(itemstack).getItem();
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new RemoveMeltRecipe(item, liquid));
        }
    }

    private static class RemoveMeltRecipe implements IUndoableAction {
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
                GeneralRegistry.removeMeltRecipe(item, meta, liquid);
            } else if (in instanceof Item) {
                GeneralRegistry.removeMeltRecipeTool((Item) in, liquid);
            } else if (in instanceof String) {
                GeneralRegistry.removeMeltRecipeOreDict((String) in, liquid);
            }
        }

        @Override
        public boolean canUndo() {
            return false;
        }

        @Override
        public void undo() {
        }

        @Override
        public String describe() {
            return "Removing melting recipe for " + liquid.name;
        }

        @Override
        public String describeUndo() {
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void addDunkRecipe(IItemStack in, String inLiq, int liquidAmount, IItemStack out) {
        ItemStack inStack = MineTweakerMC.getItemStack(in);
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(inLiq);
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + inLiq);
        } else {
            MineTweakerAPI.apply(new AddDunkRecipe(inStack, liquid, liquidAmount, outStack));
        }
    }

    @ZenMethod
    public static void addOreDunkRecipe(String dict, String liq, int amount, IItemStack out) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liq);
        ItemStack outStack = MineTweakerMC.getItemStack(out);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liq);
        } else {
            MineTweakerAPI.apply(new AddDunkRecipe(dict, liquid, amount, outStack));
        }
    }

    private static class AddDunkRecipe implements IUndoableAction {
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
                GeneralRegistry.registerDunkRecipe(item, meta, liquid, liquidAmount, out);
            } else if (in instanceof String) {
                GeneralRegistry.registerOreDictDunkRecipe((String) in, liquid, liquidAmount, out);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                GeneralRegistry.removeDunkRecipe(item, meta, liquid);
            } else if (in instanceof String) {
                GeneralRegistry.removeOreDictDunkRecipe((String) in, liquid);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Adding dunk recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.name;
            } else if (in instanceof String) {
                return "Adding dunk recipe for " + in + " and " + liquid.name;
            }
            return null;
        }

        @Override
        public String describeUndo() {
            if (in instanceof ItemStack) {
                return "Removing dunking recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.name;
            } else if (in instanceof String) {
                return "Removing dunking recipe for " + in + " and " + liquid.name;
            }
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }

    @ZenMethod
    public static void removeDunkRecipe(IItemStack in, String liquidName) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        ItemStack stack = MineTweakerMC.getItemStack(in);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new RemoveDunkRecipe(stack, liquid));
        }
    }

    @ZenMethod
    public static void removeOreDunkRecipe(String dict, String liquidName) {
        CrucibleLiquid liquid = GeneralRegistry.getLiquidFromName(liquidName);
        if (liquid == null) {
            FMLLog.warning("[EI-MT] Could not find liquid " + liquidName);
        } else {
            MineTweakerAPI.apply(new RemoveDunkRecipe(dict, liquid));
        }
    }

    private static class RemoveDunkRecipe implements IUndoableAction {
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
                GeneralRegistry.removeDunkRecipe(item, meta, liquid);
            } else if (in instanceof String) {
                GeneralRegistry.removeOreDictDunkRecipe((String) in, liquid);
            }
        }

        @Override
        public boolean canUndo() {
            return true;
        }

        @Override
        public void undo() {
            if (in instanceof ItemStack) {
                Item item = ((ItemStack) in).getItem();
                int meta = ((ItemStack) in).getItemDamage();
                GeneralRegistry.removeDunkRecipe(item, meta, liquid);
            } else if (in instanceof String) {
                GeneralRegistry.removeOreDictDunkRecipe((String) in, liquid);
            }
        }

        @Override
        public String describe() {
            if (in instanceof ItemStack) {
                return "Removing dunking recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.name;
            } else if (in instanceof String) {
                return "Removing dunking recipe for " + in + " and " + liquid.name;
            }
            return null;
        }

        @Override
        public String describeUndo() {
            if (in instanceof ItemStack) {
                return "Adding dunk recipe for " + ((ItemStack) in).getUnlocalizedName() +
                  " and " + liquid.name;
            } else if (in instanceof String) {
                return "Adding dunk recipe for " + in + " and " + liquid.name;
            }
            return null;
        }

        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
