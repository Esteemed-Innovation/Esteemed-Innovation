package flaxbeard.steamcraft.entity.projectile;

import flaxbeard.steamcraft.misc.ExplosionRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMiningRocket extends EntityRocket {

    public EntityMiningRocket(World world) {
        super(world);
    }

    public EntityMiningRocket(World world, EntityPlayer par3EntityPlayer, float par4, float size) {
        super(world, par3EntityPlayer, par4, size);
        this.explosionSize += 1.0F;
    }

    @Override
    public Explosion newExplosion(World world, Entity entity, double x, double y, double z, float explosionSize, boolean doFire, boolean doSmokeAndGrief) {
        ExplosionRocket explosion = new ExplosionRocket(world, entity, x, y, z, explosionSize, doSmokeAndGrief, true);
        explosion.isFlaming = doFire;
        explosion.isSmoking = doSmokeAndGrief;
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        for (EntityPlayer entityplayer : world.playerEntities) {
            if (entityplayer.getDistanceSq(x, y, z) < 4096.0D) {
                SPacketExplosion packet = new SPacketExplosion(x, y, z, explosionSize, explosion.affectedBlockPositions, (Vec3d) explosion.func_77277_b().get(entityplayer));
                ((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(packet);
            }
        }

        return explosion;
    }
}
