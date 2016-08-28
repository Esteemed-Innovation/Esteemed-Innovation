package eiteam.esteemedinnovation.item;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.IExosuitTank;
import eiteam.esteemedinnovation.api.exosuit.IExosuitUpgrade;
import eiteam.esteemedinnovation.api.exosuit.ModelExosuitUpgrade;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelExosuit;
import eiteam.esteemedinnovation.client.render.model.exosuit.ModelExosuitTank;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemTank extends Item implements IExosuitTank, IExosuitUpgrade {
    int capacity;

    public ItemTank(int cap, String texLoc, String texLocGrey) {
        super();
        capacity = cap;
    }

    @Override
    public int renderPriority() {
        return 0;
    }

    @Override
    public ExosuitSlot getSlot() {
        return ExosuitSlot.BODY_TANK;
    }

    @Override
    public ResourceLocation getOverlay() {
        return null;
    }

    @Override
    public Class<? extends ModelExosuitUpgrade> getModel() {
        return ModelExosuitTank.class;
    }

    @Override
    public void updateModel(ModelBiped parentModel, EntityLivingBase entityLivingBase, ItemStack itemStack, ModelExosuitUpgrade modelExosuitUpgrade) {
        float pressure = 0.0F;
        if (itemStack.getMaxDamage() != 0) {
            pressure = ((float) itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float) itemStack.getMaxDamage();
        }

        modelExosuitUpgrade.nbtTagCompound.setFloat("pressure", pressure);

        int dye = -1;
        ItemExosuitArmor item = ((ItemExosuitArmor) itemStack.getItem());
        if (item.getStackInSlot(itemStack, 2) != null) {
            Item vanity = item.getStackInSlot(itemStack, 2).getItem();
            int[] ids = OreDictionary.getOreIDs(item.getStackInSlot(itemStack, 2));
            outerloop:
            for (int id : ids) {
                String str = OreDictionary.getOreName(id);
                if (str.contains("dye")) {
                    for (int i = 0; i < ModelExosuit.DYES.length; i++) {
                        if (ModelExosuit.DYES[i].equals(str.substring(3))) {
                            dye = 15 - i;
                            break outerloop;
                        }
                    }
                }
            }
        }

        modelExosuitUpgrade.nbtTagCompound.setInteger("dye", dye);
    }

    @Override
    public void writeInfo(List list) {
        //filler
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return true;
    }

    @Override
    public int getStorage(ItemStack stack) {
        return capacity;
    }

}
