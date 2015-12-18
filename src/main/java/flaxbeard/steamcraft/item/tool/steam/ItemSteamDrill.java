package flaxbeard.steamcraft.item.tool.steam;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.entity.ExtendedPropertiesPlayer;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.List;
import java.util.HashMap;

public class ItemSteamDrill extends ItemPickaxe implements ISteamChargable, IEngineerable {
    public static HashMap<Integer, MutablePair<Integer, Integer>> stuff = new HashMap<>();
    public IIcon[] icon = new IIcon[2];
    private boolean hasBrokenBlock = false;
    public static final ResourceLocation largeIcons = new ResourceLocation("steamcraft:textures/gui/engineering2.png");

    public ItemSteamDrill() {
        super(EnumHelper.addToolMaterial("DRILL", 2, 320, 1.0F, -1.0F, 0));
    }

    public static void checkNBT(EntityPlayer player) {
        ExtendedPropertiesPlayer nbt = (ExtendedPropertiesPlayer)
          player.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);
        if (nbt.drillInfo == null) {
            nbt.drillInfo = MutablePair.of(0, 0);
        }
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        list.add(EnumChatFormatting.WHITE + "" + (me.getMaxDamage() - me.getItemDamage()) * this.steamPerDurability() + "/" + me.getMaxDamage() * this.steamPerDurability() + " SU");
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        hasBrokenBlock = true;
        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        checkNBT(player);
        ExtendedPropertiesPlayer nbt = (ExtendedPropertiesPlayer)
          player.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);

        MutablePair info = nbt.drillInfo;
        int ticks = (Integer) info.left;
        return this.icon[ticks > 50 ? 0 : 1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.icon[0] = this.itemIcon = ir.registerIcon("steamcraft:drill0");
        this.icon[1] = ir.registerIcon("steamcraft:drill1");
    }

    @Override
    public void onUpdate(ItemStack stack, World par2World, Entity player, int par4, boolean par5) {
        if (player instanceof EntityPlayer) {
            checkNBT((EntityPlayer) player);
            ExtendedPropertiesPlayer nbt = (ExtendedPropertiesPlayer)
              player.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);

            MutablePair info = nbt.drillInfo;
            int ticks = (Integer) info.left;
            int speed = (Integer) info.right;

            if (hasBrokenBlock) {
                speed -= 10;
                hasBrokenBlock = false;
            }
            int addedTicks = Math.min(((Double) Math.floor((double) speed / 1000D * 25D)).intValue(), 50);
            ticks += addedTicks;
            ////Steamcraft.log.debug("speed: "+speed + "; ticks: "+ticks + "; added: "+addedTicks);
            if (isWound((EntityPlayer) player)) {
                speed--;
            } else if (ticks <= 0) {
                ticks = 0;
            } else {
                ticks--;
            }

            ticks = ticks % 100;
            nbt.drillInfo = MutablePair.of(ticks, speed);
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
        checkNBT(player);
        ExtendedPropertiesPlayer nbt = (ExtendedPropertiesPlayer)
          player.getExtendedProperties(Steamcraft.PLAYER_PROPERTY_ID);

        if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
            MutablePair info = nbt.drillInfo;
            int ticks = (Integer) info.left;
            int speed = (Integer) info.right;
            if (speed <= 1000) {
                speed += Math.min(90, 1000 - speed);
                stack.damageItem(1, player);
            }
            nbt.drillInfo = MutablePair.of(ticks, speed);
        }
        return stack;

    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return 1.0F;
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionDrill;
    }

    @Override
    public boolean canCharge(ItemStack me) {
        return true;
    }

    // Start IEngineerable stuff
    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return new MutablePair[]{
          MutablePair.of(60, 12),
          MutablePair.of(37, 40)
        };
    }

    @Override
    public ItemStack getStackInSlot(ItemStack me, int var1) {
        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("upgrades")) {
                if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(var1))) {
                    return ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("upgrades").getCompoundTag(Integer.toString(var1)));
                }
            }
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(ItemStack me, int var1, ItemStack stack) {
        if (!me.hasTagCompound()) {
            me.setTagCompound(new NBTTagCompound());
        }
        if (!me.stackTagCompound.hasKey("upgrades")) {
            me.stackTagCompound.setTag("upgrades", new NBTTagCompound());
        }
        if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(var1))) {
            me.stackTagCompound.getCompoundTag("upgrades").removeTag(Integer.toString(var1));
        }
        NBTTagCompound stc = new NBTTagCompound();
        if (stack != null) {
            stack.writeToNBT(stc);
            me.stackTagCompound.getCompoundTag("upgrades").setTag(Integer.toString(var1), stc);
        }
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2) {
        return true;
    }

    @Override
    public ItemStack decrStackSize(ItemStack me, int var1, int var2) {
        if (this.getStackInSlot(me, var1) != null) {
            ItemStack stack;
            if (this.getStackInSlot(me, var1).stackSize <= var2) {
                stack = this.getStackInSlot(me, var1);
                this.setInventorySlotContents(me, var1, null);
            } else {
                stack = this.getStackInSlot(me, var1).splitStack(var2);
                this.setInventorySlotContents(me, var1, this.getStackInSlot(me, var1));

                if (this.getStackInSlot(me, var1).stackSize == 0) {
                    this.setInventorySlotContents(me, var1, null);
                }
            }
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void drawSlot(GuiContainer gui, int slotnum, int i, int j) {
        gui.mc.getTextureManager().bindTexture(GuiEngineeringTable.furnaceGuiTextures);
        switch (slotnum) {
            case 0: {
                gui.drawTexturedModalRect(i, j, 176, 0, 18, 18);
                break;
            }
            case 1: {
                gui.drawTexturedModalRect(i, j, 176, 0, 18, 18);
                break;
            }
        }
    }

    @Override
    public boolean canPutInSlot(ItemStack me, int slotNum, ItemStack upgrade) {
        if (upgrade != null && upgrade.getItem() instanceof ISteamToolUpgrade) {
            ISteamToolUpgrade upgradeItem = (ISteamToolUpgrade) upgrade.getItem();
            return (upgradeItem.getToolSlot().tool == 0 && upgradeItem.getToolSlot().slot == slotNum);
        }
        return false;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0, 128, 64, 64);
    }

    /**
     * Checks if the drill has a particular upgrade.
     * @param me The ItemStack version of the drill
     * @param check The item that is being checked against, or the upgrade
     * @return
     */
    public boolean hasUpgrade(ItemStack me, Item check) {
        if (check == null) {
            return false;
        }

        if (me.hasTagCompound()) {
            if (me.stackTagCompound.hasKey("upgrades")) {
                for (int i = 1; i < 10; i++) {
                    if (me.stackTagCompound.getCompoundTag("upgrades").hasKey(Integer.toString(i))) {
                        ItemStack stack = ItemStack.loadItemStackFromNBT(me.stackTagCompound.getCompoundTag("upgrades").getCompoundTag(Integer.toString(i)));
                        if (stack.getItem() == check) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the drill is wound up.
     * @param player The player to get the info for.
     * @return
     */
    public boolean isWound(EntityPlayer player) {
        checkNBT(player);
        MutablePair info = stuff.get(player.getEntityId());
        return ((int) info.right > 0);
    }
}
