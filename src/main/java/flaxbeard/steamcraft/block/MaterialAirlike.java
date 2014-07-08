package flaxbeard.steamcraft.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialAirlike extends Material {

	public MaterialAirlike(MapColor p_i2116_1_) {
		super(p_i2116_1_);
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}
	
	@Override
	public boolean isReplaceable() {
		return false;
	}
	
	@Override
	public boolean getCanBlockGrass() {
		return false;
	}
	
	@Override
	public boolean blocksMovement() {
		return false;
	}
	
	@Override
	public Material setRequiresTool() {
		return this;
	}
}
