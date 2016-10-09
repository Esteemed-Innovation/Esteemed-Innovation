package eiteam.esteemedinnovation.api.tool;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public interface ISteamToolUpgrade {

    /**
     * The priority of the upgrade's rendering. This is essentially the render pass.
     * Typically, you will use 1 for head upgrades, and 0 for core upgrades because head should go
     * on top of the body, logically, however you are free to be as creative as you' like.
     * Each upgrade should have a 0 and 1 texture, for the two different icons rendered at
     * alternating wound ticks. If you use anything above 1, it will not be rendered at all.
     */
    int renderPriority();

    /**
     * The slot that the upgrade can be used on
     *
     * See SteamToolSlot.java for the list of slots.
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
}
