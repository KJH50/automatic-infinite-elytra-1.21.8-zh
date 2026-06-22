package io.github.autoinfelytra.music;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class MusicHelper {
    public static SoundEvent registerSoundEvent(String name){
        Identifier id = Identifier.of(AutomaticInfiniteElytraClient.MOD_ID, name);
        return SoundEvent.of(id);
    }
}
