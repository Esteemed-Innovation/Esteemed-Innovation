package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.commons.Config;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

import static eiteam.esteemedinnovation.heater.TileEntitySteamFurnace.*;

// FIXME: Shift-clicking in the SteamFurnace GUI with things added by addSteamingRecipe are not put into slot 0.
public class TileEntitySteamHeater extends TileEntitySteamPipe {
    // When multiple heaters are used on a furnace, there is a single primary heater
    public boolean isPrimaryHeater;
    private boolean isInitialized = false;
    public static final int CONSUMPTION = Config.heaterConsumption;

    public TileEntitySteamHeater() {
        super();
        addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    public static void replaceWith(TileEntityFurnace current, TileEntityFurnace replacement) {
        if (current != null) {
            ItemStack[] furnaceItemStacks = new ItemStack[] {
              current.getStackInSlot(0),
              current.getStackInSlot(1),
              current.getStackInSlot(2)
            };
            int furnaceBurnTime = current.getField(FURNACE_BURN_TIME_ID);
            int currentItemBurnTime = current.getField(CURRENT_ITEM_BURN_TIME_ID);
            int furnaceCookTime = current.getField(COOK_TIME_ID);
            current.getWorld().setTileEntity(current.getPos(), replacement);
            TileEntityFurnace replaced = (TileEntityFurnace) current.getWorld().getTileEntity(current.getPos());
            assert replaced != null;
            replaced.setInventorySlotContents(0, furnaceItemStacks[0]);
            replaced.setInventorySlotContents(1, furnaceItemStacks[1]);
            replaced.setInventorySlotContents(2, furnaceItemStacks[2]);
            replaced.setField(FURNACE_BURN_TIME_ID, furnaceBurnTime);
            replaced.setField(CURRENT_ITEM_BURN_TIME_ID, currentItemBurnTime);
            replaced.setField(COOK_TIME_ID, furnaceCookTime);
        }
    }

    public static void replace(TileEntityFurnace furnace) {
        replaceWith(furnace, new TileEntitySteamFurnace());
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.superReadFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.superWriteToNBT(nbt);
        return nbt;
    }

    @Override
    public void update() {
        super.superUpdate();
        EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockSteamHeater.FACING);
        if (!isInitialized) {
            setValidDistributionDirectionsExcluding(dir);
            isInitialized = true;
        }

        ArrayList<TileEntitySteamHeater> secondaryHeaters = new ArrayList<>();
        BlockPos offsetPos = getOffsetPos(dir);
        TileEntity tile = worldObj.getTileEntity(offsetPos);
        if (tile == null || !(tile instanceof TileEntityFurnace)) {
            return;
        }
        TileEntityFurnace furnace = (TileEntityFurnace) tile;

        int numHeaters = 0;
        isPrimaryHeater = false;
        for (EnumFacing dir2 : EnumFacing.VALUES) {
            int x = pos.getX() + dir.getFrontOffsetX() + dir2.getFrontOffsetX();
            int y = pos.getY() + dir.getFrontOffsetY() + dir2.getFrontOffsetY();
            int z = pos.getZ() + dir.getFrontOffsetZ() + dir2.getFrontOffsetZ();
            BlockPos pos2 = new BlockPos(x, y, z);
            TileEntity tile2 = worldObj.getTileEntity(pos2);
            IBlockState state2 = worldObj.getBlockState(pos2);
            if (tile2 != null)  {
                if (tile2 instanceof TileEntitySteamHeater) {
                    TileEntitySteamHeater heater2 = (TileEntitySteamHeater) tile2;
                    if (heater2.getSteamShare() >= CONSUMPTION && state2.getValue(BlockSteamHeater.FACING).getOpposite() == dir2) {
                        isPrimaryHeater = x == pos.getX() && y == pos.getY() && z == pos.getZ();
                        secondaryHeaters.add(heater2);
                        numHeaters++;
                        if (secondaryHeaters.size() > 4) {
                            secondaryHeaters.remove(0);
                        }
                        numHeaters = Math.min(4, numHeaters);
                    }
                }
            }
        }
        if (isPrimaryHeater && numHeaters > 0) {
            if (!(furnace instanceof TileEntitySteamFurnace) && furnace.getClass() == TileEntityFurnace.class) {
                replace(furnace);
            }

            if (!(furnace instanceof TileEntitySteamFurnace)) {
                return;
            }

            int furnaceBurnTime = furnace.getField(FURNACE_BURN_TIME_ID);
            int furnaceCookTime = furnace.getField(COOK_TIME_ID);

            if ((furnaceBurnTime == 1 || furnaceBurnTime == 0) && getSteamShare() >= CONSUMPTION &&
              ((TileEntitySteamFurnace) furnace).canSmelt()) {
                if (furnaceBurnTime == 0) {
                    BlockFurnace.setState(true, worldObj, offsetPos);
                }

                for (TileEntitySteamHeater heater : secondaryHeaters) {
                    heater.decrSteam(CONSUMPTION);
                }

                furnace.setField(0, furnaceBurnTime + 3);

                if (numHeaters > 1 && furnaceCookTime > 0) {
                    int newCookTime = Math.min(furnaceCookTime + 2 * numHeaters - 1, 199);
                    furnace.setField(COOK_TIME_ID, newCookTime);
                }
                worldObj.notifyBlockUpdate(offsetPos, worldObj.getBlockState(offsetPos), worldObj.getBlockState(offsetPos), 0);
            }
        }
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        int steam = getSteamShare();
        getNetwork().split(this, true);
        EnumFacing dir = state.getValue(BlockSteamHeater.FACING);
        setValidDistributionDirectionsExcluding(dir);
        BlockPos offsetPos = pos.offset(dir);
        worldObj.notifyBlockUpdate(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), 0);
        SteamNetwork.newOrJoin(this);
        getNetwork().addSteam(steam);
        return true;
    }
}
