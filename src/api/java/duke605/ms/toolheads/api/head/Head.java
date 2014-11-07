package duke605.ms.toolheads.api.head;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class Head {

    public static enum ToolType {
        PICKAXE,
        AXE,
        HOE,
        SHOVEL
    }

    /** The type of tool head the head is */
    public final ToolType type;

    /** The name of the material the head is made out of */
    public final String material;

    /**
     * The chance the head has to pop off
     *
     * 0   - No chance
     * 100 - Will always pop off
     */
    public final int chance;

    /** The head as an ItemStack */
    public final ItemStack head;

    public Head(ToolType type, String material, int chance, ItemStack head) {
        this.chance = MathHelper.clamp_int(chance, 0, 100);
        this.material = material;
        this.type = type;
        this.head = head;
    }
}
