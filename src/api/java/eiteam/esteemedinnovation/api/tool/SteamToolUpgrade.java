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
 * This interface is used to create your own upgrades for {@link SteamTool SteamTools}. Below describes how you can
 * go about creating and registering your own own upgrades.
 *
 * <h2>Creating an upgrade</h2>
 * In most cases, you will need to create a new custom object/class in order to create a new upgrade for a steam tool.
 * It must inherit the interface, {@link SteamToolUpgrade}. Sometimes, you can get
 * away with using a generic base-class that provides simple and generic implementations of all of the required
 * {@link SteamToolUpgrade} methods.
 *
 * <h3>Special behavior</h3>
 * The methods above on their own do not provide any special behavior. They simply allow the upgrade to be put into
 * tools, and provide new icon overlays and tooltip info for them. SteamToolUpgrade
 * provides a variety of {@code on*} methods for special behavior. It is recommended to simply look at the methods
 * provided by the interface to see what is possible.
 * <p>
 * The {@code on*} methods that return a boolean will cancel their according event when false is returned by any
 * upgrade, preventing any further processing. There is no ensured order in which upgrade {@code on*} methods are called.
 * <p>
 * The {@code on*} methods all get called after making sure the tool is wound up. If you don't care about that, then you
 * need your own event handler.
 *
 * <h4>Tool strength</h4>
 * There are two methods that allow the upgrade to modify the tool's strength:
 * <ul>
 *     <li>{@link SteamToolUpgrade#getToolStrength(IBlockState, ItemStack, ItemStack)}</li>
 *     <li>{@link SteamToolUpgrade#modifiesToolStrength()}</li>
 * </ul>
 *
 * <h2>Registering an upgrade</h2>
 * The upgrade must be registered accordingly, otherwise it will not work. First, it must be registered as an
 * ordinary item. Then, it must be registered using
 * {@link ToolUpgradeRegistry#register(SteamToolUpgrade)}.
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
    String getInformation(@Nonnull ItemStack me, @Nonnull ItemStack tool);

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
    default int getToolStrength(IBlockState state, @Nonnull ItemStack toolStack, @Nonnull ItemStack upgradeStack) {
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
