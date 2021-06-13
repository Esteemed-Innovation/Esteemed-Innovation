package eiteam.esteemedinnovation.api.tool;

import eiteam.esteemedinnovation.api.SteamChargable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public interface SteamTool extends SteamChargable {
    /**
     * Checks if the tool is wound up.
     * @param stack The tool
     * @return Whether the tool has been wound up.
     */
    boolean isWound(ItemStack stack);

    /**
     * Checks if the tool has a particular upgrade.
     * @param me The ItemStack version of the tool
     * @param check The item that is being checked against, or the upgrade
     * @return Whether it has any upgrades.
     */
    boolean hasUpgrade(ItemStack me, Item check);

    /**
     * @return The Vanilla tool class associated with this tool.
     */
    String toolClass();

    /**
     * Public version of the {@link Item#rayTrace(World, EntityPlayer, boolean)} method. Default implementation in
     * {@link ItemSteamTool} delegates to the Item method.
     */
    @Nonnull
    RayTraceResult rayTrace(World world, EntityPlayer player, boolean useLiquids);
}
