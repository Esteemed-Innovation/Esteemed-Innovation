package flaxbeard.steamcraft.item;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemExosuitUpgrade extends Item implements IExosuitUpgrade {
	
	private ExosuitSlot mySlot;
	protected ResourceLocation myOverlay;
	protected String myInfo;
	
	public ItemExosuitUpgrade(ExosuitSlot slot, String loc, String info, int priority) {
		mySlot = slot;
		myInfo = info;
		myOverlay = new ResourceLocation(loc);
	}

	@Override
	public ExosuitSlot getSlot() {
		return mySlot;
	}

	@Override
	public boolean hasOverlay() {
		return true;
	}

	@Override
	public ResourceLocation getOverlay() {
		return myOverlay;
	}

	@Override
	public boolean hasModel() {
		return false;
	}

	@Override
	public void renderModel(ModelExosuit model, int armor, float size) {}

	@Override
	public void writeInfo(List list) {
		if (myInfo != null) {
			list.add(myInfo);
		}
	}

	@Override
	public int renderPriority() {
		return 0;
	}

}
