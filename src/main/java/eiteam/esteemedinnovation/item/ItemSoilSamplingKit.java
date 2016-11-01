package eiteam.esteemedinnovation.item;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.api.util.ItemStackUtility;
import eiteam.esteemedinnovation.init.blocks.MiscellaneousBlocks;
import eiteam.esteemedinnovation.init.items.NaturalPhilosophyItems;
import eiteam.esteemedinnovation.misc.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ItemSoilSamplingKit extends Item {
    public ItemSoilSamplingKit() {
        setMaxStackSize(1);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack item, World world, IBlockState state, BlockPos pos, EntityLivingBase elb) {
        // FIXME: block break particles spawn but do not appear until after our particles, so it looks like they don't appear at all.
        if (elb.isSneaking() || !(elb instanceof EntityPlayer) || world.isRemote) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) elb;

        Item itemFromBlock = Item.getItemFromBlock(state.getBlock());
        if (itemFromBlock == null) {
            return false;
        }

        int oresFound = 0;
        Map<Item, Integer> ores = new HashMap<>();
        boolean oreDepositFound = false;

        if (OreDictHelper.dirts.contains(itemFromBlock) || OreDictHelper.grasses.contains(itemFromBlock) ||
          OreDictHelper.arrayHasItem(OreDictHelper.sands, itemFromBlock) || OreDictHelper.gravels.contains(itemFromBlock)) {
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
            for (int x = -6; x < 6; x++) {
                for (int z = -6; z < 6; z++) {
                    mutablePos.setPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                    while (world.getBlockState(mutablePos.move(EnumFacing.DOWN, 1)).getBlock() != Blocks.BEDROCK) {
                        Block blockCheck = world.getBlockState(mutablePos).getBlock();
                        Item itemFromBlockCheck = Item.getItemFromBlock(blockCheck);
                        if (itemFromBlockCheck != null) {
                            if (OreDictHelper.ores.contains(itemFromBlockCheck)) {
                                oresFound++;
                                Integer currentlyInMap = ores.get(itemFromBlockCheck);
                                if (currentlyInMap == null) {
                                    ores.put(itemFromBlockCheck, 1);
                                } else {
                                    ores.replace(itemFromBlockCheck, currentlyInMap + 1);
                                }
                            }
                            if (blockCheck == MiscellaneousBlocks.Blocks.ORE_DEPOSIT_BLOCK.getBlock()) {
                                oreDepositFound = true;
                            }
                        }
                    }
                }
            }
        }

        // Handle ore uniqueness
        if (((float) ores.size() * 100 / oresFound) >= 2.2F) {
            EsteemedInnovation.proxy.spawnAsteriskParticles(world, pos.getX(), pos.getY(), pos.getZ());
        }

        // Handle ore density
        if (!oreDepositFound) {
            int largestOreQuantity = 0;
            for (Item oreItem : ores.keySet()) {
                int oreQuantity = ores.get(oreItem);
                if (oreQuantity > largestOreQuantity) {
                    largestOreQuantity = oreQuantity;
                }
            }
            oreDepositFound = largestOreQuantity >= 60 && largestOreQuantity >= oresFound * (2F / 3F);
        }
        if (oreDepositFound) {
            EsteemedInnovation.proxy.spawnExclamationParticles(world, pos.getX(), pos.getY(), pos.getZ());
        }

        ItemResearchLog biomeLogItem = (ItemResearchLog) NaturalPhilosophyItems.Items.BIOME_LOG.getItem();
        ItemStack biomeLog = ItemStackUtility.findItemStackFromInventory(player.inventory, biomeLogItem);
        if (biomeLog != null) {
            String biomeName = world.getBiome(pos).getBiomeName();
            biomeLogItem.addKeyword(biomeLog, biomeName);
        }

        damage(item, player);
        return true;
    }

    /**
     * Tries to damage the provided ItemStack. Breaks it if it reaches negative damage.
     * @param item The ItemStack containing the kit
     * @param player The player using the kit
     */
    private void damage(ItemStack item, EntityPlayer player) {
        if (canBeDamaged(item)) {
            int newDamage = getDamage(item) - 1;
            if (newDamage == -1) {
                player.renderBrokenItemStack(item);
                player.addStat(StatList.getObjectBreakStats(item.getItem()));
                item.stackSize = 0;
            } else {
                item.getTagCompound().setInteger("Damage", newDamage);
            }
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("MaxDamage")) {
            return stack.getTagCompound().getInteger("MaxDamage");
        }
        return 0;
    }

    @Override
    public int getDamage(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("Damage")) {
            return stack.getTagCompound().getInteger("Damage");
        }
        return 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        int max = getMaxDamage(stack);
        if (canBeDamaged(max)) {
            return 1D - (getDamage(stack) / (double) max);
        }
        return 0D;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return canBeDamaged(stack);
    }

    /**
     * @param stack The ItemStack containing the kit
     * @return Whether this sampling kit can be damaged.
     */
    private boolean canBeDamaged(ItemStack stack) {
        return canBeDamaged(getMaxDamage(stack));
    }

    /**
     * @return Whether the maxDamage value is not 0, as returned by {@link #getMaxDamage(ItemStack)} when it does not
     * contain the proper NBT.
     */
    private static boolean canBeDamaged(int maxDamage) {
        return maxDamage != 0;
    }
}
