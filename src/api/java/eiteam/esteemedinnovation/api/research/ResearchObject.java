package eiteam.esteemedinnovation.api.research;

import net.minecraft.entity.player.EntityPlayer;

/**
 * The ResearchObject represents an item or block that requires research before it can be created.
 *
 * Do not implement this interface. Instead, implement ResearchItem or ResearchBlock. Otherwise, there's no way
 * to tell if it is an item or block at compile time.
 *
 * IResearchItems and IResearchBlocks <b>must</b> extend Item or Block respectively. You will crash if you don't.
 */
public interface ResearchObject {
    /**
     * @param player The player
     * @return Whether the player has unlocked and can craft this research object.
     */
    boolean isUnlocked(EntityPlayer player);

    interface ResearchItem extends ResearchObject {}
    interface ResearchBlock extends ResearchObject {}
}
