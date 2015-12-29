package flaxbeard.steamcraft.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import flaxbeard.steamcraft.api.block.IDisguisableBlock;
import flaxbeard.steamcraft.tile.TileEntitySteamPipe;

public class CamoPacketHandler implements IMessageHandler<CamoPacket, IMessage> {
    @Override
    public IMessage onMessage(CamoPacket message, MessageContext context) {
        EntityPlayerMP player = context.getServerHandler().playerEntity;
        World world = player.worldObj;
        int x = message.blockX;
        int y = message.blockY;
        int z = message.blockZ;

        ItemStack held = player.getHeldItem();
        if (held == null || held.getItem() == null) {
            return null;
        }
        int heldMeta = held.getItem().getMetadata(held.getItemDamage());
        Block block = Block.getBlockFromItem(held.getItem());
        TileEntity tile = world.getTileEntity(x, y, z);
        int renderType = block.getRenderType();
        if (!(block instanceof BlockContainer) && !(block instanceof ITileEntityProvider) &&
          (renderType == 0 || renderType == 39 || renderType == 31) && block.isOpaqueCube() &&
          (block.renderAsNormalBlock() || (block == Blocks.glass &&
          tile instanceof TileEntitySteamPipe))) {
            if (tile instanceof TileEntitySteamPipe) {
                TileEntitySteamPipe pipe = ((TileEntitySteamPipe) tile);
                if (pipe.disguiseBlock == null || (pipe.disguiseBlock != block) ||
                  pipe.disguiseMeta != heldMeta) {
                    if (!player.capabilities.isCreativeMode && pipe.disguiseBlock != null &&
                      !pipe.disguiseBlock.isAir(world, x, y, z)) {
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
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
                      (double) ((float) z + 0.5F), block.stepSound.func_150496_b(),
                      (block.stepSound.getVolume() + 1.0F) / 2.0F,
                      block.stepSound.getPitch() * 0.8F);

                    pipe.disguiseMeta = heldMeta;
                    world.markBlockForUpdate(x, y, z);
                }
            }
            if (tile instanceof IDisguisableBlock) {
                IDisguisableBlock pipe = ((IDisguisableBlock) tile);
                if (pipe.getDisguiseBlock() == null || pipe.getDisguiseBlock() != block ||
                  pipe.getDisguiseMeta() != heldMeta) {
                    if (pipe.getDisguiseBlock() != null &&
                      !pipe.getDisguiseBlock().isAir(world, x, y, z)
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
                    world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F),
                      (double) ((float) z + 0.5F), block.stepSound.func_150496_b(),
                      (block.stepSound.getVolume() + 1.0F) / 2.0F,
                      block.stepSound.getPitch() * 0.8F);

                    pipe.setDisguiseMeta(heldMeta);
                    world.markBlockForUpdate(x, y, z);
                }
            }
        }
        return null;
    }
}
