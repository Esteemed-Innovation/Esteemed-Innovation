package eiteam.esteemedinnovation.metalcasting.mold;

import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.api.book.BookSection;
import eiteam.esteemedinnovation.api.mold.MoldRegistry;
import eiteam.esteemedinnovation.api.research.ResearchObject;
import eiteam.esteemedinnovation.metalcasting.MetalcastingBookSection;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCarvingTable extends Block implements ResearchObject.ResearchBlock {
    public BlockCarvingTable() {
        super(Material.WOOD);
        setHardness(3.5F);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (held != null) {
            Item heldItem = held.getItem();
            if (MoldRegistry.molds.stream().anyMatch(s -> s.getItem() == heldItem)) {
                int index = 0;
                int i = 0;
                for (ItemStack item : MoldRegistry.molds) {
                    if (heldItem == item.getItem() && held.getItemDamage() == item.getItemDamage()) {
                        index = i;
                    }
                    i++;
                }
                if (index + 1 == MoldRegistry.molds.size()) {
                    index = -1;
                }
                InventoryPlayer inventory = player.inventory;
                inventory.setInventorySlotContents(inventory.currentItem, MoldRegistry.molds.get(index + 1));
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isUnlocked(EntityPlayer player) {
        BookSection section = BookPageRegistry.getSectionFromName(MetalcastingBookSection.NAME);
        return section != null && section.isUnlocked(player);
    }
}