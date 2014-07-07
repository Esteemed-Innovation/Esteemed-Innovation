package flaxbeard.steamcraft.misc;

import java.util.Comparator;

import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;

public class ComparatorUpgrade implements Comparator<IExosuitUpgrade> {

	@Override
	public int compare(IExosuitUpgrade par1, IExosuitUpgrade par2) {
		return par1.renderPriority() - par2.renderPriority();
	}

}
