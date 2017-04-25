package eiteam.esteemedinnovation.commons.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.List;

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

    /**
     * Gets a single entity from the player's look vec. Scans in a 5 block radius around the player,
     * and returns the "first" result.
     * @param player The player
     * @return The EntityLivingBase near the player.
     */
    public static EntityLivingBase getEntityFromPlayer(EntityPlayer player) {
        Vec3d vec = player.getLookVec();
        double x = vec.xCoord + player.posX;
        double y = vec.yCoord + player.posY;
        double z = vec.zCoord + player.posZ;

        AxisAlignedBB aabb = new AxisAlignedBB(x - 5, y - 5, z - 5, x + 5, y + 5, z + 5);
        List<Entity> entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, aabb);
        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase target = (EntityLivingBase) entity;
                if (player.canEntityBeSeen(target) && target.canBeCollidedWith()) {
                    return target;
                }
            }
        }
        return null;
    }

    /**
     * @param entity The entity.
     * @return The block state that is currently underneath the player.
     */
    public static IBlockState getBlockUnderEntity(Entity entity) {
        int x = MathHelper.floor_double(entity.posX);
        int y = MathHelper.floor_double(entity.getEntityBoundingBox().minY - 0.11F);
        int z = MathHelper.floor_double(entity.posZ);
        BlockPos underPos = new BlockPos(x, y, z);
        return entity.worldObj.getBlockState(underPos);
    }
}
