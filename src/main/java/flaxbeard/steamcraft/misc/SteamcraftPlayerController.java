package flaxbeard.steamcraft.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import vazkii.botania.api.item.IExtendedPlayerController;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)

@Optional.Interface(iface = "IExtendedPlayerController", modid = "Botania")
public class SteamcraftPlayerController extends PlayerControllerMP implements IExtendedPlayerController {

	private float distance = 0F;

	public SteamcraftPlayerController(Minecraft p_i45062_1_, NetHandlerPlayClient p_i45062_2_) {
		super(p_i45062_1_, p_i45062_2_);
	}

	@Override
	public float getBlockReachDistance() {
		return super.getBlockReachDistance() + distance;
	}

	@Override
	public void setReachDistanceExtension(float f) {
		distance = f;
	}

	@Override
	public float getReachDistanceExtension() {
		return distance;
	}
	
}
