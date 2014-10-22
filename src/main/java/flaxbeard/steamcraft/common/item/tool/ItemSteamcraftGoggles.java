package flaxbeard.steamcraft.common.item.tool;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ItemSteamcraftGoggles extends ItemSteamcraftArmor implements IExosuitUpgrade {

    public ItemSteamcraftGoggles(ArmorMaterial p_i45325_1_, int p_i45325_2_,
                                 int p_i45325_3_, Object repair, String n) {
        super(p_i45325_1_, p_i45325_2_, p_i45325_3_, repair, n);
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.headGoggles;
    }

    @Override
    public boolean hasOverlay() {
        return true;
    }

    @Override
    public ResourceLocation getOverlay() {
        return new ResourceLocation("steamcraft:textures/models/armor/" + this.name.substring(0, 1).toLowerCase() + this.name.substring(1) + "Exosuit.png");
    }

    @Override
    public boolean hasModel() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderModel(ModelExosuit model, Entity par1Entity, int armor,
                            float par7, ItemStack me) {
    }

    @Override
    public void writeInfo(List list) {
    }

}
