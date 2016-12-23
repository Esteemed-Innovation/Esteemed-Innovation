package eiteam.esteemedinnovation.commons.handler;

import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.FMLLog;

import java.lang.reflect.Field;

public class FieldHandler {
    public static Field lastBuyingPlayerField;
    public static Field timeUntilResetField;
    public static Field merchantField;
    public static Field buyingListField;
    public static Field isJumpingField;

    public static Field getField(String fieldName, String obfName, Class clazz) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(obfName);
        } catch (NoSuchFieldException e) {
            FMLLog.warning("[EI] Unable to find field " + fieldName + " with its obfuscated " +
              "name. Trying to find it by its name " + fieldName);
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
                boolean builderHasAField = false;
                StringBuilder builder = new StringBuilder();
                for (Field field1 : clazz.getDeclaredFields()) {
                    if (builderHasAField) {
                        builder.append(", ");
                    }
                    builder.append(field1.getName());
                    builderHasAField = true;
                }
                FMLLog.warning("Unable to find " + fieldName + " field in " + clazz.getName() +
                  ".class. Available fields are: " + builder + ". Things are not going to work right.");
            }
        }
        return field;
    }

    private static void setAccessible(Field field) {
        if (field != null) {
            field.setAccessible(true);
        }
    }

    public static void init() {
        FMLLog.info("[EI] Getting some fields through reflection.");
        lastBuyingPlayerField = getField("lastBuyingPlayer", "field_82189_bL", EntityVillager.class);
        timeUntilResetField = getField("timeUntilReset", "field_70961_j", EntityVillager.class);
        buyingListField = getField("buyingList", "field_70963_i", EntityVillager.class);
        isJumpingField = getField("isJumping", "field_70703_bu", EntityLivingBase.class);

        setAccessible(lastBuyingPlayerField);
        setAccessible(timeUntilResetField);
        setAccessible(buyingListField);
        setAccessible(isJumpingField);

        try {
            merchantField = getField("merchant", "field_147037_w", GuiMerchant.class);
            setAccessible(merchantField);
        } catch (NoClassDefFoundError ignore) {
            FMLLog.warning("[EI] GuiMerchant class not found. You are probably a server.");
        }
    }

    public static boolean getIsEntityJumping(EntityLivingBase obj) throws IllegalAccessException {
        return (boolean) isJumpingField.get(obj);
    }
}
