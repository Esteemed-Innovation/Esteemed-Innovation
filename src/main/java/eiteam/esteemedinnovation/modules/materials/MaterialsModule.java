package eiteam.esteemedinnovation.modules.materials;

import eiteam.esteemedinnovation.base.EsteemedInnovation;
import eiteam.esteemedinnovation.base.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static eiteam.esteemedinnovation.base.EsteemedInnovation.MODID;

public class MaterialsModule extends Module {
	
	DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
	DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	
	public RegistryObject<Item> COPPER_INGOT = ITEMS.register("copper_ingot", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> ZINC_INGOT = ITEMS.register("zinc_ingot", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> BRASS_INGOT = ITEMS.register("brass_ingot", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> GILDED_IRON_INGOT = ITEMS.register("gilded_iron_ingot", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	
	public RegistryObject<Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> ZINC_NUGGET = ITEMS.register("zinc_nugget", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> BRASS_NUGGET = ITEMS.register("brass_nugget", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> GILDED_IRON_NUGGET = ITEMS.register("gilded_iron_nugget", () -> new Item(new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	
	public RegistryObject<Block> COPPER_BLOCK = BLOCKS.register("copper_block", () -> new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6).sound(SoundType.METAL)));
	public RegistryObject<Block> ZINC_BLOCK = BLOCKS.register("zinc_block", () -> new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6).sound(SoundType.METAL)));
	public RegistryObject<Block> BRASS_BLOCK = BLOCKS.register("brass_block", () -> new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6).sound(SoundType.METAL)));
	public RegistryObject<Block> GILDED_IRON_BLOCK = BLOCKS.register("gilded_iron_block", () -> new Block(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6).sound(SoundType.METAL)));
	
	public RegistryObject<Item> COPPER_BLOCK_ITEM = ITEMS.register("copper_block", () -> new BlockItem(COPPER_BLOCK.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> ZINC_BLOCK_ITEM = ITEMS.register("zinc_block", () -> new BlockItem(ZINC_BLOCK.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> BRASS_BLOCK_ITEM = ITEMS.register("brass_block", () -> new BlockItem(BRASS_BLOCK.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> GILDED_IRON_BLOCK_ITEM = ITEMS.register("gilded_iron_block", () -> new BlockItem(GILDED_IRON_BLOCK.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	
	public RegistryObject<Block> COPPER_PRESSURE_PLATE = BLOCKS.register("copper_pressure_plate", () -> new ClassSensitivePlateBlock<>(Block.Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD), MobEntity.class));
	public RegistryObject<Block> ZINC_PRESSURE_PLATE = BLOCKS.register("zinc_pressure_plate", () -> new ClassSensitivePlateBlock<>(Block.Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD), AgeableEntity.class, AgeableEntity::isChild));
	public RegistryObject<Block> BRASS_PRESSURE_PLATE = BLOCKS.register("brass_pressure_plate", () -> new ClassSensitivePlateBlock<>(Block.Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD), AgeableEntity.class, e -> !e.isChild()));
	public RegistryObject<Block> GILDED_IRON_PRESSURE_PLATE = BLOCKS.register("gilded_iron_pressure_plate", () -> new WeightedPressurePlateBlock(150, Block.Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD)));
	
	public RegistryObject<Item> COPPER_PRESSURE_PLATE_ITEM = ITEMS.register("copper_pressure_plate", () -> new BlockItem(COPPER_PRESSURE_PLATE.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> ZINC_PRESSURE_PLATE_ITEM = ITEMS.register("zinc_pressure_plate", () -> new BlockItem(ZINC_PRESSURE_PLATE.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> BRASS_PRESSURE_PLATE_ITEM = ITEMS.register("brass_pressure_plate", () -> new BlockItem(BRASS_PRESSURE_PLATE.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	public RegistryObject<Item> GILDED_IRON_PRESSURE_PLATE_ITEM = ITEMS.register("gilded_iron_pressure_plate", () -> new BlockItem(GILDED_IRON_PRESSURE_PLATE.get(), new Item.Properties().group(EsteemedInnovation.ITEM_GROUP)));
	
	public MaterialsModule() {
		super("materials");
		BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
