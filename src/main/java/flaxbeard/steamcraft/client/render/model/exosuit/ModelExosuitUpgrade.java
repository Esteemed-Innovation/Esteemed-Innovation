package flaxbeard.steamcraft.client.render.model.exosuit;

import com.google.common.collect.Maps;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

import java.util.Map;

/**
 * @author dmillerw
 */
public abstract class ModelExosuitUpgrade extends ModelBase {

    private final Map<String, Object> scratchpad = Maps.newHashMap();

    public void setScratchpadValue(String key, Object value) {
        scratchpad.put(key, value);
    }

    public Object getScratchpadValue(String key) {
        return scratchpad.get(key);
    }

    public abstract void renderModel(ModelExosuit modelExosuit, EntityLivingBase entityLivingBase);
}
