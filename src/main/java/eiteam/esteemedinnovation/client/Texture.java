package eiteam.esteemedinnovation.client;

import eiteam.esteemedinnovation.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum  Texture {
    HORNS("armor/horns"),
    TOPHAT("armor/tophat"),
    TOPHAT_EMERALD("armor/tophatemerald"),
    POINTER("pointer");

    private final ResourceLocation resourceLocation;

    Texture(String file) {
        resourceLocation = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/" + file + ".png");
    }

    public void bindTexture() {
        Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation);
    }
}
