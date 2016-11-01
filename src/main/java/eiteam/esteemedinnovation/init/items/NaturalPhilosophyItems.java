package eiteam.esteemedinnovation.init.items;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.misc.OreDictEntries;
import eiteam.esteemedinnovation.item.ItemKitBag;
import eiteam.esteemedinnovation.item.ItemResearchLog;
import eiteam.esteemedinnovation.item.ItemSoilSamplingKit;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import static net.minecraft.init.Items.BOOK;

public class NaturalPhilosophyItems implements IInitCategory {
    public enum Items {
        SOIL_SAPLING_KIT(new ItemSoilSamplingKit(), "soil_sampling_kit"),
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
                case SOIL_SAPLING_KIT: {
                    GameRegistry.addRecipe(new ShapelessOreRecipe(item.getItem(), BOOK,
                      OreDictEntries.DYE_WHITE, OreDictEntries.DYE_WHITE));
                }
            }
        }
    }
}
