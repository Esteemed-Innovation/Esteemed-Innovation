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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author SatanicSanta
 */
public class TileEntitySteamDistributor extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay{

    private boolean isRedstonePowered = false;
    private boolean isActive;
    private int range = 9;
    private int steamUsage = Config.distributorConsumption;
    private Material[] validMaterials = {
      Material.grass,
      Material.sand,
      Material.ground,
      Material.water,
      Material.vine,
      Material.coral,
      Material.gourd,
      Material.leaves
    };

    public int calcDirtBlocks() {
        int dirt = 0;
        for (int x = 0; x < range; x++) {
            for (int z = 0; z < range; z++) {
                Block b = this.worldObj.getBlock(this.xCoord + x, this.yCoord + 1, this.zCoord + z);
                for (int i = 0; i < validMaterials.length; i++) {
                    if (b.getMaterial().equals(validMaterials[i])) {
                        dirt++;
                    }
                }
            }
        }
        return dirt;
    }

    public Block getPlantBlockAboveDirt() {
        //Same loops and shit as above
        for (int x = 0; x < range; x++) {
            for (int z = 0; z < range; z++) {
                Block b = this.worldObj.getBlock(this.xCoord + x, this.yCoord + 1, this.zCoord + z);
                for (int i = 0; i < validMaterials.length; i++) {
                    if (b.getMaterial().equals(validMaterials[i])) {
                        return b;
                    }
                }
            }
        }
        return null;
    }

    public boolean isAbleToWork(int multiplier) {
        int powerNeeded = steamUsage * multiplier;
        if (this.getSteamShare() < powerNeeded || this.isRedstonePowered) {
            return false;
        }

        if (this.getSteamShare() > powerNeeded && !this.isRedstonePowered) {
            return true;
        }

        return false;
    }

    @Override
    public void updateEntity() {
        this.isRedstonePowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        int dirts = calcDirtBlocks();
        if (!this.worldObj.isRemote) {
            if (isAbleToWork(dirts)) {
                this.isActive = true;
            } else if (!isAbleToWork(dirts)) {
                this.isActive = false;
            }
        }

        if (isActive) {
            if (!worldObj.isRemote) {
                Block block = getPlantBlockAboveDirt();
                block.setTickRandomly(true);
                this.decrSteam(steamUsage * dirts);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setBoolean("isRedstonePowered", isRedstonePowered);
        nbt.setShort("range", (short) this.range);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.isRedstonePowered = nbt.getBoolean("isRedstonePowered");
        this.range = nbt.getShort("range");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = super.getDescriptionTag();
        nbt.setBoolean("isActive", this.getSteamShare() > steamUsage && !this.isRedstonePowered);
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
            Block block = worldObj.getBlock(x, y, z);
            int meta = worldObj.getBlockMetadata(x, y, z);
            ForgeDirection opposite = ForgeDirection.getOrientation(meta);
            block.rotateBlock(worldObj, x, y, z, opposite);
        }
        return false;
    }
}
