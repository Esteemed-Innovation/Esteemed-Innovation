package flaxbeard.steamcraft.world;

import flaxbeard.steamcraft.Config;
import flaxbeard.steamcraft.SteamcraftBlocks;
import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.tile.TileEntityBoiler;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Random;

public class ComponentSteamWorkshop extends StructureVillagePieces.House1 {

    private int averageGroundLevel = -1;

    public ComponentSteamWorkshop() {
    }

    public ComponentSteamWorkshop(StructureVillagePieces.Start villagePiece, int par2, Random par3Random, StructureBoundingBox sbb, int coordBaseMode) {
        this();
        this.coordBaseMode = coordBaseMode;
        this.boundingBox = sbb;
    }

    public static ComponentSteamWorkshop buildComponent(StructureVillagePieces.Start villagePiece, List pieces, Random random, int x, int y, int z, int coordBaseMode, int p5) {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 6, 6, coordBaseMode);
        return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new ComponentSteamWorkshop(villagePiece, p5, random,
                structureboundingbox, coordBaseMode) : null;
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
        if (this.averageGroundLevel < 0) {
            this.averageGroundLevel = this.getAverageGroundLevel(world, sbb);

            if (this.averageGroundLevel < 0) {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLevel - this.boundingBox.maxY + 5, 0);
        }

        /**
         * arguments: (World worldObj, StructureBoundingBox structBB, int minX,
         * int minY, int minZ, int maxX, int maxY, int maxZ, int placeBlockId,
         * int replaceBlockId, boolean alwaysreplace)
         */

        this.fillWithBlocks(world, sbb, 0, 0, 0, 8, 0, 5, Blocks.cobblestone, Blocks.cobblestone, false); // Base
        this.fillWithBlocks(world, sbb, 0, 5, 0, 8, 5, 5, Blocks.fence, Blocks.fence, false);
        this.fillWithBlocks(world, sbb, 1, 0, 1, 7, 0, 4, Blocks.planks, Blocks.planks, false);

