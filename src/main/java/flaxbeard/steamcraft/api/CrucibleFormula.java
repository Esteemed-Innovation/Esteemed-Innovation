package flaxbeard.steamcraft.api;

import java.util.ArrayList;
import java.util.HashMap;

public class CrucibleFormula {
    public CrucibleLiquid liquid1;
    public int liquid1num;
    public CrucibleLiquid liquid2;
    public int liquid2num;
    public int output;

    public CrucibleFormula(CrucibleLiquid liq1, int num1, CrucibleLiquid liq2, int num2, int output1) {
        this.liquid1 = liq1;
        this.liquid1num = num1;
        this.liquid2 = liq2;
        this.liquid2num = num2;
        this.output = output1;
    }

    public boolean matches(ArrayList<CrucibleLiquid> contents, HashMap<CrucibleLiquid, Integer> amounts, CrucibleFormula formula) {
        boolean req1Satisfied = false;
        boolean req2Satisfied = false;
        if (contents.contains(liquid1)) {
            if (amounts.get(liquid1) >= liquid1num) {
                req1Satisfied = true;
            }
        }
        if (contents.contains(liquid2)) {
            if (amounts.get(liquid2) >= liquid2num) {
                req2Satisfied = true;
            }
        }
        return (req1Satisfied && req2Satisfied);
    }
}
