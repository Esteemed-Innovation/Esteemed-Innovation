package eiteam.esteemedinnovation.buzzsaw;

import eiteam.esteemedinnovation.api.steamtransporter.SteamTransporterTileEntity;
import eiteam.esteemedinnovation.commons.util.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

public class TileEntitySaw extends SteamTransporterTileEntity {
    private static final int CONSUMPTION = 10;
    private static final int PASSIVE_CONSUMPTION = 1;
    private static final int WINDUP_TICKS_MAX = 50;
    private static final String WINDUP_TICKS_KEY = "WindupTicks";
    private boolean isInitialized;
    private boolean isWoundUp;
    private int windupTicks;

    public TileEntitySaw() {
        super(new EnumFacing[] { EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH });
    }

    @Override
    public void update() {
        if (!isInitialized) {
            EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockSaw.FACING);
            addSideToGaugeBlacklist(dir);
            setValidDistributionDirectionsExcluding(dir, EnumFacing.UP);
            isInitialized = true;
        }

        super.update();

        // Redstone to turn it off.
        if (getSteamShare() < CONSUMPTION || worldObj.isBlockPowered(pos)) {
            resetWinding();
            return;
        }

        // Passive consumption of steam in order to keep the blades rotating.
        decrSteam(PASSIVE_CONSUMPTION);

        if (!isWoundUp) {
            if (windupTicks == WINDUP_TICKS_MAX) {
                isWoundUp = true;
            } else {
                windupTicks++;
                return;
            }
        }

        BlockPos woodPos = getOffsetPos(worldObj.getBlockState(pos).getValue(BlockSaw.FACING));
        IBlockState woodState = worldObj.getBlockState(woodPos);
        Block woodBlock = woodState.getBlock();
        Item woodItem = Item.getItemFromBlock(woodBlock);
        if (woodItem == null) {
            return;
        }
        Pair<Item, Integer> pair = Pair.of(woodItem, woodBlock.damageDropped(woodState));
        ItemStack output = null;
        // If the block is a plankWood, output sticks; if the block is a logWood, output planks.
        if (OreDictHelper.arrayContains(OreDictHelper.planks, pair)) {
            output = new ItemStack(Items.STICK, worldObj.rand.nextInt(2) + 2);
        } else if (OreDictHelper.arrayContains(OreDictHelper.logToPlank.keySet(), pair)) {
            Pair<Item, Integer> outPair = OreDictHelper.logToPlank.get(pair);
            output = new ItemStack(outPair.getLeft(), worldObj.rand.nextInt(3) + 4, outPair.getRight());
        }

        if (output != null) {
            worldObj.destroyBlock(woodPos, false);
            worldObj.spawnEntityInWorld(new EntityItem(worldObj, woodPos.getX() + 0.5, woodPos.getY() + 0.5, woodPos.getZ() + 0.5, output));
            if (!worldObj.isRemote) {
                decrSteam(CONSUMPTION);
            }
        }
    }

    private void resetWinding() {
        if (windupTicks != 0) {
            windupTicks--;
        }
        isWoundUp = false;
    }

    public void uninitialize() {
        isInitialized = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        windupTicks = access.getInteger(WINDUP_TICKS_KEY);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setInteger(WINDUP_TICKS_KEY, windupTicks);
        return access;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = writeToNBT(getUpdateTag());
        return new SPacketUpdateTileEntity(pos, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
        markForResync();
    }
}
