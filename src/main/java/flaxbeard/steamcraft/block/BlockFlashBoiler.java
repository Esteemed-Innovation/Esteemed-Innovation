package flaxbeard.steamcraft.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.block.BlockSteamTransporter;
import flaxbeard.steamcraft.tile.TileEntityFlashBoiler;

// Notes:
// ATM meta 1 has a special texture, because that's going to be the master TE for the multi.
// meta 1 is always the one with the lowest x, y, and z.

// meta 0  : not in multiblock
// meta 1-8: in multiblock, signifies which corner



public class BlockFlashBoiler extends BlockSteamTransporter{
	
	private final Random rand = new Random();
	
	public IIcon otherIcon;
	public IIcon specialIcon;
	
	public IIcon topLeft;
	public IIcon topLeftSide;
	public IIcon bottomLeft;
	public IIcon topRight;
	public IIcon topRightSide;
	public IIcon bottomRight;
	public IIcon topLeftF;
	public IIcon bottomLeftF;
	public IIcon topRightF;
	public IIcon bottomRightF;
	public IIcon topLeftO;
	public IIcon bottomLeftO;
	public IIcon topRightO;
	public IIcon bottomRightO;

	public BlockFlashBoiler(){
		super(Material.iron);
		
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityFlashBoiler();
	}
	
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
	//	System.out.println(world.isRemote ? "Client: " : "Server: "+"onBlockPreDestroy");
		// System.out.println(world.isRemote ? "Client: " : "Server: "+"breakBlock");
     	super.onBlockPreDestroy(world, x, y, z, meta);
		TileEntityFlashBoiler boiler = (TileEntityFlashBoiler)world.getTileEntity(x, y, z);

         if (boiler != null)
         {
             for (int i1 = 0; i1 < boiler.getSizeInventory(); ++i1)
             {
                 ItemStack itemstack = boiler.getStackInSlot(i1);

                 if (itemstack != null)
                 {
                     float f = this.rand.nextFloat() * 0.8F + 0.1F;
                     float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                     float f2 = this.rand.nextFloat() * 0.8F + 0.1F;

                     while (itemstack.stackSize > 0)
                     {
                         int j1 = this.rand.nextInt(21) + 10;

                         if (j1 > itemstack.stackSize)
                         {
                             j1 = itemstack.stackSize;
                         }

                         itemstack.stackSize -= j1;
                         EntityItem entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                         if (itemstack.hasTagCompound())
                         {
                             entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                         }

                         float f3 = 0.05F;
                         entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                         entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                         entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                         world.spawnEntityInWorld(entityitem);
                     }
                 }
                 boiler.setInventorySlotContents(i1, null);
                 
             }

             
         }
	        
         if (meta > 0){
				TileEntityFlashBoiler te = (TileEntityFlashBoiler) world.getTileEntity(x, y, z);
			//System.out.println(te.getMasterTileEntity().getBlockMetadata());
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
		this.blockIcon = p_149651_1_.registerIcon("steamcraft:flashBoiler");
		this.otherIcon = p_149651_1_.registerIcon("steamcraft:testFront");
		this.specialIcon = p_149651_1_.registerIcon("steamcraft:testSpecial");
		
		this.topLeft = p_149651_1_.registerIcon("steamcraft:flashBoilerTopLeft");
		this.topLeftSide = p_149651_1_.registerIcon("steamcraft:flashBoilerTopLeftSide");
		this.bottomLeft = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomLeft");
		this.topRight = p_149651_1_.registerIcon("steamcraft:flashBoilerTopRight");
		this.topRightSide = p_149651_1_.registerIcon("steamcraft:flashBoilerTopRightSide");

		this.bottomRight = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomRight");
		this.topLeftF = p_149651_1_.registerIcon("steamcraft:flashBoilerTopLeftO");
		this.bottomLeftF = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomLeftO");
		this.topRightF = p_149651_1_.registerIcon("steamcraft:flashBoilerTopRightO");
		this.bottomRightF = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomRightO");
		this.topLeftO = p_149651_1_.registerIcon("steamcraft:flashBoilerTopLeftT");
		this.bottomLeftO = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomLeftT");
		this.topRightO = p_149651_1_.registerIcon("steamcraft:flashBoilerTopRightT");
		this.bottomRightO = p_149651_1_.registerIcon("steamcraft:flashBoilerBottomRightT");

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
		default: 
			//System.out.println(l); 
		break;
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
			IIcon tex = otherIcon;
			if (meta == 1) {
				if (side == 0) {
					tex = topLeft;
				}
				else
				{
					tex = bottomLeft;
				}
			}
			if (meta == 2) {
				if (side == 2) {
					tex = bottomRight;
				}
				else if (side == 0) {
					tex = topRight;
				}
				else
				{
					tex = bottomLeft;
				}
			}
			if (meta == 3) {
				if (side == 3 || side == 0) {
					tex = bottomLeft;
				}
				else
				{
					tex = bottomRight;
				}
			}
			if (meta == 4) {
				tex = bottomRight;
			}
			if (meta == 5) {
				if (side == 1) {
					tex = topLeft;
				}
				else
				{
					tex = topLeftSide;
				}
			}
			if (meta == 6) {
				if (side == 2) {
					tex = topRightSide;
				}
				else if (side == 1) {
					tex = topRight;
				}
				else
				{
					tex = topLeftSide;
				}
			}
			if (meta == 7) {
				if (side == 3) {
					tex = topLeftSide;
				}
				else if (side == 1)  {
					tex = bottomLeft;
				}
				else
				{
					tex = topRightSide;
				}
			}
			if (meta == 8) {
				if (side == 1) {
					tex = bottomRight;
				}
				else
				{
					tex = topRightSide;
				}
			}
			if (side == boiler.getFront()){
				if (boiler.getBurning()) {
					if (tex == topLeftSide) {
						return topLeftO;
					}
					if (tex == topRightSide) {
						return topRightO;
					}
					if (tex == bottomRight) {
						return bottomRightO;
					}
					if (tex == bottomLeft) {
						return bottomLeftO;
					}
				}
				if (tex == topLeftSide) {
					return topLeftF;
				}
				if (tex == topRightSide) {
					return topRightF;
				}
				if (tex == bottomRight) {
					return bottomRightF;
				}
				if (tex == bottomLeft) {
					return bottomLeftF;
				}
			}
			return tex;
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
			            float yOffset = (float)y + 0.25F + random.nextFloat() * 0.8F;
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
	
	 public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	    {
	       
	        super.breakBlock(world, x, y, z, block, meta);
	        
	    }
	
}
