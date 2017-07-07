package eiteam.esteemedinnovation.book;

import eiteam.esteemedinnovation.api.book.BookPageRegistry;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemEsteemedInnovationJournal extends Item {
    public ItemEsteemedInnovationJournal() {
        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        player.openGui(EsteemedInnovation.instance, 1, world, 0, 0, 0);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking() && world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            RayTraceResult rtr = new RayTraceResult(new Vec3d(hitX, hitY, hitZ), side, pos);
            ItemStack stack = state.getBlock().getPickBlock(state, rtr, world, pos, player);

            if (!stack.isEmpty()) {
                for (ItemStack stack2 : BookPageRegistry.bookRecipes.keySet()) {
                    if (stack2.getItem() == stack.getItem() && stack2.getItemDamage() == stack.getItemDamage()) {
                        GuiJournal.openRecipeFor(stack2, player);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }

        return EnumActionResult.FAIL;
    }
}
