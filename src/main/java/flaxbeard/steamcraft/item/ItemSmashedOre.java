package flaxbeard.steamcraft.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.tile.TileEntitySmasher;

public class ItemSmashedOre extends Item {
    public static final Map<Integer, String[]> map = new HashMap<>();
    public static final Map<Integer, Integer> colors = new HashMap<>();

    public ItemSmashedOre() {
        super();
        setHasSubtypes(true);

        //standard
        registerEntry(0, "Iron", "ingotIron", 0xC0A188);
        registerEntry(1, "Gold", "ingotGold", 0xE0D500);
        registerEntry(2, "Copper", "ingotCopper", 0x94451F);
        registerEntry(3, "Zinc", "ingotZinc", 0xB0B0B3);
        registerEntry(4, "Tin", "ingotTin", 0x748EB1);
        registerEntry(5, "Nickel", "ingotNickel", 0x9A9A68);
        registerEntry(6, "Silver", "ingotSilver", 0x8EC5D0);
        registerEntry(7, "Lead", "ingotLead", 0x6F80C9);
        registerEntry(8, "Aluminum", "ingotAluminum", 0xE4E4E4);
        registerEntry(9, "Osmium", "ingotOsmium", 0x41597F);
        registerEntry(10, "Cobalt", "ingotCobalt", 0x193DA9);
        registerEntry(11, "Ardite", "ingotArdite", 0xA7890E);
        registerEntry(12, "Cinnabar", "quicksilver", 0x562526);

        ModelResourceLocation loc = new ModelResourceLocation("steamcraft:smashed_ore", "inventory");
        for (Integer meta : map.keySet()) {
            ModelLoader.setCustomModelResourceLocation(this, meta, loc);
        }

        //Potentially removes a recipe
        //TileEntitySmasher.REGISTRY.oreDicts.remove("oreIron");
    }

    private void registerEntry(int meta, String name, String smeltingResult, int color) {
        TileEntitySmasher.REGISTRY.registerSmashable("ore" + name, new ItemStack(this, 1, meta));
        map.put(meta, new String[] { name, smeltingResult });
        colors.put(meta, color);
    }

    public void registerDusts() {
        for(Entry<Integer, String[]> entry : map.entrySet()) {
            String name = entry.getValue()[0];
            OreDictionary.registerOre("dust" + name, new ItemStack(this, 1, entry.getKey()));
        }
    }

    public void addSmelting() {
        for(Entry<Integer, String[]> entry : map.entrySet()) {
            String smelting = entry.getValue()[1];

            List<ItemStack> stacks = OreDictionary.getOres(smelting);
            if (!stacks.isEmpty()) {
                GameRegistry.addSmelting(new ItemStack(SteamcraftItems.smashedOre, 1, entry.getKey()), stacks.get(0).copy(), 0.5F);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> list) {
        for(Entry<Integer, String[]> entry : map.entrySet()) {
            if (!OreDictionary.getOres("ore" + entry.getValue()[0]).isEmpty()) {
                list.add(new ItemStack(item, 1, entry.getKey()));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + map.get(stack.getItemDamage())[0];
    }
}
