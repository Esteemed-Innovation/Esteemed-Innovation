package flaxbeard.steamcraft.item.armor.exosuit;

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemExosuitUpgrade extends Item implements IExosuitUpgrade {

    protected ResourceLocation myOverlay;
    protected String myInfo;
    protected int pri;
    private ExosuitSlot mySlot;

    public ItemExosuitUpgrade(ExosuitSlot slot, String loc, String info, int priority) {
        mySlot = slot;
        myInfo = info;
        myOverlay = loc == null || loc.isEmpty() ? null : new ResourceLocation(loc);
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
    public ResourceLocation getOverlay() {
        return myOverlay;
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() { return null; }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateModel(ModelBiped modelBiped, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {}

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
