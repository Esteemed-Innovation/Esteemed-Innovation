package flaxbeard.steamcraft.misc;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.tuple.MutablePair;

import flaxbeard.steamcraft.SteamcraftItems;
import flaxbeard.steamcraft.api.event.AnimalTradeEvent;
import flaxbeard.steamcraft.integration.CrossMod;
import flaxbeard.steamcraft.integration.natura.NaturaIntegration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class FrequencyMerchant implements IMerchant {
    private final EntityLiving entity;
    private EntityPlayer customer;
    private MerchantRecipeList existingList = null;
    private ArrayList<ItemStack> currencies = new ArrayList<>();
    ArrayList<MutablePair<ItemStack, Integer>> saleItems = new ArrayList<>();
    private ItemStack toSell;
    private ItemStack toBuy;

    public FrequencyMerchant(EntityLiving entity, String name) {
        this.entity = entity;
        this.entity.getEntityData().setString("merchantName", name);
    }
    @Override
    public void setCustomer(EntityPlayer player) {
        this.customer = player;
    }

    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer player) {
        NBTTagCompound nbt = this.entity.getEntityData();
        Random random = this.entity.worldObj.rand;
        if (this.existingList != null) {
            return this.existingList;
        }
        if (nbt.hasKey("stock")) {
            NBTTagCompound stocks = nbt.getCompoundTag("stock");
            if (stocks.hasNoTags()) {
                this.existingList = new MerchantRecipeList();
            } else {
                this.existingList = new MerchantRecipeList(stocks);
            }
            return this.existingList;
        }

        this.existingList = new MerchantRecipeList();

        saleItems.add(new MutablePair(new ItemStack(Items.blaze_powder, 2), 10));
        saleItems.add(new MutablePair(new ItemStack(Items.iron_ingot), 7));
        saleItems.add(new MutablePair(new ItemStack(Items.gold_nugget, 4), 9));
        saleItems.add(new MutablePair(new ItemStack(Items.flint, 3), 4));
        saleItems.add(new MutablePair(new ItemStack(Items.clay_ball, 7), 3));
        saleItems.add(new MutablePair(new ItemStack(Items.brick), 3));
        saleItems.add(new MutablePair(new ItemStack(Items.coal, 2), 4));
        saleItems.add(new MutablePair(new ItemStack(Items.potato, 3), 2));
        saleItems.add(new MutablePair(new ItemStack(Items.carrot_on_a_stick), 3));

        if (this.entity instanceof EntityWolf) {
            this.currencies.add(new ItemStack(SteamcraftItems.steamedBeef));
            this.currencies.add(new ItemStack(SteamcraftItems.steamedPorkchop));
            this.currencies.add(new ItemStack(SteamcraftItems.steamedChicken));
            this.currencies.add(new ItemStack(Items.bone));

            if (CrossMod.NATURA) {
                this.currencies.add(new ItemStack(NaturaIntegration.steamedImphide));
            }

            if (random.nextDouble() < 0.09D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.musketCartridge, 2), 12));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 1), 15));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 2), 15));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 3), 15));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.steamcraftCrafting, 1, 4), 15));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(new MutablePair(new ItemStack(Items.iron_sword), 20));
            }
        } else if (this.entity instanceof EntityOcelot) {
            this.currencies.add(new ItemStack(SteamcraftItems.steamedFish));
            this.currencies.add(new ItemStack(SteamcraftItems.steamedSalmon));

            if (random.nextDouble() < 0.09D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.blankMold, 2), 6));
            }
            if (random.nextDouble() < 0.08D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.ingotMold), 8));
            }
            if (random.nextDouble() < 0.07D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.plateMold), 8));
            }
            if (random.nextDouble() < 0.06D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.nuggetMold), 8));
            }
            if (random.nextDouble() < 0.05D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.steamcraftIngot, 2, 2), 4));
            }
            if (random.nextDouble() < 0.04D) {
                saleItems.add(new MutablePair(new ItemStack(SteamcraftItems.canister), 5));
            }

        }
        if (random.nextDouble() < 0.03D) {
            saleItems.add(new MutablePair(new ItemStack(Items.redstone), 20));
        }
        if (random.nextDouble() < 0.2D) {
            saleItems.add(new MutablePair(new ItemStack(Items.emerald), 24));
        }
        if (random.nextDouble() < 0.1D) {
            saleItems.add(new MutablePair(new ItemStack(Items.diamond), 28));
        }

        for (int i = 0; i < saleItems.size(); i++) {
            MutablePair pair = saleItems.get(i);
            ItemStack stack = (ItemStack) pair.left;
            if (stack != null && stack.getItem() != null) {
                ItemStack currency = currencies.get(random.nextInt(currencies.size()));
                int multiplier = (int) pair.right;
                ItemStack cost = currency.copy();
                cost.stackSize = multiplier;
                MerchantRecipe recipe = new MerchantRecipe(cost, stack);
                recipe.func_82783_a(0 - (6 - random.nextInt(2)));
                this.existingList.add(recipe);
            }
        }
        Collections.shuffle(this.existingList);
        nbt.setTag("stock", this.existingList.getRecipiesAsTags());
        return this.existingList;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setRecipes(MerchantRecipeList list) {}

    @Override
    public void useRecipe(MerchantRecipe recipe) {
        if (this.entity != null && this.entity.isEntityAlive() && !this.entity.worldObj.isRemote) {
            recipe.incrementToolUses();
            if (this.existingList != null) {
                NBTTagCompound nbt = this.entity.getEntityData();
                nbt.setTag("stock", this.existingList.getRecipiesAsTags());
                this.toSell = recipe.getItemToSell();
                this.toBuy = recipe.getItemToBuy();
                AnimalTradeEvent event = new AnimalTradeEvent(this.entity, this.customer, this.toBuy, this.toSell);
                MinecraftForge.EVENT_BUS.post(event);
            }
            this.entity.playLivingSound();
        }
    }

    @Override
    public void func_110297_a_(ItemStack stack) {
        this.entity.playLivingSound();
    }
}
