package eiteam.esteemedinnovation.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.network.NetHandlerPlayClient;

public class PlayerController extends PlayerControllerMP {
    private double distance = 0F;

    public PlayerController(Minecraft minecraft, NetHandlerPlayClient nhpc) {
        super(minecraft, nhpc);
    }

    @Override
    public float getBlockReachDistance() {
        return super.getBlockReachDistance() + (float) distance;
    }

    public double getReachDistanceExtension() {
        return distance;
    }

    public void setReachDistanceExtension(double d) {
        distance = d;
    }

}
