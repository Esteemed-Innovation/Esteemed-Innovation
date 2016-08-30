package eiteam.esteemedinnovation.init.misc;

import eiteam.esteemedinnovation.api.crucible.CrucibleFormula;
import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.crucible.CrucibleRegistry;
import eiteam.esteemedinnovation.init.items.MetalItems;
import eiteam.esteemedinnovation.init.items.armor.ArmorItems;
import eiteam.esteemedinnovation.init.items.tools.ToolItems;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

import static eiteam.esteemedinnovation.init.misc.OreDictEntries.*;

public class DefaultCrucibleLiquids extends MiscellaneousCategory {
    public enum Liquids {
        IRON_LIQUID(new CrucibleLiquid("iron", new ItemStack(Items.IRON_INGOT), MetalItems.Items.IRON_PLATE.createItemStack(), MetalItems.Items.IRON_NUGGET.createItemStack(), null, 200, 200, 200)),
        GOLD_LIQUID(new CrucibleLiquid("gold", new ItemStack(Items.GOLD_INGOT), MetalItems.Items.GOLD_PLATE.createItemStack(), new ItemStack(Items.GOLD_NUGGET), null, 220, 157, 11)),
        ZINC_LIQUID(new CrucibleLiquid("zinc", MetalItems.Items.ZINC_INGOT.createItemStack(), MetalItems.Items.ZINC_PLATE.createItemStack(), MetalItems.Items.ZINC_NUGGET.createItemStack(), null, 225, 225, 225)),
        COPPER_LIQUID(new CrucibleLiquid("copper", MetalItems.Items.COPPER_INGOT.createItemStack(), MetalItems.Items.COPPER_PLATE.createItemStack(), MetalItems.Items.COPPER_NUGGET.createItemStack(), null, 140, 66, 12)),
        BRASS_LIQUID(new CrucibleLiquid("brass", MetalItems.Items.BRASS_INGOT.createItemStack(), MetalItems.Items.BRASS_PLATE.createItemStack(), MetalItems.Items.BRASS_NUGGET.createItemStack(), new CrucibleFormula(ZINC_LIQUID.getLiquid(), 1, COPPER_LIQUID.getLiquid(), 3, 4), 242, 191, 66)),
        LEAD_LIQUID(new CrucibleLiquid("lead", findFirstOre(INGOT_LEAD), findFirstOre(PLATE_THIN_LEAD), findFirstOre(NUGGET_LEAD), null, 118, 128, 157));

        private CrucibleLiquid liquid;

        Liquids(CrucibleLiquid liquid) {
            this.liquid = liquid;
        }

        public CrucibleLiquid getLiquid() {
            return liquid;
        }
    }

    private static ItemStack findFirstOre(String ore) {
        for (ItemStack stack : OreDictionary.getOres(ore)) {
            if (stack != null) {
                return stack;
            }
        }
        return null;
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (Liquids liquid : Liquids.values()) {
            CrucibleRegistry.registerLiquid(liquid.getLiquid());
        }

        CrucibleLiquid liquidGold = Liquids.GOLD_LIQUID.getLiquid();
        CrucibleLiquid liquidIron = Liquids.IRON_LIQUID.getLiquid();
        CrucibleLiquid liquidZinc = Liquids.ZINC_LIQUID.getLiquid();
        CrucibleLiquid liquidCopper = Liquids.COPPER_LIQUID.getLiquid();
        CrucibleLiquid liquidBrass = Liquids.BRASS_LIQUID.getLiquid();
        CrucibleLiquid liquidLead = Liquids.LEAD_LIQUID.getLiquid();

        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_GOLD, liquidGold, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_IRON, liquidIron, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_ZINC, liquidZinc, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_COPPER, liquidCopper, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_BRASS, liquidBrass, 9);

        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_GOLD, liquidGold, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_IRON, liquidIron, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_ZINC, liquidZinc, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_COPPER, liquidCopper, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_TINY_BRASS, liquidBrass, 1);

        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_GOLD, liquidGold, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_IRON, liquidIron, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_ZINC, liquidZinc, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_COPPER, liquidCopper, 6);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_BRASS, liquidBrass, 6);

        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_GOLD, liquidGold, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_IRON, liquidIron, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_ZINC, liquidZinc, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_COPPER, liquidCopper, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_BRASS, liquidBrass, 1);
//        GeneralRegistry.registerMeltRecipe(Items.gold_nugget, liquidGold, 1);

        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_SWORD, liquidIron, 18);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_PICKAXE, liquidIron, 27);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_AXE, liquidIron, 27);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_HOE, liquidIron, 18);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_SHOVEL, liquidIron, 9);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_BOOTS, liquidIron, 36);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_CHESTPLATE, liquidIron, 72);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_HELMET, liquidIron, 45);
        CrucibleRegistry.registerMeltRecipeTool(Items.IRON_LEGGINGS, liquidIron, 63);

        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_SWORD, liquidGold, 18);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_PICKAXE, liquidGold, 27);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_AXE, liquidGold, 27);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_HOE, liquidGold, 18);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_SHOVEL, liquidGold, 9);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_BOOTS, liquidGold, 36);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_CHESTPLATE, liquidGold, 72);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_HELMET, liquidGold, 45);
        CrucibleRegistry.registerMeltRecipeTool(Items.GOLDEN_LEGGINGS, liquidGold, 63);

        CrucibleRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_SWORD.getItem(), liquidBrass, 18);
        CrucibleRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_PICKAXE.getItem(), liquidBrass, 27);
        CrucibleRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_AXE.getItem(), liquidBrass, 27);
        CrucibleRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_HOE.getItem(), liquidBrass, 18);
        CrucibleRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_SHOVEL.getItem(), liquidBrass, 9);
        CrucibleRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_BOOTS.getItem(), liquidBrass, 36);
        CrucibleRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_CHESTPLATE.getItem(), liquidBrass, 72);
        CrucibleRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_HELMET.getItem(), liquidBrass, 45);
        CrucibleRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_LEGGINGS.getItem(), liquidBrass, 63);

        CrucibleRegistry.registerOreDictDunkRecipe(INGOT_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_INGOT.createItemStack());
        CrucibleRegistry.registerOreDictDunkRecipe(PLATE_THIN_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_PLATE.createItemStack());
        CrucibleRegistry.registerOreDictDunkRecipe(NUGGET_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_NUGGET.createItemStack());

        CrucibleRegistry.registerMeltRecipeOreDict(INGOT_LEAD, liquidLead, 9);
        CrucibleRegistry.registerMeltRecipeOreDict(NUGGET_LEAD, liquidLead, 1);
        CrucibleRegistry.registerMeltRecipeOreDict(PLATE_THIN_LEAD, liquidLead, 6);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_GOLD, Liquids.GOLD_LIQUID.getLiquid(), 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_IRON, Liquids.IRON_LIQUID.getLiquid(), 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_ZINC, Liquids.ZINC_LIQUID.getLiquid(), 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_COPPER, Liquids.COPPER_LIQUID.getLiquid(), 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_BRASS, Liquids.BRASS_LIQUID.getLiquid(), 9);
        CrucibleRegistry.registerMeltRecipeOreDict(DUST_LEAD, Liquids.LEAD_LIQUID.getLiquid(), 9);
    }
}
