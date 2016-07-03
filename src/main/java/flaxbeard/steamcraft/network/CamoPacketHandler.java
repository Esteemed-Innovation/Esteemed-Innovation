package flaxbeard.steamcraft.network;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.misc.ItemStackUtility;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class CamoPacketHandler implements IMessageHandler<CamoPacket, IMessage> {
    @Override
    public IMessage onMessage(CamoPacket message, MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().playerEntity;
        World world = player.worldObj;
        int x = message.blockX;
        int y = message.blockY;
        int z = message.blockZ;

        BlockPos pos = new BlockPos(x, y, z);

        ItemStack held = ItemStackUtility.getHeldItemStack(player);
        if (held == null || held.getItem() == null) {
            return null;
        }
        int heldMeta = held.getItem().getMetadata(held.getItemDamage());
        Block block = Block.getBlockFromItem(held.getItem());

        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        EnumBlockRenderType renderType = block.getRenderType(state);
        if (!(block instanceof BlockContainer) && !(block instanceof ITileEntityProvider) &&
          renderType != EnumBlockRenderType.ENTITYBLOCK_ANIMATED && renderType != EnumBlockRenderType.LIQUID &&
          block.isOpaqueCube(state) && (block.isFullBlock(state) || (block == Blocks.GLASS &&
          tile instanceof TileEntitySteamPipe))) {
            if (tile instanceof TileEntitySteamPipe) {
                TileEntitySteamPipe pipe = ((TileEntitySteamPipe) tile);
                if (pipe.disguiseBlock == null || (pipe.disguiseBlock != block) ||
                  pipe.disguiseMeta != heldMeta) {
                    if (!player.capabilities.isCreativeMode && pipe.disguiseBlock != null &&
                      !pipe.disguiseBlock.isAir(state, world, pos)) {
                        EntityItem entityItem = new EntityItem(world, player.posX, player.posY,
                          player.posZ, new ItemStack(pipe.disguiseBlock, 1, pipe.disguiseMeta));
                        world.spawnEntityInWorld(entityItem);
                        pipe.disguiseBlock = null;
                    }

                    pipe.disguiseBlock = block;
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.getCurrentItem().stackSize--;
                        player.inventoryContainer.detectAndSendChanges();
                    }

                    playSound(block, world, pos);

                    pipe.disguiseMeta = heldMeta;
                    tile.markDirty();
                }
            }
            if (tile instanceof IDisguisableBlock) {
                IDisguisableBlock pipe = ((IDisguisableBlock) tile);
                if (pipe.getDisguiseBlock() == null || pipe.getDisguiseBlock() != block ||
                  pipe.getDisguiseMeta() != heldMeta) {
                    if (pipe.getDisguiseBlock() != null &&
                      !pipe.getDisguiseBlock().isAir(state, world, pos)
                      && !player.capabilities.isCreativeMode) {
                        EntityItem entityItem = new EntityItem(world, player.posX, player.posY,
                          player.posZ, new ItemStack(pipe.getDisguiseBlock(), 1,
                          pipe.getDisguiseMeta()));
                        world.spawnEntityInWorld(entityItem);
                        pipe.setDisguiseBlock(null);
                    }

                    pipe.setDisguiseBlock(block);
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.getCurrentItem().stackSize--;
                        player.inventoryContainer.detectAndSendChanges();
                    }
                    playSound(block, world, pos);

                    pipe.setDisguiseMeta(heldMeta);
                    tile.markDirty();
                }
            }
        }
        return null;
    }

    /**
     * Plays the sound for the block at the position.
     * @param block The block
     * @param world The world
     * @param pos The block position
     */
    private void playSound(Block block, World world, BlockPos pos) {
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        SoundType sound = block.getSoundType();
        float vol = (sound.getVolume() + 1F) / 2F;
        float pitch = sound.getPitch() * 0.8F;
        world.playSound(x, y, z, sound.getPlaceSound(), SoundCategory.BLOCKS, vol, pitch, false);
    }
}
