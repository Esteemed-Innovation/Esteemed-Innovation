package eiteam.esteemedinnovation.api.crucible;

import net.minecraft.item.ItemStack;

public class CrucibleLiquid {
    private final ItemStack ingot;
    private final ItemStack plate;
    private final ItemStack nugget;
    private final int cr;
    private final int cg;
    private final int cb;
    private final int ca;
    private final String name;

    public CrucibleLiquid(String name, ItemStack ingot, ItemStack plate, ItemStack nugget, int r, int g, int b) {
        this(name, ingot, plate, nugget, r, g, b, 255);
    }

    public CrucibleLiquid(String name, ItemStack ingot, ItemStack plate, ItemStack nugget, int r, int g, int b, int a) {
        this.name = name;
        this.ingot = ingot;
        this.plate = plate;
        this.nugget = nugget;
        //this.color = new Color(r, g, b);
        cr = r;
        cg = g;
        cb = b;
        ca = a;
    }

    public ItemStack getIngot() {
        return ingot;
    }

    public ItemStack getPlate() {
        return plate;
    }

    public ItemStack getNugget() {
        return nugget;
    }

    public int getRed() {
        return cr;
    }

    public int getBlue() {
        return cb;
    }

    public int getGreen() {
        return cg;
    }

    public int getAlpha() {
        return ca;
    }

    public String getName() {
        return name;
    }
}
