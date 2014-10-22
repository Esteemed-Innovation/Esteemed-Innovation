package flaxbeard.steamcraft.common.misc;

import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;

import java.util.Comparator;

public class ComparatorUpgrade implements Comparator<IExosuitUpgrade> {

    @Override
    public int compare(IExosuitUpgrade par1, IExosuitUpgrade par2) {
        return par1.renderPriority() - par2.renderPriority();
    }

}
