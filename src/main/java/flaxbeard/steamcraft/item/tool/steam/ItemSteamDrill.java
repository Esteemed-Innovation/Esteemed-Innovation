package flaxbeard.steamcraft.item.tool.steam;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.ISteamChargable;
import flaxbeard.steamcraft.api.tool.ISteamTool;
import flaxbeard.steamcraft.api.tool.SteamToolSlot;
import flaxbeard.steamcraft.api.tool.UtilSteamTool;
import flaxbeard.steamcraft.entity.ExtendedPropertiesPlayer;
import flaxbeard.steamcraft.api.IEngineerable;
import flaxbeard.steamcraft.api.tool.ISteamToolUpgrade;
import flaxbeard.steamcraft.gui.GuiEngineeringTable;
import flaxbeard.steamcraft.handler.SteamcraftEventHandler;
import flaxbeard.steamcraft.item.ItemExosuitArmor;
import flaxbeard.steamcraft.misc.DrillHeadMaterial;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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

import java.util.*;

public class ItemSteamDrill extends ItemPickaxe implements ISteamChargable, IEngineerable, ISteamTool {
    public IIcon[] coreIcons = new IIcon[2];
    public IIcon[] headIcons = new IIcon[2];
    private boolean hasBrokenBlock = false;
    public static final ResourceLocation largeIcons = new ResourceLocation("steamcraft:textures/gui/engineering2.png");
    private IdentityHashMap<ItemStack, MutablePair<Integer, Integer>> ticksSpeed = new IdentityHashMap<>();