        if (this.getBlockAtCurrentPosition(world, 4, 0, -1, sbb).getMaterial() == Material.air && this.getBlockAtCurrentPosition(world, 1, -1, -1, sbb).getMaterial() != Material.air) {
            this.placeBlockAtCurrentPosition(world, Blocks.stone_stairs, this.getMetadataWithOffset(Blocks.stone_stairs, 3), 4, 0, -1, sbb);
        } else if (this.getBlockAtCurrentPosition(world, 4, 0, -1, sbb).getMaterial() == Material.air && this.getBlockAtCurrentPosition(world, 1, -1, -1, sbb).getMaterial() == Material.air) {
            int i = this.getMetadataWithOffset(Blocks.ladder, 3); // Ladders
            int f = 0;
            this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 4, 0, -1, sbb);
            while (this.getBlockAtCurrentPosition(world, 4, -1 - f, -1, sbb).getMaterial() == Material.air && f < 10) {
                this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 4, -1 - f, -1, sbb);
                f++;
            }
        }


        // this.fillWithBlocks(world, sbb, 0, 5, 0, 6, 5, 6, Blocks.log,
        // Blocks.log, false);

        this.fillWithBlocks(world, sbb, 0, 1, 0, 0, 4, 0, Blocks.log, Blocks.log, false); // Edges
        this.fillWithBlocks(world, sbb, 0, 1, 5, 0, 4, 5, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, sbb, 8, 1, 0, 8, 4, 0, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, sbb, 8, 1, 5, 8, 4, 5, Blocks.log, Blocks.log, false);

        this.fillWithBlocks(world, sbb, 0, 1, 1, 0, 4, 4, Blocks.planks, Blocks.planks, false); // Edges
        this.fillWithBlocks(world, sbb, 8, 1, 1, 8, 4, 4, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, sbb, 1, 1, 0, 7, 4, 0, Blocks.planks, Blocks.planks, false);
        this.fillWithBlocks(world, sbb, 1, 1, 5, 7, 4, 5, Blocks.planks, Blocks.planks, false);

        this.fillWithBlocks(world, sbb, 0, 4, 1, 0, 4, 4, Blocks.log, Blocks.log, false); // Edges
        this.fillWithBlocks(world, sbb, 8, 4, 1, 8, 4, 4, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, sbb, 1, 4, 0, 7, 4, 0, Blocks.log, Blocks.log, false);
        this.fillWithBlocks(world, sbb, 1, 4, 5, 7, 4, 5, Blocks.log, Blocks.log, false);

        this.fillWithBlocks(world, sbb, 1, 1, 1, 7, 5, 4, Blocks.air, Blocks.air, false);
        this.fillWithBlocks(world, sbb, 1, 4, 1, 7, 4, 4, Blocks.planks, Blocks.planks, false);
        // world, blockID, metadata, x, y, z, bounds
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 2, 0, sbb);// Glass and door
        //  this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 2, 2, 0, sbb);
        this.placeDoorAtCurrentPosition(world, sbb, random, 4, 1, 0, this.getMetadataWithOffset(Blocks.wooden_door, 1));
        // this.placeBlockAtCurrentPosition(world, Blocks.planks, 0, 4, 2, 0, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 6, 2, 0, sbb);

        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 2, 2, 5, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 3, 2, 5, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 5, 2, 5, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 6, 2, 5, sbb);

        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 0, 2, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 0, 2, 3, sbb);

        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 2, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.glass_pane, 0, 8, 2, 3, sbb);

        int i = this.getMetadataWithOffset(Blocks.ladder, 3); // Ladders
        this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 7, 1, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 7, 2, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 7, 3, 4, sbb);
        this.placeBlockAtCurrentPosition(world, Blocks.ladder, i, 7, 4, 4, sbb);

        i = this.getMetadataWithOffset(Blocks.sticky_piston, 4);
        this.placeBlockAtCurrentPosition(world, Blocks.sticky_piston, i, 7, 1, 1, sbb); // Piston

        i = this.getMetadataWithOffset(Blocks.ladder, 3);
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.boiler, 0, 1, 1, 4, sbb); // Boiler
        int x = this.getXWithOffset(1, 4);
        int y = this.getYWithOffset(1);
        int z = this.getZWithOffset(1, 4);
        world.setBlockMetadataWithNotify(x, y, z, i, 2);
        populateBoiler(world, x, y, z);

        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.pipe, 0, 1, 2, 4, sbb); // Pipes
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.pipe, 0, 2, 2, 4, sbb); // Pipes
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.pipe, 0, 3, 2, 4, sbb); // Pipes
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.pipe, 0, 4, 2, 4, sbb); // Pipes
        i = this.getMetadataWithOffset(Blocks.sticky_piston, 4);
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.heater, i, 4, 1, 4, sbb); // Heater

        i = this.getMetadataWithOffset(Blocks.sticky_piston, 5);
        this.placeBlockAtCurrentPosition(world, SteamcraftBlocks.heater, i, 2, 1, 4, sbb); // Heater

        i = this.getMetadataWithOffset(Blocks.ladder, 3);
        this.placeBlockAtCurrentPosition(world, Blocks.furnace, 0, 3, 1, 4, sbb); // Furnace
        x = this.getXWithOffset(3, 4);
        y = this.getYWithOffset(1);
        z = this.getZWithOffset(3, 4);
        world.setBlockMetadataWithNotify(x, y, z, i, 2);
        populateFurnace(world, x, y, z);

        for (int l = 0; l < 6; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.clearCurrentPositionBlocksUpwards(world, i1, 9, l, sbb);
                this.func_151554_b(world, Blocks.cobblestone, 0, i1, -1, l, sbb);
            }
        }
        this.spawnVillagers(world, sbb, 3, 1, 3, 1);

        return true;
    }

    private void populateFurnace(World world, int x, int y, int z) {
        Random rand = new Random(world.getSeed() + x + y + z);
        if (world.getTileEntity(x, y, z) instanceof TileEntityFurnace) {
            TileEntityFurnace furnace = (TileEntityFurnace) world.getTileEntity(x, y, z);
            ItemStack[] possibleLoot = {new ItemStack(SteamcraftItems.steamedBeef), new ItemStack(SteamcraftItems.steamedPorkchop), new ItemStack(SteamcraftItems.steamedChicken)};
            ItemStack loot = possibleLoot[rand.nextInt(possibleLoot.length)];
            loot.stackSize = rand.nextInt(3) + 1;
            furnace.setInventorySlotContents(2, loot);
        }
    }

    private void populateBoiler(World world, int x, int y, int z) {
        Random rand = new Random(world.getSeed() + x + y + z);
        if (world.getTileEntity(x, y, z) instanceof TileEntityBoiler) {
            TileEntityBoiler boiler = (TileEntityBoiler) world.getTileEntity(x, y, z);
            boiler.refresh();
            boiler.insertSteam(1000 + rand.nextInt(3000), ForgeDirection.UP);
            boiler.myTank.setFluid(new FluidStack(FluidRegistry.WATER, 2000 + rand.nextInt(3000)));
        }
    }

    public int getMetaForBoiler(int p_151555_2_) {
        if (this.coordBaseMode == 0) {
            return 2;
        } else if (this.coordBaseMode == 1) {
            return 3;
        } else if (this.coordBaseMode == 2) {
            return 4;
        }
        return 5;
    }

    @Override
    protected int getVillagerType(int par1) {
        return Config.villagerId;
    }

}
