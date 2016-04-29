package flaxbeard.steamcraft.item.tool.steam;

import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;

public class ItemSteamToolUpgrade extends Item implements ISteamToolUpgrade {
    protected String[] myOverlays;
    protected String myInfo;
    protected int prio;
    private SteamToolSlot mySlot;
    public IIcon[] icons;

    public ItemSteamToolUpgrade(SteamToolSlot slot, String resourceLocation, String info, int priority) {
        mySlot = slot;
        if (info != null && !info.isEmpty()) {
            myInfo = info;
        } else {
            myInfo = null;
        }
        if (resourceLocation != null && !resourceLocation.isEmpty()) {
            if (isUniversal()) {
                myOverlays = new String[]{
                  resourceLocation + "Drill0",
                  resourceLocation + "Drill1",
                  resourceLocation + "Saw0",
                  resourceLocation + "Saw1",
                  resourceLocation + "Shovel0",
                  resourceLocation + "Shovel1"
                };
                icons = new IIcon[6];
            } else {
                myOverlays = new String[]{ resourceLocation + "0", resourceLocation + "1" };
                icons = new IIcon[2];
            }
        } else {
            myOverlays = null;
        }
        prio = priority;
    }

    @Override
    public boolean isUniversal() {
        return getToolSlot() == SteamToolSlot.TOOL_CORE;
    }

    @Override
    public int renderPriority() {
        return prio;
    }

    @Override
    public SteamToolSlot getToolSlot() {
        return mySlot;
    }

    @Override
    public String getInformation(ItemStack me, ItemStack tool) {
        return myInfo;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void registerIcons(IIconRegister ir) {
        if (myOverlays == null) {
            return;
        }
        if (isUniversal()) {
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
        } else {
            for (int i = 0; i < myOverlays.length; i++) {
                icons[i] = ir.registerIcon(myOverlays[i]);
            }
        }
    }

    @Override
    public IIcon[] getIIcons() {
        return icons;
    }

    public IIcon getIcon(ItemStack self, int pass) {
        return this.getIconIndex(self);
    }

    @Override
    public IIcon getIconIndex(ItemStack self) {
        return icons[0];
    }
}
