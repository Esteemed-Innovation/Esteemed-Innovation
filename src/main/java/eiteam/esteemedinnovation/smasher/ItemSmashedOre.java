package eiteam.esteemedinnovation.smasher;

import eiteam.esteemedinnovation.api.SmasherRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static eiteam.esteemedinnovation.commons.OreDictEntries.*;

public class ItemSmashedOre extends Item {
    public static final Map<Integer, String[]> map = new HashMap<>();
    public static final Map<Integer, Integer> colors = new HashMap<>();

    public ItemSmashedOre() {
        setHasSubtypes(true);

        //Potentially removes a recipe
        //TileEntitySmasher.REGISTRY.oreDicts.remove("oreIron");
    }

    public void registerEntry(Types entry) {
        int meta = entry.getMeta();
        SmasherRegistry.registerSmashable(entry.getInputOre(), (input, world) -> {
            // Ore doubling
            int amount = input.getCount();
            if (world.rand.nextInt(100) >= SmasherModule.smasherDoubleChance) {
                amount *= 2;
            }
            return Collections.singletonList(new ItemStack(this, amount, meta));
        });
        map.put(meta, new String[] { entry.getName(), entry.getSmeltingResult() });
        colors.put(meta, entry.getColor());
    }

    public void registerDusts() {
        for (Entry<Integer, String[]> entry : map.entrySet()) {
            String name = entry.getValue()[0];
            OreDictionary.registerOre(PREFIX_DUST + name, new ItemStack(this, 1, entry.getKey()));
        }
    }

    public void addSmelting() {
        for (Entry<Integer, String[]> entry : map.entrySet()) {
            String smelting = entry.getValue()[1];

            List<ItemStack> stacks = OreDictionary.getOres(smelting);
            if (!stacks.isEmpty()) {
                GameRegistry.addSmelting(new ItemStack(this, 1, entry.getKey()), stacks.get(0).copy(), 0.5F);
            }
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            for (Entry<Integer, String[]> entry : map.entrySet()) {
                items.add(new ItemStack(this, 1, entry.getKey()));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + map.get(stack.getItemDamage())[0];
    }

    public enum Types {
        IRON(0, "Iron", ORE_IRON, INGOT_IRON, 0xC0A188),
        GOLD(1, "Gold", ORE_GOLD, INGOT_GOLD, 0xE0D500),
        COPPER(2, "Copper", ORE_COPPER, INGOT_COPPER, 0x94451F),
        ZINC(3, "Zinc", ORE_ZINC, INGOT_ZINC, 0xB0B0B3),
        TIN(4, "Tin", ORE_TIN, INGOT_TIN, 0x748EB1),
        NICKEL(5, "Nickel", ORE_NICKEL, INGOT_NICKEL, 0x9A9A68),
        SILVER(6, "Silver", ORE_SILVER, INGOT_SILVER, 0x8EC5D0),
        LEAD(7, "Lead", ORE_LEAD, INGOT_LEAD, 0x6F80C9),
        ALUMINUM(8, "Aluminum", ORE_ALUMINUM, INGOT_ALUMINUM, 0xE4E4E4),
        OSMIUM(9, "Osmium", ORE_OSMIUM, INGOT_OSMIUM, 0x41597F),
        COBALT(10, "Cobalt", ORE_COBALT, INGOT_COBALT, 0x193DA9),
        ARDITE(11, "Ardite", ORE_ARDITE, INGOT_ARDITE, 0xA7890E),
        CINNABAR(12, "Cinnabar", ORE_CINNABAR, QUICKSILVER, 0x562526);

        private final int meta;
        private final String name;
        private final String inputOre;
        private final String smeltingResult;
        private final int color;

        Types(int meta, String name, String inputOre, String smeltingResult, int color) {
            this.meta = meta;
            this.name = name;
            this.inputOre = inputOre;
            this.smeltingResult = smeltingResult;
            this.color = color;
        }

        public int getMeta() {
            return meta;
        }

        public String getName() {
            return name;
        }

        public String getInputOre() {
            return inputOre;
        }

        public String getSmeltingResult() {
            return smeltingResult;
        }

        public int getColor() {
            return color;
        }
    }
}
