package flaxbeard.steamcraft.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;

// Notes:
// ATM meta 1 has a special texture, because that's going to be the master TE for the multi.
// meta 1 is always the one with the lowest x, y, and z.

// meta 0  : not in multiblock
// meta 1-8: in multiblock, signifies which corner



public class BlockFlashBoiler extends BlockContainer{
	public IIcon otherIcon;
	public IIcon specialIcon;
	
	
	public BlockFlashBoiler(){
		super(Material.iron);
		
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFlashBoiler();
	}
	
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
		if (meta > 0){
			TileEntityFlashBoiler te = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
			te.destroyMultiblock();
		}
		
	}
	
	public void checkMultiblock(World world, int x, int y, int z, boolean isBreaking, int frontSide){
		if (!world.isRemote){
			TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
			boiler.checkMultiblock(isBreaking, frontSide);
		}
		
	}
	
	public void registerBlockIcons(IIconRegister p_149651_1_){
		this.blockIcon = p_149651_1_.registerIcon("steamcraft:testSide");
		this.otherIcon = p_149651_1_.registerIcon("steamcraft:testFront");
		this.specialIcon = p_149651_1_.registerIcon("steamcraft:testSpecial");
		
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xf, float yf, float zf){
		if (world.getBlockMetadata(x, y, z) > 0){

			TileEntityFlashBoiler tileentityboiler = (TileEntityFlashBoiler)world.getTileEntity(x, y, z);

			if (player.getHeldItem() != null && player.getHeldItem().getItem() == Items.water_bucket) {
				if (tileentityboiler != null)
	            {
					tileentityboiler.fill(ForgeDirection.UP, new FluidStack(FluidRegistry.WATER, 1000),true);
					if (!player.capabilities.isCreativeMode) {
						player.inventory.consumeInventoryItem(Items.water_bucket);
						player.inventory.addItemStackToInventory(new ItemStack(Items.bucket));
					}
	            }
				return true;
			}
			else
			{
		        if (world.isRemote)
		        {
		            return true;
		        }
		        else
		        {
		
		            if (tileentityboiler != null)
		            {
		            	player.openGui(Steamcraft.instance, 0, world, x,y,z);
		            }
		
		            return true;
		        }
			}
		} else {
			return false;
		}
		
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack){
		//System.out.println("onBlockPlacedBy fired");
		int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		int frontSide = -1;
		switch(l){
		case 0: frontSide = 2; break;
		case 1: frontSide = 5; break;
		case 2: frontSide = 3; break;
		case 3: frontSide = 4; break;
		default: System.out.println(l); break;
		}

		checkMultiblock(world, x, y, z, false, frontSide);
	}
	
	public IIcon getIcon(IBlockAccess block, int x, int y, int z, int side){
		//System.out.println(meta);
		int meta = block.getBlockMetadata(x, y, z); 
		if (meta == 0){
			return blockIcon;
		} else {
			TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) block.getTileEntity(x, y, z);
			if (side == boiler.getFront()){
				return specialIcon;
			} else {
				return otherIcon;
			}
		}
		
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
		if (world.getBlockMetadata(x, y, z) < 5){
			TileEntityFlashBoiler boiler = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
					
					if (boiler.isBurning())
			        {
			            int front = boiler.getFront();
			            float xOffset = (float)x + 0.5F;
			            float yOffset = (float)y + 0.0F + random.nextFloat() * 6.0F / 12.0F;
			            float zOffset = (float)z + 0.5F;
			            float f3 = 0.52F;
			            float f4 = getRandomFlameOffset(random);

			            if (front == 4)
			            {
			                world.spawnParticle("smoke", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
//			                
//			                world.spawnParticle("smoke", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
//			                world.spawnParticle("flame", (double)(xOffset - f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			            }
			            else if (front == 5)
			            {
			                world.spawnParticle("smoke", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f3), (double)yOffset, (double)(zOffset + f4), 0.0D, 0.0D, 0.0D);
			            }
			            else if (front == 2)
			            {
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset - f3), 0.0D, 0.0D, 0.0D);
			            }
			            else if (front == 3)
			            {
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                f4 = getRandomFlameOffset(random);
			                
			                world.spawnParticle("smoke", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			                world.spawnParticle("flame", (double)(xOffset + f4), (double)yOffset, (double)(zOffset + f3), 0.0D, 0.0D, 0.0D);
			            }
			        }
		}
		
    }
	
	private float getRandomFlameOffset(Random random){
		return random.nextFloat() * 0.8F - 0.4F;
	}
	
}
