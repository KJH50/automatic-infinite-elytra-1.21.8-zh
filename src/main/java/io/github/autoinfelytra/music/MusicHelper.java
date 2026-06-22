package io.github.autoinfelytra.music;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
//import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

@Environment(EnvType.CLIENT)
public class MusicHelper {
    public static SoundEvent registerSoundEvent(String name){
        Identifier id = Identifier.fromNamespaceAndPath(AutomaticInfiniteElytraClient.MOD_ID, name);
        return SoundEvent.createVariableRangeEvent(id);
    }
}
