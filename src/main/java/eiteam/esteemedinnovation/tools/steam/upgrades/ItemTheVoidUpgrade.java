package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamTool;
import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import eiteam.esteemedinnovation.api.tool.SteamToolUpgrade;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemTheVoidUpgrade extends Item implements SteamToolUpgrade {
    private ResourceLocation baseOverlay;

    public ItemTheVoidUpgrade() {
        baseOverlay = new ResourceLocation(upgradeResource("void"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public SteamToolSlot getToolSlot() {
        return SteamToolSlot.TOOL_CORE;
    }

    @Override
    public String getInformation(ItemStack me, ItemStack tool) {
        if (tool.hasTagCompound() && tool.getTagCompound().hasKey("voidInventory")) {
            NBTTagCompound nbt = tool.getTagCompound().getCompoundTag("voidInventory");
            int x = nbt.getInteger("x");
            int y = nbt.getInteger("y");
            int z = nbt.getInteger("z");
            return I18n.format("esteemedinnovation.void.desc", x, y, z);
        }
        return null;
    }

    @Override
    public ResourceLocation getBaseIcon() {
        return baseOverlay;
    }

    @Override
    public boolean isUniversal() {
        return true;
    }

    /**
     * Plays the sounds and particles that indicate a void inventory set/unset.
     * @param world The world
     * @param pos The position of the inventory
     */
    private static void indicateVoidSet(World world, BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (world.isRemote) {
            for (int i = 0; i < world.rand.nextInt(10) + 5; i++) {
                // Particle spawning code taken from EntityEnderman.
                world.spawnParticle(EnumParticleTypes.PORTAL, x + (world.rand.nextDouble() - 0.5D),
                  y + world.rand.nextDouble(), z + (world.rand.nextDouble() - 0.5D),
                  (world.rand.nextDouble() - 0.5D) * 2.0D, -world.rand.nextDouble(),
                  (world.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }
        world.playSound(x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1F, 1F, false);
    }

    /**
     * Sets the X, Y, and Z NBT data for the given NBT compound.
     * @param nbt The NBTTagCompound to set the data in.
     * @param x The X coordinate.
     * @param y The Y coordinate.
     * @param z The Z coordinate.
     * @return The modified NBTTagCompound.
     */
    private static NBTTagCompound setVoidInventoryNBT(NBTTagCompound nbt, int x, int y, int z) {
        nbt.setInteger("x", x);
        nbt.setInteger("y", y);
        nbt.setInteger("z", z);
        return nbt;
    }

    @Override
    public boolean onRightClickBlockWithTool(PlayerInteractEvent.RightClickBlock event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isSneaking()) {
            return true;
        }
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof IInventory) || ((IInventory) tile).getSizeInventory() < 1) {
            return true;
        }

        if (!toolStack.hasTagCompound()) {
            toolStack.setTagCompound(new NBTTagCompound());
        }
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        if (toolStack.getTagCompound().hasKey("voidInventory")) {
            NBTTagCompound nbt = toolStack.getTagCompound().getCompoundTag("voidInventory");
            int existingX = nbt.getInteger("x");
            int existingY = nbt.getInteger("y");
            int existingZ = nbt.getInteger("z");
            if (existingX == x && existingY == y && existingZ == z) {
                toolStack.getTagCompound().removeTag("voidInventory");
            } else {
                toolStack.getTagCompound().setTag("voidInventory", setVoidInventoryNBT(nbt, x, y, z));
            }
        } else {
            toolStack.getTagCompound().setTag("voidInventory", setVoidInventoryNBT(new NBTTagCompound(), x, y, z));
        }
        indicateVoidSet(world, pos);
        return true;
    }

    /**
     * Adds the drops to the inventory.
     *
     * @param drops A List of items to add to the inventory.
     * @param inv   The inventory to add items to.
     * @return The items that did not get added.
     */
    private static List<ItemStack> addToInventory(List<ItemStack> drops, IInventory inv) {
        List<ItemStack> failures = new ArrayList<>();
        for (ItemStack drop : drops) {
            if (drop == null) {
                continue;
            }
            boolean added = false;
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stackInSlot = inv.getStackInSlot(i);
                if (stackInSlot == null) {
                    inv.setInventorySlotContents(i, drop);
                    added = true;
                    break;
                } else if (stackInSlot.getItem() == drop.getItem() &&
                  stackInSlot.getItemDamage() == drop.getItemDamage() &&
                  stackInSlot.stackSize + drop.stackSize < stackInSlot.getMaxStackSize()) {
                    stackInSlot.stackSize += drop.stackSize;
                    inv.setInventorySlotContents(i, stackInSlot);
                    added = true;
                    break;
                }
            }
            if (!added) {
                failures.add(drop);
            }
        }
        return failures;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void sendDropsToVoidInventory(BlockEvent.HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        if (player == null || event.getState() == null) {
            return;
        }

        World world = event.getWorld();
        ItemStack equipped = player.getHeldItemMainhand();
        if (equipped == null || player.isSneaking() || !(equipped.getItem() instanceof SteamTool) ||
          !((SteamTool) equipped.getItem()).hasUpgrade(equipped, this) ||
          !((SteamTool) equipped.getItem()).isWound(equipped)) {
            return;
        }

        List<ItemStack> failures;
        if (equipped.hasTagCompound() && equipped.getTagCompound().hasKey("voidInventory")) {
            NBTTagCompound nbt = equipped.getTagCompound().getCompoundTag("voidInventory");
            int invX = nbt.getInteger("x");
            int invY = nbt.getInteger("y");
            int invZ = nbt.getInteger("z");
            BlockPos blockPos = event.getPos();
            BlockPos invPos = new BlockPos(invX, invY, invZ);
            TileEntity tile = world.getTileEntity(invPos);
            if (tile == null || invPos == blockPos) {
                equipped.getTagCompound().removeTag("voidInventory");
                indicateVoidSet(world, blockPos);
                return;
            }
            failures = addToInventory(event.getDrops(), (IInventory) tile);
            if (tile instanceof ITickable) {
                ((ITickable) tile).update();
            }
        } else {
            InventoryEnderChest ender = player.getInventoryEnderChest();
            failures = addToInventory(event.getDrops(), ender);
            ender.saveInventoryToNBT();
        }
        event.getDrops().clear();
        if (!failures.isEmpty()) {
            event.getDrops().addAll(failures);
        }
    }
}
