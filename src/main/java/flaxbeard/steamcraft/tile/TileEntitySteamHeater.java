package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.SteamcraftRegistry;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.block.BlockSteamHeater;

import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;

public class TileEntitySteamHeater extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable {
    // When multiple heaters are used on a furnace, there is a single primary heater
    public boolean isPrimaryHeater;
    private boolean isInitialized = false;
    private boolean prevHadYuck = true;
    public static final int CONSUMPTION = Config.heaterConsumption;

    public TileEntitySteamHeater() {
        super(EnumFacing.VALUES);
        addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    public static void replace(TileEntityFurnace furnace) {
        if (furnace != null) {
            ItemStack[] furnaceItemStacks = new ItemStack[] {
              furnace.getStackInSlot(0),
              furnace.getStackInSlot(1),
              furnace.getStackInSlot(2)
            };
            int furnaceBurnTime = furnace.getField(0);
            int currentItemBurnTime = furnace.getField(1);
            int furnaceCookTime = furnace.getField(2); // This may be actually 3. TODO Double check
            furnace.getWorld().setTileEntity(furnace.getPos(), new TileEntityFurnace());
            TileEntityFurnace furnace2 = (TileEntityFurnace) furnace.getWorld().getTileEntity(furnace.getPos());
            assert furnace2 != null;
            furnace2.setInventorySlotContents(0, furnaceItemStacks[0]);
            furnace2.setInventorySlotContents(1, furnaceItemStacks[1]);
            furnace2.setInventorySlotContents(2, furnaceItemStacks[2]);
            furnace2.setField(0, furnaceBurnTime);
            furnace2.setField(1, currentItemBurnTime);
            furnace2.setField(2, furnaceCookTime);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        prevHadYuck = nbt.getBoolean("prevHadYuck");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("prevHadYuck", prevHadYuck);
        return nbt;
    }

    private void setValidDistributionDirections(EnumFacing orientation) {
        EnumFacing[] directions = new EnumFacing[6];
        int i = 0;
        for (EnumFacing dir : EnumFacing.VALUES) {
            if (dir != orientation) {
                directions[i] = dir;
                i++;
            }
        }
        setDistributionDirections(directions);
    }

    @Override
    public void update() {
        EnumFacing dir = worldObj.getBlockState(pos).getValue(BlockSteamHeater.FACING);
        if (!isInitialized) {
            setValidDistributionDirections(dir);
            isInitialized = true;
        }
        super.update();

        ArrayList<TileEntitySteamHeater> secondaryHeaters = new ArrayList<>();
        BlockPos offsetPos = getOffsetPos(dir);
        TileEntity tile = worldObj.getTileEntity(offsetPos);
        if (tile == null || !(tile instanceof TileEntityFurnace)) {
            return;
        }

        int numHeaters = 0;
        if (!isPrimaryHeater) {
            prevHadYuck = true;
        }
        isPrimaryHeater = false;
        for (int i = 0; i < 6; i++) {
            EnumFacing dir2 = EnumFacing.getFront(i);
            int x = pos.getX() + dir.getFrontOffsetX() + dir2.getFrontOffsetX();
            int y = pos.getY() + dir.getFrontOffsetY() + dir2.getFrontOffsetY();
            int z = pos.getZ() + dir.getFrontOffsetZ() + dir2.getFrontOffsetZ();
            TileEntity tile2 = worldObj.getTileEntity(new BlockPos(x, y, z));
            if (tile2 != null)  {
                if (tile2 instanceof TileEntitySteamHeater) {
                    TileEntitySteamHeater heater2 = (TileEntitySteamHeater) tile2;
                    if (heater2.getSteamShare() >= CONSUMPTION &&
                      tile2.getBlockMetadata() == EnumFacing.getFront(i).getOpposite().getIndex()) {
                        isPrimaryHeater = x == pos.getX() && y == pos.getY() && z == pos.getZ();
                        secondaryHeaters.add(heater2);
                        numHeaters++;
                        if ( secondaryHeaters.size() > 4) {
                            secondaryHeaters.remove(0);
                        }
                        numHeaters = Math.min(4, numHeaters);
                    }
                }
            } else {
                worldObj.addTileEntity(this);
            }
        }
        if (isPrimaryHeater && numHeaters > 0) {
            TileEntityFurnace furnace = (TileEntityFurnace) worldObj.getTileEntity(offsetPos);
            if (furnace == null ) {
                return;
            }

            if (!(furnace instanceof TileEntitySteamFurnace) && furnace.getClass() == TileEntityFurnace.class) {
                replace(furnace);
            }

            int furnaceBurnTime = furnace.getField(0);
            int furnaceCookTime = furnace.getField(2); // This may be actually 3. TODO Double check

            if ((furnaceBurnTime == 1 || furnaceBurnTime == 0) && getSteamShare() >= CONSUMPTION && canSmelt(furnace)) {
                if (furnaceBurnTime == 0) {
                    BlockFurnace.setState(true, worldObj, offsetPos);
                }

                for (TileEntitySteamHeater heater : secondaryHeaters) {
                    heater.decrSteam(CONSUMPTION);
                }

                furnace.setField(0, furnaceBurnTime + 3);

                if (numHeaters > 1 && furnaceCookTime > 0) {
                    int newCookTime = Math.min(furnaceCookTime + 2 * numHeaters - 1, 199);
                    furnace.setField(2, newCookTime);
                }
                // FIXME mark offsetPos for update.
            }
            ItemStack stack = furnace.getStackInSlot(2);
            prevHadYuck = !(stack == null ||
              !SteamcraftRegistry.steamingRecipes.containsKey(MutablePair.of(stack.getItem(), stack.getItemDamage())));
        }
    }

    public boolean canSmelt(TileEntityFurnace furnace) {
        ItemStack stackInSlotZero = furnace.getStackInSlot(0);
        if (stackInSlotZero == null) {
            return false;
        }
        ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(stackInSlotZero);
        if (itemstack == null) {
            return false;
        }

        MutablePair<Item, Integer> pairNoMeta = MutablePair.of(itemstack.getItem(), 0);
        MutablePair<Item, Integer> pairMeta = MutablePair.of(itemstack.getItem(), itemstack.getItemDamage());
        if (SteamcraftRegistry.steamingRecipes.containsKey(pairNoMeta)) {
            int meta = SteamcraftRegistry.steamingRecipes.get(pairNoMeta).getRight();
            Item item = SteamcraftRegistry.steamingRecipes.get(pairNoMeta).getLeft();
            itemstack = meta == 0 ? new ItemStack(item) : new ItemStack(item, 0, meta);
        }
        if (SteamcraftRegistry.steamingRecipes.containsKey(pairMeta)) {
            int meta = SteamcraftRegistry.steamingRecipes.get(pairMeta).getRight();
            Item item = SteamcraftRegistry.steamingRecipes.get(pairMeta).getLeft();
            itemstack = meta == 0 ? new ItemStack(item) : new ItemStack(item, 0, meta);
        }
        ItemStack stackInSlotTwo = furnace.getStackInSlot(2);
        if (stackInSlotTwo == null) {
            return true;
        }
        if (!stackInSlotTwo.isItemEqual(itemstack)) {
            return false;
        }
        int result = stackInSlotTwo.stackSize + itemstack.stackSize;
        return result <= furnace.getInventoryStackLimit() && result <= stackInSlotTwo.getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, IBlockState state, float hitX, float hitY, float hitZ) {
        int steam = getSteamShare();
        getNetwork().split(this, true);
        EnumFacing dir = state.getValue(BlockSteamHeater.FACING);
        setValidDistributionDirections(dir);
        SteamNetwork.newOrJoin(this);
        getNetwork().addSteam(steam);
        return true;
    }

}
