package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSteamToolUpgrade extends Item implements SteamToolUpgrade {
    private ResourceLocation baseOverlay;
    private String myInfo;
    private int prio;
    private SteamToolSlot mySlot;

    public ItemSteamToolUpgrade(SteamToolSlot slot, String resourceLocation, String info, int priority) {
        mySlot = slot;
        myInfo = info != null && !info.isEmpty() ? info : null;
        baseOverlay = new ResourceLocation(resourceLocation);
        prio = priority;
    }

    @Override
    public boolean isUniversal() {
        return getToolSlot() == SteamToolSlot.TOOL_CORE;
    }

    @Override
    public SteamToolSlot getToolSlot() {
        return mySlot;
    }

    @Override
    public String getInformation(ItemStack me, ItemStack tool) {
        return myInfo;
    }

    @Override
    public ResourceLocation getBaseIcon() {
        return baseOverlay;
    }
}
