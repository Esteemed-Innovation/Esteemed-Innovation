//package flaxbeard.steamcraft.integration.waila;
//
//import java.util.List;
//
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import net.minecraft.item.ItemStack;
//import flaxbeard.steamcraft.api.CrucibleLiquid;
//import flaxbeard.steamcraft.tile.TileEntityCrucible;
//
//public class CrucibleDataProvider implements IWailaDataProvider{
//	@Override
//	public List<String> getWailaBody(ItemStack stack, List<String> tt, IWailaDataAccessor da, IWailaConfigHandler ch) {
//		 if (da.getTileEntity() instanceof TileEntityCrucible){
//			 TileEntityCrucible  te = (TileEntityCrucible) da.getTileEntity();
//			 for (CrucibleLiquid liquid : te.contents) {
//				 int num = te.number.get(liquid);
//				 tt.add(num + " " + liquid.nugget.getDisplayName());
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