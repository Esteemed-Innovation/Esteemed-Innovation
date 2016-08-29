package eiteam.esteemedinnovation.api.research;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;

/**
 * The ResearchRecipe is a ShapedOreRecipe that requires research to first be unlocked.
 * Its constructor takes an IResearchItem or IResearchBlock. This recipe will only work if the player has unlocked the
 * resulting IResearchObject. See {@link IResearchObject#isUnlocked(EntityPlayer)}.
 */
public class ResearchRecipe extends ShapedOreRecipe {
    private IResearchObject researchItem;
    private static final Field eventHandlerField = ReflectionHelper.findField(InventoryCrafting.class, "eventHandler", "field_70465_c");
    private static final Field playerPlayerField = ReflectionHelper.findField(ContainerPlayer.class, "thePlayer", "field_82862_h");
    private static final Field slotPlayerField = ReflectionHelper.findField(SlotCrafting.class, "thePlayer", "field_75238_b");

    public ResearchRecipe(IResearchObject.IResearchItem result, Object... recipe) {
        super((Item) result, recipe);

        researchItem = result;
    }

    public ResearchRecipe(IResearchObject.IResearchBlock result, Object... recipe) {
        super((Block) result, recipe);

        researchItem = result;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        EntityPlayer player = getCurrentPlayer(inv);
        return player != null && researchItem.isUnlocked(player) && super.matches(inv, world);
    }

    /**
     * Gets the player currently crafting this recipe.
     * @param inv The current crafting inventory
     * @return The player, or null if there either is no player, or they are using a weird crafting table we don't know about.
     */
    private EntityPlayer getCurrentPlayer(InventoryCrafting inv) {
        try {
            Container container = (Container) eventHandlerField.get(inv);
            if (container instanceof ContainerPlayer) {
                return (EntityPlayer) playerPlayerField.get(container);
            } else if (container instanceof ContainerWorkbench) {
                return (EntityPlayer) slotPlayerField.get(container.getSlot(0));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
