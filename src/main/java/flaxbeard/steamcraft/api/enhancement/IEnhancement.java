package flaxbeard.steamcraft.api.enhancement;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IEnhancement {
    /**
     * Whether this enhancement is valid for the given itemstack.
     * @param stack The ItemStack being upgraded.
     * @return Whether the enhancement can be used on the ItemStack.
     */
    boolean canApplyTo(ItemStack stack);

    /**
     * The enhancement's unique identifier. This should be something humanly readable and easily
     * identified in code. This is used when determining which upgrade is installed in the tool.
     * @return The String ID.
     */
    String getID();

    /**
     * The location of the new icon for the weapon. This is not the same as the enhancement's
     * texture name.
     * For example: "steamcraft:weaponRocketLauncherAirStrike". Follow the existing MC way of doing this.
     * @param item The weapon item.
     * @return The resource location string for the new weapon created by upgrading it.
     */
    String getIcon(Item item);

    /**
     * The unlocalized name, exactly as it is in the lang file, for the weapon that will be created
     * by installing this upgrade.
     * For example: "item.steamcraft:rocketLauncherAirStrike".
     * @param item The weapon item.
     * @return The unlocalized name of the new weapon.
     */
    String getName(Item item);
}
