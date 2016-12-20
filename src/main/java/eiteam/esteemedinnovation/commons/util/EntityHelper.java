package eiteam.esteemedinnovation.commons.util;

import net.minecraft.entity.Entity;

public class EntityHelper {
    /**
     * @param entity The entity to check
     * @return Whether the provided {@link Entity}'s current coordinates are identical to its previous coordinates.
     *         It truncates the values with an int cast, so if they have only moved around in the same 1x1x1 block space
     *         it will return false.
     */
    public static boolean hasEntityMoved(Entity entity) {
        return (int) entity.posX != (int) entity.prevPosX ||
          (int) entity.posY != (int) entity.prevPosY ||
          (int) entity.posZ != (int) entity.prevPosZ;
    }
}
