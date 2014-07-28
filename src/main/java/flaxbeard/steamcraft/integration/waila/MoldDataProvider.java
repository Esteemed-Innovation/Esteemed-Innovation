//package flaxbeard.steamcraft.integration.waila;
//
//import java.util.List;
//
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import flaxbeard.steamcraft.tile.TileEntityCrucible;
//import flaxbeard.steamcraft.tile.TileEntityMold;
//import flaxbeard.steamcraft.tile.TileEntityValvePipe;
//
//public class MoldDataProvider implements IWailaDataProvider{
//	@Override
//	public List<String> getWailaBody(ItemStack stack, List<String> tt, IWailaDataAccessor da, IWailaConfigHandler ch) {
//		 if (da.getTileEntity() instanceof TileEntityMold){
//			 TileEntityMold  te = (TileEntityMold) da.getTileEntity();
//			 if (te.mold[0] != null) {
//				 tt.add(I18n.format("steamcraft.waila.mold") + " " + te.mold[0].getDisplayName());
//			 }
//		 }
//		 return tt;
//	}
//
//	@Override
//	public List<String> getWailaHead(ItemStack stack, List<String> tt,
//			IWailaDataAccessor da, IWailaConfigHandler cj) {
//		return tt;
//	}
//
//	@Override
//	public ItemStack getWailaStack(IWailaDataAccessor da,
//			IWailaConfigHandler ch) {
//		return null;
//	}
//
//	@Override
//	public List<String> getWailaTail(ItemStack stack, List<String> tt,
//			IWailaDataAccessor da, IWailaConfigHandler ch) {
//		return tt;
//	}
//}