package flaxbeard.steamcraft.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import duke605.ms.toolheads.api.ToolHeadsAPI;
import duke605.ms.toolheads.api.head.Head;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.item.ItemHead;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * @author SatanicSanta
 */
public class ToolHeadsIntegration {

    public static Item head;

    public static void register(){
        head = new ItemHead().setCreativeTab(Steamcraft.tabTools).setUnlocalizedName("toolHead");
        GameRegistry.registerItem(head, "toolHead");

        //brass
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.AXE, "BRASS", Config.brassChance, new ItemStack(head, 1, 0)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.HOE, "BRASS", Config.brassChance, new ItemStack(head, 1, 1)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.PICKAXE, "BRASS", Config.brassChance, new ItemStack(head, 1, 2)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.SHOVEL, "BRASS", Config.brassChance, new ItemStack(head, 1, 3)));

        //gilded
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.AXE, "GILDEDGOLD", Config.gildedChance, new ItemStack(head, 1, 4)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.HOE, "GILDEDGOLD", Config.gildedChance, new ItemStack(head, 1, 5)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.PICKAXE, "GILDEDGOLD", Config.gildedChance, new ItemStack(head, 1, 6)));
        ToolHeadsAPI.registerHead(new Head(Head.ToolType.SHOVEL, "GILDEDGOLD", Config.gildedChance, new ItemStack(head, 1, 7)));
    }

    public static void recipes(){
        //brass recipes
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.pick("BRASS")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 2)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.axe("BRASS")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 0)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.hoe("BRASS")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 1)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.shovel("BRASS")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 3)});

        //gilded iron recipes
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.pick("GILDEDGOLD")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 6)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.axe("GILDEDGOLD")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 4)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.hoe("GILDEDGOLD")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 5)});
        GameRegistry.addRecipe(new ItemStack(SteamcraftItems.shovel("GILDEDGOLD")), new Object[]{ "H", "S", "S", 'S', Items.stick, 'H', new ItemStack(head, 1, 7)});
    }
}
