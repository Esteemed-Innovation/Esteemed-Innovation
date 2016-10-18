package eiteam.esteemedinnovation.api.crucible;

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
    public int ca;
    public String name;

    public CrucibleLiquid(String string, ItemStack ingot1, ItemStack plate1, ItemStack nugget1, CrucibleFormula formula1, int r, int g, int b) {
        this(string, ingot1, plate1, nugget1, formula1, r, g, b, 255);
    }

    public CrucibleLiquid(String string, ItemStack ingot1, ItemStack plate1, ItemStack nugget1, CrucibleFormula formula1, int r, int g, int b, int a) {
        name = string;
        ingot = ingot1;
        plate = plate1;
        nugget = nugget1;
        recipe = formula1;
        //this.color = new Color(r, g, b);
        cr = r;
        cg = g;
        cb = b;
        ca = a;
    }
}
