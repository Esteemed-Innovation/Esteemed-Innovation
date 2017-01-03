package eiteam.esteemedinnovation.tools.steam;

import com.google.common.collect.Sets;
import eiteam.esteemedinnovation.api.tool.ItemSteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;
import java.util.Set;

import static eiteam.esteemedinnovation.tools.ToolsModule.STEAMSAW_MAT;

public class ItemSteamAxe extends ItemSteamTool {
    // Taken from ItemAxe.
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG,
      Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
      Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    public ItemSteamAxe() {
        super(8F, -3.2F, STEAMSAW_MAT, EFFECTIVE_ON);
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionAxe;
    }

    @Override
    public void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(LARGE_ICONS);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 64, 128, 64, 64);
    }

    @Override
    public String toolClass() {
        return "axe";
    }

    @Override
    public int getToolInteger() {
        return SteamToolSlot.SAW_CORE.tool;
    }

    @Nonnull
    @Override
    public SteamToolSlot getRedSlot() {
        return SteamToolSlot.SAW_HEAD;
    }
}
