package eiteam.esteemedinnovation.naturalphilosophy;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemResearchLog extends Item {
    public ItemResearchLog() {
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            if (stack.hasTagCompound()) {
                NBTTagCompound compound = stack.getTagCompound();
                assert compound != null;
                if (compound.hasKey("keywords")) {
                    NBTTagList keywords = compound.getTagList("keywords", Constants.NBT.TAG_STRING);
                    for (int i = 0; i < keywords.tagCount(); i++) {
                        String keyword = keywords.getStringTagAt(i);
                        tooltip.add(keyword);
                    }
                }
            }
        } else {
            tooltip.add(TextFormatting.DARK_GRAY + I18n.format("esteemedinnovation.research.shiftforlist"));
        }
    }

    /**
     * Adds a keyword to this research log's list of unlocked keywords.
     * @param self The specific ItemStack
     * @param keyword The keyword to add
     */
    public boolean addKeyword(ItemStack self, String keyword) {
        if (!self.hasTagCompound()) {
            self.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound compound = self.getTagCompound();
        assert compound != null;
        if (!compound.hasKey("keywords")) {
            compound.setTag("keywords", new NBTTagList());
        }
        NBTTagList keywords = compound.getTagList("keywords", Constants.NBT.TAG_STRING);
        if (!hasKeyword(self, keyword)) {
            keywords.appendTag(new NBTTagString(keyword));
            compound.setTag("keywords", keywords);
            return true;
        }
        return false;
    }

    private boolean checkForTags(ItemStack self) {
        //noinspection ConstantConditions
        return self != null && self.hasTagCompound() && self.getTagCompound().hasKey("keywords");
    }

    /**
     * @param self The ItemStack
     * @param check The keyword to check for
     * @return Whether the research log has the desired keyword.
     */
    public boolean hasKeyword(ItemStack self, String check) {
        if (!checkForTags(self)) {
            return false;
        }
        NBTTagList keywords = self.getTagCompound().getTagList("keywords", Constants.NBT.TAG_STRING);
        for (int i = 0; i < keywords.tagCount(); i++) {
            String keyword = keywords.getStringTagAt(i);
            if (keyword.equals(check)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param self The ItemStack
     * @param toCheck A list of keywords to check for.
     * @return Whether the ItemStack research log has all of the desired keywords.
     */
    public boolean hasKeywords(ItemStack self, List<String> toCheck) {
        if (!checkForTags(self)) {
            return false;
        }

        int amountNeeded = toCheck.size();
        int amountFound = 0;
        //noinspection ConstantConditions
        NBTTagList keywords = self.getTagCompound().getTagList("keywords", Constants.NBT.TAG_STRING);
        for (int i = 0; i < keywords.tagCount(); i++) {
            if (toCheck.contains(keywords.getStringTagAt(i))) {
                amountFound++;
            }
        }
        return amountFound >= amountNeeded;
    }
}
