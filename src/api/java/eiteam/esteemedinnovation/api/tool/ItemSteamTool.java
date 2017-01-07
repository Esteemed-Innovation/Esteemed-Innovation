package eiteam.esteemedinnovation.api.tool;

import com.google.common.collect.ImmutableSet;
import eiteam.esteemedinnovation.api.Constants;
import eiteam.esteemedinnovation.api.Engineerable;
import eiteam.esteemedinnovation.api.SteamChargable;
import eiteam.esteemedinnovation.api.exosuit.ExosuitArmor;
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
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public abstract class ItemSteamTool extends ItemTool implements SteamChargable, Engineerable, SteamTool {
    private boolean hasBrokenBlock = false;
    protected static final ResourceLocation LARGE_ICONS = new ResourceLocation(Constants.EI_MODID + ":textures/gui/engineering2.png");
    private IdentityHashMap<ItemStack, MutablePair<Integer, Integer>> ticksSpeed = new IdentityHashMap<>();

    protected ItemSteamTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
        super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
        MinecraftForge.EVENT_BUS.register(new ToolUpgradeEventDelegator());
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of(toolClass());
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        return getToolMaterial().getHarvestLevel() >= state.getBlock().getHarvestLevel(state);
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
        ArrayList<String> upgradeStrings = UtilSteamTool.getInformationFromStacks(upgradeStacks, getRedSlot(), me);

        for (String string : upgradeStrings) {
            list.add(string);
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        NBTTagCompound nbt = UtilSteamTool.checkNBT(stack);
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
            NBTTagCompound nbt = UtilSteamTool.checkNBT(stack);
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
        NBTTagCompound nbt = UtilSteamTool.checkNBT(stack);

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

    /**
     * @param itemStack The tool's ItemStack
     * @return The mining speed against a valid block
     */
    protected float getSpeed(ItemStack itemStack) {
        return getSpeed(UtilSteamTool.checkNBT(itemStack).getInteger("Speed"));
    }

    /**
     * @param speed The speed value in the tool's NBT
     * @return The mining speed against a valid block
     */
    protected float getSpeed(int speed) {
        return 11F * (speed / 1000F);
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
            if (chest == null || !(chest.getItem() instanceof ExosuitArmor)) {
                return false;
            }
            ExosuitArmor armor = (ExosuitArmor) chest.getItem();
            if (armor.hasPower(chest, amount)) {
                int exoAmount = (-amount) / armor.steamPerDurability();
                armor.drainSteam(chest, exoAmount);
                return true;
            }
        }
        return false;
    }

    @Override
    public Pair<Integer, Integer>[] engineerCoordinates() {
        return UtilSteamTool.ENGINEER_COORDINATES;
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
        UtilSteamTool.setNBTInventory(me, var1, stack);
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
        gui.mc.getTextureManager().bindTexture(Constants.ENG_GUI_TEXTURES);
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
        if (upgrade != null && upgrade.getItem() instanceof SteamToolUpgrade) {
            SteamToolUpgrade upgradeItem = (SteamToolUpgrade) upgrade.getItem();
            return ((upgradeItem.getToolSlot().tool == getToolInteger() &&
              upgradeItem.getToolSlot().slot == slotNum) ||
              upgradeItem.getToolSlot() == SteamToolSlot.TOOL_CORE);
        }
        return false;
    }

    @Override
    public boolean isWound(ItemStack stack) {
        return UtilSteamTool.checkNBT(stack).getInteger("Speed") > 0;
    }

    @Override
    public boolean hasUpgrade(ItemStack me, Item check) {
        return UtilSteamTool.hasUpgrade(me, check);
    }

    @Override
    public RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids) {
        return super.rayTrace(world, player, useLiquids);
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

    /**
     * Delegates events to {@link SteamToolUpgrade}'s according methods.
     *
     * Side note: I really wish that Java FP wasn't terrible...
     */
    private final class ToolUpgradeEventDelegator {
        /**
         * @param tool The ItemStack to check.
         * @return Whether the provided ItemStack contains a steam tool that is wound up.
         */
        private boolean isToolOkay(ItemStack tool) {
            return tool != null && tool.getItem() != null && tool.getItem() instanceof SteamTool && ((SteamTool) tool.getItem()).isWound(tool);
        }

        /**
         * Calls {@link SteamToolUpgrade#onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onHarvestDrops(BlockEvent.HarvestDropsEvent event) {
            EntityPlayer player = event.getHarvester();
            IBlockState state = event.getState();
            Block block = state.getBlock();
            if (player == null || block == null) {
                return;
            }
            ItemStack equipped = player.getHeldItemMainhand();
            if (!isToolOkay(equipped)) {
                return;
            }
            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                upgrade.onPlayerHarvestDropsWithTool(event, equipped, upgradeStack);
            }
        }

        /**
         * Calls {@link SteamToolUpgrade#onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onBlockBreakSpeedUpdate(PlayerEvent.BreakSpeed event) {
            ItemStack equipped = event.getEntityPlayer().getHeldItemMainhand();
            BlockPos pos = event.getPos();
            World world = event.getEntity().worldObj;
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            if (block == null || !isToolOkay(equipped)) {
                return;
            }

            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                upgrade.onUpdateBreakSpeedWithTool(event, equipped, upgradeStack);
            }
        }

        /**
         * Calls {@link SteamToolUpgrade#onBlockBreakWithTool(BlockEvent.BreakEvent, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onBlockBreak(BlockEvent.BreakEvent event) {
            EntityPlayer player = event.getPlayer();
            if (player == null) {
                return;
            }
            ItemStack equipped = player.getHeldItemMainhand();
            if (!isToolOkay(equipped)) {
                return;
            }
            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                if (!upgrade.onBlockBreakWithTool(event, equipped, upgradeStack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        /**
         * Calls {@link SteamToolUpgrade#onAttackWithTool(EntityPlayer, EntityLivingBase, DamageSource, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onAttack(LivingAttackEvent event) {
            DamageSource dSource = event.getSource();
            Entity source = dSource.getSourceOfDamage();
            if (!(source instanceof EntityPlayer)) {
                return;
            }
            EntityPlayer player = (EntityPlayer) source;
            ItemStack equipped = player.getHeldItemMainhand();
            if (!isToolOkay(equipped)) {
                return;
            }

            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                if (!upgrade.onAttackWithTool(player, event.getEntityLiving(), dSource, equipped, upgradeStack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        /**
         * Calls {@link SteamToolUpgrade#onRightClickBlockWithTool(PlayerInteractEvent.RightClickBlock, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            ItemStack equipped = event.getItemStack();
            if (!isToolOkay(equipped)) {
                return;
            }

            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                if (!upgrade.onRightClickBlockWithTool(event, equipped, upgradeStack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        @SubscribeEvent
        public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
            ItemStack equipped = event.getItemStack();
            if (!isToolOkay(equipped)) {
                return;
            }

            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                if (!upgrade.onLeftClickBlockWithTool(event, equipped, upgradeStack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        /**
         * Calls {@link SteamToolUpgrade#onRightClickWithTool(PlayerInteractEvent.RightClickItem, ItemStack, ItemStack)}
         * for every upgrade in the tool.
         */
        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            ItemStack equipped = event.getItemStack();
            if (!isToolOkay(equipped)) {
                return;
            }

            for (ItemStack upgradeStack : UtilSteamTool.getUpgradeStacks(equipped)) {
                SteamToolUpgrade upgrade = (SteamToolUpgrade) upgradeStack.getItem();
                if (!upgrade.onRightClickWithTool(event, equipped, upgradeStack)) {
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
