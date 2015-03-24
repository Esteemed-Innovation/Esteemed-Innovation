package flaxbeard.steamcraft.tile;

import flaxbeard.steamcraft.api.tile.SteamTransporterTileEntity;
import flaxbeard.steamcraft.api.util.Coord4;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityBlockPlacer extends SteamTransporterTileEntity implements IInventory {

    private ItemStack[] inventory = new ItemStack[1];
    int workingTick = 0;
    int meta = -1;
    Coord4 target;
    boolean wasRunning = false;

    Block placingBlock;
    int placingMeta;
    @Override
    public void readFromNBT(NBTTagCompound access)
    {
        super.readFromNBT(access);
        this.workingTick = access.getInteger("workingTick");

        this.placingBlock = Block.getBlockById(access.getInteger("block"));
        this.placingMeta = access.getInteger("placingMeta");
        NBTTagList nbttaglist = (NBTTagList) access.getTag("Items");

        this.inventory = new ItemStack[2];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound access)
    {
        super.writeToNBT(access);
        access.setInteger("workingTick", workingTick);
        access.setInteger("block", Block.getIdFromBlock(placingBlock));
        access.setInteger("placingMeta", placingMeta);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        access.setTag("Items", nbttaglist);
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound access = super.getDescriptionTag();
        access.setInteger("workingTick", workingTick);
        access.setInteger("block", Block.getIdFromBlock(placingBlock));
        access.setInteger("placingMeta", placingMeta);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        //log.debug("updated");
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();

        this.workingTick = access.getInteger("workingTick");
        this.placingBlock = Block.getBlockById(access.getInteger("block"));
        this.placingMeta = access.getInteger("placingMeta");
    }

    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (inventory.length -1 >= slot){
            return inventory[slot];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack stack = this.getStackInSlot(slot);
        if (stack != null){
            if (stack.stackSize <= amount){
                this.inventory[slot] = null;
            } else {
                stack = stack.splitStack(amount);
                if (getStackInSlot(slot).stackSize == 0){
                    this.inventory[slot] = null;
                }
            }
            return stack;
        }
        return null;
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return getStackInSlot(slot);
    }
    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (this.inventory.length -1 >= slot){
            this.inventory[slot] = stack;
        }
    }

    @Override
    public String getInventoryName() {
        return null;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlock){
            //log.debug("it's a block");
            Block b = Block.getBlockFromItem(item);
            if (b.isNormalCube() && b.renderAsNormalBlock() && !b.hasTileEntity(stack.getItemDamage())){
                return true;
            }
        }
        return false;
    }

    private boolean hasItem(){
        if (this.getStackInSlot(0) != null){
            return true;
        }
        return false;
    }

    @Override
    public void updateEntity(){
        super.updateEntity();

        if (this.meta != this.getBlockMetadata()){
            this.meta = this.getBlockMetadata();
            this.target = this.getTarget();
        }

        if (this.target == null){
            this.target = this.getTarget();
        }

        if (worldObj.isRemote){
            if (workingTick > 0 && workingTick < 20){
                workingTick++;
            } else if (workingTick >= 20){
                //log.debug("stopped");
                workingTick = 0;
            }
        } else {
            if (this.hasItem() &&
              worldObj.isAirBlock(target.x, target.y, target.z) &&
              worldObj.getTileEntity(target.x, target.y, target.z) == null
              ) {
                if (workingTick == 0) {
                    if (this.getSteamShare() > 100) {
                        this.decrSteam(100);
                        worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D,
                          "steamcraft:hiss", 0.3F, 1.5F);
                        if (!wasRunning) {
                            //log.debug("is now running");
                            this.wasRunning = true;
                        }

                        ItemStack stack = this.getStackInSlot(0);
                        Item item = stack.getItem();
                        Block block = Block.getBlockFromItem(item);
                        this.placingBlock = block;
                        this.placingMeta = stack.getItemDamage();
                        this.workingTick++;
                        this.markForUpdate();
                    }
                } else if (workingTick < 20) {
                    this.workingTick++;
                } else {
                    this.decrStackSize(0, 1);
                    worldObj.playSoundEffect(target.x + 0.5D, target.y + 0.5D, target.z + 0.5D,
                      this.placingBlock.stepSound.getBreakSound(), 0.5F,
                      (float) (0.75F + (Math.random() * 0.1F)));
                    worldObj.setBlock(target.x, target.y, target.z, this.placingBlock);
                    worldObj
                      .setBlockMetadataWithNotify(target.x, target.y, target.z, placingMeta, 2);
                    this.workingTick = 0;

                }
            } else {
                this.workingTick = 0;
                if (wasRunning){
                    //log.debug("is no longer running");
                    this.wasRunning = false;
                    this.markForUpdate();
                }
            }

            if (this.hasItem() && worldObj.getBlock(target.x, target.y, target.z)
              .isReplaceable(worldObj, target.x, target.y, target.z)) {
                if (worldObj.getTileEntity(target.x, target.y, target.z) != null) {
                    worldObj.setTileEntity(target.x, target.y, target.z, null);
                }
                worldObj.setBlock(target.x, target.y, target.z, Blocks.air);
            }
        }
    }

    private Coord4 getTarget(){
        if (this.meta >=0){
            ForgeDirection dir = ForgeDirection.getOrientation(meta);
            return new Coord4(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, getDimension());
        }
        return null;
    }
}
