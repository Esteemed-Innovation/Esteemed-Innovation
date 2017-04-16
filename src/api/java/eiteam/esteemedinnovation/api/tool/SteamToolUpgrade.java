package eiteam.esteemedinnovation.api.tool;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;

import javax.annotation.Nonnull;

/**
 * TODO: Write proper documentation for the interface.
 * <p>
 * The on* methods all get called *after* making sure the tool is wound up. If you don't care about that, then you
 * need your own event handler.
 */
public interface SteamToolUpgrade {
    /**
     * The slot that the upgrade can be used on
     * @see SteamToolSlot
     */
    SteamToolSlot getToolSlot();

    /**
     * The information added to the tool's tooltip. If it is null it will simply use the item's name
     * @param me The ItemStack of the upgrade.
     * @param tool The ItemStack containing the tool
     * @return The information string. Can be null, expect null.
     */
    String getInformation(ItemStack me, ItemStack tool);

    /**
     * @return The base icon name. Does not include the name of the tool (Drill, Saw, Shovel) or the index (0, 1).
     *         Return null if the upgrade does not add any texture to the tool.
     * <p>
     * Examples:
     * The void upgrade would return {@code esteemedinnovation:toolUpgrades/void}
     * The thermal upgrade would return {@code esteemedinnovation:toolUpgrades/thermal}
     */
    ResourceLocation getBaseIcon();

    /**
     * Whether the upgrade is a universal upgrade. This will determine how to load the icons.
     * @return Boolean
     */
    boolean isUniversal();

    /**
     * Called in {@link ItemSteamTool.ToolUpgradeEventDelegator#onBlockBreak(BlockEvent.BreakEvent)}
     * for every upgrade in the steam tool, after the ticks and speed NBT are updated.
     * @param event The actual event container. The player is not null.
     * @param toolStack The ItemStack containing the tool.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return Return false to cancel the breaking of the block and prevent any other processing from happening.
     */
    default boolean onBlockBreakWithTool(BlockEvent.BreakEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return true;
    }

    /**
     * Called in {@link ItemSteamTool.ToolUpgradeEventDelegator#onRightClickItem(PlayerInteractEvent.RightClickItem)}
     * for every upgrade in the steam tool.
     * @param event The actual event container.
     * @param toolStack The ItemStack containing the tool.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return Return false to cancel the click and prevent any other processing from happening.
     */
    default boolean onRightClickWithTool(PlayerInteractEvent.RightClickItem event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return true;
    }

    /**
     * Called from {@link ItemSteamTool.ToolUpgradeEventDelegator#onHarvestDrops(BlockEvent.HarvestDropsEvent)}
     * for every upgrade in the steam tool.
     * @param event The actual event container. A few things in the event are sanitized before this method is called.
     *              <p>
     *              The things sanitized/checked before this is called:
     *              <ul>
     *                  <li>The player ({@link BlockEvent.HarvestDropsEvent#getHarvester()})</li>
     *                  <li>The blockstate and block for the blockstate ({@link BlockEvent.HarvestDropsEvent#getState()} and {@link IBlockState#getBlock()}</li>
     *                  <li>The tool being used to harvest (which is passed as a parameter to this method). The nullness
     *                      of its item is also checked, as well as whether it is actually a {@link SteamTool}.</li>
     *              </ul>
     * @param toolStack The tool in the player's main hand, confirmed to contain a {@link SteamTool} and be nonnull.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     */
    default void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {}

    /**
     * Called from {@link ItemSteamTool.ToolUpgradeEventDelegator#onBlockBreakSpeedUpdate(PlayerEvent.BreakSpeed)}
     * for every upgrade in the steam tool. Although the event is cancellable, don't. Use onBlockBreakWithTool for that.
     * @param event The actual event container. A few things in the event are sanitized before this method is called.
     *              <p>
     *              The things sanitized/checked before this is called:
     *              <ul>
     *                  <li>The block and blockstate at the position are not null.</li>
     *              </ul>
     *              Use {@link net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed#setNewSpeed(float)} to set
     *              the new speed value, just as you would if you had subscribed to this event normally.
     * @param toolStack The ItemStack containing the tool (player's main hand). Confirmed to be nonnull and contain a
     *                  {@link SteamTool}.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     */
    default void onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {}

    /**
     * Called in {@link ItemSteamTool.ToolUpgradeEventDelegator#onRightClickBlock(PlayerInteractEvent.RightClickBlock)}
     * for every upgrade in the steam tool.
     * @param event The actual event container.
     * @param toolStack The ItemStack containing the tool.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return Return false to cancel the click and prevent any other processing from happening.
     */
    default boolean onRightClickBlockWithTool(PlayerInteractEvent.RightClickBlock event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return true;
    }

    /**
     * Called in {@link ItemSteamTool.ToolUpgradeEventDelegator#onLeftClickBlock(PlayerInteractEvent.LeftClickBlock)}
     * for every upgrade in the steam tool.
     * @param event The actual event container.
     * @param toolStack The ItemStack containing the tool
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return Return false to cancel the click and prevent any other processing from happening.
     */
    default boolean onLeftClickBlockWithTool(PlayerInteractEvent.LeftClickBlock event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return true;
    }

    /**
     * Called in {@link ItemSteamTool.ToolUpgradeEventDelegator#onAttack(LivingAttackEvent)} for every upgrade in the
     * steam tool.
     * @param attacker The player attacking.
     * @param victim The entity being attacked by the player.
     * @param damageSource The DamageSource.
     * @param toolStack The ItemStack containing the tool.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return Return false to cancel the attack and prevent any other processing from happening.
     */
    default boolean onAttackWithTool(@Nonnull EntityPlayer attacker, @Nonnull EntityLivingBase victim, DamageSource damageSource, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return true;
    }

    /**
     * Gets the harvest level for a tool with this upgrade installed. This is called only if {@link #modifiesToolStrength()}
     * returns true.
     * @param state The block state being used to mine.
     * @param toolStack The ItemStack for the tool being used to mine.
     * @param upgradeStack The ItemStack for the upgrade.
     * @return The strength against the block.
     */
    default int getToolStrength(IBlockState state, ItemStack toolStack, ItemStack upgradeStack) {
        return 0;
    }

    /**
     * @return If true, {@link #getToolStrength(IBlockState, ItemStack, ItemStack)} will be used for the tool's
     *         {@link net.minecraft.item.Item#canHarvestBlock(IBlockState, ItemStack)} implementation instead of the
     *         standard value.
     */
    default boolean modifiesToolStrength() {
        return false;
    }
}
