package flaxbeard.steamcraft.item.tool.steam;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamChargable;

public class ItemSteamShovel extends ItemSpade implements ISteamChargable {
	public IIcon[] icon = new IIcon[2];
	public static HashMap<Integer,MutablePair<Integer,Integer>> stuff = new HashMap<Integer,MutablePair<Integer,Integer>>();
	
	public ItemSteamShovel() {
		super(EnumHelper.addToolMaterial("SHOVEL", 2, 1600, 1.0F, -1.0F, 0));
	}
	
	@Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
    {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_)
    {
        return true;
    }
	
	public static void checkNBT(EntityPlayer player) {
		if (!stuff.containsKey(player.getEntityId())) {
			stuff.put(player.getEntityId(), MutablePair.of(0,0));
		}
	}

	
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
    	this.checkNBT(player);

    	MutablePair info = stuff.get(player.getEntityId());
    	int ticks = (Integer) info.left;
    	return this.icon[ticks > 125 ? 0 : 1];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{
		this.icon[0] = this.itemIcon = ir.registerIcon("steamcraft:shovel0");
		this.icon[1] = ir.registerIcon("steamcraft:shovel1");
	}
	
    public void onUpdate(ItemStack stack, World par2World, Entity player, int par4, boolean par5) {
    	if (!stack.hasTagCompound()) {
    		stack.setTagCompound(new NBTTagCompound());
    	}
    	if (!stack.stackTagCompound.hasKey("player")) {
    		stack.stackTagCompound.setInteger("player", -1);
    	}
    	int oldPlayer = stack.stackTagCompound.getInteger("player");
    	if (oldPlayer != player.getEntityId()) {
    		stack.stackTagCompound.setInteger("player", player.getEntityId());

    	}
    	if (player instanceof EntityPlayer) {
	    	this.checkNBT((EntityPlayer) player);
	    	MutablePair info = stuff.get(player.getEntityId());
	    	int ticks = (Integer) info.left;
	    	int speed = (Integer) info.right;
	    	ticks += speed;
	    	if (speed > 0) {
	    		speed--;
	    	}
	    	ticks = ticks%250;
			stuff.put(player.getEntityId(), MutablePair.of(ticks, speed));
    	}
    }
    
   
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player)
    {
    	this.checkNBT(player);
		if (stack.getItemDamage() < stack.getMaxDamage()-1) {
	    	MutablePair info = stuff.get(player.getEntityId());
	    	int ticks = (Integer) info.left;
	    	int speed = (Integer) info.right;
	    	if (speed <= 100) {
	    		speed+=Math.min(9,100-speed);
	    		stack.damageItem(1, player);
	    	}
			stuff.put(player.getEntityId(), MutablePair.of(ticks, speed));
			System.out.println(speed);
		}
    	return stack;
    }
    
    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
    	return 1.0F;
    }

	@Override
	public int steamPerDurability() {
		return 20;
	}

	@Override
	public boolean canCharge(ItemStack me) {
		return true;
	}

}
