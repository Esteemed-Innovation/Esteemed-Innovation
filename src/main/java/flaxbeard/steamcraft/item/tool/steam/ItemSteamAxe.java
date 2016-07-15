package flaxbeard.steamcraft.item.tool.steam;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.tool.ItemSteamTool;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.init.items.tools.ToolItems;

import java.util.Set;

import javax.annotation.Nonnull;

public class ItemSteamAxe extends ItemSteamTool {
    // Taken from ItemAxe.
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG,
      Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER,
      Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    public ItemSteamAxe() {
        super(8F, -3.2F, ToolItems.Materials.STEAM_SAW.getMaterial(), EFFECTIVE_ON);
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionAxe;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
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
