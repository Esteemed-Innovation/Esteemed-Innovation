package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by elijahfoster-wysocki on 10/18/14.
 */

public class TileEntitySaw extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay {

    private boolean hasBeenSet = false;
    private boolean hasBlockUpdate = false;

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        this.hasBeenSet = access.getBoolean("hasBeenSet");
        NBTTagList nbttaglist = (NBTTagList) access.getTag("Items");
    }

    @Override
    public void writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setBoolean("hasBeenSet", hasBeenSet);
        NBTTagList nbttaglist = new NBTTagList();

        access.setTag("Items", nbttaglist);
    }

    @Override
    public void displayWrench(RenderGameOverlayEvent.Post event) {
        GL11.glPushMatrix();
        int color = Minecraft.getMinecraft().thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.resolution.getScaledWidth() / 2 - 8;
        int y = event.resolution.getScaledHeight() / 2 - 8;
        GL11.glPopMatrix();
    }

    private int[] getTarget(int distance) {
        int x = xCoord, z = zCoord, opposite = 0, meta = getBlockMetadata();
        opposite = meta % 2 == 0 ? meta + 1 : meta - 1;
        switch (meta) {
            case 2:
                z -= distance;
                opposite = 3;
                break;
            case 3:
                z += distance;
                opposite = 2;
                break;
            case 4:
                x -= distance;
                opposite = 5;
                break;
            case 5:
                x += distance;
                opposite = 4;
                break;
            default:
                break;
        }

        return new int[]{x, z, opposite};
    }

    public ForgeDirection myDir() {
        int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
        switch (meta) {
            case 2:
                return ForgeDirection.NORTH;
            case 3:
                return ForgeDirection.SOUTH;
            case 4:
                return ForgeDirection.WEST;
            case 5:
                return ForgeDirection.EAST;
        }
        return ForgeDirection.NORTH;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO, float yO, float zO) {
        if (player.isSneaking()) {
            this.hasBeenSet = true;
            int[] target = getTarget(2);
            int x2 = target[0], y2 = yCoord, z2 = target[1], opposite = target[2];
            if (worldObj.getBlock(x2, y2, z2) == SteamcraftBlocks.saw && worldObj.getBlockMetadata(x2, y2, z2) == opposite) {
                ((TileEntitySaw) worldObj.getTileEntity(x2, y2, z2)).hasBeenSet = true;
                this.worldObj.markBlockForUpdate(x2, y2, z2);
            }
            this.worldObj.markBlockForUpdate(x, y, z);
            return true;
        } else {
            int steam = this.getSteamShare();
            this.getNetwork().split(this, true);
            ForgeDirection[] directions = new ForgeDirection[5];
            int i = 0;
            for (ForgeDirection direction : ForgeDirection.values()) {
                if (direction != myDir() && direction != ForgeDirection.UP) {
                    directions[i] = direction;
                    i++;
                }
            }
            this.setDistributionDirections(directions);
            SteamNetwork.newOrJoin(this);
            this.getNetwork().addSteam(steam);
            return true;
        }
    }

    @Override
    public void updateEntity(){
        super.updateEntity();
    }

    public void blockUpdate() {
        this.hasBlockUpdate = true;
    }

    public boolean hasUpdate() {
        return hasBlockUpdate;
    }

    private boolean hasWoodToSaw(){
        int[] target = getTarget(1);
        int x = target[0], y = yCoord, z = target[1];
        if (!worldObj.isAirBlock(x, y, z) && worldObj.getBlock(x, y, z) != Blocks.bedrock && worldObj.getTileEntity(x, y, z) == null && worldObj.getBlock(x, y, z).getBlockHardness(worldObj, x, y, z) < 50F) {
            return true;
        } else {
            return false;
        }
    }


}
