package flaxbeard.steamcraft.item.tool.steam;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;

import java.util.ArrayList;

public class ItemTheVoidUpgrade extends Item implements ISteamToolUpgrade {
    private String[] myOverlays;
    private IIcon[] icons = new IIcon[6];

    public ItemTheVoidUpgrade() {
        String resource = "steamcraft:toolUpgrades/void";
        myOverlays = new String[] {
          resource + "Drill0",
          resource + "Drill1",
          resource + "Saw0",
          resource + "Saw1",
          resource + "Shovel0",
          resource + "Shovel1"
        };
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
            return I18n.format("steamcraft.void.desc", x, y, z);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void registerIcons(IIconRegister ir) {
        ArrayList<String> list = new ArrayList<>();
        for (String overlay : myOverlays) {
            int index = overlay.contains("0") ? 0 : 1;
            if (overlay.contains("Drill")) {
                index += 0;
            } else if (overlay.contains("Saw")) {
                index += 2;
            } else if (overlay.contains("Shovel")) {
                index += 4;
            }
            list.add(index, overlay);
        }
        for (int i = 0; i < list.size(); i++) {
            icons[i] = ir.registerIcon(list.get(i));
        }
    }

    @Override
    public IIcon[] getIIcons() {
        return icons;
    }

    @Override
    public IIcon getIcon(ItemStack self, int pass) {
        return this.getIconIndex(self);
    }

    @Override
    public IIcon getIconIndex(ItemStack self) {
        return icons[0];
    }

    @Override
    public boolean isUniversal() {
        return true;
    }
}
