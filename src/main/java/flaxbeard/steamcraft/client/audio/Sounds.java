package flaxbeard.steamcraft.client.audio;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;

public class Sounds {
    private static SoundHandler soundMgr;
    
    private Sounds() throws InstantiationException{
		throw new InstantiationException("This class is not meant to be instantiated");
	}
    
    public static void addSoundTile(ISoundTile soundTile) {
        addSound(new SoundTile(soundTile));
    }

    public static void addSound(ISound sound) {
        if (canAddSound(sound)) {
            soundMgr.playSound(sound);
        }
    }

    public static boolean canAddSound(ISound sound) {
        if (soundMgr == null) {
            soundMgr = Minecraft.getMinecraft().getSoundHandler();
        }

        return !soundMgr.isSoundPlaying(sound);
    }
}
