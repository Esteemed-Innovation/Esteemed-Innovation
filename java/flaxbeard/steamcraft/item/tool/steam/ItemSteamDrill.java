package flaxbeard.steamcraft.item.tool.steam;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import org.apache.commons.lang3.tuple.MutablePair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.ISteamChargable;

public class ItemSteamDrill extends ItemPickaxe implements ISteamChargable {
	public IIcon[] icon = new IIcon[2];
	public static HashMap<Integer,MutablePair<Integer,Integer>> stuff = new HashMap<Integer,MutablePair<Integer,Integer>>();
	
	public ItemSteamDrill() {
		super(EnumHelper.addToolMaterial("DRILL", 2, 1600, 1.0F, -1.0F, 0));
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
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
    {
		//this.checkNBT(stack);
		//int ticks = stack.stackTagCompound.getInteger("ticks");
		return icon[0];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{
		this.icon[0] = ir.registerIcon("steamcraft:drill0");
		this.icon[1] = ir.registerIcon("steamcraft:drill1");
	}
	
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }
	
    public void onUpdate(ItemStack stack, World par2World, Entity player, int par4, boolean par5) {
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
    	
    	MutablePair info = stuff.get(player.getEntityId());
    	int ticks = (Integer) info.left;
    	int speed = (Integer) info.right;
    	if (speed <= 100) {
    		speed+=Math.min(5,100-speed);
    		stack.damageItem(1, player);
    	}
		stuff.put(player.getEntityId(), MutablePair.of(ticks, speed));
    	return stack;
    }
    
    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta)
    {
    	return 1.0F;
    }

	@Override
	public int steamPerDurability() {
		return 1;
	}

	@Override
	public boolean canCharge(ItemStack me) {
		return true;
	}

}
