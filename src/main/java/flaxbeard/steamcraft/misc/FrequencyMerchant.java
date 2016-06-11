package flaxbeard.steamcraft.misc;

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

import flaxbeard.steamcraft.Steamcraft;
import flaxbeard.steamcraft.api.event.AnimalTradeEvent;
import flaxbeard.steamcraft.data.capabilities.animal.IAnimalData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static flaxbeard.steamcraft.init.items.CraftingComponentItems.Items.*;
import static flaxbeard.steamcraft.init.items.FoodItems.Items.*;
import static flaxbeard.steamcraft.init.items.MetalItems.Items.BRASS_INGOT;
import static flaxbeard.steamcraft.init.items.MetalcastingItems.Items.*;
import static flaxbeard.steamcraft.init.items.tools.GadgetItems.Items.ITEM_CANISTER;
import static flaxbeard.steamcraft.init.items.firearms.FirearmAmmunitionItems.Items.MUSKET_CARTRIDGE;

public class FrequencyMerchant implements IMerchant {
    private final EntityLiving entity;
    private EntityPlayer customer;
    private MerchantRecipeList existingList = null;
    private ArrayList<ItemStack> currencies = new ArrayList<>();
    ArrayList<MutablePair<ItemStack, Integer>> saleItems = new ArrayList<>();
    private String merchantName;
    private MerchantRecipeList stock;

    public FrequencyMerchant(EntityLiving entity, String name) {
        this.entity = entity;
        IAnimalData data = entity.getCapability(Steamcraft.ANIMAL_DATA, null);
        this.merchantName = data.getMerchantName();
        this.stock = data.getStock();
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
        Random random = entity.worldObj.rand;
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

        if (entity instanceof EntityWolf) {
            currencies.add(new ItemStack(STEAMED_BEEF.getItem()));
            currencies.add(new ItemStack(STEAMED_PORKCHOP.getItem()));
            currencies.add(new ItemStack(STEAMED_CHICKEN.getItem()));
            currencies.add(new ItemStack(Items.BONE));

            if (random.nextDouble() < 0.09D) {
                saleItems.add(MutablePair.of(new ItemStack(MUSKET_CARTRIDGE.getItem(), 2), 12));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(MutablePair.of(GUN_STOCK.createItemStack(), 15));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(MutablePair.of(IRON_BARREL.createItemStack(), 15));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(MutablePair.of(BLUNDERBUSS_BARREL.createItemStack(), 15));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(MutablePair.of(FLINTLOCK.createItemStack(), 15));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(MutablePair.of(new ItemStack(Items.IRON_SWORD), 20));
            }
        } else if (entity instanceof EntityOcelot) {
            currencies.add(new ItemStack(STEAMED_FISH.getItem()));
            currencies.add(new ItemStack(STEAMED_SALMON.getItem()));

            if (random.nextDouble() < 0.09D) {
                saleItems.add(MutablePair.of(new ItemStack(BLANK_MOLD.getItem(), 2), 6));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(MutablePair.of(new ItemStack(INGOT_MOLD.getItem()), 8));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(MutablePair.of(new ItemStack(PLATE_MOLD.getItem()), 8));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(MutablePair.of(new ItemStack(NUGGET_MOLD.getItem()), 8));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(MutablePair.of(BRASS_INGOT.createItemStack(2), 4));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(MutablePair.of(new ItemStack(ITEM_CANISTER.getItem()), 5));
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
        return entity.hasCustomName() ? entity.getDisplayName() : new TextComponentString(merchantName);
    }
}
