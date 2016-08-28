package eiteam.esteemedinnovation.tile;

import eiteam.esteemedinnovation.Config;
import eiteam.esteemedinnovation.api.ISteamTransporter;
import eiteam.esteemedinnovation.api.IWrenchDisplay;
import eiteam.esteemedinnovation.api.IWrenchable;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.block.BlockVacuum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;

public class TileEntityVacuum extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay {
    private static final int VACUUM_STEAM_CONSUMPTION = Config.vacuumConsumption;

    // half angle of cone
    private static final float THETA = (float) Math.PI / 2.0F;
    public boolean active;
    public boolean powered = false;
    public boolean lastSteam = false;
    public int rotateTicks = 0;
    public int range = 9;
    private boolean isInitialized = false;

    public static boolean isLyingInCone(float[] x, float[] t, float[] b, float aperture) {
        // This is for our convenience
        float halfAperture = aperture / 2.F;

        // Vector pointing to X point from apex
        float[] apexToXVect = dif(t, x);

        // Vector pointing from apex to circle-center point.
        float[] axisVect = dif(t, b);

        // X is lying in cone only if it's lying in
        // infinite version of its cone -- that is,
        // not limited by "round basement".
        // We'll use dotProd() to
        // determine angle between apexToXVect and axis.
        boolean isInInfiniteCone = dotProd(apexToXVect, axisVect)
          / magn(apexToXVect) / magn(axisVect)
          >
          // We can safely compare cos() of angles
          // between vectors instead of bare angles.
          Math.cos(halfAperture);


        if (!isInInfiniteCone) {
            return false;
        }

        // X is contained in cone only if projection of apexToXVect to axis
        // is shorter than axis.
        // We'll use dotProd() to figure projection length.
        return dotProd(apexToXVect, axisVect) / magn(axisVect) < magn(axisVect);
    }

    public static float dotProd(float[] a, float[] b) {
        return a[0] * b[0] + a[1] * b[1] + a[2] * b[2];
    }

    public static float[] dif(float[] a, float[] b) {
        return (new float[]{
          a[0] - b[0],
          a[1] - b[1],
          a[2] - b[2]
        });
    }

