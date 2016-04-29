package flaxbeard.steamcraft.api.tool;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

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
     * The two (or six, if isUniversal is true) icons for the upgrade.
     * If the upgrade is universal, this MUST be in the following order:
     * 0 and 1: Drill textures
     * 2 and 3: Saw textures
     * 4 and 5: Shovel textures
     * @return The IIcon array.
     */
    IIcon[] getIIcons();

    /**
     * Whether the upgrade is a universal upgrade. This will determine how to load the icons.
     * @return Boolean
     */
    boolean isUniversal();
}
