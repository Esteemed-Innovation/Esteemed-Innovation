package flaxbeard.steamcraft.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.modulartool.IToolUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author SatanicSanta
 */
public class ItemModularTool extends Item implements ISteamChargable {

    public ItemModularTool(){
        super();
    }

    @Override
    public boolean getIsRepairable(ItemStack par1Stack, ItemStack par2Stack){
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemstack){
        if (!itemstack.hasTagCompound()){
            itemstack.setTagCompound(new NBTTagCompound());
        }
        if (!itemstack.stackTagCompound.hasKey("steamFill")){
            itemstack.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!itemstack.stackTagCompound.hasKey("maxFill")){
            itemstack.stackTagCompound.setInteger("maxFill", 0);
        }
        return 1.0D - (itemstack.stackTagCompound.getInteger("steamFill") / (double) itemstack.stackTagCompound.getInteger("maxFill"));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack){
        if (!stack.hasTagCompound()){
            stack.setTagCompound(new NBTTagCompound());
        }
        if (!stack.stackTagCompound.hasKey("maxFill")){
            stack.stackTagCompound.setInteger("maxFill", 0);
        }
        return stack.stackTagCompound.getInteger("maxFill") > 0;
    }

    @Override
    public int steamPerDurability() {
        return 800;
    }

    @Override
    public boolean canCharge(ItemStack me) {
        return true;
    }

    public boolean hasPower(ItemStack me, int powerNeeded){
        if (!me.hasTagCompound()){
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("steamFill")){
            me.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!me.stackTagCompound.hasKey("maxFill")){
            me.stackTagCompound.setInteger("maxFill", 0);
        }
        if (me.stackTagCompound.getInteger("steamFill") > powerNeeded){
            return true;
        }
        return false;
    }

    public boolean hasUpgrade(ItemStack me, Item check){
        if (check == null){
            return false;
        }

        if (me.hasTagCompound()){
            if (me.stackTagCompound.hasKey("inv")){
                for (int i = 1; i < 10; i++){
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))){
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() == check){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public IToolUpgrade[] getUpgrades(ItemStack me){
        ArrayList<IToolUpgrade> upgrades = new ArrayList<IToolUpgrade>();
        if (me.hasTagCompound()){
            if (me.stackTagCompound.hasKey("inv")){
                for (int i = 2; i < 10; i++){
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))){
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() instanceof IToolUpgrade){
                            upgrades.add((IToolUpgrade) stack.getItem());
                        }
                    }
                }
            }
        }
        return upgrades.toArray(new IToolUpgrade[0]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4){
        super.addInformation(me, player, list, par4);
        if (me.hasTagCompound()){
            if (me.stackTagCompound.hasKey("inv")){
                for (int i = 3; i < 10; i++){
                    if (me.stackTagCompound.getCompoundTag("inv").hasKey(Integer.toString(i))){
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("inv").getCompoundTag(Integer.toString(i)));
                        list.add(EnumChatFormatting.RED + stack.getDisplayName());
                    }
                }
            }
        }
        if (!me.hasTagCompound()){
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("steamFill")){
            me.stackTagCompound.setInteger("steamFill", 0);
        }
        if (!me.stackTagCompound.hasKey("maxFill")){
            me.stackTagCompound.setInteger("maxFill", 0);
        }
    }
}
