package flaxbeard.steamcraft.init.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.block.BlockSteamcraftCrucible;
import flaxbeard.steamcraft.init.items.MetalItems;
import flaxbeard.steamcraft.init.items.armor.ArmorItems;
import flaxbeard.steamcraft.init.items.tools.ToolItems;

import static flaxbeard.steamcraft.init.misc.OreDictEntries.*;

public class DefaultCrucibleLiquids extends MiscellaneousCategory {
    public enum Liquids {
        IRON_LIQUID(new CrucibleLiquid("iron", new ItemStack(Items.IRON_INGOT), MetalItems.Items.IRON_PLATE.createItemStack(), MetalItems.Items.IRON_NUGGET.createItemStack(), null, 200, 200, 200)),
        GOLD_LIQUID(new CrucibleLiquid("gold", new ItemStack(Items.GOLD_INGOT), MetalItems.Items.GOLD_PLATE.createItemStack(), new ItemStack(Items.GOLD_NUGGET), null, 220, 157, 11)),
        ZINC_LIQUID(new CrucibleLiquid("zinc", MetalItems.Items.ZINC_INGOT.createItemStack(), MetalItems.Items.ZINC_PLATE.createItemStack(), MetalItems.Items.ZINC_NUGGET.createItemStack(), null, 225, 225, 225)),
        COPPER_LIQUID(new CrucibleLiquid("copper", MetalItems.Items.COPPER_INGOT.createItemStack(), MetalItems.Items.COPPER_PLATE.createItemStack(), MetalItems.Items.COPPER_NUGGET.createItemStack(), null, 140, 66, 12)),
        BRASS_LIQUID(new CrucibleLiquid("brass", MetalItems.Items.BRASS_INGOT.createItemStack(), MetalItems.Items.BRASS_PLATE.createItemStack(), MetalItems.Items.BRASS_NUGGET.createItemStack(), new CrucibleFormula(ZINC_LIQUID.getLiquid(), 1, COPPER_LIQUID.getLiquid(), 3, 4), 242, 191, 66)),
        LEAD_LIQUID(new CrucibleLiquid("lead", findFirstOre(INGOT_LEAD), findFirstOre(PLATE_LEAD), findFirstOre(NUGGET_LEAD), null, 118, 128, 157));

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
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide() == Side.CLIENT) {
            Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(BlockSteamcraftCrucible.LIQUID_ICON);
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        for (Liquids liquid : Liquids.values()) {
            SteamcraftRegistry.registerLiquid(liquid.getLiquid());
        }

        CrucibleLiquid liquidGold = Liquids.GOLD_LIQUID.getLiquid();
        CrucibleLiquid liquidIron = Liquids.IRON_LIQUID.getLiquid();
        CrucibleLiquid liquidZinc = Liquids.ZINC_LIQUID.getLiquid();
        CrucibleLiquid liquidCopper = Liquids.COPPER_LIQUID.getLiquid();
        CrucibleLiquid liquidBrass = Liquids.BRASS_LIQUID.getLiquid();
        CrucibleLiquid liquidLead = Liquids.LEAD_LIQUID.getLiquid();

        SteamcraftRegistry.registerMeltRecipeOreDict(INGOT_GOLD, liquidGold, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(INGOT_IRON, liquidIron, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(INGOT_ZINC, liquidZinc, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(INGOT_COPPER, liquidCopper, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(INGOT_BRASS, liquidBrass, 9);

        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_TINY_GOLD, liquidGold, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_TINY_IRON, liquidIron, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_TINY_ZINC, liquidZinc, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_TINY_COPPER, liquidCopper, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_TINY_BRASS, liquidBrass, 1);

        SteamcraftRegistry.registerMeltRecipeOreDict(PLATE_GOLD, liquidGold, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict(PLATE_IRON, liquidIron, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict(PLATE_ZINC, liquidZinc, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict(PLATE_COPPER, liquidCopper, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict(PLATE_BRASS, liquidBrass, 6);

        SteamcraftRegistry.registerMeltRecipeOreDict(NUGGET_GOLD, liquidGold, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(NUGGET_IRON, liquidIron, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(NUGGET_ZINC, liquidZinc, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(NUGGET_COPPER, liquidCopper, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict(NUGGET_BRASS, liquidBrass, 1);
//        SteamcraftRegistry.registerMeltRecipe(Items.gold_nugget, liquidGold, 1);

        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_SWORD, liquidIron, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_PICKAXE, liquidIron, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_AXE, liquidIron, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_HOE, liquidIron, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_SHOVEL, liquidIron, 9);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_BOOTS, liquidIron, 36);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_CHESTPLATE, liquidIron, 72);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_HELMET, liquidIron, 45);
        SteamcraftRegistry.registerMeltRecipeTool(Items.IRON_LEGGINGS, liquidIron, 63);

        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_SWORD, liquidGold, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_PICKAXE, liquidGold, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_AXE, liquidGold, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_HOE, liquidGold, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_SHOVEL, liquidGold, 9);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_BOOTS, liquidGold, 36);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_CHESTPLATE, liquidGold, 72);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_HELMET, liquidGold, 45);
        SteamcraftRegistry.registerMeltRecipeTool(Items.GOLDEN_LEGGINGS, liquidGold, 63);

        SteamcraftRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_SWORD.getItem(), liquidBrass, 18);
        SteamcraftRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_PICKAXE.getItem(), liquidBrass, 27);
        SteamcraftRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_AXE.getItem(), liquidBrass, 27);
        SteamcraftRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_HOE.getItem(), liquidBrass, 18);
        SteamcraftRegistry.registerMeltRecipeTool(ToolItems.Items.BRASS_SHOVEL.getItem(), liquidBrass, 9);
        SteamcraftRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_BOOTS.getItem(), liquidBrass, 36);
        SteamcraftRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_CHESTPLATE.getItem(), liquidBrass, 72);
        SteamcraftRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_HELMET.getItem(), liquidBrass, 45);
        SteamcraftRegistry.registerMeltRecipeTool(ArmorItems.Items.BRASS_LEGGINGS.getItem(), liquidBrass, 63);

        SteamcraftRegistry.registerOreDictDunkRecipe(INGOT_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_INGOT.createItemStack());
        SteamcraftRegistry.registerOreDictDunkRecipe(PLATE_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_PLATE.createItemStack());
        SteamcraftRegistry.registerOreDictDunkRecipe(NUGGET_IRON, liquidGold, 1, MetalItems.Items.GILDED_IRON_NUGGET.createItemStack());

        SteamcraftRegistry.registerMeltRecipeOreDict("ingotLead", liquidLead, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetLead", liquidLead, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftLead", liquidLead, 6);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_GOLD, Liquids.GOLD_LIQUID.getLiquid(), 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_IRON, Liquids.IRON_LIQUID.getLiquid(), 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_ZINC, Liquids.ZINC_LIQUID.getLiquid(), 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_COPPER, Liquids.COPPER_LIQUID.getLiquid(), 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_BRASS, Liquids.BRASS_LIQUID.getLiquid(), 9);
        SteamcraftRegistry.registerMeltRecipeOreDict(DUST_LEAD, Liquids.LEAD_LIQUID.getLiquid(), 9);
    }
}
