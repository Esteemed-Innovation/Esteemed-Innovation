package flaxbeard.steamcraft.item;

import flaxbeard.steamcraft.api.exosuit.ExosuitSlot;
import flaxbeard.steamcraft.api.exosuit.IExosuitTank;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.api.exosuit.ModelExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuit;
import flaxbeard.steamcraft.client.render.model.exosuit.ModelExosuitTank;
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
        return ExosuitSlot.bodyTank;
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
