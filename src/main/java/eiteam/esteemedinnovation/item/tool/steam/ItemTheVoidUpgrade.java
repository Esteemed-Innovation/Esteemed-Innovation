package eiteam.esteemedinnovation.item.tool.steam;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.tool.ISteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;

public class ItemTheVoidUpgrade extends Item implements ISteamToolUpgrade {
    private ResourceLocation[] myOverlays = new ResourceLocation[6];

    public ItemTheVoidUpgrade() {
        String resource = "toolUpgrades/void";
        String[] overlays = new String[] {
          resource + "Drill0",
          resource + "Drill1",
          resource + "Saw0",
          resource + "Saw1",
          resource + "Shovel0",
          resource + "Shovel1"
        };
        for (int i = 0; i < 6; i++) {
            myOverlays[i] = new ResourceLocation(EsteemedInnovation.MOD_ID, overlays[i]);
        }
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public SteamToolSlot getToolSlot() {
        return SteamToolSlot.TOOL_CORE;
    }

    @Override
    public String getInformation(ItemStack me, ItemStack tool) {
        if (tool.hasTagCompound() && tool.getTagCompound().hasKey("voidInventory")) {
            NBTTagCompound nbt = tool.getTagCompound().getCompoundTag("voidInventory");
            int x = nbt.getInteger("x");
            int y = nbt.getInteger("y");
            int z = nbt.getInteger("z");
            return I18n.format("esteemedinnovation.void.desc", x, y, z);
        }
        return null;
    }

    @Override
    public ResourceLocation[] getIIcons() {
        return myOverlays;
    }

    @Override
    public boolean isUniversal() {
        return true;
    }
}
