package eiteam.esteemedinnovation.transport.entity;

import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.api.tile.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.api.wrench.IWrenchDisplay;
import eiteam.esteemedinnovation.api.wrench.IWrenchable;
import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.commons.util.MathUtility;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TileEntityFan extends SteamTransporterTileEntity implements IWrenchable, IWrenchDisplay {
    public boolean active;
    public boolean powered = false;
    public boolean lastSteam = false;
    public int rotateTicks = 0;
    public int range = 9;
    private boolean isInitialized = false;
    private static final int STEAM_CONSUMPTION = Config.fanConsumption;

    public TileEntityFan() {
        addSidesToGaugeBlacklist(EnumFacing.HORIZONTALS);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
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
        access.setBoolean("active", getSteamShare() > 0 && !powered);
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

    /**
     * Gets either a random double, or 0.5 based on the offset.
     * @param offset The front offset for the direction
     * @return double
     */
    private double getRandomOrSlight(int offset) {
        return offset == 0 ? Math.random() : 0.5D;
    }

    /**
     * Gets the X, Y, Z positions for the smoke particles.
     * @param dir The dir
     * @return {double, double, double}
     */
    private double[] getSmokePositions(EnumFacing dir) {
        return new double[] {
          pos.getX() + getRandomOrSlight(dir.getFrontOffsetX()),
          pos.getY() + getRandomOrSlight(dir.getFrontOffsetY()),
          pos.getZ() + getRandomOrSlight(dir.getFrontOffsetZ())
        };
    }

    /**
     * Gets the X, Y, Z speeds for the smoke particles.
     * @param dir The dir
     * @return {double, double, double}
     */
    private double[] getSmokeSpeeds(EnumFacing dir) {
        return new double[] {
          dir.getFrontOffsetX() * 0.2F,
          dir.getFrontOffsetY() * 0.2F,
          dir.getFrontOffsetZ() * 0.2F
        };
    }

    @Override
    public void update() {
        if (lastSteam != getSteamShare() >= STEAM_CONSUMPTION) {
            markForResync();
        }
        lastSteam = getSteamShare() > STEAM_CONSUMPTION;
        if (!lastSteam && !worldObj.isRemote && active) {
            markForResync();
        }
        if (!isInitialized) {
            powered = worldObj.isBlockPowered(pos);
            setDistributionDirections(new EnumFacing[] { worldObj.getBlockState(pos).getValue(BlockFan.FACING).getOpposite() });
            isInitialized = true;
        }
        super.update();
        if (active && worldObj.isRemote) {
            rotateTicks++;
        }

        if (active && worldObj.isRemote || (getSteamShare() >= STEAM_CONSUMPTION && !powered)) {
            if (!worldObj.isRemote) {
                decrSteam(STEAM_CONSUMPTION);
            }
            EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockFan.FACING);
            double[] positions = getSmokePositions(dir);
            double[] speeds = getSmokeSpeeds(dir);
            worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, positions[0], positions[1], positions[2],
              speeds[0], speeds[1], speeds[2]);
            int blocksInFront = 0;
            boolean blocked = false;
            for (int i = 1; i < range; i++) {
                int x = pos.getX() + dir.getFrontOffsetX() * i;
                int y = pos.getY() + dir.getFrontOffsetY() * i;
                int z = pos.getZ() + dir.getFrontOffsetZ() * i;
                BlockPos offsetPos = new BlockPos(x, y, z);
                IBlockState offsetState = worldObj.getBlockState(offsetPos);
                Block offsetBlock = offsetState.getBlock();
                if (!worldObj.isRemote && worldObj.rand.nextInt(20) == 0 && !blocked && offsetBlock != Blocks.AIR &&
                  offsetBlock.isReplaceable(worldObj, offsetPos) || offsetBlock instanceof BlockCrops) {
                    // We don't need to check the other FluidBlock classes because they all inherit Base.
                    if (offsetBlock instanceof BlockFluidBase) {
                        for (int v = 0; v < 5; v++) {
                            EsteemedInnovation.proxy.spawnBreakParticles(worldObj, x + 0.5F, y + 0.5F, z + 0.5F, offsetBlock, 0F, 0F, 0F);
                        }
                    }
                    worldObj.setBlockToAir(offsetPos);
                }
                if (!blocked && (offsetBlock.isReplaceable(worldObj, offsetPos) || worldObj.isAirBlock(offsetPos) ||
                  offsetBlock instanceof BlockTrapDoor || offsetState.getCollisionBoundingBox(worldObj, offsetPos) == null)) {
                    blocksInFront = i;
                    if (i != range - 1) {
                        worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + getRandomOrSlight(dir.getFrontOffsetX()),
                          y + getRandomOrSlight(dir.getFrontOffsetY()), z + getRandomOrSlight(dir.getFrontOffsetZ()),
                          speeds[0], speeds[1], speeds[2]);
                    }
                } else {
                    blocked = true;
                }
            }
            List<Entity> entities = worldObj.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX() + (dir.getFrontOffsetX() < 0 ? dir.getFrontOffsetX() * blocksInFront : 0), pos.getY() + (dir.getFrontOffsetY() < 0 ? dir.getFrontOffsetY() * blocksInFront : 0), pos.getZ() + (dir.getFrontOffsetZ() < 0 ? dir.getFrontOffsetZ() * blocksInFront : 0), pos.getX() + 1 + (dir.getFrontOffsetX() > 0 ? dir.getFrontOffsetX() * blocksInFront : 0), pos.getY() + 1 + (dir.getFrontOffsetY() > 0 ? dir.getFrontOffsetY() * blocksInFront : 0), pos.getZ() + 1 + (dir.getFrontOffsetZ() > 0 ? dir.getFrontOffsetZ() * blocksInFront : 0)));
            for (Entity entity : entities) {
                if (!(entity instanceof EntityPlayer) || !(((EntityPlayer) entity).capabilities.isFlying && ((EntityPlayer) entity).capabilities.isCreativeMode)) {
                    if (entity instanceof EntityPlayer && entity.isSneaking()) {
                        entity.motionX += dir.getFrontOffsetX() * 0.025F;
                        entity.motionY += dir.getFrontOffsetY() * 0.05F;
                        entity.motionZ += dir.getFrontOffsetZ() * 0.025F;
                    } else {
                        entity.motionX += dir.getFrontOffsetX() * 0.075F;
                        entity.motionY += dir.getFrontOffsetY() * 0.1F;
                        entity.motionZ += dir.getFrontOffsetZ() * 0.075F;
                    }
                    entity.fallDistance = 0.0F;
                }
            }
        }
    }

    public void updateRedstoneState(boolean flag) {
        if (flag != powered) {
            powered = flag;
            markForResync();
        }
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            range = MathUtility.minWithDefault(19, range + 2, 5);
            markForResync(state);
        } else {
            int steam = getSteamShare();
            getNetwork().split(this, true);
            setDistributionDirections(new EnumFacing[] { world.getBlockState(pos).getValue(BlockFan.FACING).getOpposite() });

            SteamNetwork.newOrJoin(this);
            getNetwork().addSteam(steam);
        }
        return true;
    }

    public static void rangeDisplay(Post event, int range) {
        GL11.glPushMatrix();
        Minecraft mc =  Minecraft.getMinecraft();
        int color = mc.thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.getResolution().getScaledWidth() / 2 - 8;
        int y = event.getResolution().getScaledHeight() / 2 - 8;
        mc.fontRendererObj.drawStringWithShadow(I18n.format("esteemedinnovation.fan.range", range), x + 15, y + 13, color);
        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayWrench(Post event) {
        rangeDisplay(event, range);
    }
}
