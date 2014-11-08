package flaxbeard.steamcraft.misc;

import cpw.mods.fml.common.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;
import vazkii.botania.api.item.IExtendedPlayerController;

@Optional.Interface(iface = "vazkii.botania.api.item.IExtendedPlayerController", modid = "Botania")
public class SteamcraftPlayerController extends PlayerControllerMP implements IExtendedPlayerController {

    private float distance = 0F;

    public SteamcraftPlayerController(Minecraft minecraft, NetHandlerPlayClient nhpc) {
        super(minecraft, nhpc);
    }

    public float getBlockReachDistance() {
        return super.getBlockReachDistance() + distance;
    }

    public float getReachDistanceExtension() {
        return distance;
    }

    public void setReachDistanceExtension(float f) {
        distance = f;
    }

}
