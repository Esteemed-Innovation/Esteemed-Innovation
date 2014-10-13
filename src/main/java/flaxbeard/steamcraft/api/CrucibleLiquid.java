package flaxbeard.steamcraft.api;

import net.minecraft.item.ItemStack;

public class CrucibleLiquid {

    public ItemStack ingot;
    public ItemStack plate;
    public ItemStack nugget;
    public CrucibleFormula recipe;
    //public Color color;
    public int cr;
    public int cg;
    public int cb;
    public String name;

    public CrucibleLiquid(String string, ItemStack ingot1, ItemStack plate1, ItemStack nugget1, CrucibleFormula formula1, int r, int g, int b) {
        this.name = string;
        this.ingot = ingot1;
        this.plate = plate1;
        this.nugget = nugget1;
        this.recipe = formula1;
        //this.color = new Color(r, g, b);
        this.cr = r;
        this.cg = g;
        this.cb = b;
    }
}
