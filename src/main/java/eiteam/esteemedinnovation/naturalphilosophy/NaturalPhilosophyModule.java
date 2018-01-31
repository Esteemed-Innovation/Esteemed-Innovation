package eiteam.esteemedinnovation.naturalphilosophy;

import eiteam.esteemedinnovation.commons.init.ContentModule;
import eiteam.esteemedinnovation.storage.StorageModule;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static eiteam.esteemedinnovation.commons.OreDictEntries.*;
import static net.minecraft.init.Items.*;

public class NaturalPhilosophyModule extends ContentModule {
    public static Item SOIL_SAMPLING_KIT;
    public static Item BLANK_RESEARCH_LOG;
    public static Item BIOME_LOG;

    @Override
    public void create(Side side) {
        SOIL_SAMPLING_KIT = setup(new ItemSoilSamplingKit(), "soil_sampling_kit");

        BLANK_RESEARCH_LOG = setup(new Item(), "research_log_blank");
        BIOME_LOG = setup(new ItemResearchLog(), "research_log_biome");
    }

    @Override
    public void recipes(Side side) {
        //TODO: transfer recipes to json
        /*GameRegistry.addRecipe(new ShapelessOreRecipe(BLANK_RESEARCH_LOG,
          PLANK_WOOD, PLANK_WOOD, STRING_ORE, PAPER, PAPER, PAPER));
        GameRegistry.addRecipe(new ResearchLogCopyRecipe());

        GameRegistry.addRecipe(new ShapelessOreRecipe(BIOME_LOG,
          BLANK_RESEARCH_LOG, DIRT_ORE));
        GameRegistry.addRecipe(new ShapelessOreRecipe(BIOME_LOG,
          BLANK_RESEARCH_LOG, SAND_ORE));

        for (Item vItem : Item.REGISTRY) {
            if (vItem instanceof ItemSpade) {
                NBTTagCompound nbt = new NBTTagCompound();
                int max = (int) (vItem.getMaxDamage() / 4F);
                nbt.setInteger("MaxDamage", max);
                nbt.setInteger("Damage", max);
                ItemStack result = new ItemStack(SOIL_SAMPLING_KIT);
                result.setTagCompound(nbt);
                GameRegistry.addRecipe(new ShapedOreRecipe(result,
                  "MKS",
                  "WIW",
                  " W ",
                  'M', DYE_WHITE,
                  'K', StorageModule.KIT_BAG,
                  'S', SUGAR,
                  'W', POTIONITEM,
                  'I', vItem
                ));
            }
        }*/
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void preInitClient() {
        registerModel(SOIL_SAMPLING_KIT);
        registerModel(BIOME_LOG);
    }
}
