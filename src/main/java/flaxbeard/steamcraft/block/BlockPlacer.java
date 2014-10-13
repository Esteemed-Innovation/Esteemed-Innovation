package flaxbeard.steamcraft.block;

public class BlockPlacer /*extends BlockSteamTransporter implements IWrenchable*/ {/*

    IIcon frontIcon;
    private final Random rand = new Random();

    public BlockPlacer() {
        super(Material.iron);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int someInt) {
        return new TileEntityBlockPlacer();
    }

    public boolean isOpaqueCube()
    {
        return false;
    }



    public IIcon getIcon(int side, int meta)
    {
        return side == meta ? frontIcon : blockIcon;
    }

    public void registerBlockIcons(IIconRegister ir)
    {
        this.blockIcon = ir.registerIcon("steamcraft:testSide");
        this.frontIcon = ir.registerIcon("steamcraft:testFront");
    }

    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        int meta = determineOrientation(world, x, y, z, player);
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta){
        super.onBlockPreDestroy(world, x, y, z, meta);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null){
            if (te instanceof TileEntityBlockPlacer){
                TileEntityBlockPlacer bp = (TileEntityBlockPlacer) te;
                for (int i = 0; i < bp.getSizeInventory(); ++i){
                    ItemStack stack = bp.getStackInSlot(i);

                    if (stack != null){
                        float offsetX = this.rand.nextFloat() * 0.8F + 0.1F;
                        float offsetY = this.rand.nextFloat() * 0.8F + 0.1F;
                        float offsetZ = this.rand.nextFloat() * 0.8F + 0.1F;

                        EntityItem entityitem = new EntityItem(
                                world,
                                (double)((float)x + offsetX),
                                (double)((float)y + offsetY),
                                (double)((float)z + offsetZ),
                                new ItemStack(stack.getItem(), 1, stack.getItemDamage())
                        );

                        if (stack.hasTagCompound())
                        {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
                        }

                        float scale = 0.05F;
                        entityitem.motionX = (double)((float)this.rand.nextGaussian() * scale);
                        entityitem.motionY = (double)((float)this.rand.nextGaussian() * scale + 0.2F);
                        entityitem.motionZ = (double)((float)this.rand.nextGaussian() * scale);
                        world.spawnEntityInWorld(entityitem);
                    }
                }
            }
        }
    }

    public static int determineOrientation(World world, int x, int y, int z, EntityLivingBase player)
    {
        if (MathHelper.abs((float)player.posX - (float)x) < 2.0F && MathHelper.abs((float)player.posZ - (float)z) < 2.0F)
        {
            double d0 = player.posY + 1.82D - (double)player.yOffset;

            if (d0 - (double)y > 2.0D)
            {
                return 1;
            }

            if ((double)y - d0 > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }





    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        int meta = world.getBlockMetadata(x, y, z);
        world.setBlockMetadataWithNotify(x, y, z, side == meta ? ForgeDirection.getOrientation(side).getOpposite().ordinal() : side, 2);
        return true;
    }*/

}

