package flaxbeard.steamcraft.block;

import flaxbeard.steamcraft.api.SteamcraftRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCarvingTable extends Block {
    public BlockCarvingTable() {
        super(Material.WOOD);
        setHardness(3.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (held != null) {
            Item heldItem = held.getItem();
            if (SteamcraftRegistry.molds.contains(heldItem)) {
                int index = 0;
                int i = 0;
                for (Item item : SteamcraftRegistry.molds) {
                    if (heldItem == item) {
                        index = i;
                    }
                    i++;
                }
                if (index + 1 == SteamcraftRegistry.molds.size()) {
                    index = -1;
                }
                Item next = SteamcraftRegistry.molds.get(index + 1);
                InventoryPlayer inventory = player.inventory;
                ItemStack stack = new ItemStack(next);
                inventory.setInventorySlotContents(inventory.currentItem, stack);
                return true;
            }
        }
        return false;
    }
}