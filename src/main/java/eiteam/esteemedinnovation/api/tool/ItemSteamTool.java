package eiteam.esteemedinnovation.api.tool;

import com.google.common.collect.ImmutableSet;
import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.IEngineerable;
import eiteam.esteemedinnovation.api.ISteamChargable;
import eiteam.esteemedinnovation.gui.GuiEngineeringTable;
import eiteam.esteemedinnovation.handler.GenericEventHandler;
import eiteam.esteemedinnovation.item.armor.exosuit.ItemExosuitArmor;
import eiteam.esteemedinnovation.item.tool.steam.SteamToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public abstract class ItemSteamTool extends ItemTool implements ISteamChargable, IEngineerable, ISteamTool {
    private boolean hasBrokenBlock = false;
    protected static final ResourceLocation LARGE_ICONS = new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/gui/engineering2.png");
    private IdentityHashMap<ItemStack, MutablePair<Integer, Integer>> ticksSpeed = new IdentityHashMap<>();

    protected ItemSteamTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(toolClass());
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        /*
         We have to check the upgrades so that the models reload when you switch between two tools of the same type with
         different upgrades. Otherwise, it would appear to have oldStack's upgrades on it.
          */
        return !UtilSteamTool.getUpgrades(oldStack).equals(UtilSteamTool.getUpgrades(newStack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack me, EntityPlayer player, List<String> list, boolean advanced) {
        super.addInformation(me, player, list, advanced);
        list.add(TextFormatting.WHITE + "" + (me.getMaxDamage() - me.getItemDamage()) * steamPerDurability() + "/" + me.getMaxDamage() * steamPerDurability() + " SU");
        ArrayList<ItemStack> upgradeStacks = UtilSteamTool.getUpgradeStacks(me);
        ArrayList<String> upgradeStrings = SteamToolHelper.getInformationFromStacks(upgradeStacks, getRedSlot(), me);

        for (String string : upgradeStrings) {
            list.add(string);
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
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
    public void onUpdate(ItemStack stack, World world, Entity player, int itemSlot, boolean isSelected) {
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
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
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        return true;
    }

    @Override
    public boolean canCharge(ItemStack me) {
        return true;
    }

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
            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chest == null || !(chest.getItem() instanceof ItemExosuitArmor)) {
                return false;
            }
            ItemExosuitArmor armor = (ItemExosuitArmor) chest.getItem();
            if (armor.hasPower(chest, amount)) {
                int exoAmount = (-amount) / armor.steamPerDurability();
                GenericEventHandler.drainSteam(chest, exoAmount);
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
    public ItemStack getStackInSlot(ItemStack me, int slot) {
        if (me.hasTagCompound() && me.getTagCompound().hasKey("upgrades") &&
          me.getTagCompound().getCompoundTag("upgrades").hasKey(Integer.toString(slot))) {
            return ItemStack.loadItemStackFromNBT(me.getTagCompound().getCompoundTag("upgrades").getCompoundTag(Integer.toString(slot)));
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

    @Override
    public ItemStack decrStackSize(ItemStack me, int var1, int var2) {
        if (getStackInSlot(me, var1) != null) {
            ItemStack stack;
            if (getStackInSlot(me, var1).stackSize <= var2) {
                stack = getStackInSlot(me, var1);
                setInventorySlotContents(me, var1, null);
            } else {
                stack = getStackInSlot(me, var1).splitStack(var2);
                setInventorySlotContents(me, var1, getStackInSlot(me, var1));

                if (getStackInSlot(me, var1).stackSize == 0) {
                    setInventorySlotContents(me, var1, null);
                }
            }
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public void drawSlot(GuiContainer gui, int slotnum, int i, int j) {
        gui.mc.getTextureManager().bindTexture(GuiEngineeringTable.GUI_TEXTURES);
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
            return ((upgradeItem.getToolSlot().tool == getToolInteger() &&
              upgradeItem.getToolSlot().slot == slotNum) ||
              upgradeItem.getToolSlot() == SteamToolSlot.TOOL_CORE);
        }
        return false;
    }

    @Override
    public boolean isWound(ItemStack stack) {
        return SteamToolHelper.checkNBT(stack).getInteger("Speed") > 0;
    }

    @Override
    public boolean hasUpgrade(ItemStack me, Item check) {
        return UtilSteamTool.hasUpgrade(me, check);
    }

    /**
     * Returns the according integer from SteamToolSlot that corresponds with this tool.
     */
    public abstract int getToolInteger();

    /**
     * Returns the according SteamToolSlot that shows red text for upgrades in the tooltip.
     */
    @Nonnull
    public abstract SteamToolSlot getRedSlot();
}
