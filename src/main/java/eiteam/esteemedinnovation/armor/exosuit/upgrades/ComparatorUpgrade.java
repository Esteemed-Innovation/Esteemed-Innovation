package eiteam.esteemedinnovation.armor.exosuit.upgrades;

import eiteam.esteemedinnovation.api.exosuit.ExosuitUpgrade;

import java.util.Comparator;

public class ComparatorUpgrade implements Comparator<ExosuitUpgrade> {
    @Override
    public int compare(ExosuitUpgrade par1, ExosuitUpgrade par2) {
        return par1.renderPriority() - par2.renderPriority();
    }
}
