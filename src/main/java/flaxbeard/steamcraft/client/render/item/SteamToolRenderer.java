//package flaxbeard.steamcraft.client.render;
//
//import net.minecraft.client.renderer.entity.RenderItem;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.minecraftforge.client.IItemRenderer;
//
//import org.apache.commons.lang3.tuple.MutablePair;
//
//import flaxbeard.steamcraft.item.tool.steam.ItemSteamDrill;
//
//public class SteamToolRenderer implements IItemRenderer {
//	private static RenderItem renderItem = new RenderItem();
//
//    @Override
//    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
//    	if (type == ItemRenderType.EQUIPPED) {
//            return true;
//    	}
//    	return false;
//    }
//
//    @Override
//    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
//                    ItemRendererHelper helper) {
//            return false;
//    }
//
//    @Override
//    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {
//    	if (type == ItemRenderType.EQUIPPED) {
//    		//Steamcraft.log.debug("TA");
//
//	    	IIcon icon = itemStack.getItem().getIconFromDamage(itemStack.getItemDamage());
//	    		//Steamcraft.log.debug("TA");
//	        	EntityPlayer player = (EntityPlayer) data[1];
//
//		    	MutablePair info = ItemSteamDrill.stuff.get(player.getEntityId());
//		    	int ticks = (Integer) info.left;
//		    	icon = itemStack.getItem().getIcon(itemStack, ((int)(ticks/4.0F))%2);
//	    		
//	    	renderItem.renderIcon(0, 0, icon, 16, 16);
//    	}
//    }
//}