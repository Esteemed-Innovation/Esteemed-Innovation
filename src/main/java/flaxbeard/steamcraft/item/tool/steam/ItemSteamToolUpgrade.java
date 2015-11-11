package flaxbeard.steamcraft.item.tool.steam;

import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;


import java.util.HashMap;
import java.util.List;

public class ItemSteamToolUpgrade extends Item implements ISteamToolUpgrade {
    public HashMap<String, Integer> basicEffects;
    protected ResourceLocation myOverlay;
    protected String myInfo;
    protected int prio;
    private SteamToolSlot mySlot;

    public ItemSteamToolUpgrade(SteamToolSlot slot, String resourceLocation, String info, int priority, HashMap<String, Integer> hash) {
        mySlot = slot;
        myInfo = info;
        myOverlay = resourceLocation == null || resourceLocation.isEmpty() ? null : new ResourceLocation(resourceLocation);
        prio = priority;
        basicEffects = hash;
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
    public ResourceLocation getOverlay() {
        return myOverlay;
    }

    @Override
    public void writeInfo(List list) {
        if (myInfo != null) {
            list.add(myInfo);
        }
    }
}
