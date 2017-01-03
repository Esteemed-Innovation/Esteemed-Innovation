package eiteam.esteemedinnovation.workshop;

import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import eiteam.esteemedinnovation.metals.refined.plates.ItemMetalPlate;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static eiteam.esteemedinnovation.metals.MetalsModule.METAL_PLATE;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_PISTON;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.BRASS_TURBINE;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.transport.TransportationModule.BRASS_PIPE;
import static eiteam.esteemedinnovation.workshop.SteamWorkshopModule.STEAM_ENGINEER_PROFESSION;

public class SteamEngineerCareer extends VillagerRegistry.VillagerCareer {
    private static ItemStack BRASS_PLATE = new ItemStack(METAL_PLATE, 1, ItemMetalPlate.Types.BRASS_PLATE.getMeta());
    private static ItemStack TURBINE = new ItemStack(COMPONENT, 1, BRASS_TURBINE.getMetadata());
    private static ItemStack PISTON = new ItemStack(COMPONENT, 1, BRASS_PISTON.getMetadata());
    private static Item PIPE = Item.getItemFromBlock(BRASS_PIPE);
    private static final Random RANDOM = new Random();

    public SteamEngineerCareer() {
        this(STEAM_ENGINEER_PROFESSION, "steam_engineer");
    }

    public SteamEngineerCareer(VillagerRegistry.VillagerProfession parent, String name) {
        super(parent, name);
    }

    @Override
    public List<EntityVillager.ITradeList> getTrades(int level) {
        return Arrays.asList(new EntityVillager.ITradeList[] {
          new ItemStackForItemStack(new ItemStack(Items.EMERALD, 2), TURBINE),
          new ItemStackForItemStack(new ItemStack(Items.EMERALD, 2), PISTON),
          new ItemStackForItemStack(new ItemStack(Items.EMERALD), new ItemStack(PIPE, 2)),
          new ItemStackForItemStack(BRASS_PLATE, new ItemStack(Items.EMERALD, RANDOM.nextInt(2) + 8))
        });
    }

    /**
     * Probably reinventing the wheel here, but the existing ITradeLists are really, really confusing in their intent.
     */
    private static class ItemStackForItemStack implements EntityVillager.ITradeList {
        private ItemStack in;
        private ItemStack out;

        public ItemStackForItemStack(ItemStack in, ItemStack out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
            recipeList.add(new MerchantRecipe(in, out));
        }
    }
}
