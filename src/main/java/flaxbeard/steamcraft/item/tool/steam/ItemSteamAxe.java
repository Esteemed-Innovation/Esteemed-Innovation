package flaxbeard.steamcraft.item.tool.steam;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.tool.ISteamTool;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import flaxbeard.steamcraft.api.tool.UtilSteamTool;
import flaxbeard.steamcraft.entity.ExtendedPropertiesPlayer;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.ArrayList;
import java.util.List;

public class ItemSteamAxe extends ItemAxe implements ISteamChargable, IEngineerable, ISteamTool {
    public IIcon[] coreIcons = new IIcon[2];
    public IIcon[] headIcons = new IIcon[2];
    private boolean hasBrokenBlock = false;
    public static final ResourceLocation largeIcons = new ResourceLocation("steamcraft:textures/gui/engineering2.png");

    public ItemSteamAxe() {
        super(EnumHelper.addToolMaterial("AXE", 2, 320, 1.0F, -1.0F, 0));
    }

    public static NBTTagCompound checkNBT(ItemStack axe) {
        if (!axe.hasTagCompound()) {
            axe.setTagCompound(new NBTTagCompound());
        }
        if (!axe.getTagCompound().hasKey("Speed")) {
            axe.getTagCompound().setInteger("Speed", 0);
        }
        if (!axe.getTagCompound().hasKey("Ticks")) {
            axe.getTagCompound().setInteger("Ticks", 0);
        }
        return axe.getTagCompound();
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        list.add(EnumChatFormatting.WHITE + "" + (me.getMaxDamage() - me.getItemDamage()) * this.steamPerDurability() + "/" + me.getMaxDamage() * this.steamPerDurability() + " SU");
        ArrayList<ItemStack> upgradeStacks = UtilSteamTool.getUpgradeStacks(me);
        ArrayList<String> upgradeStrings = SteamToolHelper.getInformationFromStacks(upgradeStacks,
          SteamToolSlot.SAW_HEAD);
        if (upgradeStrings == null) {
            return;
        }

        for (String string : upgradeStrings) {
            list.add(string);
        }
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack p_150894_1_, World p_150894_2_, Block p_150894_3_, int p_150894_4_, int p_150894_5_, int p_150894_6_, EntityLivingBase p_150894_7_) {
        hasBrokenBlock = true;
        return true;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public int getRenderPasses(int meta) {
        return 2;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass) {
        NBTTagCompound nbt = checkNBT(stack);

        int which = nbt.getInteger("Ticks") > 50 ? 0 : 1;
        ArrayList<ISteamToolUpgrade> upgrades = UtilSteamTool.getUpgrades(stack);
        for (ISteamToolUpgrade upgrade : upgrades) {
            IIcon[] icons = upgrade.getIIcons();
            if (renderPass == upgrade.renderPriority() && icons != null) {
                if (upgrade.isUniversal()) {
                    which += 2;
                }
                if (icons.length >= which + 1 && icons[which] != null) {
                    return icons[which];
                }
                // Set it back for fallback.
                which -= 2;
            }
        }

        return renderPass == 0 ? coreIcons[which] : headIcons[which];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.coreIcons[0] = ir.registerIcon("steamcraft:axeBaseCore0");
        this.coreIcons[1] = ir.registerIcon("steamcraft:axeBaseCore1");
        this.headIcons[0] = ir.registerIcon("steamcraft:axeBaseHead0");
        this.headIcons[1] = ir.registerIcon("steamcraft:axeBaseHead1");
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
        if (player instanceof EntityPlayer) {
            NBTTagCompound nbt = checkNBT(stack);
            int ticks = nbt.getInteger("Ticks");
            int speed = nbt.getInteger("Speed");

            if (hasBrokenBlock) {
                speed -= 10;
                hasBrokenBlock = false;
            }
            int addedTicks = Math.min(((Double) Math.floor((double) speed / 1000D * 25D)).intValue(), 50);
            ticks += addedTicks;
            ////Steamcraft.log.debug("speed: "+speed + "; ticks: "+ticks + "; added: "+addedTicks);
            if (speed > 0) {
                speed--;
            } else if (ticks <= 0) {
                ticks = 0;
            } else {
                ticks--;
            }

            if (ticks <= 50 && speed > 0) {
                world.playSoundAtEntity(player, "minecraft:note.bassattack", 1.0F, 1.0F);
            }

            ticks = ticks % 100;
            nbt.setInteger("Ticks", ticks);
            nbt.setInteger("Speed", speed);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
        NBTTagCompound nbt = checkNBT(stack);

        int flag = -1;
        if (stack.getItemDamage() < stack.getMaxDamage() - 1) {
            flag = 0;
        } else if (SteamcraftEventHandler.hasPower(player, 1)) {
            flag = 1;
        }

        if (flag >= 0) {
            int ticks = nbt.getInteger("Ticks");
            int speed = nbt.getInteger("Speed");
            if (speed <= 1000) {
                speed += Math.min(90, 1000 - speed);
                if (flag == 0) {
                    addSteam(stack, -steamPerDurability(), player);
                } else if (flag == 1) {
                    SteamcraftEventHandler.drainSteam(player.getEquipmentInSlot(3), 1);
                }
            }

            nbt.setInteger("Ticks", ticks);
            nbt.setInteger("Speed", speed);
        }
        return stack;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public float getDigSpeed(ItemStack stack, Block block, int meta) {
        return 1.0F;
    }

    @Override
    public int steamPerDurability() {
        return Config.steamToolConsumptionAxe;
    }

    @Override
    public boolean canCharge(ItemStack me) {
        return true;
    }

    @Override
    public boolean addSteam(ItemStack me, int amount, EntityPlayer player) {
        int trueAmount = steamPerDurability() / (-amount);
        int newAmount = me.getItemDamage() + trueAmount;
        if (me.getMaxDamage() >= newAmount) {
            me.setItemDamage(newAmount);
            return true;
        }
        return false;
    }

    @Override
    public MutablePair<Integer, Integer>[] engineerCoordinates() {
        return SteamToolHelper.ENGINEER_COORDINATES;
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
        SteamToolHelper.setNBTInventory(me, var1, stack);
    }

    @Override
    public boolean isItemValidForSlot(ItemStack me, int var1, ItemStack var2) {
        return true;
    }

    @SuppressWarnings("Duplicates")
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

    @SuppressWarnings("Duplicates")
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
            return ((upgradeItem.getToolSlot().tool == 1 &&
              upgradeItem.getToolSlot().slot == slotNum) ||
              upgradeItem.getToolSlot() == SteamToolSlot.TOOL_CORE);
        }
        return false;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 64, 128, 64, 64);
    }

    @Override
    public boolean isWound(ItemStack stack) {
        return checkNBT(stack).getInteger("Speed") > 0;
    }

    @Override
    public boolean hasUpgrade(ItemStack me, Item check) {
        return UtilSteamTool.hasUpgrade(me, check);
    }

    @Override
    public String toolClass() {
        return "axe";
    }
}
