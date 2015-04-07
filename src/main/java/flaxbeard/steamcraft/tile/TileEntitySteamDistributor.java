package flaxbeard.steamcraft.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

/**
 * @author SatanicSanta
 */
public class TileEntitySteamDistributor extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay{

    private boolean isPowered = false; //Referring to redstone power
    private boolean isActive;
    private int range = 9;
    private int steamUsage = Config.distributorConsumption;

    public int calcDirtBlocks() {
        int dirt = 0;
        for (int x = 0; x < 15; x++) {
            for (int z = 0; z < 15; z++) {
                Block b = this.worldObj.getBlock(this.xCoord + x, this.yCoord + 1, this.zCoord + z);
                if (b.getMaterial() == Material.grass || b.getMaterial() == Material.ground ||
                  b.getMaterial() == Material.water || b.getMaterial() == Material.sand) {
                    dirt++;
                }
            }
        }
        return dirt;
    }

    @Override
    public void updateEntity() {
        this.isPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        if (!this.worldObj.isRemote) {
            if (this.getSteamShare() < steamUsage || !this.isPowered) {
                this.isActive = false;
            } else {
                this.isActive = true;
                this.decrSteam(steamUsage);
            }
        }

        if (isActive) {
            if (this.worldObj.isRemote) {

            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isPowered", isPowered);
        nbt.setShort("range", (short) this.range);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isPowered = nbt.getBoolean("isPowered");
        this.range = nbt.getShort("range");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = super.getDescriptionTag();
        nbt.setBoolean("isActive", this.getSteamShare() > steamUsage && !this.isPowered);
        nbt.setShort("range", (short) this.range);
        return new S35PacketUpdateTileEntity(xCoord, zCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound nbt = pkt.func_148857_g();
        this.isActive = nbt.getBoolean("isActive");
        this.range = nbt.getShort("range");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }



    @Override
    @SideOnly(Side.CLIENT)
    public void displayWrench(RenderGameOverlayEvent.Post event) {
        GL11.glPushMatrix();
        int color = Minecraft.getMinecraft().thePlayer.isSneaking() ? 0xC6C6C6 : 0x777777;
        int x = event.resolution.getScaledWidth() / 2 - 8;
        int y = event.resolution.getScaledHeight() / 2 - 8;
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(StatCollector.translateToLocal(
          "steamcraft.fan.range") + this.range, x + 15, y + 13, color);
        GL11.glPopMatrix();
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float xO, float yO, float zO) {
        if (player.isSneaking()) {
            switch (range) {
                case 3: {
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
                    range = 3;
                    break;
                }
            }
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            return true;
        } else {
            return false;
        }
    }
}
