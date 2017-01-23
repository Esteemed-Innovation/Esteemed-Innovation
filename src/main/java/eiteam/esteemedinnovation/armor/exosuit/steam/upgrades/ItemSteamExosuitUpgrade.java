package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemSteamExosuitUpgrade extends Item implements ExosuitUpgrade {
    private ResourceLocation myOverlay;
    private String myInfo;
    private int pri;
    private ExosuitSlot mySlot;

    public ItemSteamExosuitUpgrade(ExosuitSlot slot, String loc, String info, int priority) {
        mySlot = slot;
        myInfo = info;
        myOverlay = loc == null || loc.isEmpty() ? null : new ResourceLocation(loc);
        pri = priority;
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EsteemedInnovation.upgrade;
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
    public void writeInfo(List<String> list) {
        if (myInfo != null) {
            list.add(myInfo);
        }
    }

    @Override
    public int renderPriority() {
        return pri;
    }

    @Override
    public String toString() {
        ResourceLocation overlay = getOverlay();
        return overlay == null ? "" : overlay.toString();
    }
}
