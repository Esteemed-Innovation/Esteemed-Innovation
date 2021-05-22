package eiteam.esteemedinnovation.firearms;

import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.book.*;
import eiteam.esteemedinnovation.api.enhancement.Enhancement;
import eiteam.esteemedinnovation.api.enhancement.EnhancementRegistry;
import eiteam.esteemedinnovation.api.enhancement.Rocket;
import eiteam.esteemedinnovation.api.enhancement.UtilEnhancements;
import eiteam.esteemedinnovation.api.entity.EntityRocket;
import eiteam.esteemedinnovation.api.research.ResearchRecipe;
import eiteam.esteemedinnovation.api.research.ShapelessResearchRecipe;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.init.ConfigurableModule;
import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.firearms.flintlock.FlintlockBookCategory;
import eiteam.esteemedinnovation.firearms.flintlock.ItemFirearm;
import eiteam.esteemedinnovation.firearms.flintlock.enhancements.*;
import eiteam.esteemedinnovation.firearms.rocket.ItemRocketLauncher;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketBasic;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketConcussive;
import eiteam.esteemedinnovation.firearms.rocket.ammo.ItemRocketMining;
import eiteam.esteemedinnovation.firearms.rocket.ammo.RenderRocket;
import eiteam.esteemedinnovation.firearms.rocket.enhancements.ItemEnhancementAirStrike;
import eiteam.esteemedinnovation.firearms.rocket.enhancements.ItemEnhancementAmmo;
import eiteam.esteemedinnovation.firearms.rocket.enhancements.ItemEnhancementFastRockets;
import eiteam.esteemedinnovation.steamsafety.SafetyModule;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static eiteam.esteemedinnovation.commons.Config.CATEGORY_ITEMS;
import static eiteam.esteemedinnovation.commons.Config.CATEGORY_WEAPONS;
import static eiteam.esteemedinnovation.commons.EsteemedInnovation.GADGET_SECTION;
import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.*;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static net.minecraft.init.Blocks.SAND;
import static net.minecraft.init.Blocks.WOOL;
import static net.minecraft.init.Items.*;

public class FirearmModule extends ContentModule implements ConfigurableModule {
    public static Item MUSKET;
    public static Item PISTOL;
    public static Item ROCKET_LAUNCHER;
    public static Item BLUNDERBUSS;
    public static Item MUSKET_CARTRIDGE;
    public static Item ROCKET;
    public static Item CONCUSSIVE_ROCKET;
    public static Item MINING_ROCKET;
    public static Item BLAZE_BARREL;
    public static Item REVOLVER_CHAMBER;
    public static Item BREECH;
    public static Item MAKESHIFT_SUPPRESSOR;
    public static Item RECOIL_PAD;
    public static Item BOLT_ACTION;
    public static Item SPYGLASS;
    public static Item STREAMLINED_BARREL;
    public static Item AIR_STRIKE_CONVERSION_KIT;
    public static Item EXTENDED_MAGAZINE;
    private static boolean enableSpyglass;
    private static boolean enableRocketMining;
    private static boolean enableRocketConcussive;
    private static boolean enableRocket;
    private static boolean enableRL;
    public static boolean enableFirearms;
    private static boolean enableEnhancementAirStrike;
    private static boolean enableEnhancementAmmo;
    private static boolean enableEnhancementFastRockets;
    private static boolean enableEnhancementSpeedy;
    private static boolean enableEnhancementRecoil;
    private static boolean enableEnhancementSilencer;
    private static boolean enableEnhancementSpeedloader;
    public static boolean enableEnhancementRevolver;
    private static boolean enableEnhancementAblaze;
    private static float blunderbussDamage;
    private static float pistolDamage;
    private static float musketDamage;
    private static boolean expensiveMusketRecipes;

