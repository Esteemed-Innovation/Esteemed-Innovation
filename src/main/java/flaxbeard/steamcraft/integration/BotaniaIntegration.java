package flaxbeard.steamcraft.integration;

import net.minecraft.item.Item;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import vazkii.botania.common.item.ModItems;
import cpw.mods.fml.common.registry.GameRegistry;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;
import flaxbeard.steamcraft.item.ItemExosuitUpgrade;

public class BotaniaIntegration {
    public static Item floralLaurel;

    public static void addItems() {
    	floralLaurel = new ItemExosuitUpgrade(ExosuitSlot.headHelm, "steamcraft:textures/models/armor/floralLaurel.png",null,5).setCreativeTab(Steamcraft.tab).setUnlocalizedName("steamcraft:floralLaurel").setTextureName("steamcraft:floralLaurel");
		GameRegistry.registerItem(floralLaurel, "floralLaurel");

    }

	public static void displayThings(MovingObjectPosition pos, RenderGameOverlayEvent.Post event) {
//		Minecraft mc = Minecraft.getMinecraft();
//		Block block = mc.theWorld.getBlock(pos.blockX, pos.blockY, pos.blockZ);
//		if(block instanceof IWandHUD)
//			((IWandHUD) block).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
	}
	
	public static Item twigWand() {
		return ModItems.twigWand;
	}

}
