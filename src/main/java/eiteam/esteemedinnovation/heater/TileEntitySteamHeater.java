package eiteam.esteemedinnovation.heater;

import eiteam.esteemedinnovation.api.heater.HeatableRegistry;
import eiteam.esteemedinnovation.api.heater.ISteamable;
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
import java.util.ArrayList;
import java.util.List;

// FIXME: Shift-clicking in the SteamFurnace GUI with things added by addSteamingRecipe are not put into slot 0.
public class TileEntitySteamHeater extends TileEntitySteamPipe {
    // When multiple heaters are used on a furnace, there is a single primary heater
    public boolean isPrimaryHeater;
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

        List<TileEntitySteamHeater> secondaryHeaters = new ArrayList<>();
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

        int numHeaters = 0;
        isPrimaryHeater = false;
        for (EnumFacing dir2 : EnumFacing.VALUES) {
            int x = pos.getX() + dir.getXOffset() + dir2.getXOffset();
            int y = pos.getY() + dir.getYOffset() + dir2.getYOffset();
            int z = pos.getZ() + dir.getZOffset() + dir2.getZOffset();
            BlockPos pos2 = new BlockPos(x, y, z);
            TileEntity tile2 = world.getTileEntity(pos2);
            IBlockState state2 = world.getBlockState(pos2);
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

        ISteamable steamable = HeatableRegistry.getSteamable(world, offsetPos);

        if (steamable == null) {
            return;
        }

        if (isPrimaryHeater) {
            if (numHeaters > 0) {
                if (getSteamShare() >= CONSUMPTION && steamable.acceptsSteam()) {
                    decrSteam(CONSUMPTION);
                    steamable.steam();

                    for (TileEntitySteamHeater heater : secondaryHeaters) {
                        heater.decrSteam(CONSUMPTION);
                        steamable.steam();
                    }

                    world.notifyBlockUpdate(offsetPos, world.getBlockState(offsetPos), world.getBlockState(offsetPos), 0);
                }
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
}
