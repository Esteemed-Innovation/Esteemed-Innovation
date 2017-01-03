package eiteam.esteemedinnovation.armor.exosuit.upgrades.frequency;

import eiteam.esteemedinnovation.api.event.AnimalTradeEvent;
import eiteam.esteemedinnovation.armor.ArmorModule;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.MutablePair;

import java.util.*;

import static eiteam.esteemedinnovation.firearms.FirearmModule.MUSKET_CARTRIDGE;
import static eiteam.esteemedinnovation.heater.HeaterModule.*;
import static eiteam.esteemedinnovation.metalcasting.MetalcastingModule.*;
import static eiteam.esteemedinnovation.metals.MetalsModule.METAL_INGOT;
import static eiteam.esteemedinnovation.metals.refined.ItemMetalIngot.Types.BRASS_INGOT;
import static eiteam.esteemedinnovation.misc.ItemCraftingComponent.Types.*;
import static eiteam.esteemedinnovation.misc.MiscellaneousModule.COMPONENT;
import static eiteam.esteemedinnovation.storage.StorageModule.ITEM_CANISTER;

public class FrequencyMerchant implements IMerchant {
    private final EntityLiving entity;
    private EntityPlayer customer;
    private MerchantRecipeList existingList = null;
    private List<ItemStack> currencies = new ArrayList<>();
    private Collection<MutablePair<ItemStack, Integer>> saleItems = new ArrayList<>();
    private String merchantName;
    private MerchantRecipeList stock;

    public FrequencyMerchant(EntityLiving entity, String name) {
        this.entity = entity;
        AnimalData data = entity.getCapability(ArmorModule.ANIMAL_DATA, null);
        merchantName = name;
        stock = data.getStock();
    }
    @Override
    public void setCustomer(EntityPlayer player) {
        customer = player;
    }

    @Override
    public EntityPlayer getCustomer() {
        return customer;
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        if (existingList != null) {
            return existingList;
        }
        if (stock != null) {
            if (stock.isEmpty()) {
                existingList = new MerchantRecipeList();
            } else {
                existingList = stock;
            }
            return existingList;
        }

        existingList = new MerchantRecipeList();

        saleItems.add(MutablePair.of(new ItemStack(Items.BLAZE_POWDER, 2), 10));
        saleItems.add(MutablePair.of(new ItemStack(Items.IRON_INGOT), 7));
        saleItems.add(MutablePair.of(new ItemStack(Items.GOLD_NUGGET, 4), 9));
        saleItems.add(MutablePair.of(new ItemStack(Items.FLINT, 3), 4));
        saleItems.add(MutablePair.of(new ItemStack(Items.CLAY_BALL, 7), 3));
        saleItems.add(MutablePair.of(new ItemStack(Items.BRICK), 3));
        saleItems.add(MutablePair.of(new ItemStack(Items.COAL, 2), 4));
        saleItems.add(MutablePair.of(new ItemStack(Items.POTATO, 3), 2));
        saleItems.add(MutablePair.of(new ItemStack(Items.CARROT_ON_A_STICK), 3));

        Random random = entity.worldObj.rand;
        if (entity instanceof EntityWolf) {
            currencies.add(new ItemStack(STEAMED_BEEF));
            currencies.add(new ItemStack(STEAMED_PORKCHOP));
            currencies.add(new ItemStack(STEAMED_CHICKEN));
            currencies.add(new ItemStack(Items.BONE));

            if (random.nextDouble() < 0.09D) {
                saleItems.add(MutablePair.of(new ItemStack(MUSKET_CARTRIDGE, 2), 12));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(MutablePair.of(new ItemStack(COMPONENT, 1, GUN_STOCK.getMetadata()), 15));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(MutablePair.of(new ItemStack(COMPONENT, 1, IRON_BARREL.getMetadata()), 15));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(MutablePair.of(new ItemStack(COMPONENT, 1, BLUNDERBUSS_BARREL.getMetadata()), 15));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(MutablePair.of(new ItemStack(COMPONENT, 1, FLINTLOCK.getMetadata()), 15));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(MutablePair.of(new ItemStack(Items.IRON_SWORD), 20));
            }
        } else if (entity instanceof EntityOcelot) {
            currencies.add(new ItemStack(STEAMED_FISH));
            currencies.add(new ItemStack(STEAMED_SALMON));

            if (random.nextDouble() < 0.09D) {
                saleItems.add(MutablePair.of(new ItemStack(BLANK_MOLD, 2), 6));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(MutablePair.of(new ItemStack(INGOT_MOLD), 8));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(MutablePair.of(new ItemStack(PLATE_MOLD), 8));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(MutablePair.of(new ItemStack(NUGGET_MOLD), 8));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(MutablePair.of(new ItemStack(METAL_INGOT, 2, BRASS_INGOT.getMeta()), 4));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(MutablePair.of(new ItemStack(ITEM_CANISTER), 5));
            }

        }
        if (random.nextDouble() < 0.03D) {
            saleItems.add(MutablePair.of(new ItemStack(Items.REDSTONE), 20));
        }
        if (random.nextDouble() < 0.2D) {
            saleItems.add(MutablePair.of(new ItemStack(Items.EMERALD), 24));
        }
        if (random.nextDouble() < 0.1D) {
            saleItems.add(MutablePair.of(new ItemStack(Items.DIAMOND), 28));
        }

        for (MutablePair<ItemStack, Integer> saleItem : saleItems) {
            ItemStack stack = saleItem.left;
            if (stack != null && stack.getItem() != null) {
                ItemStack currency = currencies.get(random.nextInt(currencies.size()));
                int multiplier = saleItem.right;
                ItemStack cost = currency.copy();
                cost.stackSize = multiplier;
                MerchantRecipe recipe = new MerchantRecipe(cost, stack);
//                recipe.func_82783_a(0 - (6 - random.nextInt(2)));
                existingList.add(recipe);
            }
        }
        Collections.shuffle(existingList);
        stock = existingList;
        return existingList;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setRecipes(MerchantRecipeList list) {
        existingList = list;
    }

    @Override
    public void useRecipe(MerchantRecipe recipe) {
        if (entity != null && entity.isEntityAlive() && !entity.worldObj.isRemote) {
            recipe.incrementToolUses();
            if (existingList != null) {
                NBTTagCompound nbt = entity.getEntityData();
                nbt.setTag("stock", existingList.getRecipiesAsTags());
                ItemStack toSell = recipe.getItemToSell();
                ItemStack toBuy = recipe.getItemToBuy();
                AnimalTradeEvent event = new AnimalTradeEvent(entity, customer, toBuy, toSell);
                MinecraftForge.EVENT_BUS.post(event);
            }
            entity.playLivingSound();
        }
    }

    @Override
    public void verifySellingItem(ItemStack stack) {
        entity.playLivingSound();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(merchantName);
    }
}
