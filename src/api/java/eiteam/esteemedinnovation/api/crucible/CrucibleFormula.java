package eiteam.esteemedinnovation.api.crucible;

import java.util.Collection;
import java.util.HashMap;

public class CrucibleFormula {
    private final CrucibleLiquid liquid1;
    private final int liquid1num;
    private final CrucibleLiquid liquid2;
    private final int liquid2num;
    private final CrucibleLiquid outputLiquid;
    private final int outputAmount;

    public CrucibleFormula(CrucibleLiquid outputLiq, int outputAmount, CrucibleLiquid liq1, int num1, CrucibleLiquid liq2, int num2) {
        liquid1 = liq1;
        liquid1num = num1;
        liquid2 = liq2;
        liquid2num = num2;
        outputLiquid = outputLiq;
        this.outputAmount = outputAmount;
    }

    public boolean matches(Collection<CrucibleLiquid> contents, HashMap<CrucibleLiquid, Integer> amounts) {
        boolean req1Satisfied = false;
        if (contents.contains(liquid1)) {
            if (amounts.get(liquid1) >= liquid1num) {
                req1Satisfied = true;
            }
        }
        boolean req2Satisfied = false;
        if (contents.contains(liquid2)) {
            if (amounts.get(liquid2) >= liquid2num) {
                req2Satisfied = true;
            }
        }
        return req1Satisfied && req2Satisfied;
    }

    public CrucibleLiquid getLiquid1() {
        return liquid1;
    }

    public int getLiquid1Amount() {
        return liquid1num;
    }

    public CrucibleLiquid getLiquid2() {
        return liquid2;
    }

    public int getLiquid2Amount() {
        return liquid2num;
    }

    public CrucibleLiquid getOutputLiquid() {
        return outputLiquid;
    }

    public int getOutputAmount() {
        return outputAmount;
    }
}
