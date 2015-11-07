package flaxbeard.steamcraft.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.tile.TileEntitySmasher;

public class ItemSmashedOre extends Item {

    private static final Map<Integer, String[]> map = new HashMap<Integer, String[]>();
    @SideOnly(Side.CLIENT)
    private Map<Integer, IIcon> icons;
    
    public ItemSmashedOre() {
        super();
        this.setHasSubtypes(true);

        //standard
        registerEntry(0,  "Iron",     "ingotIron");
        registerEntry(1,  "Gold",     "ingotGold");
        registerEntry(2,  "Copper",   "ingotCopper");
        registerEntry(3,  "Zinc",     "ingotZinc");
        registerEntry(4,  "Tin",      "ingotTin");
        registerEntry(5,  "Nickel",   "ingotNickel");
        registerEntry(6,  "Silver",   "ingotSilver");
        registerEntry(7,  "Lead",     "ingotLead");
        registerEntry(8,  "Aluminum", "ingotAluminum");
        registerEntry(9,  "Osmium",   "ingotOsmium");
        registerEntry(10, "Cobalt",   "ingotCobalt");
        registerEntry(11, "Ardite",   "ingotArdite");
        registerEntry(12, "Cinnabar", "quicksilver");
        
        //me
        registerEntry(13, "PoorIron",   "nuggetIron");
        registerEntry(14, "PoorGold",   "nuggetGold");
        registerEntry(15, "PoorCopper", "nuggetCopper");
        registerEntry(16, "PoorZinc",   "nuggetZinc");
        registerEntry(17, "PoorTin",    "nuggetTin");
        
        //Potentially removes a recipe
        //TileEntitySmasher.REGISTRY.oreDicts.remove("oreIron");
    }
    
    private void registerEntry(int meta, String name, String smeltingResult) {
    	TileEntitySmasher.REGISTRY.registerSmashable("ore" + name, new ItemStack(this, 1, meta));
    	map.put(meta, new String[]{ name, smeltingResult });
    }

    public void registerDusts() {
    	for(Entry<Integer, String[]> entry : map.entrySet()) {
    		String name = entry.getValue()[0];
    		
    		if (name.startsWith("Poor")) {
    			OreDictionary.registerOre("dustTiny" + name.substring(4), new ItemStack(this, 1, entry.getKey()));
    		} else {
    			OreDictionary.registerOre("dust" + name, new ItemStack(this, 1, entry.getKey()));
    		}
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
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
    	for(Entry<Integer, String[]> entry : map.entrySet()) {
    		if (!OreDictionary.getOres("ore" + entry.getValue()[0]).isEmpty())
    			list.add(new ItemStack(item, 1, entry.getKey()));
    	}
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + "." + map.get(stack.getItemDamage())[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int meta) {
        return icons.get(meta);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register) {
    	icons = new HashMap<Integer, IIcon>();
    	for (Entry<Integer, String[]> entry : map.entrySet()) {
    		icons.put(entry.getKey(), register.registerIcon(getIconString() + entry.getValue()[0]));
    	}
    }
}
