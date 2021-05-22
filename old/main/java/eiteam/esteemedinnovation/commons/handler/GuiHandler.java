package eiteam.esteemedinnovation.commons.handler;

import eiteam.esteemedinnovation.boiler.ContainerBoiler;
import eiteam.esteemedinnovation.boiler.GuiBoiler;
import eiteam.esteemedinnovation.boiler.TileEntityBoiler;
import eiteam.esteemedinnovation.book.GuiJournal;
import eiteam.esteemedinnovation.engineeringtable.ContainerEngineeringTable;
import eiteam.esteemedinnovation.engineeringtable.GuiEngineeringTable;
import eiteam.esteemedinnovation.engineeringtable.TileEntityEngineeringTable;
import eiteam.esteemedinnovation.hammer.ContainerSteamAnvil;
import eiteam.esteemedinnovation.hammer.GuiSteamAnvil;
import eiteam.esteemedinnovation.hammer.TileEntitySteamHammer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public GuiHandler() {}

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity entity = world.getTileEntity(pos);
        switch (id) {
            case 0: {
                if (entity != null && entity instanceof TileEntityBoiler) {
                    return new ContainerBoiler(player.inventory, (TileEntityBoiler) entity);
                } else {
                    return null;
                }
            }
            case 2: {
                if (entity != null && entity instanceof TileEntityEngineeringTable) {
                    return new ContainerEngineeringTable(player.inventory, (TileEntityEngineeringTable) entity);
                } else {
                    return null;
                }
            }
            case 3: {
                if (entity != null && entity instanceof TileEntitySteamHammer) {
                    return new ContainerSteamAnvil(player.inventory, (TileEntitySteamHammer) entity, world, x, y - 1, z, player);
                } else {
                    return null;
                }
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity entity = world.getTileEntity(pos);
        switch (id) {
            case 0: {
                if (entity != null && entity instanceof TileEntityBoiler) {
                    return new GuiBoiler(player.inventory, (TileEntityBoiler) entity);
                } else {
                    return null;
                }
            }
            case 1: {
                return new GuiJournal(player);
            }
            case 2: {
                if (entity != null && entity instanceof TileEntityEngineeringTable) {
                    return new GuiEngineeringTable(player.inventory, (TileEntityEngineeringTable) entity);
                } else {
                    return null;
                }
            }
            case 3: {
                if (entity != null && entity instanceof TileEntitySteamHammer) {
                    return new GuiSteamAnvil(player.inventory, (TileEntitySteamHammer) entity, world, x, y - 1, z);
                } else {
                    return null;
                }
            }
            default: {
                return null;
            }
        }
    }
}