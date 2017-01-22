package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitSlot;
import eiteam.esteemedinnovation.api.exosuit.ExosuitUtility;
import eiteam.esteemedinnovation.commons.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.BlockFluidBase;

import static eiteam.esteemedinnovation.armor.ArmorModule.resource;

public class ItemPistonPushUpgrade extends ItemExosuitUpgrade {
    public ItemPistonPushUpgrade() {
        super(ExosuitSlot.BODY_HAND, resource("pistonarm"), null, 0);
    }

    @Override
    public void onPlayerLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, ItemStack armorStack, EntityEquipmentSlot slot) {
        EnumFacing face = event.getFace();
        if (face == null) {
            return;
        }
        EntityPlayer player = event.getEntityPlayer();
        if (player.getHeldItemMainhand() == null && ExosuitUtility.hasPower(player, Config.pistonPushConsumption)) {
            World world = event.getWorld();
            BlockPos curPos = event.getPos();
            BlockPos newPos = curPos.offset(face.getOpposite());
            IBlockState clickedState = world.getBlockState(curPos);
            IBlockState stateInPlace = world.getBlockState(newPos);
            Block clickedBlock = clickedState.getBlock();
            Block blockInPlace = stateInPlace.getBlock();
            EnumPushReaction reaction = clickedBlock.getMobilityFlag(clickedState);
            if ((blockInPlace == Blocks.AIR || blockInPlace instanceof BlockFluidBase) &&
              clickedState.getBlockHardness(world, curPos) >= 0F &&
              reaction != EnumPushReaction.IGNORE && reaction != EnumPushReaction.DESTROY &&
              clickedBlock != Blocks.OBSIDIAN && !clickedBlock.hasTileEntity(clickedState)) {
                world.setBlockToAir(curPos);
                world.setBlockState(newPos, clickedState, 3);
                world.playSound(newPos.getX() + 0.5D, newPos.getY() + 0.5D, newPos.getZ() + 0.5D, SoundEvents.BLOCK_PISTON_EXTEND,
                  SoundCategory.PLAYERS, 0.5F, world.rand.nextFloat() * 0.25F + 0.6F, false);
            }
        }
    }
}
