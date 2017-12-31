package eiteam.esteemedinnovation.metalcasting.mold;

import eiteam.esteemedinnovation.api.crucible.CrucibleLiquid;
import eiteam.esteemedinnovation.api.mold.CrucibleMold;
import eiteam.esteemedinnovation.commons.EsteemedInnovation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemMold extends Item implements CrucibleMold {
    public ItemMold() {
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public ResourceLocation getBlockTexture(@Nonnull ItemStack moldStack) {
        return Type.getFromMeta(moldStack.getItemDamage()).getBlockTexture();
    }

    @Override
    public int getCostToMold(CrucibleLiquid liquid, @Nonnull ItemStack moldStack) {
        return Type.getFromMeta(moldStack.getItemDamage()).getCostToMold();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tabs, NonNullList<ItemStack> subItems) {
        for (Type type : Type.LOOKUP) {
            subItems.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName() + "." + Type.getFromMeta(stack.getItemDamage()).name().toLowerCase();
    }

    public enum Type {
        INGOT(0, 9, new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/moldIngot.png")),
        NUGGET(1, 1, new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/moldNugget.png")),
        THIN_PLATE(2, 6, new ResourceLocation(EsteemedInnovation.MOD_ID + ":textures/models/mold_thin_plate.png")),
        PIPE(3, 54, INGOT.getBlockTexture() /* TODO: Texture */),
        AXE(4, 27, INGOT.getBlockTexture() /* TODO: Texture */),
        HOE(5, 18, INGOT.getBlockTexture() /* TODO: Texture */),
        PICKAXE(6, 27, INGOT.getBlockTexture() /* TODO: Texture */),
        SHOVEL(7, 9, INGOT.getBlockTexture() /* TODO: Texture */),
        SWORD(8, 18, INGOT.getBlockTexture() /* TODO: Texture */);

        private final int meta;
        private final int costToMold;
        private final ResourceLocation blockTexture;
        public static final Type[] LOOKUP = new Type[values().length];

        Type(int meta, int costToMold, ResourceLocation blockTexture) {
            this.meta = meta;
            this.costToMold = costToMold;
            this.blockTexture = blockTexture;
        }

        public int getMeta() {
            return meta;
        }

        public int getCostToMold() {
            return costToMold;
        }

        public ResourceLocation getBlockTexture() {
            return blockTexture;
        }

        public ItemStack createItemStack(Item item) {
            return new ItemStack(item, 1, getMeta());
        }

        private static Type getFromMeta(int meta) {
            return LOOKUP[meta];
        }

        static {
            for (Type type : values()) {
                LOOKUP[type.getMeta()] = type;
            }
        }
    }
}
