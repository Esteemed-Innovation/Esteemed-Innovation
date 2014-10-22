//package flaxbeard.steamcraft.integration.waila;
//
//import java.util.List;
//
//import mcp.mobius.waila.api.IWailaConfigHandler;
//import mcp.mobius.waila.api.IWailaDataAccessor;
//import mcp.mobius.waila.api.IWailaDataProvider;
//import net.minecraft.client.resources.I18n;
//import net.minecraft.item.ItemStack;
//import flaxbeard.steamcraft.api.ISteamTransporter;
//import flaxbeard.steamcraft.tile.TileEntityItemMortar;
//import flaxbeard.steamcraft.tile.TileEntitySteamCharger;
//import flaxbeard.steamcraft.tile.TileEntitySteamHammer;
//import flaxbeard.steamcraft.tile.TileEntityValvePipe;
//
//public class SteamTransporterDataProvider implements IWailaDataProvider{
//	@Override
//	public List<String> getWailaBody(ItemStack stack, List<String> tt, IWailaDataAccessor da, IWailaConfigHandler ch) {
//		 if (da.getTileEntity() instanceof ISteamTransporter){
//			 ISteamTransporter  te = (ISteamTransporter) da.getTileEntity();
//			 tt.add(te.getSteam() + "/" + te.getCapacity() + " SU");
//			 tt.add((int)(te.getPressure()*100.0F) + "% " + I18n.format("steamcraft.waila.pressure"));
//			 if (te instanceof TileEntityValvePipe) {
//				 tt.add(((TileEntityValvePipe)te).open ? I18n.format("steamcraft.waila.valveOpen") : I18n.format("steamcraft.waila.valveClosed"));
//			 }
//			 if (te instanceof TileEntitySteamHammer) {
//				 if (((TileEntitySteamHammer)te).cost > 0) {
//					 tt.add((int)((float)(100F*((TileEntitySteamHammer)te).progress)/(float)((TileEntitySteamHammer)te).cost) + "% " + I18n.format("steamcraft.waila.complete"));
//				 }
//			 }
//			 if (te instanceof TileEntityItemMortar) {
//				 if (((TileEntityItemMortar)te).hasTarget) {
//					 int dimNum = ((TileEntityItemMortar)te).getWorldObj().provider.dimensionId;
//					 String dimension = I18n.format("steamcraft.astrolabe.dimension") + " " + dimNum;
//					 if (dimNum == 0) {
//						 dimension = I18n.format("steamcraft.astrolabe.dimension.overworld");
//					 }
//					 if (dimNum == -1) {
//						 dimension = I18n.format("steamcraft.astrolabe.dimension.nether");
//					 }
//					 if (dimNum == 1) {
//						 dimension = I18n.format("steamcraft.astrolabe.dimension.end");
//					 }
//					 tt.add(I18n.format("steamcraft.astrolabe.target") + " " + ((TileEntityItemMortar)te).xT + ", " + ((TileEntityItemMortar)te).zT + " " + I18n.format("steamcraft.astrolabe.in") + " " + dimension);
//				 }
//				 else
//				 {
//					 tt.add(I18n.format("steamcraft.astrolabe.noTarget"));
//				 }
//			 }
//			 if (te instanceof TileEntitySteamCharger) {
//				 if (((TileEntitySteamCharger)te).getStackInSlot(0) != null) {
//					 float percent = 100F*(((TileEntitySteamCharger)te).getStackInSlot(0).getMaxDamage() - ((TileEntitySteamCharger)te).getStackInSlot(0).getItemDamage())/((TileEntitySteamCharger)te).getStackInSlot(0).getMaxDamage();
//					 tt.add(I18n.format("steamcraft.waila.filling") + " " + ((TileEntitySteamCharger)te).getStackInSlot(0).getDisplayName() + " (" + (int)percent + "%)");
//				 }
//			 }
//
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