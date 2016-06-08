package flaxbeard.steamcraft.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockRuptureDiscItem extends ItemBlock {
    public BlockRuptureDiscItem(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        return super.getUnlocalizedName() + "." + par1ItemStack.getItemDamage();
    }

    public enum RuptureStates implements IStringSerializable {
        CLOSED(0, "closed"),
        RUPTURED(1, "ruptured");

        private int metadata;
        private String name;

        private static final RuptureStates[] LOOKUP = new RuptureStates[values().length];

        static {
            for (RuptureStates value : values()) {
                LOOKUP[value.getMetadata()] = value;
            }
        }

        RuptureStates(int metadata, String name) {
            this.metadata = metadata;
            this.name = name;
        }

        public int getMetadata() {
            return metadata;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }

        public static RuptureStates getStateByMetadata(int metadata) {
            return LOOKUP[metadata];
        }
    }
}
