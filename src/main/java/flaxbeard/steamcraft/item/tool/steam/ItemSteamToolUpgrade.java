package flaxbeard.steamcraft.item.tool.steam;

import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemSteamToolUpgrade extends Item implements ISteamToolUpgrade {
    protected String[] myOverlays;
    protected String myInfo;
    protected int prio;
    private SteamToolSlot mySlot;
    public IIcon[] icons;

    public ItemSteamToolUpgrade(SteamToolSlot slot, String resourceLocation, String info, int priority) {
        mySlot = slot;
        myInfo = info;
        if (resourceLocation.isEmpty()) {
            myOverlays = new String[]{ resourceLocation + "0", resourceLocation + "1"};
        } else {
            myOverlays = null;
        }
        prio = priority;
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
    public void registerIcons(IIconRegister ir) {
        if (myOverlays != null) {
            for (int i = 0; i < myOverlays.length; i++) {
                icons[i] = ir.registerIcon(myOverlays[i]);
            }
        }
    }

    @Override
    public void writeInfo(List list) {
        if (myInfo != null) {
            list.add(myInfo);
        }
    }

    @Override
    public IIcon[] getIIcons() {
        return icons;
    }
}
