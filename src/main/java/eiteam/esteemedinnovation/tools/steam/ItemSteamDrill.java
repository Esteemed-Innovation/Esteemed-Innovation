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

import static eiteam.esteemedinnovation.tools.ToolsModule.STEAMDRILL_MAT;

public class ItemSteamDrill extends ItemSteamTool {
    // Taken from ItemPickaxe.
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE,
      Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE,
      Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE,
      Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE,
      Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE,
      Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON,
      Blocks.STONE_PRESSURE_PLATE);

    public ItemSteamDrill() {
        super(1F, -2.8F, STEAMDRILL_MAT, EFFECTIVE_ON);
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionDrill;
    }

    @Override
    public void drawBackground(GuiContainer guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(LARGE_ICONS);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0, 128, 64, 64);
    }

    @Override
    public String toolClass() {
        return "pickaxe";
    }

    @Override
    public int getToolInteger() {
        return SteamToolSlot.DRILL_CORE.tool;
    }

    @Nonnull
    @Override
    public SteamToolSlot getRedSlot() {
        return SteamToolSlot.DRILL_HEAD;
    }
}