    public ItemSteamDrill() {
        super(EnumHelper.addToolMaterial("DRILL", 2, 320, 1.0F, -1.0F, 0));
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List list, boolean par4) {
        super.addInformation(me, player, list, par4);
        list.add(EnumChatFormatting.WHITE + "" + (me.getMaxDamage() - me.getItemDamage()) * this.steamPerDurability() + "/" + me.getMaxDamage() * this.steamPerDurability() + " SU");
        ArrayList<ItemStack> upgradeStacks = UtilSteamTool.getUpgradeStacks(me);
        ArrayList<String> upgradeStrings = SteamToolHelper.getInformationFromStacks(upgradeStacks,
          SteamToolSlot.DRILL_HEAD, me);
        if (upgradeStrings == null) {
            return;
        }

        for (String string : upgradeStrings) {
            list.add(string);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        NBTTagCompound nbt = SteamToolHelper.checkNBT(stack);
        if (ticksSpeed.containsKey(stack)) {
            MutablePair<Integer, Integer> pair = ticksSpeed.get(stack);
            nbt.setInteger("Ticks", pair.getLeft());
            nbt.setInteger("Speed", pair.getRight());
            hasBrokenBlock = true;
            ticksSpeed.remove(stack);
        }
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
        NBTTagCompound nbt = SteamToolHelper.checkNBT(stack);

        int which = nbt.getInteger("Ticks") > 50 ? 0 : 1;
        ArrayList<ISteamToolUpgrade> upgrades = UtilSteamTool.getUpgrades(stack);
        for (ISteamToolUpgrade upgrade : upgrades) {
            if (upgrade instanceof ItemDrillHeadUpgrade && upgrade.renderPriority() == renderPass) {
                return headIcons[which];
            }
            IIcon[] icons = upgrade.getIIcons();
            // No special behavior for universal upgrades, as we expect 0 and 1 to be the
            // drill textures anyway.
            if (renderPass == upgrade.renderPriority() && icons != null &&
              icons.length >= which + 1 && icons[which] != null) {
                return icons[which];
            }
        }

        return renderPass == 0 ? coreIcons[which] : headIcons[which];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack self, int renderPass) {
        ArrayList<ItemStack> upgrades = UtilSteamTool.getUpgradeStacks(self);
        for (ItemStack upgrade : upgrades) {
            if (upgrade.getItem() instanceof ItemDrillHeadUpgrade &&
              ((ISteamToolUpgrade) upgrade.getItem()).renderPriority() == renderPass) {
                String materialName = ItemDrillHeadUpgrade.getMyMaterial(upgrade);
                DrillHeadMaterial material = DrillHeadMaterial.materials.get(materialName);
                return material.getColorInt();
            }
        }
        return super.getColorFromItemStack(self, renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir) {
        this.coreIcons[0] = ir.registerIcon("steamcraft:drillBaseCore0");
        this.coreIcons[1] = ir.registerIcon("steamcraft:drillBaseCore1");
        this.headIcons[0] = ir.registerIcon("steamcraft:drillBaseHead0");
        this.headIcons[1] = ir.registerIcon("steamcraft:drillBaseHead1");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
        if (player instanceof EntityPlayer) {
            NBTTagCompound nbt = SteamToolHelper.checkNBT(stack);
            int ticks = nbt.getInteger("Ticks");
            int speed = nbt.getInteger("Speed");

            if (hasBrokenBlock) {
                speed -= 10;
                hasBrokenBlock = false;
            }
            int addedTicks = Math.min(((Double) Math.floor((double) speed / 1000D * 25D)).intValue(), 50);
            ticks += addedTicks;
            ////Steamcraft.log.debug("speed: "+speed + "; ticks: "+ticks + "; added: "+addedTicks);
            if (isWound(stack)) {
                speed--;
            } else if (ticks <= 0) {
                ticks = 0;
            } else {
                ticks--;
            }

            ticks = ticks % 100;
            if (((EntityLivingBase) player).isSwingInProgress) {
                if (ticksSpeed.containsKey(stack)) {
                    ticksSpeed.get(stack).setLeft(ticks);
                    ticksSpeed.get(stack).setRight(speed);
                } else {
                    ticksSpeed.put(stack, MutablePair.of(ticks, speed));
                }
            } else {
                nbt.setInteger("Ticks", ticks);
                nbt.setInteger("Speed", speed);
            }
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World par2World, EntityPlayer player) {
        NBTTagCompound nbt = SteamToolHelper.checkNBT(stack);

        int ticks = nbt.getInteger("Ticks");
        int speed = nbt.getInteger("Speed");
        boolean result = false;
        if (speed <= 1000) {
            speed += Math.min(90, 1000 - speed);
            result = addSteam(stack, -steamPerDurability(), player);
        }

        if (result) {
            nbt.setInteger("Ticks", ticks);
            nbt.setInteger("Speed", speed);
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

    @SuppressWarnings("Duplicates")
    @Override
    public boolean addSteam(ItemStack me, int amount, EntityPlayer player) {
        int trueAmount = -amount / steamPerDurability();
        int newAmount = me.getItemDamage() + trueAmount;
        if (newAmount <= 0) {
            me.setItemDamage(0);
            return false;
        }
        if (me.getMaxDamage() >= newAmount) {
            me.setItemDamage(newAmount);
            return true;
        }
        if (amount < 0) {
            ItemStack chest = player.getEquipmentInSlot(3);
            if (chest == null || !(chest.getItem() instanceof ItemExosuitArmor)) {
                return false;
            }
            ItemExosuitArmor armor = (ItemExosuitArmor) chest.getItem();
            if (armor.hasPower(chest, amount)) {
                int exoAmount = (-amount) / armor.steamPerDurability();
                SteamcraftEventHandler.drainSteam(chest, exoAmount);
                return true;
            }
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
        if (upgrade != null) {
            if (upgrade.getItem() instanceof ISteamToolUpgrade) {
                ISteamToolUpgrade upgradeItem = (ISteamToolUpgrade) upgrade.getItem();
                return ((upgradeItem.getToolSlot().tool == 0 &&
                  upgradeItem.getToolSlot().slot == slotNum) ||
                  upgradeItem.getToolSlot() == SteamToolSlot.TOOL_CORE);
            }
        }
        return false;
    }

    @Override
    public void drawBackground(GuiEngineeringTable guiEngineeringTable, int i, int j, int k) {
        guiEngineeringTable.mc.getTextureManager().bindTexture(largeIcons);
        guiEngineeringTable.drawTexturedModalRect(j + 26, k + 3, 0, 128, 64, 64);
    }

    @Override
    public boolean isWound(ItemStack stack) {
        return SteamToolHelper.checkNBT(stack).getInteger("Speed") > 0;
    }

    @Override
    public boolean hasUpgrade(ItemStack me, Item check) {
        return UtilSteamTool.hasUpgrade(me, check);
    }

    @Override
    public String toolClass() {
        return "pickaxe";
    }
}
