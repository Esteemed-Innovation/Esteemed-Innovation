package eiteam.esteemedinnovation.api.enhancement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public interface Enhancement {
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
     * The location of the new model for the weapon. This is not the same as the enhancement's
     * texture name. The "inventory" is assumed by the firearms' mesher.
     * For example: EsteemedInnovation.MOD_ID + ":rocket_launcher_air_strike". Follow the existing MC way of doing this.
     * @param item The weapon item.
     * @return The resource location string for the new weapon created by upgrading it.
     */
    ResourceLocation getModel(Item item);

    /**
     * The unlocalized name, exactly as it is in the lang file, for the weapon that will be created
     * by installing this upgrade.
     * For example: "item.esteemedinnovation:rocketLauncherAirStrike".
     * @param item The weapon item.
     * @return The unlocalized name of the new weapon.
     */
    String getName(Item item);

    /**
     * Called in Item#onUpdate for enhancement-containing items.
     * @param weaponStack The ItemStack for the weapon.
     * @param world The current world
     * @param entity The current containing entity
     * @param itemSlot The slot in the entity's inventory that the weapon is contained in.
     * @param isWeaponCurrentItem Whether the weapon is the current item.
     */
    default void onWeaponUpdate(ItemStack weaponStack, World world, Entity entity, int itemSlot, boolean isWeaponCurrentItem) {}

    /**
     * @param weaponStack The ItemStack for the weapon.
     * @param world The world
     * @param user The user
     * @return The volume modifier for sounds produced by the weapon. Depending on the item and case,
     *         this might be the actual volume, or used in some volume determining math.
     */
    default float getVolume(ItemStack weaponStack, World world, EntityLivingBase user) {
        return 1F;
    }

    /**
     * Called <i>after</i> the weapon fires a round and the entity is spawned. This is after the ArrowLooseEvent is sent
     * and after the ItemStack's loaded NBT key is decremented.
     * @param weaponStack The ItemStack for the weapon
     * @param world The world
     * @param player The player firing the round.
     */
    default void afterRoundFired(ItemStack weaponStack, World world, EntityPlayer player) {}
}
