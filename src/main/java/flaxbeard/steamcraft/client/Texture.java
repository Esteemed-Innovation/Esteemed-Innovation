package flaxbeard.steamcraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

/**
 * @author dmillerw
 */
public enum  Texture {

    HORNS("armor/horns"),
    TOPHAT("armor/tophat"),
    TOPHAT_EMERALD("armor/tophatemerald"),
    POINTER("pointer");

    private final ResourceLocation resourceLocation;

    private Texture(String file) {
        resourceLocation = new ResourceLocation("steamcraft:textures/models/" + file + ".png");
    }

    public void bindTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
}
