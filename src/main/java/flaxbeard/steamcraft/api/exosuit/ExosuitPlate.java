package flaxbeard.steamcraft.api.exosuit;

import java.util.Arrays;

import net.minecraft.item.ItemArmor;
import net.minecraft.util.DamageSource;
import flaxbeard.steamcraft.item.ItemExosuitArmor;

public class ExosuitPlate {
	
	private String identifier;
	private String invMod;
	private String armorMod;
	private Object plate;
	private DamageSource[] bonusSources;
	
	public ExosuitPlate(String id, Object item, String invLocMod, String armorLocMod, DamageSource... sources) {
		identifier = id;
		invMod = invLocMod;
		armorMod = armorLocMod;
		plate = item;
		bonusSources = sources;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public Object getItem() {
		return plate;
	}

	public String getIcon(ItemExosuitArmor item) {
		return item.getString() + invMod;
	}

	public String getArmorLocation(ItemExosuitArmor item, int armorType) {
		if (armorType != 2) {
			return "steamcraft:textures/models/armor/exoPlate" + armorMod + "_1.png";
		}
		else
		{
			return "steamcraft:textures/models/armor/exoPlate" + armorMod + "_2.png";
		}
	}

	public int getDamageReductionAmount(int slot, DamageSource source) {
		if (bonusSources.length == 0) {
			return ItemArmor.ArmorMaterial.IRON.getDamageReductionAmount(3-slot);
		}
		else
		{
			if (Arrays.asList(bonusSources).contains(source)) {
				return ItemArmor.ArmorMaterial.DIAMOND.getDamageReductionAmount(3-slot);
			}
			else
			{
				return ItemArmor.ArmorMaterial.CHAIN.getDamageReductionAmount(3-slot);
			}
		}
	}
}
