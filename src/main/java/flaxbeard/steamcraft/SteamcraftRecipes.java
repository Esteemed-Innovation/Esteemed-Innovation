package flaxbeard.steamcraft;

import cpw.mods.fml.common.registry.GameRegistry;

import flaxbeard.steamcraft.api.CrucibleFormula;
import flaxbeard.steamcraft.api.CrucibleLiquid;
import flaxbeard.steamcraft.api.DrillHeadRecipe;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.book.BookRecipeRegistry;
import flaxbeard.steamcraft.handler.CanisterHandler;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.tile.TileEntitySmasher;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Map;

public class SteamcraftRecipes {
    public static CrucibleLiquid liquidIron;
    public static CrucibleLiquid liquidZinc;
    public static CrucibleLiquid liquidCopper;
    public static CrucibleLiquid liquidGold;
    public static CrucibleLiquid liquidBrass;

    private SteamcraftRecipes() throws InstantiationException{
    	throw new InstantiationException("This class is not to be meant for instantiation");
    }
    
    public static void registerRecipes() {
        registerFluid();
        registerCraftingRecipes();
        registerSmeltingRecipes();
        registerSmashingRecipes();
    }

	private static void registerFluid() {
        liquidIron = new CrucibleLiquid("iron", new ItemStack(Items.iron_ingot), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 2), new ItemStack(SteamcraftItems.steamcraftNugget, 1, 2), null, 200, 200, 200);
        SteamcraftRegistry.registerLiquid(liquidIron);

