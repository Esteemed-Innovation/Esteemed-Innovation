package eiteam.esteemedinnovation.modules.materials;

import eiteam.esteemedinnovation.base.module.Module;
import eiteam.esteemedinnovation.modules.materials.datagen.BlockStateProvider;
import eiteam.esteemedinnovation.modules.materials.datagen.ItemModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.block.material.Material;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static eiteam.esteemedinnovation.base.EsteemedInnovation.MODID;
import static eiteam.esteemedinnovation.base.ModNames.*;

@ObjectHolder(MODID)
public class MaterialsModule extends Module {
	
	@ObjectHolder(COPPER + Suffix.INGOT) public static Item copperIngot;
	@ObjectHolder(ZINC + Suffix.INGOT) public static Item zincIngot;
	@ObjectHolder(BRASS + Suffix.INGOT) public static Item brassIngot;
	@ObjectHolder(GILDED_IRON + Suffix.INGOT) public static Item gildedIronIngot;
	
	@ObjectHolder(COPPER + Suffix.NUGGET) public static Item copperNugget;
	@ObjectHolder(ZINC + Suffix.NUGGET) public static Item zincNugget;
	@ObjectHolder(BRASS + Suffix.NUGGET) public static Item brassNugget;
	@ObjectHolder(GILDED_IRON + Suffix.NUGGET) public static Item gildedIronNugget;
	
	@ObjectHolder(COPPER + Suffix.BLOCK) public static Block copperBlock;
	@ObjectHolder(ZINC + Suffix.BLOCK) public static Block zincBlock;
	@ObjectHolder(BRASS + Suffix.BLOCK) public static Block brassBlock;
	@ObjectHolder(GILDED_IRON + Suffix.BLOCK) public static Block gildedIronBlock;
	
	@ObjectHolder(COPPER + Suffix.BLOCK) public static Item copperBlockItem;
	@ObjectHolder(ZINC + Suffix.BLOCK) public static Item zincBlockItem;
	@ObjectHolder(BRASS + Suffix.BLOCK) public static Item brassBlockItem;
	@ObjectHolder(GILDED_IRON + Suffix.BLOCK) public static Item gildedIronBlockItem;
	
	@ObjectHolder(COPPER + Suffix.PRESSURE_PLATE) public static Block copperPressurePlate;
	@ObjectHolder(ZINC + Suffix.PRESSURE_PLATE) public static Block zincPressurePlate;
	@ObjectHolder(BRASS + Suffix.PRESSURE_PLATE) public static Block brassPressurePlate;
	@ObjectHolder(GILDED_IRON + Suffix.PRESSURE_PLATE) public static Block gildedIronPressurePlate;
	
	@ObjectHolder(COPPER + Suffix.PRESSURE_PLATE) public static Item copperPressurePlateItem;
	@ObjectHolder(ZINC + Suffix.PRESSURE_PLATE) public static Item zincPressurePlateItem;
	@ObjectHolder(BRASS + Suffix.PRESSURE_PLATE) public static Item brassPressurePlateItem;
	@ObjectHolder(GILDED_IRON + Suffix.PRESSURE_PLATE) public static Item gildedIronPressurePlateItem;
	
	@ObjectHolder(COPPER + Suffix.ORE) public static Block copperOre;
	@ObjectHolder(ZINC + Suffix.ORE) public static Block zincOre;
	
	@ObjectHolder(COPPER + Suffix.ORE) public static Item copperOreItem;
	@ObjectHolder(ZINC + Suffix.ORE) public static Item zincOreItem;
	
	public MaterialsModule() {
		super("materials");
	}
	
	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> r = event.getRegistry();
		
		Item.Properties props = defaultItemProp();
		for (MetalTypes metal : MetalTypes.values()) {
			register(r, new Item(props), metal.getName() + Suffix.INGOT);
			register(r, new Item(props), metal.getName() + Suffix.NUGGET);
		}
		register(r, new BlockItem(copperBlock, props), copperBlock.getRegistryName());
		register(r, new BlockItem(zincBlock, props), zincBlock.getRegistryName());
		register(r, new BlockItem(brassBlock, props), brassBlock.getRegistryName());
		register(r, new BlockItem(gildedIronBlock, props), gildedIronBlock.getRegistryName());
		
		register(r, new BlockItem(copperPressurePlate, props), copperPressurePlate.getRegistryName());
		register(r, new BlockItem(zincPressurePlate, props), zincPressurePlate.getRegistryName());
		register(r, new BlockItem(brassPressurePlate, props), brassPressurePlate.getRegistryName());
		register(r, new BlockItem(gildedIronPressurePlate, props), gildedIronPressurePlate.getRegistryName());
		
		register(r, new BlockItem(copperOre, props), copperOre.getRegistryName());
		register(r, new BlockItem(zincOre, props), zincOre.getRegistryName());
	}
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> r = event.getRegistry();
		
		Block.Properties props = Block.Properties.create(Material.IRON).hardnessAndResistance(5, 6).sound(SoundType.METAL);
		for (MetalTypes metal : MetalTypes.values()) {
			register(r, new Block(props), metal.getName() + Suffix.BLOCK);
		}
		
		props = Block.Properties.create(Material.IRON).doesNotBlockMovement().hardnessAndResistance(0.5F).sound(SoundType.WOOD);
		register(r, new ClassSensitivePlateBlock<>(props, MobEntity.class), COPPER + Suffix.PRESSURE_PLATE);
		register(r, new ClassSensitivePlateBlock<>(props, AgeableEntity.class, AgeableEntity::isChild), ZINC + Suffix.PRESSURE_PLATE);
		register(r, new ClassSensitivePlateBlock<>(props, AgeableEntity.class, e -> !e.isChild()), BRASS + Suffix.PRESSURE_PLATE);
		register(r, new WeightedPressurePlateBlock(150, props), GILDED_IRON + Suffix.PRESSURE_PLATE);
		
		props = Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 5).harvestTool(ToolType.PICKAXE).harvestLevel(1).sound(SoundType.STONE);
		for (MetalTypes metal : MetalTypes.getRawTypes()) {
			register(r, new Block(props), metal.getName() + Suffix.ORE);
		}
	}
	
	@Override
	public IDataProvider[] getDataProviders(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		return new IDataProvider[]{
		  new BlockStateProvider(generator, existingFileHelper),
		  new ItemModelProvider(generator, existingFileHelper)
		};
	}
}