    @Override
    public void create(Side side) {
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.EI_MODID, "Rocket"), EntityRocket.class, "Rocket", 3, EsteemedInnovation.instance, 64, 20, true);
        MinecraftForge.EVENT_BUS.register(new FlintlockBookCategory.EventHandler());
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        MUSKET = setup(event, new ItemFirearm(musketDamage, 84, 0.2F, 5.0F, false, 1, INGOT_IRON), "musket");
        PISTOL = setup(event, new ItemFirearm(pistolDamage, 42, 0.5F, 2.0F, false, 1, INGOT_IRON), "pistol");
        ROCKET_LAUNCHER = setup(event, new ItemRocketLauncher(2.0F, 95, 10, 3.5F, 4, INGOT_IRON), "rocket_launcher");
        BLUNDERBUSS = setup(event, new ItemFirearm(blunderbussDamage, 95, 3.5F, 7.5F, true, 1, INGOT_BRASS), "blunderbuss");
        MUSKET_CARTRIDGE = setup(event, new Item(), "musket_cartridge");
        ROCKET = setup(event, new ItemRocketBasic(), "rocket");
        EnhancementRegistry.registerRocket((Rocket) ROCKET);
        CONCUSSIVE_ROCKET = setup(event, new ItemRocketConcussive(), "rocket_concussive");
        EnhancementRegistry.registerRocket((Rocket) CONCUSSIVE_ROCKET);
        MINING_ROCKET = setup(event, new ItemRocketMining(), "rocket_miner");
        EnhancementRegistry.registerRocket((Rocket) MINING_ROCKET);
        BLAZE_BARREL = setup(event, new ItemEnhancementFireMusket(), "enhancement_blaze");
        EnhancementRegistry.registerEnhancement((Enhancement) BLAZE_BARREL);
        REVOLVER_CHAMBER = setup(event, new ItemEnhancementRevolver(), "enhancement_revolver");
        EnhancementRegistry.registerEnhancement((Enhancement) REVOLVER_CHAMBER);
        BREECH = setup(event, new ItemEnhancementSpeedloader(), "enhancement_speedloader");
        EnhancementRegistry.registerEnhancement((Enhancement) BREECH);
        MAKESHIFT_SUPPRESSOR = setup(event, new ItemEnhancementSilencer(), "enhancement_silencer");
        EnhancementRegistry.registerEnhancement((Enhancement) MAKESHIFT_SUPPRESSOR);
        RECOIL_PAD = setup(event, new ItemEnhancementRecoil(), "enhancement_recoil");
        EnhancementRegistry.registerEnhancement((Enhancement) RECOIL_PAD);
        BOLT_ACTION = setup(event, new ItemEnhancementSpeedy(), "enhancement_speedy");
        EnhancementRegistry.registerEnhancement((Enhancement) BOLT_ACTION);
        SPYGLASS = setup(event, new ItemSpyglass(), "spyglass");
        EnhancementRegistry.registerEnhancement((Enhancement) SPYGLASS);
        STREAMLINED_BARREL = setup(event, new ItemEnhancementFastRockets(), "enhancement_fast_rockets");
        EnhancementRegistry.registerEnhancement((Enhancement) STREAMLINED_BARREL);
        AIR_STRIKE_CONVERSION_KIT = setup(event, new ItemEnhancementAirStrike(), "enhancement_air_strike");
        EnhancementRegistry.registerEnhancement((Enhancement) AIR_STRIKE_CONVERSION_KIT);
        EXTENDED_MAGAZINE = setup(event, new ItemEnhancementAmmo(), "enhancement_ammo");
        EnhancementRegistry.registerEnhancement((Enhancement) EXTENDED_MAGAZINE);

    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        if (enableRL) {
            addRocketLauncherRecipe(event, "rocket1", PLATE_THIN_BRASS, PLATE_THIN_COPPER);
            addRocketLauncherRecipe(event, "rocket2", INGOT_BRASS, PLATE_THIN_COPPER);
            addRocketLauncherRecipe(event, "rocket3", PLATE_THIN_BRASS, INGOT_COPPER);
            addRocketLauncherRecipe(event, "rocket4", INGOT_BRASS, INGOT_COPPER);
            if (enableRocket) {
                RecipeUtility.addRecipe(event, true, "normalRocket1", ROCKET,
                  " i ",
                  "igi",
                  'i', INGOT_IRON,
                  'g', GUNPOWDER
                );
                RecipeUtility.addRecipe(event, true, "normalRocket2", ROCKET,
                  " i ",
                  "igi",
                  'i', PLATE_THIN_IRON,
                  'g', GUNPOWDER
                );
            }
            if (enableRocketConcussive) {
                if (enableRocket) {
                    RecipeUtility.addShapelessRecipe(event, true, "concussiveRocket", CONCUSSIVE_ROCKET,
                      ROCKET, SAND);
                } else {
                    RecipeUtility.addRecipe(event, true, "concussiveRocket1", CONCUSSIVE_ROCKET,
                      " i ",
                      "igi",
                      'i', INGOT_IRON,
                      'g', GUNPOWDER
                    );
                    RecipeUtility.addRecipe(event, true, "concussiveRocket2", CONCUSSIVE_ROCKET,
                      " i ",
                      "igi",
                      'i', PLATE_THIN_IRON,
                      'g', GUNPOWDER
                    );
                }
            }
            if (enableRocketMining) {
                if (enableRocket) {
                    RecipeUtility.addShapelessRecipe(event, true, "miningRocket", MINING_ROCKET,
                      ROCKET, STRING, STRING, GUNPOWDER
                    );
                } else {
                    RecipeUtility.addShapelessRecipe(event, true, "miningRocket", MINING_ROCKET,
                      CONCUSSIVE_ROCKET, STRING, STRING, GUNPOWDER
                    );
                }
            }
            if (enableEnhancementFastRockets) {
                RecipeUtility.addRecipe(event, true, "fastRockets", STREAMLINED_BARREL,
                  "b  ",
                  "gid",
                  "  i",
                  'b', new ItemStack(COMPONENT, 1, BLUNDERBUSS_BARREL.getMetadata()),
                  'g', SafetyModule.STEAM_GAUGE,
                  'i', new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata()),
                  'd', SafetyModule.RUPTURE_DISC
                );
            }
            if (enableEnhancementAirStrike) {
                RecipeUtility.addShapelessRecipe(event, true, "airStrike1", AIR_STRIKE_CONVERSION_KIT,
                  INGOT_IRON, INGOT_IRON, PLANK_WOOD, PLANK_WOOD,
                  new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()));
                RecipeUtility.addShapelessRecipe(event, true, "airStrike2", AIR_STRIKE_CONVERSION_KIT,
                  PLATE_THIN_IRON, PLATE_THIN_IRON, PLANK_WOOD, PLANK_WOOD,
                  new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()));
            }
            if (enableEnhancementAmmo) {
                RecipeUtility.addRecipe(event, true, "ammo1", EXTENDED_MAGAZINE,
                  "icc",
                  "icc",
                  "cc ",
                  'i', NUGGET_IRON,
                  'c', INGOT_COPPER
                );
                RecipeUtility.addRecipe(event, true, "ammo2", EXTENDED_MAGAZINE,
                  "icc",
                  "icc",
                  "cc ",
                  'i', NUGGET_IRON,
                  'c', PLATE_THIN_COPPER
                );
            }
        }
        if (enableFirearms) {
            RecipeUtility.addRecipe(event, true, "musket", new ResearchRecipe(new ItemStack(MUSKET), "category.Musket.name",
              "b  ",
              " bf",
              "  s",
              'b', new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata()),
              'f', new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()),
              's', new ItemStack(COMPONENT, 1, GUN_STOCK.getMetadata())
            ));
            RecipeUtility.addRecipe(event, true, "pistol", new ResearchRecipe(new ItemStack(PISTOL), "category.Pistol.name",
              "b  ",
              " pf",
              " p ",
              'b', new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata()),
              'p', PLANK_WOOD,
              'f', new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata())
            ));
            RecipeUtility.addRecipe(event, true, "blunderbuss", new ResearchRecipe(new ItemStack(BLUNDERBUSS), "category.Blunderbuss.name",
              "b  ",
              " bf",
              "  s",
              'b', new ItemStack(COMPONENT, 1, BLUNDERBUSS_BARREL.getMetadata()),
              'f', new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()),
              's', new ItemStack(COMPONENT, 1, GUN_STOCK.getMetadata())
            ));
            // TODO: ShapelessResearchRecipe
            String[] ores = {NUGGET_IRON, NUGGET_LEAD, NUGGET_STEEL, NUGGET_SILVER};
            if (expensiveMusketRecipes) {
                int i = 1;
                for (String ore : ores) {
                    RecipeUtility.addRecipe(event, true, "cartridge" + i, new ShapelessResearchRecipe(new ItemStack(MUSKET_CARTRIDGE),
                      "category.Musket.name", ore, PAPER, GUNPOWDER));
                    i++;
                }
            } else {
                int i = 1;
                for (String ore : ores) {
                    RecipeUtility.addRecipe(event, true, "cartridge" + i, new ShapelessResearchRecipe(new ItemStack(MUSKET_CARTRIDGE, 2, 0),
                      "category.Musket.name", ore, ore, PAPER, PAPER, GUNPOWDER));
                    i++;
                }
            }

            if (enableEnhancementAblaze) {
                RecipeUtility.addRecipe(event, true, "ablaze", BLAZE_BARREL,
                  "rp ",
                  "pbp",
                  " pr",
                  'r', BLAZE_ROD,
                  'p', BLAZE_POWDER,
                  'b', new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata())
                );
            }
            if (enableEnhancementRevolver) {
                RecipeUtility.addRecipe(event, true, "revolver", REVOLVER_CHAMBER,
                  "ni ",
                  "iii",
                  " in",
                  'n', NUGGET_IRON,
                  'i', INGOT_IRON
                );
            }
            if (enableEnhancementSpeedy) {
                RecipeUtility.addRecipe(event, true, "speedy", BREECH,
                  "iii",
                  "iii",
                  " n ",
                  'i', INGOT_IRON,
                  'n', NUGGET_IRON
                );
            }
            if (enableEnhancementSilencer) {
                RecipeUtility.addRecipe(event, true, "silencer", MAKESHIFT_SUPPRESSOR,
                  "wls",
                  "lll",
                  "slw",
                  'l', LEATHER,
                  'w', WOOL,
                  's', STRING
                );
            }
            if (enableEnhancementRecoil) {
                RecipeUtility.addRecipe(event, true, "recoil", RECOIL_PAD,
                  "ss ",
                  " ss",
                  "ss ",
                  's', SLIMEBALL_ORE
                );
            }
            if (enableEnhancementSpeedloader) {
                RecipeUtility.addRecipe(event, true, "speedloader1", BOLT_ACTION,
                  "  n",
                  "iii",
                  "iri",
                  'n', NUGGET_IRON,
                  'i', INGOT_IRON,
                  'r', DUST_REDSTONE
                );
                RecipeUtility.addRecipe(event, true, "speedloader2", BOLT_ACTION,
                  "  n",
                  "iii",
                  "iri",
                  'n', NUGGET_IRON,
                  'i', PLATE_THIN_IRON,
                  'r', DUST_REDSTONE
                );
            }
        }
        if (enableSpyglass) {
            RecipeUtility.addRecipe(event, true, "spyglass1", SPYGLASS,
              "gb ",
              "bgb",
              " bb",
              'b', INGOT_BRASS,
              'g', PANE_GLASS_COLORLESS
            );
            RecipeUtility.addRecipe(event, true, "spyglass2", SPYGLASS,
              "gb ",
              "bgb",
              " bb",
              'b', PLATE_THIN_BRASS,
              'g', PANE_GLASS_COLORLESS
            );
        }
    }

    private static void addRocketLauncherRecipe(RegistryEvent.Register<IRecipe> event, String name, String brassOre, String copperOre) {
        RecipeUtility.addRecipe(event, true, name, ROCKET_LAUNCHER,
          "bx ",
          "fic",
          " pi",
          'i', new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata()),
          'x', brassOre,
          'c', copperOre,
          'p', PLANK_WOOD,
          'b', new ItemStack(COMPONENT, 1, BLUNDERBUSS_BARREL.getMetadata()),
          'f', new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata())
        );
    }


    @Override
    public void finish(Side side) {
        if (!enableFirearms && !enableRL) {
            return;
        }
        BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION,
          new BookCategory("category.Parts.name",
            new BookEntry("research.Parts.name",
              new BookPageItem("research.Parts.name", "research.Parts.0",
                new ItemStack(COMPONENT, 1, BLUNDERBUSS_BARREL.getMetadata()),
                new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()),
                new ItemStack(COMPONENT, 1, GUN_STOCK.getMetadata()),
                new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata())),
              new BookPageCrafting("", "stock"),
              new BookPageCrafting("", "barrel1", "barrel2"),
              new BookPageCrafting("", "blunderBarrel1", "blunderBarrel2"),
              new BookPageCrafting("", "flintlock1", "flintlock2"))));

        if (enableFirearms) {
            BookCategory musketCategory = new FlintlockBookCategory("category.Musket.name",
              new BookEntry("research.Musket.name",
                new BookPageItem("research.Musket.name", "research.Musket.0", new ItemStack(MUSKET)),
                new BookPageCrafting("", "cartridge1", "cartridge2", "cartridge3", "cartridge4"),
                new BookPageCrafting("", "musket")));
            BookCategory blunderbussCategory = new FlintlockBookCategory("category.Blunderbuss.name",
              new BookEntry("research.Blunderbuss.name",
                new BookPageItem("research.Blunderbuss.name", "research.Blunderbuss.0", new ItemStack(BLUNDERBUSS)),
                new BookPageCrafting("", "blunderbuss")));

            if (enableSpyglass) {
                musketCategory.appendEntries(new BookEntry("research.EnhancementSpyglass.name",
                  new BookPageItem("research.EnhancementSpyglass.name", "research.EnhancementSpyglass.0", true, new ItemStack(SPYGLASS))));
            }
            if (enableEnhancementAblaze) {
                BookPageCrafting ablazeCraft = new BookPageCrafting("", "ablaze");
                musketCategory.appendEntries(new BookEntry("research.EnhancementAblaze.name",
                  new BookPageItem("research.EnhancementAblaze.name", "research.EnhancementAblaze.0", true, new ItemStack(BLAZE_BARREL)),
                  ablazeCraft));
                blunderbussCategory.appendEntries(new BookEntry("research.EnhancementAblaze2.name",
                  new BookPageItem("research.EnhancementAblaze2.name", "research.EnhancementAblaze2.0", true, new ItemStack(BLAZE_BARREL)),
                  ablazeCraft));
            }
            if (enableEnhancementSpeedloader) {
                BookPageCrafting speedloaderCraft = new BookPageCrafting("", "speedloader1", "speedloader2");
                musketCategory.appendEntries(new BookEntry("research.EnhancementSpeedloader.name",
                  new BookPageItem("research.EnhancementSpeedloader.name", "research.EnhancementSpeedloader.0", true, new ItemStack(BOLT_ACTION)),
                  speedloaderCraft));
                blunderbussCategory.appendEntries(new BookEntry("research.EnhancementSpeedloader2.name",
                  new BookPageItem("research.EnhancementSpeedloader2.name", "research.EnhancementSpeedloader2.0", true, new ItemStack(BOLT_ACTION)),
                  speedloaderCraft));
            }
            if (enableEnhancementRecoil) {
                blunderbussCategory.appendEntries(new BookEntry("research.EnhancementRecoil.name",
                  new BookPageItem("research.EnhancementRecoil.name", "research.EnhancementRecoil.0", true, new ItemStack(RECOIL_PAD)),
                  new BookPageCrafting("", "recoil")));
            }

            BookCategory pistolCategory = new FlintlockBookCategory("category.Pistol.name",
              new BookEntry("research.Pistol.name",
                new BookPageItem("research.Pistol.name", "research.Pistol.0", new ItemStack(PISTOL)),
                new BookPageCrafting("", "pistol")));

            if (enableEnhancementRevolver) {
                pistolCategory.appendEntries(new BookEntry("research.EnhancementRevolver.name",
                  new BookPageItem("research.EnhancementRevolver.name", "research.EnhancementRevolver.0", true, new ItemStack(REVOLVER_CHAMBER)),
                  new BookPageCrafting("", "revolver")));
            }
            if (enableEnhancementSilencer) {
                pistolCategory.appendEntries(new BookEntry("research.EnhancementSilencer.name",
                  new BookPageItem("research.EnhancementSilencer.name", "research.EnhancementSilencer.0", true, new ItemStack(MAKESHIFT_SUPPRESSOR)),
                  new BookPageCrafting("", "silencer")));
            }
            if (enableEnhancementSpeedy) {
                pistolCategory.appendEntries(new BookEntry("research.EnhancementSpeedy.name",
                  new BookPageItem("research.EnhancementSpeedy.name", "research.EnhancementSpeedy.0", true, new ItemStack(BREECH)),
                  new BookPageCrafting("", "speedy")));
            }

            BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION, musketCategory);
            BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION, blunderbussCategory);
            BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION, pistolCategory);
        }
        if (enableRL) {
            BookCategory rocketLauncherCategory = new BookCategory("category.RocketLauncher.name",
              new BookEntry("research.RocketLauncher.name",
                new BookPageItem("research.RocketLauncher.name", "research.RocketLauncher.0", new ItemStack(ROCKET_LAUNCHER)),
                new BookPageCrafting("", "rocket1", "rocket2", "rocket3", "rocket4")));

            if (enableEnhancementFastRockets) {
                rocketLauncherCategory.appendEntries(new BookEntry("research.EnhancementFastRockets.name",
                  new BookPageItem("research.EnhancementFastRockets.name", "research.EnhancementFastRockets.0", true, new ItemStack(STREAMLINED_BARREL)),
                  new BookPageCrafting("", "fastRockets")));
            }
            if (enableEnhancementAmmo) {
                rocketLauncherCategory.appendEntries(new BookEntry("research.EnhancementAmmo.name",
                  new BookPageItem("research.EnhancementAmmo.name", "research.EnhancementAmmo.0", true, new ItemStack(EXTENDED_MAGAZINE)),
                  new BookPageCrafting("", "ammo1", "ammo2")));
            }
            if (enableEnhancementAirStrike) {
                rocketLauncherCategory.appendEntries(new BookEntry("research.EnhancemenAirStrike.name",
                  new BookPageItem("research.EnhancementAirStrike.name", "research.EnhancementAirStrike.0", true, new ItemStack(AIR_STRIKE_CONVERSION_KIT)),
                  new BookPageCrafting("", "airStrike1", "airStrike2")));
            }

            BookCategory rocketsCategory = new BookCategory("category.Rockets.name");
            if (enableRocket) {
                rocketsCategory.appendEntries(new BookEntry("research.Rocket.name",
                  new BookPageItem("research.Rocket.name", "research.Rocket.0", true, new ItemStack(ROCKET)),
                  new BookPageCrafting("", "normalRocket1", "normalRocket2")));
            }
            if (enableRocketConcussive) {
                BookPageCrafting crafting = enableRocket ? new BookPageCrafting("", "concussiveRocket") : new BookPageCrafting("", "concussiveRocket1", "concussiveRocket2");
                rocketsCategory.appendEntries(new BookEntry("research.RocketConcussive.name",
                  new BookPageItem("research.RocketConcussive.name", "research.RocketConcussive.0", true, new ItemStack(CONCUSSIVE_ROCKET)),
                  crafting));
            }
            if (enableRocketMining) {
                rocketsCategory.appendEntries(new BookEntry("research.RocketMining.name",
                  new BookPageItem("research.RocketMining.name", "research.RocketMining.0", true, new ItemStack(MINING_ROCKET)),
                  new BookPageCrafting("", "miningRocket")));
            }

            BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION, rocketLauncherCategory);
            BookPageRegistry.addCategoryToSection(EsteemedInnovation.FLINTLOCK_SECTION, rocketsCategory);
        }

        if (enableSpyglass) {
            BookPageRegistry.addCategoryToSection(GADGET_SECTION, 1,
              new BookCategory("category.Spyglass.name",
                new BookEntry("research.Spyglass.name",
                  new BookPageItem("research.Spyglass.name", "research.Spyglass.0", new ItemStack(SPYGLASS)),
                  new BookPageCrafting("", "spyglass1", "spyglass2"))));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(SPYGLASS);
        registerModel(MUSKET_CARTRIDGE);
        registerModel(ROCKET);
        registerModel(CONCUSSIVE_ROCKET);
        registerModel(MINING_ROCKET);
        registerModel(BLAZE_BARREL);
        registerModel(REVOLVER_CHAMBER);
        registerModel(BREECH);
        registerModel(MAKESHIFT_SUPPRESSOR);
        registerModel(RECOIL_PAD);
        registerModel(BOLT_ACTION);
        registerModel(SPYGLASS);
        registerModel(STREAMLINED_BARREL);
        registerModel(AIR_STRIKE_CONVERSION_KIT);
        registerModel(EXTENDED_MAGAZINE);
        registerFirearmModel(ROCKET_LAUNCHER);
        registerFirearmModel(PISTOL);
        registerFirearmModel(MUSKET);
        registerFirearmModel(BLUNDERBUSS);
    }

    private void registerFirearmModel(Item firearm) {
        List<ModelResourceLocation> locations = UtilEnhancements.registerEnhancementsForItem(firearm)
          .stream()
          .map(r -> new ModelResourceLocation(r, "inventory"))
          .collect(Collectors.toList());
        // No enhancement.
        locations.add(new ModelResourceLocation(firearm.getRegistryName(), "inventory"));
        ModelBakery.registerItemVariants(firearm, locations.toArray(new ModelResourceLocation[locations.size()]));
        ModelLoader.setCustomMeshDefinition(firearm, stack -> {
            ResourceLocation loc = UtilEnhancements.getIconFromEnhancement(stack);
            if (loc == null) {
                loc = stack.getItem().getRegistryName();
            }
            return new ModelResourceLocation(loc, "inventory");
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void initClient() {
        RenderingRegistry.registerEntityRenderingHandler(EntityRocket.class, RenderRocket::new);
    }

    @Override
    public void loadConfigurationOptions(Configuration config) {
        expensiveMusketRecipes = config.get(CATEGORY_WEAPONS, "Hardcore Musket Cartridge recipe (1 gunpowder per cartridge)", false).getBoolean();
        enableFirearms = config.get(CATEGORY_WEAPONS, "Enable firearms", true).getBoolean();
        enableRL = config.get(CATEGORY_WEAPONS, "Enable Rocket Launcher", true).getBoolean();
        enableRocket = config.get(CATEGORY_WEAPONS, "Enable Normal Rocket", true).getBoolean();
        enableRocketConcussive = config.get(CATEGORY_WEAPONS, "Enable Concussive Rocket", true).getBoolean();
        enableRocketMining = config.get(CATEGORY_WEAPONS, "Enable Mining Charge", true).getBoolean();
        enableEnhancementAblaze = config.get(CATEGORY_WEAPONS, "Enable Blaze Barrel enhancement", true).getBoolean();
        enableEnhancementRevolver = config.get(CATEGORY_WEAPONS, "Enable Revolver enhancement", true).getBoolean();
        enableEnhancementSpeedloader = config.get(CATEGORY_WEAPONS, "Enable Bolt Action enhancement", true).getBoolean();
        enableEnhancementSilencer = config.get(CATEGORY_WEAPONS, "Enable Makeshift Suppressor enhancement", true).getBoolean();
        enableEnhancementRecoil = config.get(CATEGORY_WEAPONS, "Enable Recoil Pad enhancement", true).getBoolean();
        enableEnhancementSpeedy = config.get(CATEGORY_WEAPONS, "Enable Breech Loader enhancement", true).getBoolean();
        enableEnhancementFastRockets = config.get(CATEGORY_WEAPONS, "Enable Streamlined Barrel enhancement", true).getBoolean();
        enableEnhancementAmmo = config.get(CATEGORY_WEAPONS, "Enable extended Magazine enhancement", true).getBoolean();
        enableEnhancementAirStrike = config.get(CATEGORY_WEAPONS, "Enable Air Strike enhancement", true).getBoolean();
        musketDamage = Float.valueOf(config.get(CATEGORY_WEAPONS, "Musket damage", "20.0F").getString());
        pistolDamage = Float.valueOf(config.get(CATEGORY_WEAPONS, "Pistol damage", "15.0F").getString());
        blunderbussDamage = Float.valueOf(config.get(CATEGORY_WEAPONS, "Blunderbuss damage", "25.0F").getString());
        enableSpyglass = config.get(CATEGORY_ITEMS, "Enable Spyglass", true).getBoolean();
    }
}
