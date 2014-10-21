package flaxbeard.steamcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuit;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ItemExosuitUpgrade extends Item implements IExosuitUpgrade {

    protected ResourceLocation myOverlay;
    protected String myInfo;
    protected int pri;
    private ExosuitSlot mySlot;

    public ItemExosuitUpgrade(ExosuitSlot slot, String loc, String info, int priority) {
        mySlot = slot;
        myInfo = info;
        myOverlay = loc == "" || loc == null ? null : new ResourceLocation(loc);
        pri = priority;
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return Steamcraft.upgrade;
    }

    @Override
    public ExosuitSlot getSlot() {
        return mySlot;
    }

    @Override
    public boolean hasOverlay() {
        return myOverlay != null;
    }

    @Override
    public ResourceLocation getOverlay() {
        return myOverlay;
    }

    @Override
    public boolean hasModel() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderModel(ModelExosuit model, Entity entity, int armor, float size, ItemStack me) {
    }

    @Override
    public void writeInfo(List list) {
        if (myInfo != null) {
            list.add(myInfo);
        }
    }

    @Override
    public int renderPriority() {
        return pri;
    }

}
