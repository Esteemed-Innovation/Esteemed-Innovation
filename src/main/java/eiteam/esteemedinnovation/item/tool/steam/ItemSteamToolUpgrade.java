package eiteam.esteemedinnovation.item.tool.steam;

import eiteam.esteemedinnovation.api.tool.ISteamToolUpgrade;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemSteamToolUpgrade extends Item implements ISteamToolUpgrade {
    private ResourceLocation[] myOverlays;
    private String myInfo;
    private int prio;
    private SteamToolSlot mySlot;

    public ItemSteamToolUpgrade(SteamToolSlot slot, String resourceLocation, String info, int priority) {
        mySlot = slot;
        if (info != null && !info.isEmpty()) {
            myInfo = info;
        } else {
            myInfo = null;
        }
        if (resourceLocation != null && !resourceLocation.isEmpty()) {
            if (isUniversal()) {
                myOverlays = new ResourceLocation[] {
                  new ResourceLocation(resourceLocation + "Drill0"),
                  new ResourceLocation(resourceLocation + "Drill1"),
                  new ResourceLocation(resourceLocation + "Saw0"),
                  new ResourceLocation(resourceLocation + "Saw1"),
                  new ResourceLocation(resourceLocation + "Shovel0"),
                  new ResourceLocation(resourceLocation + "Shovel1")
                };
            } else {
                myOverlays = new ResourceLocation[] {
                  new ResourceLocation(resourceLocation + "0"),
                  new ResourceLocation(resourceLocation + "1")
                };
            }
        } else {
            myOverlays = new ResourceLocation[] {};
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

    @Override
    public ResourceLocation[] getIIcons() {
        return myOverlays;
    }
}
