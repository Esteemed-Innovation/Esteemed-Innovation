package eiteam.esteemedinnovation.commons.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ItemStackHelper {
    /**
     * Drops all items inside the handler into the world
     * @param handler The {@link IItemHandler} to drop contents of
     * @param world The {@link World} to drop contents in
     * @param pos Where to drop the contents
     */
    public static void dropItems(IItemHandler handler, World world, BlockPos pos) {
        for (int i = 0; i < handler.getSlots(); i++) {
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), handler.getStackInSlot(i));
            world.spawnEntity(entityItem);
        }
    }
}
