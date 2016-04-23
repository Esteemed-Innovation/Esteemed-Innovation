package flaxbeard.steamcraft.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * AnimalTradeEvent is fired when a player trades with a cat or a dog using the Frequency Transmitter.
 */
public class AnimalTradeEvent extends Event {
    /**
     * The ocelot or wolf selling the items.
     */
    public final EntityLiving salesperson;

    /**
     * The customer buying items.
     */
    public final EntityPlayer customer;

    /**
     * The ItemStack to buy.
     */
    private final ItemStack toBuy;

    /**
     * The ItemStack to sell.
     */
    private final ItemStack toSell;

    /**
     * The constructor for the AnimalTradeEvent.
     * @param entity The EntityLiving that is selling items.
     * @param customer The EntityPlayer that is buying items.
     * @param toBuy The ItemStack to buy.
     * @param toSell The ItemStack to sell.
     */
    public AnimalTradeEvent(EntityLiving entity, EntityPlayer customer, ItemStack toBuy, ItemStack toSell) {
        this.salesperson = entity;
        this.customer = customer;
        this.toBuy = toBuy;
        this.toSell = toSell;
    }
}
