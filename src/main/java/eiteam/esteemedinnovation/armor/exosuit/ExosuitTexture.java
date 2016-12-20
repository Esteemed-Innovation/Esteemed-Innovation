package eiteam.esteemedinnovation.armor.exosuit;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum ExosuitTexture {
    EXOSUIT("exo_%s"),
    EXOSUIT_GREY("exo_%s_grey"),
    PLATE_BRASS("exoPlateBrass_%s"),
    PLATE_ZINC("exoPlateZinc_%s"),
    PLATE_COPPER("exoPlateCopper_%s"),
    PLATE_ELEMENTUM("exoPlateElementum_%s"),
    PLATE_ENDERIUM("exoPlateEnderium_%s"),
    PLATE_FIERY("exoPlateFiery_%s"),
    PLATE_GOLD("exoPlateGold_%s"),
    PLATE_IRON("exoPlateIron_%s"),
    PLATE_LEAD("exoPlateLead_%s"),
    PLATE_SADIST("exoPlateSadist_%s"),
    PLATE_TERRASTEEL("exoPlateTerrasteel_%s"),
    PLATE_THAUMIUM("exoPlateThaumium_%s"),
    PLATE_VIBRANT("exoPlateVibrant_%s"),
    PLATE_YETI("exoPlateYeti_%s"),
    PLATE_GILDED("exoPlateGilded_%s"),
    TANK("exo_3", 1),
    TANK_GREY("exo_3_grey", 1),
    WINGS("wings", 1),
    ANCHOR_HEELS("anchor", 1),
    DRAGON_ROAR("dragonsroar", 1),
    FREQUENCY_SHIFTER("frequencyshifter", 1),
    RELOADING_HOLSTER("holster", 1);

    private final ResourceLocation resourceLocation1;
    private final ResourceLocation resourceLocation2;

    ExosuitTexture(String file) {
        resourceLocation1 = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + (String.format(file, "1")) + ".png");
        resourceLocation2 = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + (String.format(file, "2")) + ".png");
    }

    ExosuitTexture(String file, int part) {
        if (part == 1) {
            resourceLocation1 = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + file + ".png");
            resourceLocation2 = null;
        } else {
            resourceLocation1 = null;
            resourceLocation2 = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/armor/" + file + ".png");
        }
    }

    public void bindTexturePart(int part) {
        if (part == 1) {
            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation1);
        } else {
            Minecraft.getMinecraft().renderEngine.bindTexture(resourceLocation2);
        }
    }
}
