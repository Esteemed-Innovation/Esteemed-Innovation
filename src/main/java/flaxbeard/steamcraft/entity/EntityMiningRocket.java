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

public class EntityMiningRocket extends EntityRocket {

    public EntityMiningRocket(World world) {
        super(world);
    }

    public EntityMiningRocket(World world, EntityPlayer par3EntityPlayer, float par4, float size) {
        super(world, par3EntityPlayer, par4, size);
        this.explosionSize += 1.0F;
    }

    @Override
    public Explosion newExplosion(World world, Entity p_72885_1_, double p_72885_2_, double p_72885_4_, double p_72885_6_, float p_72885_8_, boolean p_72885_9_, boolean p_72885_10_) {
        Explosion explosion = new ExplosionRocket(world, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_10_, true);
        explosion.isFlaming = p_72885_9_;
        explosion.isSmoking = p_72885_10_;
        explosion.doExplosionA();
        explosion.doExplosionB(true);

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
