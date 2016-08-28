package eiteam.esteemedinnovation.item.tool.steam;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.api.tool.*;
import eiteam.esteemedinnovation.gui.GuiEngineeringTable;
import eiteam.esteemedinnovation.init.items.tools.ToolItems;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.Set;

import javax.annotation.Nonnull;

public class ItemSteamShovel extends ItemSteamTool {
    // Taken from ItemSpade.
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(
      Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND,
      Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH);

    public ItemSteamShovel() {
        super(1.5F, -3.0F, ToolItems.Materials.STEAM_SHOVEL.getMaterial(), EFFECTIVE_ON);
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionShovel;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(LARGE_ICONS);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 128, 128, 64, 64);
    }

    @Override
    public String toolClass() {
        return "shovel";
    }

    @Override
    public int getToolInteger() {
        return SteamToolSlot.SHOVEL_CORE.tool;
    }

    @Override
    @Nonnull
    public SteamToolSlot getRedSlot() {
        return SteamToolSlot.SHOVEL_HEAD;
    }
}