    public static float magn(float[] a) {
        return (float) (Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]));
    }

    @Override
    public void update() {
        if (lastSteam != getSteamShare() > VACUUM_STEAM_CONSUMPTION) {
            markForResync();
        }
        lastSteam = getSteamShare() > VACUUM_STEAM_CONSUMPTION;
        if (!isInitialized) {
            powered = worldObj.isBlockPowered(pos);
            EnumFacing myDir = worldObj.getBlockState(pos).getValue(BlockVacuum.FACING);
            EnumFacing[] directions = new EnumFacing[5];
            int i = 0;
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (direction.getAxis() != myDir.getAxis()) {
                    directions[i] = direction;
                    i++;
                }
            }
            setDistributionDirections(Arrays.copyOf(directions, i));
            isInitialized = true;
        }
        super.update();
        if (!worldObj.isRemote) {
        	if ((getSteamShare() < VACUUM_STEAM_CONSUMPTION) || powered) {
        		active = false;
        	}
        	else {
        		active = true;
        		decrSteam(VACUUM_STEAM_CONSUMPTION);
        	}
        }
        if (active) {
            if (worldObj.isRemote) {
                rotateTicks++;
            }
            EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockVacuum.FACING);
            float[] M = {
              pos.getX() + 0.5F,
              pos.getY() + 0.5F,
              pos.getZ() + 0.5F
            };
            float[] N = {
              pos.getX() + 0.5F + range * dir.getFrontOffsetX(),
              pos.getY() + 0.5F + range * dir.getFrontOffsetY(),
              pos.getZ() + 0.5F + range * dir.getFrontOffsetZ()
            };
            //List entities = worldObj.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBox(xCoord+(dir.offsetX < 0 ? dir.offsetX * blocksInFront : 0), yCoord+(dir.offsetY < 0 ? dir.offsetY * blocksInFront : 0), zCoord+(dir.offsetZ < 0 ? dir.offsetZ * blocksInFront : 0), xCoord+1+(dir.offsetX > 0 ? dir.offsetX * blocksInFront : 0), yCoord+1+(dir.offsetY > 0 ? dir.offsetY * blocksInFront : 0), zCoord+1+(dir.offsetZ > 0 ? dir.offsetZ * blocksInFront : 0)));
            List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class,
              new AxisAlignedBB(pos.getX() - 20, pos.getY() - 20, pos.getZ() - 20, pos.getX() + 20, pos.getY() + 20, pos.getZ() + 20));
            for (int i = 0; i < 200; i++) {
                float[] X = {
                  (worldObj.rand.nextFloat() * 40.0F) - 20.0F + pos.getX(),
                  (worldObj.rand.nextFloat() * 40.0F) - 20.0F + pos.getY(),
                  (worldObj.rand.nextFloat() * 40.0F) - 20.0F + pos.getZ()
                };
                if (isLyingInCone(X, M, N, THETA) && worldObj.rayTraceBlocks(new Vec3d(X[0], X[1], X[2]),
                  new Vec3d(pos.getX() + 0.5F + dir.getFrontOffsetX(), pos.getY() + 0.5F + dir.getFrontOffsetY(),
                  pos.getZ() + 0.5F + dir.getFrontOffsetZ())) == null) {
                    Vec3d vec = new Vec3d(X[0] - M[0], X[1] - M[1], X[2] - M[2]);
                    vec = vec.normalize();
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, X[0], X[1], X[2], -vec.xCoord * 0.5F,
                      -vec.yCoord * 0.5F, -vec.zCoord * 0.5F);
                }
            }

            for (Entity entity : entities) {
                float[] X = {
                  (float) entity.posX,
                  (float) entity.posY,
                  (float) entity.posZ
                };

                if (isLyingInCone(X, M, N, THETA) && worldObj.rayTraceBlocks(
                  new Vec3d(entity.posX, entity.posY, entity.posZ),
                  new Vec3d(pos.getX() + 0.5F + dir.getFrontOffsetX(), pos.getY() + 0.5F + dir.getFrontOffsetY(),
                  pos.getZ() + 0.5F + dir.getFrontOffsetZ())) == null) {
                    if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isFlying &&
                      ((EntityPlayer) entity).capabilities.isCreativeMode)) {
                        Vec3d vec = new Vec3d(X[0] - M[0], X[1] - M[1], X[2] - M[2]);
                        vec = vec.normalize();
                        double y = vec.yCoord;
                        double x = vec.xCoord;
                        double z = vec.zCoord;
                        y *= 1;
                        if (entity.isSneaking()) {
                            x *= 0.25F;
                            y *= 0.25F;
                            z *= 0.25F;
                        }
                        entity.motionX -= x * 0.025F;
                        entity.motionY -= y * 0.05F;
                        entity.motionZ -= z * 0.025F;

                        entity.fallDistance = 0.0F;
                    }
                }
            }

            List<EntityItem> list = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
              pos.getX() + dir.getFrontOffsetX() * 0.25F, pos.getY() + dir.getFrontOffsetY() * 0.25F,
              pos.getZ() + dir.getFrontOffsetZ() * 0.25F, pos.getX() + 1.0D + dir.getFrontOffsetX() * 0.25F,
              pos.getY() + 1.0D + dir.getFrontOffsetY() * 0.25F, pos.getZ() + 1.0D + dir.getFrontOffsetZ() * 0.25F));

            // TODO: This may be able to be optimized by iterating over the entire list. Test.
            if (list.size() > 0) {
                EntityItem item = list.get(0);
                BlockPos offsetPos = new BlockPos(pos.getX() - dir.getFrontOffsetX(),
                  pos.getY() - dir.getFrontOffsetY(), pos.getZ() - dir.getFrontOffsetZ());
                TileEntity tile = worldObj.getTileEntity(offsetPos);
                if (tile != null && tile instanceof ISidedInventory) {
                    ISidedInventory inv = (ISidedInventory) tile;
                    int[] access = inv.getSlotsForFace(dir.getOpposite());
                    for (int slot : access) {
                        putInInventory(item, slot, inv);
                    }
                } else if (tile != null && tile instanceof IInventory) {
                    IInventory inv = (IInventory) tile;
                    for (int i = 0; i < inv.getSizeInventory(); i++) {
                        putInInventory(item, i, inv);
                    }
                }
            }
        }
    }

    private void putInInventory(EntityItem item, int slot, IInventory inv) {
        ItemStack checkStack1 = null;
        ItemStack checkStack2 = null;
        ItemStack stackInSlot = inv.getStackInSlot(slot);
        if (inv.getStackInSlot(slot) != null) {
            checkStack1 = stackInSlot.copy();
            checkStack1.stackSize = 1;
            checkStack2 = item.getEntityItem().copy();
            checkStack2.stackSize = 1;
        }
        if ((stackInSlot == null || (ItemStack.areItemStacksEqual(checkStack1, checkStack2) &&
          stackInSlot.stackSize < stackInSlot.getMaxStackSize())) &&
          inv.isItemValidForSlot(slot, item.getEntityItem())) {
            ItemStack stack = item.getEntityItem().copy();
            boolean setDead = true;
            if (inv.getStackInSlot(slot) != null) {
                if ((stackInSlot.stackSize + stack.stackSize) > stack.getMaxStackSize() &&
                  checkStack2 != null) {
                    setDead = false;
                    int total = stackInSlot.stackSize + stack.stackSize;
                    stack.stackSize = stack.getMaxStackSize();
                    total -= stack.getMaxStackSize();
                    checkStack2.stackSize = total;
                    item.setEntityItemStack(checkStack2);
                    //item.getEntityItem().stackSize = (inv.getStackInSlot(slot).stackSize + stack.stackSize - stack.getMaxStackSize());
                } else {
                    stack.stackSize = stackInSlot.stackSize + item.getEntityItem().stackSize;
                }
            }
            inv.setInventorySlotContents(slot, stack);
            if (setDead) {
                item.setDead();
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setBoolean("powered", powered);
        access.setShort("range", (short) range);

        return access;
    }

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        powered = access.getBoolean("powered");
        range = access.getShort("range");

    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = super.getUpdateTag();
        access.setBoolean("active", getSteamShare() > VACUUM_STEAM_CONSUMPTION && !powered);
        access.setShort("range", (short) range);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        active = access.getBoolean("active");
        range = access.getShort("range");
        markForResync();
    }


    public void updateRedstoneState(boolean flag) {
        if (flag != powered) {
            powered = flag;
            markForResync();
        }
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float xO, float yO, float zO) {
        if (player.isSneaking()) {
            switch (range) {
                case 9: {
                    range = 11;
                    break;
                }
                case 11: {
                    range = 13;
                    break;
                }
                case 13: {
                    range = 15;
                    break;
                }
                case 15: {
                    range = 17;
                    break;
                }
                case 17: {
                    range = 19;
                    break;
                }
                case 19: {
                    range = 5;
                    break;
                }
                case 5: {
                    range = 7;
                    break;
                }
                case 7: {
                    range = 9;
                    break;
                }
                default: {
                    break;
                }
            }
            //EsteemedInnovation.log.debug(range);
            markForResync();
            return true;
        } else {
            int steam = getSteamShare();

            getNetwork().split(this, true);
            EnumFacing myDir = EnumFacing.getFront(getBlockMetadata());
            EnumFacing[] directions = new EnumFacing[5];
            int i = 0;
            for (EnumFacing direction : EnumFacing.VALUES) {
                if (direction != myDir && direction != myDir.getOpposite()) {
                    directions[i] = direction;
                    i++;
                }
            }
            setDistributionDirections(directions);

            SteamNetwork.newOrJoin(this);
            getNetwork().addSteam(steam);
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayWrench(Post event) {
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        int color = mc.thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.getResolution().getScaledWidth() / 2 - 8;
        int y = event.getResolution().getScaledHeight() / 2 - 8;
        mc.fontRendererObj.drawStringWithShadow(I18n.format("esteemedinnovation.fan.range") + " " + range + " " +
          I18n.format("esteemedinnovation.fan.blocks"), x + 15, y  +13, color);
        GL11.glPopMatrix();
    }
}
