package flaxbeard.steamcraft.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flaxbeard.steamcraft.api.exosuit.IExosuitUpgrade;
import flaxbeard.steamcraft.client.render.model.ModelExosuit;
import flaxbeard.steamcraft.item.ItemExosuitArmor.ExosuitSlot;

public class ItemExosuitUpgrade extends Item implements IExosuitUpgrade {
	
	private ExosuitSlot mySlot;
	protected ResourceLocation myOverlay;
	protected String myInfo;
	protected int pri;
	
	public ItemExosuitUpgrade(ExosuitSlot slot, String loc, String info, int priority) {
		mySlot = slot;
		myInfo = info;
		myOverlay = new ResourceLocation(loc);
		pri = priority;
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
	@SideOnly(Side.CLIENT)
	public void renderModel(ModelExosuit model, Entity entity, int armor, float size, ItemStack me) {}

	@Override
	public void writeInfo(List list) {
		if (myInfo != null) {
			list.add(myInfo);
		}
	}

	@Override
	public int renderPriority() {
		return pri;
	}

}
