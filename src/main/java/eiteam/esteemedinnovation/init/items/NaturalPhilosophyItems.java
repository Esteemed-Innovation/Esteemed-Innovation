package eiteam.esteemedinnovation.init.items;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.storage.item.ItemKitBag;
import eiteam.esteemedinnovation.naturalphilosophy.ItemResearchLog;
import eiteam.esteemedinnovation.naturalphilosophy.ItemSoilSamplingKit;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static net.minecraft.init.Items.*;

public class NaturalPhilosophyItems implements IInitCategory {
    public enum Items {
        SOIL_SAMPLING_KIT(new ItemSoilSamplingKit(), "soil_sampling_kit"),
        BIOME_LOG(new ItemResearchLog(), "research_log_biome"),
        KIT_BAG(new ItemKitBag(), "kit_bag");

        public static Items[] LOOKUP = new Items[values().length];

        static {
            for (Items item : values()) {
                LOOKUP[item.ordinal()] = item;
            }
        }

        private Item item;

        Items(Item item, String name) {
            item.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            item.setCreativeTab(EsteemedInnovation.tab);
            item.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(item);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }
    @Override
    public void oreDict() {
        Items.values();
    }

    @Override
    public void recipes() {
        for (Items item : Items.LOOKUP) {
            switch (item) {
                case BIOME_LOG: {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(item.getItem(), BOOK,
                      OreDictEntries.DYE_WHITE, OreDictEntries.DYE_WHITE));
                    break;
                }
                case SOIL_SAMPLING_KIT: {
                    for (Item vItem : Item.REGISTRY) {
                        if (vItem instanceof ItemSpade) {
                            NBTTagCompound nbt = new NBTTagCompound();
                            int max = (int) (vItem.getMaxDamage() / 4F);
                            nbt.setInteger("MaxDamage", max);
                            nbt.setInteger("Damage", max);
                            ItemStack result = new ItemStack(item.getItem());
                            result.setTagCompound(nbt);
                            GameRegistry.addRecipe(new ShapedOreRecipe(result,
                              "MKS",
                              "WIW",
                              " W ",
                              'M', OreDictEntries.DYE_WHITE,
                              'K', Items.KIT_BAG.getItem(),
                              'S', SUGAR,
                              'W', POTIONITEM,
                              'I', vItem
                            ));
                        }
                    }
                    break;
                }
                case KIT_BAG: {
                    GameRegistry.addRecipe(new ShapedOreRecipe(item.getItem(),
                      "SSS",
                      "LWL",
                      " L ",
                      'S', OreDictEntries.STRING_ORE,
                      'L', OreDictEntries.LEATHER_ORE,
                      'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));
                    GameRegistry.addRecipe(new ShapedOreRecipe(item.getItem(),
                      "SSS",
                      "LWL",
                      " L ",
                      'S', OreDictEntries.STRING_ORE,
                      'L', RABBIT_HIDE,
                      'W', new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE)));
                    break;
                }
            }
        }
    }
}
