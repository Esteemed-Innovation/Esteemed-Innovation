package eiteam.esteemedinnovation.api.tool;

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
 *
 * The on* methods all get called *after* making sure the tool is wound up. If you don't care about that, then you
 * need your own event handler.
 */
public interface ISteamToolUpgrade {
    /**
     * The slot that the upgrade can be used on
     *
     * See {@link SteamToolSlot} for the list of slots.
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
     *
     * Examples:
     * The void upgrade would return `esteemedinnovation:toolUpgrades/void`
     * The thermal upgrade would return `esteemedinnovation:toolUpgrades/thermal`
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
     *              The things sanitized/checked before this is called:
     *              * The player (getHarvester)
     *              * The blockstate and block for the blockstate (getState and state#getBlock)
     *              * The tool being used to harvest (which is passed as a parameter to this method). The nullness of
     *                its item is also checked, as well as whether it is actually an ISteamTool.
     * @param toolStack The tool in the player's main hand, confirmed to contain an ISteamTool and be nonnull.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     */
    default void onPlayerHarvestDropsWithTool(BlockEvent.HarvestDropsEvent event, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {}

    /**
     * Called from {@link ItemSteamTool.ToolUpgradeEventDelegator#onBlockBreakSpeedUpdate(PlayerEvent.BreakSpeed)}
     * for every upgrade in the steam tool. Although the event is cancellable, don't. Use onBlockBreakWithTool for that.
     * @param event The actual event container. A few things in the event are sanitized before this method is called.
     *              The things sanitized/checked before this is called:
     *              * The block and blockstate at the position are not null.
     * @param newSpeed The speed that has been modified by each upgrade so far. It starts at 0.
     * @param toolStack The ItemStack containing the tool (player's main hand). Confirmed to be nonnull and contain an
     *                  ISteamTool.
     * @param thisUpgradeStack The ItemStack containing this upgrade.
     * @return The new speed. Passed as this method's newSpeed parameter for following calls on other upgrades. This is
     *         eventually set to the event's new speed, after all of the upgrades have been called. Return the newSpeed
     *         parameter to not change anything (this is what the default impl does).
     */
    default float onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, float newSpeed, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return newSpeed;
    }

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
}
