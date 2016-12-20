package eiteam.esteemedinnovation.storage.item;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.List;

public class ItemKitBag extends Item {
    public ItemKitBag() {
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (isEmpty(stack)) {
            tooltip.add(I18n.format(EsteemedInnovation.MOD_ID + ".kitbag.empty"));
        } else {
            if (GuiScreen.isShiftKeyDown()) {
                NBTTagList list = stack.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
                for (int i = 0; i < list.tagCount(); i++) {
                    String unloc = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)).getUnlocalizedName();
                    tooltip.add(I18n.format(unloc + ".name"));
                }
            } else {
                tooltip.add(TextFormatting.DARK_GRAY + I18n.format(EsteemedInnovation.MOD_ID + ".kitbag.shiftforlist"));
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            if (isEmpty(itemStack)) {
                populate(itemStack, player.inventory);
            } else {
                depopulate(itemStack, player.inventory);
            }

            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        }
        return super.onItemRightClick(itemStack, world, player, hand);
    }

    /**
     * Checks whether the itemStack has any stored ItemStacks. It checks if it has the proper tags, and if not, creates
     * empty ones.
     * @param itemStack The ItemStack to check for emptiness.
     * @return True if the ItemStack has no items stored in it.
     */
    private static boolean isEmpty(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = itemStack.getTagCompound();
        if (!nbt.hasKey("Items")) {
            nbt.setTag("Items", new NBTTagList());
        }
        NBTTagList items = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        return items.hasNoTags();
    }

    /**
     * Fills the itemStack with the hotbar items in the inventory. Skips this specific item. {@link #isEmpty(ItemStack)}
     * must be called before this in order to ensure the proper tags exists.
     * @param itemStack The ItemStack to populate
     * @param inventory The inventory to depopulate
     */
    private static void populate(ItemStack itemStack, IInventory inventory) {
        // 9 slots in the hotbar.
        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
            ItemStack stackInSlot = inventory.getStackInSlot(i);
            if (stackInSlot != null && itemStack != stackInSlot) {
                itemStack.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND).appendTag(stackInSlot.serializeNBT());
                inventory.setInventorySlotContents(i, null);
            }
        }
    }

    /**
     * Depopulates the itemStack's list of stored items and inserts them into the inventory.
     * @param itemStack The ItemStack to depopulate
     * @param inventory The inventory populate
     */
    private static void depopulate(ItemStack itemStack, InventoryPlayer inventory) {
        NBTTagList list = itemStack.getTagCompound().getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
            // loadItemStackFromNBT can in fact return null.
            //noinspection ConstantConditions
            if (stack != null) {
                inventory.setInventorySlotContents(inventory.getFirstEmptyStack(), stack);
            }
        }
        itemStack.getTagCompound().removeTag("Items");
    }
}
