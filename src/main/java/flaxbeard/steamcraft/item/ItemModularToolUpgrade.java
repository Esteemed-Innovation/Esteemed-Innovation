package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.modulartool.IModularToolUpgrade;
import flaxbeard.steamcraft.api.modulartool.ToolSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author SatanicSanta
 */
public class ItemModularToolUpgrade extends Item implements IModularToolUpgrade {

	protected ResourceLocation resourceLoc;
    protected String info;
    private ToolSlot slot;
    protected int priority;

    public ItemModularToolUpgrade(ToolSlot slot, String loc, String info, int priority){
        this.slot = slot;
        this.info = info;
        resourceLoc = loc == null || loc.isEmpty() ? null : new ResourceLocation(loc);
        this.priority = priority;
    }

    @Override
    public EnumRarity getRarity(ItemStack par1){
        return Steamcraft.upgrade;
    }

    @Override
    public ToolSlot getSlot(){
        return slot;
    }

    @Override
    public ResourceLocation getOverlay(){
        return resourceLoc;
    }

    @Override
    public void writeInfo(List list){
        if (info != null){
            list.add(info);
        }
    }

    @Override
    public int renderPriority(){
        return priority;
    }
}
