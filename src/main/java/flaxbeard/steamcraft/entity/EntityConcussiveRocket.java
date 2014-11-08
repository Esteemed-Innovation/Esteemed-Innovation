package flaxbeard.steamcraft.entity;

import flaxbeard.steamcraft.misc.ExplosionRocket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.Iterator;

public class EntityConcussiveRocket extends EntityRocket {

    public EntityConcussiveRocket(World world) {
        super(world);
    }

    public EntityConcussiveRocket(World world, EntityPlayer par3EntityPlayer, float par4, float size) {
        super(world, par3EntityPlayer, par4, size);
    }

    @Override
    public Explosion newExplosion(World world, Entity entity, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
        Explosion explosion = new ExplosionRocket(world, entity, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, false);
        explosion.isFlaming = p_72885_9_;
        explosion.isSmoking = p_72885_10_;
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        if (!p_72885_10_) {
            explosion.affectedBlockPositions.clear();
        }

        Iterator iterator = world.playerEntities.iterator();

        while (iterator.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer) iterator.next();

            if (entityplayer.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0D) {
                ((EntityPlayerMP) entityplayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, explosion.affectedBlockPositions, (Vec3) explosion.func_77277_b().get(entityplayer)));
            }
        }

        return explosion;
    }

}