        liquidGold = new CrucibleLiquid("gold", new ItemStack(Items.gold_ingot), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 3), new ItemStack(Items.gold_nugget), null, 220, 157, 11);
        SteamcraftRegistry.registerLiquid(liquidGold);

        liquidZinc = new CrucibleLiquid("zinc", new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 1), new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1), null, 225, 225, 225);
        SteamcraftRegistry.registerLiquid(liquidZinc);

        liquidCopper = new CrucibleLiquid("copper", new ItemStack(SteamcraftItems.steamcraftIngot, 1, 0), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 0), new ItemStack(SteamcraftItems.steamcraftNugget, 1, 0), null, 140, 66, 12);
        SteamcraftRegistry.registerLiquid(liquidCopper);

        liquidBrass = new CrucibleLiquid("brass", new ItemStack(SteamcraftItems.steamcraftIngot, 1, 2), new ItemStack(SteamcraftItems.steamcraftPlate, 1, 4), new ItemStack(SteamcraftItems.steamcraftNugget, 1, 3), new CrucibleFormula(liquidZinc, 1, liquidCopper, 3, 4), 242, 191, 66);
        SteamcraftRegistry.registerLiquid(liquidBrass);

        SteamcraftRegistry.registerMeltRecipeOreDict("ingotGold", liquidGold, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("ingotIron", liquidIron, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("ingotZinc", liquidZinc, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("ingotCopper", liquidCopper, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("ingotBrass", liquidBrass, 9);

        SteamcraftRegistry.registerMeltRecipeOreDict("dustTinyGold", liquidGold, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustTinyIron", liquidIron, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustTinyZinc", liquidZinc, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustTinyCopper", liquidCopper, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustTinyBrass", liquidBrass, 1);

        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftGold", liquidGold, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftIron", liquidIron, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftZinc", liquidZinc, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftCopper", liquidCopper, 6);
        SteamcraftRegistry.registerMeltRecipeOreDict("plateSteamcraftBrass", liquidBrass, 6);

        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetGold", liquidGold, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetIron", liquidIron, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetZinc", liquidZinc, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetCopper", liquidCopper, 1);
        SteamcraftRegistry.registerMeltRecipeOreDict("nuggetBrass", liquidBrass, 1);
        SteamcraftRegistry.registerMeltRecipe(Items.gold_nugget, liquidGold, 1);

        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_sword, liquidIron, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_pickaxe, liquidIron, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_axe, liquidIron, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_hoe, liquidIron, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_shovel, liquidIron, 9);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_boots, liquidIron, 36);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_chestplate, liquidIron, 72);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_helmet, liquidIron, 45);
        SteamcraftRegistry.registerMeltRecipeTool(Items.iron_leggings, liquidIron, 63);

        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_sword, liquidGold, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_pickaxe, liquidGold, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_axe, liquidGold, 27);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_hoe, liquidGold, 18);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_shovel, liquidGold, 9);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_boots, liquidGold, 36);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_chestplate, liquidGold, 72);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_helmet, liquidGold, 45);
        SteamcraftRegistry.registerMeltRecipeTool(Items.golden_leggings, liquidGold, 63);

        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.sword("Brass"), liquidBrass, 18);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.pick("Brass"), liquidBrass, 27);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.axe("Brass"), liquidBrass, 27);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.hoe("Brass"), liquidBrass, 18);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.shovel("Brass"), liquidBrass, 9);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.feet("Brass"), liquidBrass, 36);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.chest("Brass"), liquidBrass, 72);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.helm("Brass"), liquidBrass, 45);
        SteamcraftRegistry.registerMeltRecipeTool(SteamcraftItems.legs("Brass"), liquidBrass, 63);

        SteamcraftRegistry.registerOreDictDunkRecipe("ingotIron", liquidGold, 1, new ItemStack(SteamcraftItems.steamcraftIngot, 1, 3));
        SteamcraftRegistry.registerOreDictDunkRecipe("plateSteamcraftIron", liquidGold, 1,
          new ItemStack(SteamcraftItems.steamcraftPlate, 1, 12));
    }


    private static void registerSmeltingRecipes() {
        GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre, 1, 0), new ItemStack(SteamcraftItems.steamcraftIngot, 1, 0), 0.5F);
        GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre, 1, 1), new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), 0.5F);
        if (CrossMod.RAILCRAFT) {
            GameRegistry.addSmelting(new ItemStack(SteamcraftBlocks.steamcraftOre, 1, 2), new ItemStack(SteamcraftItems.steamcraftNugget, 1, 1), 0.5F);
        }
    }

    private static void registerCraftingRecipes() {
        if (Config.hasAllCrucial) {
            registerMisc();
            registerFirearms();
            registerExosuit();
            registerExoUpgrades();
            registerSteamTools();
            registerSteamNet();
            registerSteamMachines();
            registerMetalCrafting();
        }
    }

    public static void registerMisc() {
        BookRecipeRegistry.addRecipe("book", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.book), Items.book, "oreZinc", "oreCopper"));
        if (Config.enableCanister) {
            GameRegistry.addRecipe(new CanisterHandler());
        }

        if (Config.enableSurvivalist) {
            BookRecipeRegistry.addRecipe("survivalist", new ItemStack(SteamcraftItems.survivalist), "b s", "xwx", "xxx",
                    'x', Items.leather, 's', Items.string, 'b', Items.brick, 'w', Items.stick);
        }

        if (Config.enableAstrolabe) {
            BookRecipeRegistry.addRecipe("astrolabe", new ShapedOreRecipe(new ItemStack(SteamcraftItems.astrolabe), " x ", "xrx", " x ",
                    'x', "ingotBrass", 'r', Items.redstone));
        }

        if (Config.enableEngineering) {
            BookRecipeRegistry.addRecipe("engineering1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.engineering), "xzx", "x x", "xxx",
                    'x', "blockCobble", 'z', "plateSteamcraftIron"));
            BookRecipeRegistry.addRecipe("engineering2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.engineering), "xzx", "x x", "xxx",
                    'x', Blocks.cobblestone, 'z', "plateSteamcraftIron"));
        }

        if (Config.enableSpyglass) {
            BookRecipeRegistry.addRecipe("spyglass1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.spyglass), "gb ", "bgb", " bb",
                    'b', "ingotBrass", 'g', Blocks.glass_pane));
            BookRecipeRegistry.addRecipe("spyglass2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.spyglass), "gb ", "bgb", " bb",
                    'b', "plateSteamcraftBrass", 'g', Blocks.glass_pane));
        }

        BookRecipeRegistry.addRecipe("piston1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), " x ", "xpx", " i ",
                'x', "ingotBrass", 'p', Blocks.piston, 'i', SteamcraftBlocks.pipe));
        BookRecipeRegistry.addRecipe("piston2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), " x ", "xpx", " i ",
                'x', "plateSteamcraftBrass", 'p', Blocks.piston, 'i', SteamcraftBlocks.pipe));

        BookRecipeRegistry.addRecipe("turbine1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), " x ", "xnx", " x ",
                'x', "ingotBrass", 'n', "nuggetBrass"));
        BookRecipeRegistry.addRecipe("turbine2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), " x ", "xnx", " x ",
                'x', "plateSteamcraftBrass", 'n', "nuggetBrass"));

        if (Config.enableWrench) {
            BookRecipeRegistry.addRecipe("wrench1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.wrench, 1, 0), "  i", " bb", "b  ",
                    'i', "ingotIron", 'b', "plateSteamcraftBrass"));
            BookRecipeRegistry.addRecipe("wrench2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.wrench, 1, 0), "  i", " bb", "b  ",
                    'i', "ingotIron", 'b', "ingotBrass"));
        }


        if (Config.enableTopHat) {
            BookRecipeRegistry.addRecipe("hat", new ShapedOreRecipe(new ItemStack(SteamcraftItems.tophatNoEmerald), " l ", " l ", "lll",
                    'e', Items.emerald, 'l', new ItemStack(Blocks.wool, 1, 15)));
            if (Config.enableEmeraldHat) {
                BookRecipeRegistry.addRecipe("hatEmerald", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.tophat), SteamcraftItems.tophatNoEmerald, Blocks.emerald_block));

            }
        }
        if (Config.enableGoggles) {
            BookRecipeRegistry.addRecipe("goggles1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.goggles), " l ", "l l", "tbg",
                    'b', "ingotBrass", 'l', Items.leather, 't', SteamcraftItems.spyglass, 'g', Blocks.glass));
            BookRecipeRegistry.addRecipe("goggles2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.goggles), " l ", "l l", "tbg",
                    'b', "plateSteamcraftBrass", 'l', Items.leather, 't', SteamcraftItems.spyglass, 'g', Blocks.glass));

            BookRecipeRegistry.addRecipe("monocle1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.monacle), " l ", "l l", "btb",
                    'b', "ingotBrass", 'l', Items.leather, 't', SteamcraftItems.spyglass));
            BookRecipeRegistry.addRecipe("monocle2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.monacle), " l ", "l l", "btb",
                    'b', "plateSteamcraftBrass", 'l', Items.leather, 't', SteamcraftItems.spyglass));
        }

        if (Config.enableCanister) {
            BookRecipeRegistry.addRecipe("canister", new ShapedOreRecipe(new ItemStack(SteamcraftItems.canister, 4, 0), " i ", "i i", " i ",
                    'i', "nuggetZinc"));
        }

        if (Config.enableHorn) {
            BookRecipeRegistry.addRecipe("whistle1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.horn, 1, 0), " bb", " bn", "pp ",
                    'n', "nuggetBrass", 'b', "plateSteamcraftBrass", 'p', new ItemStack(SteamcraftBlocks.pipe, 1, 0)));
            BookRecipeRegistry.addRecipe("whistle2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.horn, 1, 0), " bb", " bn", "pp ",
                    'n', "nuggetBrass", 'b', "ingotBrass", 'p', new ItemStack(SteamcraftBlocks.pipe, 1, 0)));
        }

        if (Config.enableSteamCell) {
            BookRecipeRegistry.addRecipe("steamcell",
              new ShapedOreRecipe(SteamcraftItems.steamcellEmpty,
                "nbn",
                "bpb",
                "nbn",
                'n', "nuggetBrass",
                'b', Items.netherbrick,
                'p', Items.blaze_powder));
        }

        if (Config.enableSteamCellBauble && CrossMod.BAUBLES) {
            BookRecipeRegistry.addRecipe("steamcellFiller",
              new ShapedOreRecipe(SteamcraftItems.steamcellBauble,
                " p ",
                "i i",
                "i i",
                'p', SteamcraftBlocks.pipe,
                'i', "plateSteamcraftIron"));
        }
    }

    public static void registerCasting() {
        if (Config.enableCrucible) {
            BookRecipeRegistry.addRecipe("crucible", new ItemStack(SteamcraftBlocks.crucible), "x x", "x x", "xxx",
                    'x', Items.brick);
            if (Config.enableHellCrucible) {
                BookRecipeRegistry.addRecipe("hellCrucible", new ItemStack(SteamcraftBlocks.hellCrucible), "x x", "x x", "xxx",
                        'x', Items.netherbrick);
            }
        }

        if (Config.enableMold) {
            BookRecipeRegistry.addRecipe("mold", new ItemStack(SteamcraftBlocks.mold), "xxx", "xxx",
                    'x', Items.brick);
            BookRecipeRegistry.addRecipe("blankMold", new ItemStack(SteamcraftItems.blankMold), "xx",
                    'x', Items.brick);
            BookRecipeRegistry.addRecipe("carving", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.carving), "xzx", "x x", "xxx",
                    'x', "plankWood", 'z', SteamcraftItems.blankMold));
        }


    }

    public static void registerFirearms() {
        if (Config.enableFirearms || Config.enableRL) {
            BookRecipeRegistry.addRecipe("stock", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1), "p  ", " p ", " pp",
                    'p', "plankWood"));

            if (Config.disableMainBarrelRecipe) {
                BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), "i  ", " i ", "  i",
                        'i', "ingotIron"));
            } else {
                BookRecipeRegistry.addRecipe("barrel1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), "i  ", " i ", "  i",
                        'i', "plateSteamcraftIron"));
            }
            BookRecipeRegistry.addRecipe("barrel2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), "i  ", " i ", "  i",
                    'i', "plateSteamcraftIron"));

            BookRecipeRegistry.addRecipe("blunderBarrel1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), "i  ", " i ", "  i",
                    'i', "ingotBrass"));
            BookRecipeRegistry.addRecipe("blunderBarrel2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), "i  ", " i ", "  i",
                    'i', "plateSteamcraftBrass"));

            BookRecipeRegistry.addRecipe("flintlock1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), "f i", "iri",
                    'i', "ingotIron", 'r', Items.redstone, 'f', Items.flint_and_steel));
            BookRecipeRegistry.addRecipe("flintlock2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), "f i", "iri",
                    'i', "plateSteamcraftIron", 'r', Items.redstone, 'f', Items.flint_and_steel));
        }
        if (Config.enableFirearms) {
            if (!Config.expensiveMusketRecipes) {
                BookRecipeRegistry.addRecipe("cartridge1", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge, 2, 0), "nuggetIron", "nuggetIron", Items.paper, Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge2", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge, 2, 0), "nuggetLead", "nuggetLead", Items.paper, Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge3", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge, 2, 0), "nuggetSteel", "nuggetSteel", Items.paper, Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge4", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge, 2, 0), "nuggetSilver", "nuggetSilver", Items.paper, Items.paper, Items.gunpowder));
            } else {
                BookRecipeRegistry.addRecipe("cartridge1", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetIron", Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge2", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetLead", Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge3", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetSteel", Items.paper, Items.gunpowder));
                BookRecipeRegistry.addRecipe("cartridge4", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.musketCartridge), "nuggetSilver", Items.paper, Items.gunpowder));
            }

            BookRecipeRegistry.addRecipe("musket", new ShapedOreRecipe(new ItemStack(SteamcraftItems.musket), "b  ", " bf", "  s",
                    'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));

            BookRecipeRegistry.addRecipe("pistol", new ShapedOreRecipe(new ItemStack(SteamcraftItems.pistol), "b  ", " pf", " p ",
                    'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 'p', "plankWood"));

            BookRecipeRegistry.addRecipe("blunderbuss", new ShapedOreRecipe(new ItemStack(SteamcraftItems.blunderbuss), "b  ", " bf", "  s",
                    'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));

            if (Config.enableEnhancementAblaze) {
                BookRecipeRegistry.addRecipe("ablaze", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementAblaze), "rp ", "pbp", " pr",
                        'r', Items.blaze_rod, 'p', Items.blaze_powder, 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2)));
            }
            if (Config.enableEnhancementSpeedloader) {
                BookRecipeRegistry.addRecipe("speedloader1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementSpeedloader), "  n", "iii", "iri",
                        'i', "ingotIron", 'r', Items.redstone, 'n', "nuggetIron"));
                BookRecipeRegistry.addRecipe("speedloader2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementSpeedloader), "  n", "iii", "iri",
                        'i', "plateSteamcraftIron", 'r', Items.redstone, 'n', "nuggetIron"));
            }
            if (Config.enableEnhancementRecoil) {
                BookRecipeRegistry.addRecipe("recoil", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementRecoil), "ss ", " ss", "ss ",
                        's', "slimeball"));
            }
            if (Config.enableEnhancementRevolver) {
                BookRecipeRegistry.addRecipe("revolver1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementRevolver), "ni ", "iii", " in",
                        'i', "ingotIron", 'n', "nuggetIron"));
                BookRecipeRegistry.addRecipe("revolver2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementRevolver), "ni ", "iii", " in",
                        'i', "ingotIron", 'n', "nuggetIron"));
            }
            if (Config.enableEnhancementSilencer) {
                BookRecipeRegistry.addRecipe("silencer", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementSilencer), "wls", "lll", "slw",
                        'l', Items.leather, 'w', Blocks.wool, 's', Items.string));
            }
            if (Config.enableEnhancementSpeedy) {
                BookRecipeRegistry.addRecipe("speedy1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementSpeedy), "iii", "iii", " n ",
                        'i', "ingotIron", 'n', "nuggetIron"));
                BookRecipeRegistry.addRecipe("speedy2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementSpeedy), "iii", "iii", " n ",
                        'i', "ingotIron", 'n', "nuggetIron"));
            }
        }
        if (Config.enableRL) {
            BookRecipeRegistry.addRecipe("rocket1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketLauncher), "bx ", "fic", " pi",
                    'i', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'x', "plateSteamcraftBrass", 'c', "plateSteamcraftCopper", 'p', "plankWood", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));
            BookRecipeRegistry.addRecipe("rocket2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketLauncher), "bx ", "fic", " pi",
                    'i', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'x', "ingotBrass", 'c', "plateSteamcraftCopper", 'p', "plankWood", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));
            BookRecipeRegistry.addRecipe("rocket3", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketLauncher), "bx ", "fic", " pi",
                    'i', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'x', "plateSteamcraftBrass", 'c', "ingotCopper", 'p', "plankWood", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));
            BookRecipeRegistry.addRecipe("rocket4", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketLauncher), "bx ", "fic", " pi",
                    'i', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'x', "ingotBrass", 'c', "ingotCopper", 'p', "plankWood", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 'f', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 's', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1)));

            if (Config.enableRocket) {
                BookRecipeRegistry.addRecipe("normalRocket1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocket), " i ", "igi",
                        'i', "ingotIron", 'g', Items.gunpowder));
                BookRecipeRegistry.addRecipe("normalRocket2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocket), " i ", "igi",
                        'i', "plateSteamcraftIron", 'g', Items.gunpowder));
            }

            if (Config.enableRocketConcussive) {
                if (Config.enableRocket) {
                    BookRecipeRegistry.addRecipe("concussiveRocket", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.rocketConcussive), SteamcraftItems.rocket, Blocks.sand));
                } else {
                    BookRecipeRegistry.addRecipe("concussiveRocket1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketConcussive), " i ", "igi",
                            'i', "ingotIron", 'g', Items.gunpowder));
                    BookRecipeRegistry.addRecipe("concussiveRocket2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.rocketConcussive), " i ", "igi",
                            'i', "plateSteamcraftIron", 'g', Items.gunpowder));
                }
            }

            if (Config.enableRocketMining) {
                if (Config.enableRocket) {
                    BookRecipeRegistry.addRecipe("miningRocket", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.rocketMiner), SteamcraftItems.rocket, Items.string, Items.string, Items.gunpowder));
                } else {
                    BookRecipeRegistry.addRecipe("miningRocket", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.rocketMiner), SteamcraftItems.rocketConcussive, Items.string, Items.string, Items.gunpowder));
                }
            }

            if (Config.enableEnhancementFastRockets) {
                BookRecipeRegistry.addRecipe("fastRockets", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementFastRockets), "b  ", "gid", "  i",
                        'i', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 'd', SteamcraftBlocks.ruptureDisc, 'g', SteamcraftBlocks.gauge, 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3)));
            }

            if (Config.enableEnhancementAmmo) {
                BookRecipeRegistry.addRecipe("ammo1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementAmmo), "icc", "icc", "cc ",
                        'i', "nuggetIron", 'c', "plateSteamcraftCopper"));
                BookRecipeRegistry.addRecipe("ammo2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enhancementAmmo), "icc", "icc", "cc ",
                        'i', "nuggetIron", 'c', "ingotCopper"));
            }


            if (Config.enableEnhancementAirStrike) {
                BookRecipeRegistry.addRecipe("airStrike1", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.enhancementAirStrike), "ingotIron", "ingotIron", "plankWood", "plankWood", new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4)));
                BookRecipeRegistry.addRecipe("airStrike2", new ShapelessOreRecipe(new ItemStack(SteamcraftItems.enhancementAirStrike), "plateSteamcraftIron", "plateSteamcraftIron", "plankWood", "plankWood", new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4)));
            }
        }
    }

    public static void registerExosuit() {
        if (Config.enableExosuit) {
            BookRecipeRegistry.addRecipe("exoHead", new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorHead), "xyx", "p p", "xyx",
                    'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));

            BookRecipeRegistry.addRecipe("exoBody", new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorBody, 1, SteamcraftItems.exoArmorBody.getMaxDamage() - 1), "p p", "ygy", "xxx",
                    'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'g', SteamcraftBlocks.gauge, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));

            BookRecipeRegistry.addRecipe("exoLegs", new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorLegs), "yxy", "p p", "x x",
                    'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));

            BookRecipeRegistry.addRecipe("exoFeet", new ShapedOreRecipe(new ItemStack(SteamcraftItems.exoArmorFeet), "p p", "x x",
                    'x', "plateSteamcraftBrass", 'y', "nuggetBrass", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }
    }

    public static void registerExoUpgrades() {
        if (Config.enableCopperPlate) {
            addExosuitPlateRecipes("exoCopper", "plateSteamcraftCopper", new ItemStack(SteamcraftItems.exosuitPlate, 1, 0), liquidCopper);
        }
        if (Config.enableZincPlate) {
            addExosuitPlateRecipes("exoZinc", "plateSteamcraftZinc", new ItemStack(SteamcraftItems.exosuitPlate, 1, 1), liquidZinc);
        }
        if (Config.enableIronPlate) {
            addExosuitPlateRecipes("exoIron", "plateSteamcraftIron", new ItemStack(SteamcraftItems.exosuitPlate, 1, 2), liquidIron);
        }
        if (Config.enableGoldPlate) {
            addExosuitPlateRecipes("exoGold", "plateSteamcraftGold", new ItemStack(SteamcraftItems.exosuitPlate, 1, 3), liquidGold);
        }
        if (Config.enableBrassPlate) {
            addExosuitPlateRecipes("exoBrass", "plateSteamcraftBrass", new ItemStack(SteamcraftItems.exosuitPlate, 1, 4), liquidBrass);
        }
        if (Config.enableGildedIronPlate) {
            addExosuitPlateRecipes("exoGildedIron", "plateSteamcraftGildedIron",
              new ItemStack(SteamcraftItems.exosuitPlate, 1, 14));
        }
        if (Config.enableEnderShroud) {
            BookRecipeRegistry.addRecipe("enderShroud", new ShapedOreRecipe(new ItemStack(SteamcraftItems.enderShroud), " g ", "geg", " g ",
                    'g', Blocks.glass, 'e', Items.ender_pearl));
        }
        if (Config.enableJetpack) {
            BookRecipeRegistry.addRecipe("jetpack1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.jetpack), "p p", "ptg", "p p",
                    'p', SteamcraftBlocks.pipe, 'g', SteamcraftBlocks.gauge, 't', "ingotBrass"));
            BookRecipeRegistry.addRecipe("jetpack2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.jetpack), "p p", "ptg", "p p",
                    'p', SteamcraftBlocks.pipe, 'g', SteamcraftBlocks.gauge, 't', "plateSteamcraftBrass"));

        }

        if (Config.enableThrusters) {
            BookRecipeRegistry.addRecipe("thrusters1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.thrusters), "tnt", "ptp", "tnt",
                    'p', SteamcraftBlocks.pipe, 't', "ingotBrass", 'n', "nuggetBrass"));
            BookRecipeRegistry.addRecipe("thrusters2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.thrusters), "tnt", "ptp", "tnt",
                    'p', SteamcraftBlocks.pipe, 't', "plateSteamcraftBrass", 'n', "nuggetBrass"));
        }

        if (Config.enableReinforcedTank) {
            BookRecipeRegistry.addRecipe("reinforcedTank1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.reinforcedTank), "ppp", "tpt", "ppp",
                    't', new ItemStack(SteamcraftBlocks.tank, 1, 0), 'p', "ingotBrass"));
            BookRecipeRegistry.addRecipe("reinforcedTank2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.reinforcedTank), "ppp", "tpt", "ppp",
                    't', new ItemStack(SteamcraftBlocks.tank, 1, 0), 'p', "plateSteamcraftBrass"));
        }

        if (Config.enableUberReinforcedTank) {
            BookRecipeRegistry.addRecipe("uberReinforcedTank1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.uberReinforcedTank), "ppp", "tbt", "ppp",
                    't', SteamcraftItems.reinforcedTank, 'p', "ingotBrass", 'b', "blockBrass"));
            BookRecipeRegistry.addRecipe("uberReinforcedTank2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.uberReinforcedTank), "ppp", "tbt", "ppp",
                    't', SteamcraftItems.reinforcedTank, 'p', "plateSteamcraftTank", 'b', "blockBrass"));
        }

        if (Config.enableCanningMachine) {
            BookRecipeRegistry.addRecipe("canner1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.canner), "bbn", "p p", "i i",
                    'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "ingotBrass", 'n', "nuggetIron", 'i', "ingotIron"));
            BookRecipeRegistry.addRecipe("canner2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.canner), "bbn", "p p", "i i",
                    'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "plateSteamcraftBrass", 'n', "nuggetIron", 'i', "plateSteamcraftIron"));
            BookRecipeRegistry.addRecipe("canner3", new ShapedOreRecipe(new ItemStack(SteamcraftItems.canner), "bbn", "p p", "i i",
                    'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "ingotBrass", 'n', "nuggetIron", 'i', "ingotIron"));
            BookRecipeRegistry.addRecipe("canner4", new ShapedOreRecipe(new ItemStack(SteamcraftItems.canner), "bbn", "p p", "i i",
                    'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "plateSteamcraftBrass", 'n', "nuggetIron", 'i', "plateSteamcraftIron"));
        }

        if (Config.enableWings) {
            BookRecipeRegistry.addRecipe("wings1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.wings), "xxx", "ccc", "c c",
                    'x', "ingotBrass", 'c', "plateSteamcraftCopper"));
            BookRecipeRegistry.addRecipe("wings2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.wings), "xxx", "ccc", "c c",
                    'x', "plateSteamcraftBrass", 'c', "plateSteamcraftCopper"));
        }

        if (Config.enablePowerFist) {
            BookRecipeRegistry.addRecipe("powerFist1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.powerFist), "b i", "bpi", "b i",
                    'i', "ingotIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "nuggetBrass"));
            BookRecipeRegistry.addRecipe("powerFist2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.powerFist), "b i", "bpi", "b i",
                    'i', "plateSteamcraftIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "nuggetBrass"));
        }

        if (Config.enableExtendoFist) {
            BookRecipeRegistry.addRecipe("extendoFist1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.extendoFist), " ii", "bbi", "bb ",
                    'i', "ingotIron", 'b', "nuggetBrass"));
            BookRecipeRegistry.addRecipe("extendoFist2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.extendoFist), " ii", "bbi", "bb ",
                    'i', "plateSteamcraftIron", 'b', "nuggetBrass"));
        }

        if (Config.enablePitonDeployer) {
            BookRecipeRegistry.addRecipe("pitonDeployer", new ShapedOreRecipe(new ItemStack(SteamcraftItems.pitonDeployer), " i ", "lli", "ll ",
                    'l', Items.leather, 'i', "nuggetIron"));
        }

        if (Config.enableFallAssist) {
            BookRecipeRegistry.addRecipe("noFall", new ShapedOreRecipe(new ItemStack(SteamcraftItems.fallAssist), "pbp", "sss",
                    'b', Items.leather_boots, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 's', Items.slime_ball));
        }
        if (Config.enableJumpAssist) {
            BookRecipeRegistry.addRecipe("jumpAssist1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.jumpAssist), "s s", "pbp", "s s",
                    'b', Items.leather_boots, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 's', "ingotBrass"));
            BookRecipeRegistry.addRecipe("jumpAssist2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.jumpAssist), "s s", "pbp", "s s",
                    'b', Items.leather_boots, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 's', "plateSteamcraftBrass"));
        }
        if (Config.enableRunAssist) {
            BookRecipeRegistry.addRecipe("runAssist1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.runAssist), "p p", "s s", "p p",
                    'b', Items.leather_boots, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 's', "ingotBrass"));
            BookRecipeRegistry.addRecipe("runAssist2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.runAssist), "p p", "s s", "p p",
                    'b', Items.leather_boots, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 's', "plateSteamcraftBrass"));
        }
        if (Config.enableStealthUpgrade) {
            BookRecipeRegistry.addRecipe("stealthUpgrade", new ShapedOreRecipe(new ItemStack(SteamcraftItems.stealthUpgrade), "lwl", "lsl", "lwl",
                    'l', Items.leather, 'w', Blocks.wool, 's', Items.string));
        }
        if (Config.enableDoubleJump) {
            BookRecipeRegistry.addRecipe("doubleJump1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.doubleJump), "s s", "v v",
                    'v', SteamcraftBlocks.valvePipe, 's', "ingotBrass"));
            BookRecipeRegistry.addRecipe("doubleJump2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.doubleJump), "s s", "v v",
                    'v', SteamcraftBlocks.valvePipe, 's', "plateSteamcraftBrass"));
        }
        if (Config.enableAnchorHeels) {
            if (Config.enableLeadPlate && OreDictionary.getOres("ingotLead").size() > 0 &&
              !Config.enableAnchorAnvilRecipe) {
                BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                  new ItemStack(SteamcraftItems.anchorHeels),
                  "p p",
                  "e e",
                  'p', "plateSteamcraftLead",
                  'e', new ItemStack(SteamcraftItems.exosuitPlate, 1, 11)
                ));
            } else {
                BookRecipeRegistry.addRecipe("anchorHeels", new ShapedOreRecipe(
                  new ItemStack(SteamcraftItems.anchorHeels),
                  "p p",
                  "eae",
                  'p', "plateSteamcraftIron",
                  'e', new ItemStack(SteamcraftItems.exosuitPlate, 1, 2),
                  'a', Blocks.anvil
                ));
            }
        }
        if (Config.enableFrequencyShifter) {
            BookRecipeRegistry.addRecipe("frequencyShifter",
              new ShapedOreRecipe(SteamcraftItems.frequencyShifter,
                "t n",
                "pjp",
                " p ",
                't', Blocks.redstone_torch,
                'n', "nuggetBrass",
                'p', "plateSteamcraftBrass",
                'j', Blocks.jukebox));
        }
        if (Config.enableRebreather) {
            BookRecipeRegistry.addRecipe("rebreather",
              new ShapedOreRecipe(SteamcraftItems.rebreather,
                " l ",
                "ptp",
                " c ",
                'l', Items.leather,
                'p', SteamcraftBlocks.valvePipe,
                't', SteamcraftBlocks.tank,
                'c', new ItemStack(Blocks.carpet, 1, OreDictionary.WILDCARD_VALUE)));
        }
        if (Config.enableDragonRoar) {
            BookRecipeRegistry.addRecipe("dragonRoar",
              new ShapedOreRecipe(SteamcraftItems.dragonRoar,
                "ccc",
                "cfc",
                "cpc",
                'c', "plateSteamcraftCopper",
                'f', SteamcraftBlocks.fan,
                'p', SteamcraftBlocks.valvePipe));
        }
        if (Config.enableReloadingHolsters) {
            BookRecipeRegistry.addRecipe("reloadingHolsters",
              new ShapedOreRecipe(SteamcraftItems.reloadingHolsters,
                "lbl",
                "c c",
                "p p",
                'l', Items.leather,
                'b', "plateSteamcraftBrass",
                'c', SteamcraftItems.enhancementRevolver,
                'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }
        if (Config.enablePistonPush) {
            BookRecipeRegistry.addRecipe("pistonPush",
              new ShapedOreRecipe(SteamcraftItems.pistonPush,
                "n p",
                "nbp",
                "n p",
                'n', "nuggetBrass",
                'p', Blocks.piston,
                'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }
        if (Config.enableHydrophobic) {
            BookRecipeRegistry.addRecipe("hydrophobic",
              new ShapedOreRecipe(SteamcraftItems.coatingsHydrophobic,
                "zbz",
                "hzh",
                "sss",
                'z', "plateSteamcraftZinc",
                'b', Items.leather_boots,
                'h', SteamcraftBlocks.heater,
                's', "slimeball"));
        }
        if (Config.enablePyrophobic) {
            BookRecipeRegistry.addRecipe("pyrophobic",
              new ShapedOreRecipe(SteamcraftItems.coatingsPyrophobic,
                "cbc",
                "hch",
                "mmm",
                'c', "plateSteamcraftCopper",
                'b', Items.leather_boots,
                'h', SteamcraftBlocks.heater,
                'm', Items.magma_cream));
        }
    }

    public static void registerSteamToolUpgrades() {
        if (!Config.enableSteamTools) {
            return;
        }
        // Not sure how we'd do this in the book, so for now it is not in the book.
        for (Map.Entry<String, DrillHeadMaterial> entry : DrillHeadMaterial.materials.entrySet()) {
            String materialString = entry.getKey();
            DrillHeadMaterial headMat = entry.getValue();
            if (headMat.standard) {
                GameRegistry.addRecipe(new DrillHeadRecipe(SteamcraftItems.drillHead,
                  " n ",
                  "iii",
                  "ppp",
                  'n', "nugget" + materialString,
                  'i', "ingot" + materialString,
                  'p', "plateSteamcraftIron")
                );
            } else {
                GameRegistry.addRecipe(new DrillHeadRecipe(SteamcraftItems.drillHead,
                  " g ",
                  "ggg",
                  "ppp",
                  'g', headMat.oreName,
                  'p', "plateSteamcraftIron")
                );
            }
        }

        if (Config.enableFortune) {
            ItemStack fortuneBook = new ItemStack(Items.enchanted_book);
            fortuneBook.addEnchantment(Enchantment.fortune, 3);
            BookRecipeRegistry.addRecipe("multiplicativeResonator",
              new ShapedOreRecipe(SteamcraftItems.fortuneUpgrade,
                "rgr",
                "rbr",
                "rgr",
                'r', "dustRedstone",
                'g', "plateSteamcraftGildedIron",
                'b', fortuneBook));
        }

        if (Config.enableBigDrill) {
            BookRecipeRegistry.addRecipe("bigDrill", new ShapedOreRecipe(SteamcraftItems.bigDrill,
              " p ",
              "pip",
              "ibi",
              'p', "plateSteamcraftIron",
              'i', "ingotIron",
              'b', "blockIron"));
        }

        if (Config.enableBattleDrill) {
            BookRecipeRegistry.addRecipe("battleDrill",
              new ShapedOreRecipe(SteamcraftItems.battleDrill,
                " s ",
                "sbs",
                " p ",
                's', Items.iron_sword,
                'b', "plateSteamcraftBrass",
                'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }

        if (Config.enablePreciseCuttingHead) {
            BookRecipeRegistry.addRecipe("preciseCuttingHead",
              new ShapedOreRecipe(SteamcraftItems.preciseCuttingHead,
                "f f",
                "pdp",
                "p p",
                'f', Items.flint,
                'p', "plateSteamcraftBrass",
                'd', "gemDiamond"));
        }

        if (Config.enableStoneGrinder) {
            BookRecipeRegistry.addRecipe("stoneGrinder",
              new ShapedOreRecipe(SteamcraftItems.stoneGrinder,
                "i i",
                "ctc",
                "pcp",
                'i', "ingotIron",
                'c', "cobblestone",
                'p', "plateSteamcraftIron",
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableThermalDrill) {
            BookRecipeRegistry.addRecipe("thermalDrill",
              new ShapedOreRecipe(SteamcraftItems.thermalDrill,
                " b ",
                "bnb",
                "iii",
                'b', Items.blaze_rod,
                'n', Blocks.nether_brick,
                'i', "ingotBrass"));
        }

        if (Config.enableChargePlacer) {
            BookRecipeRegistry.addRecipe("chargePlacer",
              new ShapedOreRecipe(SteamcraftItems.chargePlacer,
                "g g",
                "vbv",
                "sps",
                'g', Items.golden_sword,
                'v', SteamcraftBlocks.valvePipe,
                'b', Blocks.stone_button,
                's', SteamcraftBlocks.pipe,
                'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }

        if (Config.enableInternalProcessingUnit) {
            BookRecipeRegistry.addRecipe("internalProcessingUnit",
              new ShapedOreRecipe(SteamcraftItems.internalProcessingUnit,
                "sco",
                's', SteamcraftBlocks.smasher,
                'c', Blocks.chest,
                'o', Blocks.obsidian));
        }

        if (Config.enableForestFire) {
            BookRecipeRegistry.addRecipe("forestFire",
              new ShapedOreRecipe(SteamcraftItems.forestFire,
                " b ",
                "btb",
                " b ",
                'b', Items.blaze_rod,
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableTreeFeller) {
            BookRecipeRegistry.addRecipe("treeFeller",
              new ShapedOreRecipe(SteamcraftItems.treeFeller,
                "fpf",
                "p p",
                "fpf",
                'f', Items.flint,
                'p', "plateSteamcraftIron"));
        }

        if (Config.enableLeafBlower) {
            BookRecipeRegistry.addRecipe("leafBlower",
              new ShapedOreRecipe(SteamcraftItems.leafBlower,
                " p ",
                "ptp",
                " p ",
                'p', "plateSteamcraftBrass",
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableChainsaw) {
            BookRecipeRegistry.addRecipe("chainsaw",
              new ShapedOreRecipe(SteamcraftItems.chainsaw,
                " s ",
                "sps",
                " t ",
                's', Items.iron_sword,
                'p', "plateSteamcraftBrass",
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableBackhoe) {
            BookRecipeRegistry.addRecipe("backhoe",
              new ShapedOreRecipe(SteamcraftItems.backhoe,
                "s",
                "p",
                "p",
                's', SteamcraftItems.shovel("Brass"),
                'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }

        if (Config.enableCultivator) {
            BookRecipeRegistry.addRecipe("cultivator",
              new ShapedOreRecipe(SteamcraftItems.cultivator,
                "zsz",
                " z ",
                'z', "plateSteamcraftZinc",
                's', "stickWood"));
        }

        if (Config.enableRotaryBlades) {
            BookRecipeRegistry.addRecipe("rotaryBlades",
              new ShapedOreRecipe(SteamcraftItems.rotaryBlades,
                " f ",
                "ftf",
                " f ",
                'f', Items.flint,
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableSifter) {
            BookRecipeRegistry.addRecipe("sifter",
              new ShapedOreRecipe(SteamcraftItems.sifter,
                " p ",
                "ctc",
                " p ",
                'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0),
                'c', "cobblestone",
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));

        }

        if (Config.enableOverclocker) {
            BookRecipeRegistry.addRecipe("overclocker",
              new ShapedOreRecipe(SteamcraftItems.overclocker,
                "r r",
                "btb",
                "r r",
                'r', "dustRedstone",
                'b', "ingotBrass",
                't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableAutosmelting) {
            BookRecipeRegistry.addRecipe("autosmelting",
              new ShapedOreRecipe(SteamcraftItems.autosmelting,
              " f ",
              " h ",
              "rpr",
              'f', SteamcraftBlocks.fan,
              'h', SteamcraftBlocks.heater,
              'r', Items.blaze_rod,
              'p', Items.blaze_powder));
        }

        if (Config.enableTheVoid) {
            BookRecipeRegistry.addRecipe("theVoid",
              new ShapelessOreRecipe(SteamcraftItems.theVoid, Blocks.ender_chest, Blocks.hopper));
        }
    }

    public static void registerSteamTools() {
        if (Config.enableSteamTools) {
            BookRecipeRegistry.addRecipe("drill1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill, 1, SteamcraftItems.steamDrill.getMaxDamage() - 1), "xii", "pti", "xpx",
                    'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("drill2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill, 1, SteamcraftItems.steamDrill.getMaxDamage() - 1), "xii", "pti", "xpx",
                    'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("drill3", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill, 1, SteamcraftItems.steamDrill.getMaxDamage() - 1), "xii", "pti", "xpx",
                    'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("drill4", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamDrill, 1, SteamcraftItems.steamDrill.getMaxDamage() - 1), "xii", "pti", "xpx",
                    'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));

            BookRecipeRegistry.addRecipe("shovel1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel, 1, SteamcraftItems.steamShovel.getMaxDamage() - 1), "ixi", "ptx", "xpi",
                    'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("shovel2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel, 1, SteamcraftItems.steamShovel.getMaxDamage() - 1), "ixi", "ptx", "xpi",
                    'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("shovel3", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel, 1, SteamcraftItems.steamShovel.getMaxDamage() - 1), "ixi", "ptx", "xpi",
                    'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("shovel4", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamShovel, 1, SteamcraftItems.steamShovel.getMaxDamage() - 1), "ixi", "ptx", "xpi",
                    'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));

            BookRecipeRegistry.addRecipe("axe1", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe, 1, SteamcraftItems.steamAxe.getMaxDamage() - 1), "ini", "ptn", "xpi",
                    'x', "ingotBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), 'n', "nuggetIron"));
            BookRecipeRegistry.addRecipe("axe2", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe, 1, SteamcraftItems.steamAxe.getMaxDamage() - 1), "ini", "ptn", "xpi",
                    'x', "plateSteamcraftBrass", 'i', "ingotIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), 'n', "nuggetIron"));
            BookRecipeRegistry.addRecipe("axe3", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe, 1, SteamcraftItems.steamAxe.getMaxDamage() - 1), "ini", "ptn", "xpi",
                    'x', "ingotBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), 'n', "nuggetIron"));
            BookRecipeRegistry.addRecipe("axe4", new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamAxe, 1, SteamcraftItems.steamAxe.getMaxDamage() - 1), "ini", "ptn", "xpi",
                    'x', "plateSteamcraftBrass", 'i', "plateSteamcraftIron", 'p', SteamcraftBlocks.pipe, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5), 'n', "nuggetIron"));
        }
    }

    public static void registerSteamNet() {
        if (Config.enableTank) {
            BookRecipeRegistry.addRecipe("tank1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.tank), "iii", "i i", "iii",
                    'i', "plateSteamcraftBrass"));
            BookRecipeRegistry.addRecipe("tank2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.tank), "iii", "i i", "iii",
                    'i', "ingotBrass"));
        }

        if (Config.enableBoiler) {
            BookRecipeRegistry.addRecipe("boiler1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx",
                    'x', "ingotBrass", 'f', Blocks.furnace));
            BookRecipeRegistry.addRecipe("boiler2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.boiler), "xxx", "xfx", "xxx",
                    'x', "plateSteamcraftBrass", 'f', Blocks.furnace));
        }

        if (Config.enablePipe) {
            BookRecipeRegistry.addRecipe("pipe1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe, 4, 0), "xxx", "   ", "xxx",
                    'x', "ingotBrass"));
            BookRecipeRegistry.addRecipe("pipe2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pipe, 4, 0), "xxx", "   ", "xxx",
                    'x', "plateSteamcraftBrass"));
        }

        if (Config.enableValvePipe) {
            BookRecipeRegistry.addRecipe("valvePipe", new ShapelessOreRecipe(new ItemStack(SteamcraftBlocks.valvePipe), SteamcraftBlocks.pipe, Blocks.lever));
        }

        if (Config.enableGauge) {
            BookRecipeRegistry.addRecipe("gauge", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.gauge), " x ", "xrx", " x ",
                    'x', "nuggetBrass", 'r', Items.compass));
        }

        if (Config.enableRuptureDisc) {
            BookRecipeRegistry.addRecipe("disc", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.ruptureDisc), " x ", "xrx", " x ",
                    'x', "nuggetBrass", 'r', "plateSteamcraftZinc"));
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftBlocks.ruptureDisc, 1, 0), "plateSteamcraftZinc", new ItemStack(SteamcraftBlocks.ruptureDisc, 1, 1)));
        }

        if (Config.enableFlashBoiler) {
            BookRecipeRegistry.addRecipe("flashBoiler1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.flashBoiler, 2), "xtx", "pbp", "nnn",
                    'x', "ingotBrass", 'b', SteamcraftBlocks.boiler, 't', new ItemStack(SteamcraftBlocks.tank, 1, 0), 'n', Blocks.nether_brick, 'p', SteamcraftBlocks.pipe));
            BookRecipeRegistry.addRecipe("flashBoiler2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.flashBoiler, 2), "xtx", "pbp", "nnn",
                    'x', "plateSteamcraftBrass", 'b', SteamcraftBlocks.boiler, 't', new ItemStack(SteamcraftBlocks.tank, 1, 0), 'n', Blocks.nether_brick, 'p', SteamcraftBlocks.pipe));
        }
    }

    public static void registerSteamMachines() {
        if (Config.enableCharger) {
            BookRecipeRegistry.addRecipe("filler1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.charger), " p ", "xpx", "xpx",
                    'x', Blocks.cobblestone, 'p', SteamcraftBlocks.pipe));
            BookRecipeRegistry.addRecipe("filler2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.charger), " p ", "xpx", "xpx",
                    'x', "blockCobble", 'p', SteamcraftBlocks.pipe));
        }

        if (Config.enableChargingPad && Config.enableCharger) {
            BookRecipeRegistry.addRecipe("fillingPad1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.chargingPad), "p p", "pcp", "pbp",
                    'c', SteamcraftBlocks.charger, 'p', SteamcraftBlocks.pipe, 'b', "ingotBrass"));
            BookRecipeRegistry.addRecipe("fillingPad2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.chargingPad), "p p", "pcp", "pbp",
                    'c', SteamcraftBlocks.charger, 'p', SteamcraftBlocks.pipe, 'b', "plateSteamcraftBrass"));
        }

        if (Config.enablePump) {
            BookRecipeRegistry.addRecipe("pump1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pump), "gng", "iii", "ngn",
                    'i', "plateSteamcraftBrass", 'n', "nuggetBrass", 'g', Blocks.glass_pane));
            BookRecipeRegistry.addRecipe("pump2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.pump), "gng", "iii", "ngn",
                    'i', "ingotBrass", 'n', "nuggetBrass", 'g', Blocks.glass_pane));
        }

        if (Config.enableFluidSteamConverter) {
            BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(SteamcraftBlocks.fluidSteamConverter, "xlx", "gpp", "xlx",
                    'x', "ingotBrass", 'l', Items.leather, 'p', SteamcraftBlocks.pipe, 'g', Blocks.glass_pane));
            BookRecipeRegistry.addRecipe("fsc1", new ShapedOreRecipe(SteamcraftBlocks.fluidSteamConverter, "xlx", "gpp", "xlx",
                    'x', "plateSteamcraftBrass", 'l', Items.leather, 'p', SteamcraftBlocks.pipe, 'g', Blocks.glass_pane));
        }

        if (Config.enableFan) {
            BookRecipeRegistry.addRecipe("fan1", new ShapedOreRecipe(SteamcraftBlocks.fan, "xxx", "btb", "xxx",
                    'x', "ingotBrass", 'b', Blocks.iron_bars, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
            BookRecipeRegistry.addRecipe("fan2", new ShapedOreRecipe(SteamcraftBlocks.fan, "xxx", "btb", "xxx",
                    'x', "plateSteamcraftBrass", 'b', Blocks.iron_bars, 't', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 5)));
        }

        if (Config.enableVacuum && Config.enableFan) {
            BookRecipeRegistry.addRecipe("vacuum1", new ShapedOreRecipe(SteamcraftBlocks.vacuum, " x ", "pfx", " x ",
                    'x', "ingotBrass", 'p', SteamcraftBlocks.pipe, 'f', SteamcraftBlocks.fan));
            BookRecipeRegistry.addRecipe("vacuum2", new ShapedOreRecipe(SteamcraftBlocks.vacuum, " x ", "pfx", " x ",
                    'x', "plateSteamcraftBrass", 'p', SteamcraftBlocks.pipe, 'f', SteamcraftBlocks.fan));
        }

        if (Config.enableHeater) {
            BookRecipeRegistry.addRecipe("heater1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ",
                    'x', "ingotBrass", 'c', "nuggetCopper", 'f', Blocks.furnace, 'p', SteamcraftBlocks.pipe));
            BookRecipeRegistry.addRecipe("heater2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.heater), "ccc", "xfx", " p ",
                    'x', "plateSteamcraftBrass", 'c', "nuggetCopper", 'f', Blocks.furnace, 'p', SteamcraftBlocks.pipe));
        }


        if (Config.enableHammer) {
            BookRecipeRegistry.addRecipe("hammer1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.hammer), " ix", "bix",
                    'x', "ingotBrass", 'i', "ingotIron", 'b', Blocks.iron_block));
            BookRecipeRegistry.addRecipe("hammer2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.hammer), " ix", "bix",
                    'x', "plateSteamcraftBrass", 'i', "ingotIron", 'b', Blocks.iron_block));
        }

        if (Config.enableMortar) {
            BookRecipeRegistry.addRecipe("itemMortar1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc",
                    'p', "plateSteamcraftBrass", 'c', "plateSteamcraftCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
            BookRecipeRegistry.addRecipe("itemMortar2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc",
                    'p', "ingotBrass", 'c', "plateSteamcraftCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
            BookRecipeRegistry.addRecipe("itemMortar3", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc",
                    'p', "plateSteamcraftBrass", 'c', "ingotCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
            BookRecipeRegistry.addRecipe("itemMortar4", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.itemMortar), "p p", "pbp", "ccc",
                    'p', "ingotBrass", 'c', "ingotCopper", 'b', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0)));
        }

        if (Config.enableSmasher) {
            BookRecipeRegistry.addRecipe("smasher1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
                    'i', "plateSteamcraftIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "plateSteamcraftBrass"));
            BookRecipeRegistry.addRecipe("smasher2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
                    'i', "ingotIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "ingotBrass"));
            BookRecipeRegistry.addRecipe("smasher3", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
                    'i', "ingotIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "plateSteamcraftBrass"));
            BookRecipeRegistry.addRecipe("smasher4", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.smasher), "bpi", "bpi", "bpi",
                    'i', "plateSteamcraftIron", 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'b', "ingotBrass"));
        }

        if (Config.enableThumper) {
            BookRecipeRegistry.addRecipe("thumper1", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.thumper), "pbp", "ebe", "xix",
                    'i', "blockIron", 'b', "blockBrass", 'e', SteamcraftBlocks.pipe, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'x', "plateSteamcraftBrass"));
            BookRecipeRegistry.addRecipe("thumper2", new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.thumper), "pbp", "ebe", "xix",
                    'i', "blockIron", 'b', "blockBrass", 'e', SteamcraftBlocks.pipe, 'p', new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 0), 'x', "ingotBrass"));
        }

    }

    public static void registerMetalCrafting() {
        // metals
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockBrass),
                "iii",
                "iii",
                "iii",
                'i', "ingotBrass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 9, 2),
                "i",
                'i', "blockBrass"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockCopper),
                "iii",
                "iii",
                "iii",
                'i', "ingotCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 9, 0),
                "i",
                'i', "blockCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftBlocks.blockZinc),
                "iii",
                "iii",
                "iii",
                'i', "ingotZinc"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 9, 1),
                "i",
                'i', "blockZinc"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 0), "ingotCopper"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 1), "ingotZinc"));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 2), new ItemStack(Items.iron_ingot)));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(SteamcraftItems.steamcraftNugget, 9, 3), "ingotBrass"));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 0), "xxx", "xxx", "xxx",
                'x', "nuggetCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 1), "xxx", "xxx", "xxx",
                'x', "nuggetZinc"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Items.iron_ingot), "xxx", "xxx", "xxx",
                'x', "nuggetIron"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(SteamcraftItems.steamcraftIngot, 1, 2), "xxx", "xxx", "xxx",
                'x', "nuggetBrass"));


    }

    public static void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate) {
        BookRecipeRegistry.addRecipe(str, new ShapedOreRecipe(plate, "xx", "xx",
                'x', ingredient));
        if (ingredient instanceof ItemStack) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.stackSize = 4;
            GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
        } else if (ingredient instanceof String) {
            ItemStack stack = OreDictionary.getOres((String) ingredient).get(0).copy();
            stack.stackSize = 4;
            GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
        }

    }

    public static void addExosuitPlateRecipes(String str, Object ingredient, ItemStack plate, CrucibleLiquid liq) {
        BookRecipeRegistry.addRecipe(str, new ShapedOreRecipe(plate, "xx", "xx",
                'x', ingredient));
        if (ingredient instanceof ItemStack) {
            ItemStack stack = ((ItemStack) ingredient).copy();
            stack.stackSize = 4;
            GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
        } else if (ingredient instanceof String) {
            ItemStack stack = OreDictionary.getOres((String) ingredient).get(0).copy();
            stack.stackSize = 4;
            GameRegistry.addRecipe(new ShapelessOreRecipe(stack, plate));
        }
        SteamcraftRegistry.registerMeltRecipe(plate.getItem(), plate.getItemDamage(), liq, 24);
    }

    public static void registerDustLiquids() {
        SteamcraftRegistry.registerMeltRecipeOreDict("dustGold", liquidGold, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustIron", liquidIron, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustZinc", liquidZinc, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustCopper", liquidCopper, 9);
        SteamcraftRegistry.registerMeltRecipeOreDict("dustBrass", liquidBrass, 9);
    }
    
    private static void registerSmashingRecipes(){
		TileEntitySmasher.REGISTRY.registerSmashable(Blocks.cobblestone, new ItemStack(Blocks.gravel));
		TileEntitySmasher.REGISTRY.registerSmashable(Blocks.cobblestone_wall, new ItemStack(Blocks.gravel));
		TileEntitySmasher.REGISTRY.registerSmashable(Blocks.gravel, new ItemStack(Blocks.sand));
		TileEntitySmasher.REGISTRY.registerSmashable(Blocks.glowstone, new ItemStack(Items.glowstone_dust, 4));
		TileEntitySmasher.REGISTRY.registerSmashable(Blocks.sandstone, new ItemStack(Blocks.sand));
	}
}
