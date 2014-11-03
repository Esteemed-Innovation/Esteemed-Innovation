package flaxbeard.steamcraft.client.audio;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author dmillerw
 */
public class Sounds {

    private static Field playingSounds;
    private static Field soundMgr;

    public static void addSoundTile(ISoundTile soundTile) {
        addSound(new SoundTile(soundTile));
    }

    public static void addSound(ISound sound) {
        if (canAddSound(sound))
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
    }

    public static boolean canAddSound(ISound sound) {
        if (playingSounds == null) {
            playingSounds = ReflectionHelper.findField(SoundManager.class, "playingSounds", "field_148629_h");
        }

        if (soundMgr == null) {
            soundMgr = ReflectionHelper.findField(SoundHandler.class, "sndManager", "field_147694_f");
        }

        try {
            SoundManager soundManager = (SoundManager) soundMgr.get(Minecraft.getMinecraft().getSoundHandler());
            Map map = (Map)playingSounds.get(soundManager);

            return !map.containsValue(sound);
        } catch (IllegalAccessException e) {
            return false;
        }
    }
}
