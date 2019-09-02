package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.api.heater.HeatableRegistry;
import eiteam.esteemedinnovation.api.heater.Steamable;
import eiteam.esteemedinnovation.api.steamnet.SteamNetwork;
import eiteam.esteemedinnovation.commons.util.WorldHelper;
import eiteam.esteemedinnovation.transport.steam.TileEntitySteamPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

// FIXME: Shift-clicking in the SteamFurnace GUI with things added by addSteamingRecipe are not put into slot 0.
public class TileEntitySteamHeater extends TileEntitySteamPipe {
    // When multiple heaters are used on a furnace, there is a single primary heater
    private boolean isPrimary;
    private BlockPos primaryPos;
    private int numSecondary;
    public static final int CONSUMPTION = HeaterModule.heaterConsumption;

    public TileEntitySteamHeater() {
        addSidesToGaugeBlacklist(EnumFacing.VALUES);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        superReadFromNBT(nbt);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        superWriteToNBT(nbt);
        return nbt;
    }

    @Override
    public void initialUpdate() {
        super.initialUpdate();
        setValidDistributionDirectionsExcluding(world.getBlockState(pos).getValue(BlockSteamHeater.FACING));
    }

    @Override
    public void safeUpdate() {
        superUpdate();
        EnumFacing dir = world.getBlockState(pos).getValue(BlockSteamHeater.FACING);

        BlockPos offsetPos = getOffsetPos(dir);
        TileEntity tile = world.getTileEntity(offsetPos);
        if (tile == null && getSteamShare() >= CONSUMPTION) {
            AxisAlignedBB heaterAABB = new AxisAlignedBB(0, 0, 1, 1, 1, 21F / 16F);
            heaterAABB = WorldHelper.getDirectionalBoundingBox(dir, heaterAABB, true).offset(pos);
            List<Entity> burnedEntities = world.getEntitiesWithinAABB(Entity.class, heaterAABB);
            for (Entity burnedEntity : burnedEntities) {
                burnedEntity.attackEntityFrom(SteamNetwork.STEAMED_DAMAGE, 1F);
                decrSteam(CONSUMPTION);
            }
            return;
        }

        Steamable steamable = HeatableRegistry.getSteamable(world, offsetPos);

        if (steamable == null) {
            return;
        }

        if (primaryPos == null) {
            primaryPos = pos;
        }

        if (primaryPos.equals(pos)) {
            setToPrimary();
        } else {
            TileEntity tilePrimary = world.getTileEntity(primaryPos);
            if (!(tilePrimary instanceof TileEntitySteamHeater)) {
                setToPrimary();
            }
        }

        if (isPrimary) {
            // 1 is incremented in conditional and in for loop to factor in the primary heater (this)
            if (numSecondary + 1 > 0 && getSteamShare() >= CONSUMPTION && steamable.acceptsSteam()) {
                for (int i = 0; i < numSecondary + 1; i++) {
                    decrSteam(CONSUMPTION);
                    steamable.steam();
                }

                world.notifyBlockUpdate(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), 0);
            } else {
                steamable.stopSteam();
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
        world.notifyBlockUpdate(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), 0);
        SteamNetwork.newOrJoin(this);
        getNetwork().addSteam(steam);
        return true;
    }

    /**
     * Sets this heater to be primary, and all other heaters facing our Steamable to secondary
     */
    private void setToPrimary() {
        EnumFacing dir = world.getBlockState(pos).getValue(BlockSteamHeater.FACING);
        BlockPos steamablePos = getOffsetPos(dir);
        isPrimary = true;
        primaryPos = pos;
        // Reset numSecondary before getting its value again.
        numSecondary = 0;
        for (EnumFacing offset : EnumFacing.VALUES) {
            BlockPos secondaryPos = steamablePos.offset(offset);
            TileEntity secondaryTile = world.getTileEntity(secondaryPos);
            // Skip ourselves
            if (secondaryTile instanceof TileEntitySteamHeater && !secondaryPos.equals(pos)) {
                TileEntitySteamHeater secondaryHeater = (TileEntitySteamHeater) secondaryTile;
                // Check if the secondary heater is in the primary heater's network
                SteamNetwork primaryNetwork = getNetwork();
                SteamNetwork secondaryNetwork = secondaryHeater.getNetwork();
                if (primaryNetwork != null && secondaryNetwork != null &&
                  secondaryNetwork.getName().equals(primaryNetwork.getName())) {
                    // Check if the secondary heater is actually facing the furnace
                    EnumFacing dirSecondary = world.getBlockState(secondaryPos).getValue(BlockSteamHeater.FACING);
                    if (secondaryPos.offset(dirSecondary).equals(steamablePos)) {
                        numSecondary++;
                        secondaryHeater.setToSecondary(pos);
                    }
                }
            }
        }
    }

    /**
     * Sets this heater to be secondary
     * @param primaryPos the BlockPos of the primary heater
     */
    private void setToSecondary(BlockPos primaryPos) {
        isPrimary = false;
        this.primaryPos = primaryPos;
    }

    /**
     * Called in {@link BlockSteamHeater#breakBlock(World, BlockPos, IBlockState)} to handle the Steamable's "stop"
     * functionality when the last remaining and primary heater is broken.
     * @param state the block state of this heater
     */
    void onBreak(IBlockState state) {
        if (isPrimary && numSecondary == 0) {
            EnumFacing dir = state.getValue(BlockSteamHeater.FACING);
            Steamable steamable = HeatableRegistry.getSteamable(world, getOffsetPos(dir));
            if (steamable != null) {
                steamable.stopSteam();
            }
        }
    }
}
