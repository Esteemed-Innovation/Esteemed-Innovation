package eiteam.esteemedinnovation.init.blocks;

import eiteam.esteemedinnovation.EsteemedInnovation;
import eiteam.esteemedinnovation.block.pressureplate.BlockClassSensitivePlate;
import eiteam.esteemedinnovation.block.pressureplate.BlockWeightedPlate;
import eiteam.esteemedinnovation.init.IInitCategory;
import eiteam.esteemedinnovation.init.items.MetalItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class PressurePlateBlocks implements IInitCategory {
    public enum Blocks {
        COPPER_PLATE(new BlockClassSensitivePlate<>(MetalItems.Items.COPPER_PLATE.getMetadata(), EntityMob.class), "copper_pressure_plate"),
        ZINC_PLATE(new BlockClassSensitivePlate<>(MetalItems.Items.ZINC_PLATE.getMetadata(), EntityAgeable.class, EntityAgeable::isChild), "zinc_pressure_plate"),
        BRASS_PLATE(new BlockClassSensitivePlate<>(MetalItems.Items.BRASS_PLATE.getMetadata(), EntityAgeable.class, e -> !e.isChild()), "brass_pressure_plate"),
        // See Block.registerBlocks, the numbers come from there.
        GILDED_IRON_PLATE(new BlockWeightedPlate(150, MetalItems.Items.GILDED_IRON_PLATE.getMetadata()), "gilded_iron_pressure_plate"),
        IRON_PLATE(new BlockWeightedPlate(150, MetalItems.Items.IRON_PLATE.getMetadata()), "iron_pressure_plate"),
        GOLD_PLATE(new BlockWeightedPlate(15, MetalItems.Items.GOLD_PLATE.getMetadata()), "gold_pressure_plate");

        public static final Blocks[] LOOKUP = new Blocks[values().length];
        public static final Blocks[] LOOKUP_BY_ITEM_META = new Blocks[values().length];
        private Block block;

        static {
            for (Blocks block : values()) {
                LOOKUP[block.ordinal()] = block;
                LOOKUP_BY_ITEM_META[block.getBlock().damageDropped(null)] = block;
            }
        }

        Blocks(Block block, String name) {
            block.setUnlocalizedName(EsteemedInnovation.MOD_ID + ":" + name);
            block.setRegistryName(EsteemedInnovation.MOD_ID, name);
            GameRegistry.register(block);
            this.block = block;
        }

        public Block getBlock() {
            return block;
        }
    }

    @Override
    public void oreDict() {
        Blocks.values();
    }

    @Override
    public void recipes() {}
}
