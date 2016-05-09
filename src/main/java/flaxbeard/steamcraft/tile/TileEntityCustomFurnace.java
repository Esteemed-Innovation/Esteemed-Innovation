package flaxbeard.steamcraft.tile;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.IWrenchable;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.block.BlockCustomFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.*;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class TileEntityCustomFurnace extends TileEntityFurnace implements ISidedInventory, IWrenchable, IDisguisableBlock {
    private static final int[] slotsTop = new int[]{0};
    private static final int[] slotsBottom = new int[]{2, 1};
    private static final int[] slotsSides = new int[]{1};
    private static final String __OBFID = "CL_00000357";
    public Block disguiseBlock = null;
    public int disguiseMeta = 0;
    /**
     * The number of ticks that the furnace will keep burning
     */
    public int furnaceBurnTime;
    /**
     * The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for
     */
    public int currentItemBurnTime;
    /**
     * The number of ticks that the current item has been cooking for
     */
    public int furnaceCookTime;
    /**
     * The ItemStacks that hold the items currently being used in the furnace
     */
    private ItemStack[] furnaceItemStacks = new ItemStack[3];
    private String field145958O;

    /**
     * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
     * fuel
     */
    public static int getItemBurnTime(ItemStack stack) {
        if (stack == null) {
            return 0;
        } else {
            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air) {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab) {
                    return 150;
                }

                if (block.getMaterial() == Material.wood) {
                    return 300;
                }

                if (block == Blocks.coal_block) {
                    return 16000;
                }
            }

            if (item instanceof ItemTool && ((ItemTool) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemSword && ((ItemSword) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item instanceof ItemHoe && ((ItemHoe) item).getToolMaterialName().equals("WOOD")) return 200;
            if (item == Items.stick) return 100;
            if (item == Items.coal) return 1600;
            if (item == Items.lava_bucket) return 20000;
            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
            if (item == Items.blaze_rod) return 2400;
            return GameRegistry.getFuelValue(stack);
        }
    }

    public static boolean isItemFuel(ItemStack stack) {
        /**
         * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
         * fuel
         */
        return getItemBurnTime(stack) > 0;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    public int getSizeInventory() {
        return this.furnaceItemStacks.length;
    }

    /**
     * Returns the stack in slot i
     */
    public ItemStack getStackInSlot(int par1) {
        return this.furnaceItemStacks[par1];
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a
     * new stack.
     */
    public ItemStack decrStackSize(int slot, int num) {
        if (this.furnaceItemStacks[slot] != null) {
            ItemStack itemstack;

            if (this.furnaceItemStacks[slot].stackSize <= num) {
                itemstack = this.furnaceItemStacks[slot];
                this.furnaceItemStacks[slot] = null;
                return itemstack;
            } else {
                itemstack = this.furnaceItemStacks[slot].splitStack(num);

                if (this.furnaceItemStacks[slot].stackSize == 0) {
                    this.furnaceItemStacks[slot] = null;
                }

                return itemstack;
            }
        } else {
            return null;
        }
    }

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem -
     * like when you close a workbench GUI.
     */
    public ItemStack getStackInSlotOnClosing(int p_70304_1_) {
        if (this.furnaceItemStacks[p_70304_1_] != null) {
            ItemStack itemstack = this.furnaceItemStacks[p_70304_1_];
            this.furnaceItemStacks[p_70304_1_] = null;
            return itemstack;
        } else {
            return null;
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int p_70299_1_, ItemStack stack) {
        this.furnaceItemStacks[p_70299_1_] = stack;

        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound access = new NBTTagCompound();
        access.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        access.setInteger("disguiseMeta", disguiseMeta);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, access);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        //	super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.func_148857_g();
        this.disguiseBlock = Block.getBlockById(access.getInteger("disguiseBlock"));
        this.disguiseMeta = access.getInteger("disguiseMeta");
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    /**
     * Returns the name of the inventory
     */
    public String getInventoryName() {
        return this.hasCustomInventoryName() ? this.field145958O : "container.furnace";
    }

    /**
     * Returns if the inventory is named
     */
    public boolean hasCustomInventoryName() {
        return this.field145958O != null && this.field145958O.length() > 0;
    }

    @Override
    public void func_145951_a(String p_145951_1_) {
        this.field145958O = p_145951_1_;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagList nbttaglist = nbtTagCompound.getTagList("Items", 10);
        this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.furnaceItemStacks.length) {
                this.furnaceItemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = nbtTagCompound.getShort("BurnTime");
        this.furnaceCookTime = nbtTagCompound.getShort("CookTime");
        this.currentItemBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
        this.disguiseBlock = Block.getBlockById(nbtTagCompound.getInteger("disguiseBlock"));
        this.disguiseMeta = nbtTagCompound.getInteger("disguiseMeta");

        if (nbtTagCompound.hasKey("CustomName", 8)) {
            this.field145958O = nbtTagCompound.getString("CustomName");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setShort("BurnTime", (short) this.furnaceBurnTime);
        nbtTagCompound.setShort("CookTime", (short) this.furnaceCookTime);
        nbtTagCompound.setInteger("disguiseBlock", Block.getIdFromBlock(disguiseBlock));
        nbtTagCompound.setInteger("disguiseMeta", disguiseMeta);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.furnaceItemStacks.length; ++i) {
            if (this.furnaceItemStacks[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.furnaceItemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        nbtTagCompound.setTag("Items", nbttaglist);

        if (this.hasCustomInventoryName()) {
            nbtTagCompound.setString("CustomName", this.field145958O);
        }
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
     * cooked
     */
    @SideOnly(Side.CLIENT)
    public int getCookProgressScaled(int p_145953_1_) {
        return this.furnaceCookTime * p_145953_1_ / 200;
    }

    /**
     * Returns an integer between 0 and the passed value representing how much burn time is left on the current fuel
     * item, where 0 means that the item is exhausted and the passed value means that the item is fresh
     */
    @SideOnly(Side.CLIENT)
    public int getBurnTimeRemainingScaled(int p_145955_1_) {
        if (this.currentItemBurnTime == 0) {
            this.currentItemBurnTime = 200;
        }

        return this.furnaceBurnTime * p_145955_1_ / this.currentItemBurnTime;
    }

    /**
     * Furnace isBurning
     */
    public boolean isBurning() {
        return this.furnaceBurnTime > 0;
    }

    @Override
    public void updateEntity() {
        if (!this.worldObj.isRemote) {
            super.furnaceBurnTime = this.furnaceBurnTime;
            super.currentItemBurnTime = this.currentItemBurnTime;
            super.furnaceCookTime = this.furnaceCookTime;
        }
        boolean flag = this.furnaceBurnTime > 0;
        boolean flag1 = false;

        if (this.furnaceBurnTime > 0) {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote) {
            if (this.furnaceBurnTime != 0 || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null) {
                if (this.furnaceBurnTime == 0 && this.canSmelt()) {
                    this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);

                    if (this.furnaceBurnTime > 0) {
                        flag1 = true;

                        if (this.furnaceItemStacks[1] != null) {
                            --this.furnaceItemStacks[1].stackSize;

                            if (this.furnaceItemStacks[1].stackSize == 0) {
                                this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    ++this.furnaceCookTime;

                    if (this.furnaceCookTime == 200) {
                        this.furnaceCookTime = 0;
                        this.smeltItem();
                        flag1 = true;
                    }
                } else {
                    this.furnaceCookTime = 0;
                }
            }

            if (flag != this.furnaceBurnTime > 0) {
                flag1 = true;
                BlockCustomFurnace.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt() {
        if (this.furnaceItemStacks[0] == null) {
            return false;
        } else {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);
            if (itemstack == null) return false;
            if (this.furnaceItemStacks[2] == null) return true;
            if (!this.furnaceItemStacks[2].isItemEqual(itemstack)) return false;
            int result = furnaceItemStacks[2].stackSize + itemstack.stackSize;
            return result <= getInventoryStackLimit() && result <= this.furnaceItemStacks[2].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0]);

            if (this.furnaceItemStacks[2] == null) {
                this.furnaceItemStacks[2] = itemstack.copy();
            } else if (this.furnaceItemStacks[2].getItem() == itemstack.getItem()) {
                this.furnaceItemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
            }

            --this.furnaceItemStacks[0].stackSize;

            if (this.furnaceItemStacks[0].stackSize <= 0) {
                this.furnaceItemStacks[0] = null;
            }
        }
    }

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : p_70300_1_.getDistanceSq((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
        return p_94041_1_ == 2 ? false : (p_94041_1_ == 1 ? isItemFuel(p_94041_2_) : true);
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return p_94128_1_ == 0 ? slotsBottom : (p_94128_1_ == 1 ? slotsTop : slotsSides);
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return this.isItemValidForSlot(slot, item);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return side != 0 || slot != 1 || item.getItem() == Items.bucket;
    }

    @Override
    public boolean onWrench(ItemStack stack, EntityPlayer player, World world,
                            int x, int y, int z, int side, float xO, float yO, float zO) {
        if (player.isSneaking()) {
            if (this.disguiseBlock != null) {
                if (!player.capabilities.isCreativeMode) {
                    EntityItem entityItem = new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(disguiseBlock, 1, disguiseMeta));
                    world.spawnEntityInWorld(entityItem);
                }
                world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), disguiseBlock.stepSound.getBreakSound(), (disguiseBlock.stepSound.getVolume() + 1.0F) / 2.0F, disguiseBlock.stepSound.getPitch() * 0.8F);
                disguiseBlock = null;
                this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                return true;
            }
        }
        return false;
    }

    @Override
    public Block getDisguiseBlock() {
        return this.disguiseBlock;
    }

    @Override
    public void setDisguiseBlock(Block block) {
        this.disguiseBlock = block;
    }

    @Override
    public int getDisguiseMeta() {
        return this.disguiseMeta;
    }

    @Override
    public void setDisguiseMeta(int meta) {
        this.disguiseMeta = meta;
    }
}