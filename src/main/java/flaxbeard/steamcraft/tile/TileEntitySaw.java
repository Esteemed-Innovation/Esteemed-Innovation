package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.api.ISteamTransporter;
import flaxbeard.steamcraft.api.IWrenchDisplay;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.steamnet.SteamNetwork;
import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by elijahfoster-wysocki on 10/18/14.
 */

public class TileEntitySaw extends SteamTransporterTileEntity implements ISteamTransporter, IWrenchable, IWrenchDisplay {

    private boolean hasBeenSet = false;
    private boolean hasBlockUpdate = false;
    private boolean isInitialized = false;
    public int spinup = 0;
    public float extendedLength = 0.0F;
    public Block sawingBlock;
    public int sawingMeta;
    public int extendedTicks = 0;
    public ArrayList<ItemStack> sawingStack;
    private boolean shouldStop = false;
    private boolean isActive = false;
    private boolean running = false;
    private boolean sawNextRound = false;
    private boolean isSawing = false;

    @Override
    public void readFromNBT(NBTTagCompound access) {
        super.readFromNBT(access);
        this.hasBeenSet = access.getBoolean("hasBeenSet");
    }

    @Override
    public void writeToNBT(NBTTagCompound access) {
        super.writeToNBT(access);
        access.setBoolean("hasBeenSet", hasBeenSet);
        NBTTagList nbttaglist = new NBTTagList();
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
        if (!isInitialized){
            ForgeDirection myDir = myDir();
            this.addSideToGaugeBlacklist(myDir);
            ForgeDirection[] directions = new ForgeDirection[5];
            int i = 0;
            for (ForgeDirection direction : ForgeDirection.values()){
                if (direction != myDir && direction != ForgeDirection.UP){
                    directions[i] = direction;
                    i++;
                }
            }
            this.setDistributionDirections(directions);
            this.isInitialized = true;
        }
        super.updateEntity();
        if (!worldObj.isRemote){
            int[] target = getTarget(1);

            int x = target[0], y = yCoord, z = target[1];
            if (this.spinup == 1){
                this.worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "steamcraft:hiss", Block.soundTypeAnvil.getVolume(), 0.9F);
            }
            if (this.extendedTicks > 15){
                this.worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "steamcraft:leaking", 2.0F, 0.9F);
            }
            if (extendedTicks == 5){
                this.worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, "random.break", 0.5F, (float) (0.75F + (Math.random() * 0.1F)));
            }
            if (extendedTicks > 0 && extendedTicks < 6){
                if (sawingBlock != null && sawingBlock.stepSound != null){
                    this.worldObj.playSoundEffect(this.xCoord + 0.5F, this.yCoord + 0.5F, this.zCoord + 0.5F, sawingBlock.stepSound.getBreakSound(), 0.5F, (float) (0.75F + (Math.random() * 0.1F)));
                }
            }
            if (this.shouldStop){
                this.spinup = 0;
                this.extendedLength = 0;
                this.extendedTicks = 0;
                this.isActive = false;
                this.shouldStop = false;
                this.isSawing = false;
                this.running = false;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return;
            }
            if (!this.sawNextRound && this.hasStuffToSaw() && this.getSteamShare() > 1000 && !isActive){
                this.sawNextRound = true;
                return;
            }
            boolean sawThisRound = false;
            if (this.sawNextRound){
                sawThisRound = true;
                this.sawNextRound = true;
            }
            if (sawThisRound){
                if (this.hasStuffToSaw() && !this.isActive){
                    this.decrSteam(750);
                    this.running = true;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    this.isActive = true;
                    this.isSawing = true;
                }
            }
            if (this.isActive){
                if (this.isSawing){
                    if (!this.hasStuffToSaw() && this.spinup < 40 && this.getBlockMetadata() % 2 == 0){
                        this.shouldStop = true;
                        int[] target2 = getTarget(2);
                        return;
                    }
                    if (this.spinup < 41){
                        if (this.spinup == 40){
                            if (worldObj.getBlock(x, y, z) == Blocks.log && worldObj.getBlock(x, y, z) == Blocks.log2){
                                if (this.getBlockMetadata() % 2 == 0){
                                    try {
                                        this.sawingBlock = worldObj.getBlock(x, y, z);
                                        this.sawingMeta = worldObj.getBlockMetadata(x, y, z);
                                    } catch (Exception exception) {

                                    }
                                }
                            }
                        } else if (this.extendedTicks < 25){
                            this.extendedTicks++;
                        }
                    } else {
                        this.spinup = 0;
                        this.extendedTicks = 0;
                    }
                }
            }
        }
    }

    private void spawnItems(int x, int y, int z){
        if (sawingBlock != null){
            for (ItemStack itemStack : sawingStack){
                int[] ids = OreDictionary.getOreIDs(itemStack);
                boolean isWood = false;
                int id = 0;
                if (ids != null && ids.length > 0){
                    id = OreDictionary.getOreIDs(itemStack)[0];
                }
                if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.log){
                    boolean duplicateLogs = worldObj.rand.nextInt(Config.duplicateLogs) == 0;
                    ItemStack items = new ItemStack(Blocks.planks);
                    EntityItem entityItem = new EntityItem(this.worldObj, x + 0.5F, y + 0.1F, z + 0.5F, items);
                    this.worldObj.spawnEntityInWorld(entityItem);
                    this.sawingBlock = null;
                }
            }
        }
    }

    private boolean hasStuffToSaw(){
        int[] target = getTarget(1);
        int x = target[0], y = yCoord, z = target[1];
        if (worldObj.getBlock(x, y, z) == Blocks.log && worldObj.getBlock(x, y, z) == Blocks.log2){
            return true;
        } else {
            return false;
        }
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
