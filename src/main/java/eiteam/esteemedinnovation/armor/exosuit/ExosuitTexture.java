package eiteam.esteemedinnovation.armor.exosuit;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public enum ExosuitTexture {
    STEAM_EXOSUIT("steam_exo_layer_%s"),
    STEAM_EXOSUIT_GREY("steam_exo_layer_%s_grey"),
    PLATE_BRASS("exo_plate_brass_%s"),
    PLATE_ZINC("exo_plate_zinc_%s"),
    PLATE_COPPER("exo_plate_copper_%s"),
    PLATE_ELEMENTUM("exo_plate_elementum_%s"),
    PLATE_ENDERIUM("exo_plate_enderium_%s"),
    PLATE_FIERY("exo_plate_fiery_%s"),
    PLATE_GOLD("exo_plate_gold_%s"),
    PLATE_IRON("exo_plate_iron_%s"),
    PLATE_LEAD("exo_plate_lead_%s"),
    PLATE_SADIST("exo_plate_sadist_%s"),
    PLATE_TERRASTEEL("exo_plate_terrasteel_%s"),
    PLATE_THAUMIUM("exo_plate_thaumium_%s"),
    PLATE_VIBRANT("exo_plate_vibrant_%s"),
    PLATE_YETI("exo_plate_yeti_%s"),
    PLATE_GILDED("exo_plate_gilded_iron_%s"),
    TANK("steam_exo_layer_3", 1),
    TANK_GREY("steam_exo_layer_3_grey", 1),
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
