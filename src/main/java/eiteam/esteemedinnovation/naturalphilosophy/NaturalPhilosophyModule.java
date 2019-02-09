package eiteam.esteemedinnovation.naturalphilosophy;

import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.commons.util.RecipeUtility;
import eiteam.esteemedinnovation.storage.StorageModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Items.*;

public class NaturalPhilosophyModule extends ContentModule {
    public static Item SOIL_SAMPLING_KIT;
    public static Item BLANK_RESEARCH_LOG;
    public static Item BIOME_LOG;

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        SOIL_SAMPLING_KIT = setup(event, new ItemSoilSamplingKit(), "soil_sampling_kit");

        BLANK_RESEARCH_LOG = setup(event, new Item(), "research_log_blank");
        BIOME_LOG = setup(event, new ItemResearchLog(), "research_log_biome");
    }

    @Override
    public void recipes(RegistryEvent.Register<IRecipe> event) {
        RecipeUtility.addShapelessRecipe(event, false, "blank_research_log", BLANK_RESEARCH_LOG,
          PLANK_WOOD, PLANK_WOOD, STRING_ORE, PAPER, PAPER, PAPER);
        //TODO: make sure this works
        RecipeUtility.addRecipe(event, false, "research_log_copy", new ResearchLogCopyRecipe());

        RecipeUtility.addShapelessRecipe(event, false, "biome_log", BIOME_LOG,
          BLANK_RESEARCH_LOG, DIRT_ORE);
        RecipeUtility.addShapelessRecipe(event, false, "biome_log2", BIOME_LOG,
          BLANK_RESEARCH_LOG, SAND_ORE);

        for (Item vItem : Item.REGISTRY) {
            if (vItem instanceof ItemSpade) {
                NBTTagCompound nbt = new NBTTagCompound();
                int max = (int) (vItem.getMaxDamage() / 4F);
                nbt.setInteger("MaxDamage", max);
                nbt.setInteger("Damage", max);
                ItemStack result = new ItemStack(SOIL_SAMPLING_KIT);
                result.setTagCompound(nbt);
                RecipeUtility.addRecipe(event, false, vItem.getRegistryName().getResourcePath() + "soil", result,
                  "MKS",
                  "WIW",
                  " W ",
                  'M', DYE_WHITE,
                  'K', StorageModule.KIT_BAG,
                  'S', SUGAR,
                  'W', POTIONITEM,
                  'I', vItem
                );
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(ModelRegistryEvent event) {
        registerModel(SOIL_SAMPLING_KIT);
        registerModel(BIOME_LOG);
    }
}
