package eiteam.esteemedinnovation.armor.exosuit.steam.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.commons.util.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemPitonDeployerUpgrade extends ItemSteamExosuitUpgrade {
    public ItemPitonDeployerUpgrade() {
        super(ExosuitSlot.BODY_HAND, resource("pitonDeployer"), null, 1);
    }

    @Override
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event, EntityPlayer player, ItemStack armorStack, EntityEquipmentSlot slot) {
        if (armorStack.hasTagCompound()) {
            NBTTagCompound compound = armorStack.getTagCompound();
            if (compound.hasKey("IsGrappled") && compound.getBoolean("IsGrappled")) {
                double lastX = compound.getFloat("PlayerX");
                double lastY = compound.getFloat("PlayerY");
                double lastZ = compound.getFloat("PlayerZ");
                int blockX = compound.getInteger("BlockX");
                int blockY = compound.getInteger("BlockY");
                int blockZ = compound.getInteger("BlockZ");

                BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

                if ((Math.abs(lastX - player.posX) > 0.1F || Math.abs(lastZ - player.posZ) > 0.1F || player.isSneaking() || player.worldObj.isAirBlock(blockPos))) {
                    compound.setBoolean("IsGrappled", false);
                } else {
                    player.motionX = 0.0F;
                    player.motionY = (player.motionY > 0) ? player.motionY : 0.0F;
                    player.motionZ = 0.0F;
                }
            }
        }
    }

    @Override
    public void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event, ItemStack armorStack, EntityEquipmentSlot slot) {
        BlockPos pos = event.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        World world = event.getWorld();
        EnumFacing face = event.getFace();
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (face != EnumFacing.UP && block.isSideSolid(state, world, pos, face)) {
            AxisAlignedBB aabb;
            if (face == EnumFacing.DOWN) {
                aabb = new AxisAlignedBB(x - 0.5F, y + (face.getFrontOffsetY() / 6F) - 0.4F,
                  z - 0.20F, x + 0.5F + 1, y + (face.getFrontOffsetY() / 6F) + 1, z + 0.5F + 1);
            } else {
                aabb = new AxisAlignedBB(x + (face.getFrontOffsetX() / 6F),
                  y + (face.getFrontOffsetY() / 6F) - 1.0F, z + (face.getFrontOffsetZ() / 6F),
                  x + (face.getFrontOffsetX() / 6F) + 1, y + (face.getFrontOffsetY() / 6F) + 2.0F,
                  z + (face.getFrontOffsetZ() / 6F) + 1);
            }
            boolean canStick = false;
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
            for (EntityPlayer player1 : players) {
                if (player1 == player) {
                    canStick = true;
                }
            }
            if (canStick) {
                if (!world.isRemote) {
                    armorStack.getTagCompound().setFloat("PlayerX", (float) player.posX);
                    armorStack.getTagCompound().setFloat("PlayerZ", (float) player.posZ);
                    armorStack.getTagCompound().setFloat("PlayerY", (float) player.posY);
                    armorStack.getTagCompound().setInteger("BlockX", x);
                    armorStack.getTagCompound().setInteger("BlockY", y);
                    armorStack.getTagCompound().setInteger("BlockZ", z);
                    armorStack.getTagCompound().setBoolean("IsGrappled", true);
                }
                player.motionX = 0.0F;
                player.motionY = 0.0F;
                player.motionZ = 0.0F;
                player.fallDistance = 0.0F;
            }
        }
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event, ItemStack armorStack, EntityEquipmentSlot slot) {
        boolean isJumping = false;
        try {
            isJumping = ReflectionHelper.getIsEntityJumping(event.player);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (isJumping && event.side == Side.SERVER && armorStack.getTagCompound().hasKey("IsGrappled") &&
          armorStack.getTagCompound().getBoolean("IsGrappled")) {
            armorStack.getTagCompound().setBoolean("IsGrappled", false);
        }
    }
}
