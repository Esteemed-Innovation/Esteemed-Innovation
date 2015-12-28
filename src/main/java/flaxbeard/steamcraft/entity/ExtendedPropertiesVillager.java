package flaxbeard.steamcraft.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPropertiesVillager implements IExtendedEntityProperties {
    public EntityVillager villager;
    public World world;
    public Boolean lastHadCustomer;

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        if (this.lastHadCustomer != null) {
            compound.setBoolean("lastHadCustomer", this.lastHadCustomer);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        if (compound.hasKey("lastHadCustomer")) {
            this.lastHadCustomer = compound.getBoolean("lastHadCustomer");
        } else {
            this.lastHadCustomer = null;
        }
    }

    @Override
    public void init(Entity entity, World world) {
        this.villager = (EntityVillager) entity;
        this.world = world;
        this.lastHadCustomer = null;
    }
}
