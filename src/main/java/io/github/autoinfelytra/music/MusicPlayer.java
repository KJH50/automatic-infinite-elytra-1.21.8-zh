package io.github.autoinfelytra.music;

import io.github.autoinfelytra.config.AutomaticElytraConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
//import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class MusicPlayer {
    public static SoundEvent EMBARK;
    public static SoundEvent SWEEPING_CLOUDS_SOUND;
    public static SoundEvent SUNSHINE;
    public static SoundEvent HOMESICK;
    public static SoundEvent FEELING;

    private static boolean isPlayingMusic = false;
    private static boolean firstSoundPlaying = false;
    private static int musicNumber = 1;
    public static void playMusic(Player player){
        if(Minecraft.getInstance().options.getSoundSourceOptionInstance(SoundSource.MASTER).get() > 0) {
            //PLAYING WAIT
            if (musicNumber == 1 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_wait_disc) {
                    player.playSound(SoundEvents.MUSIC_DISC_WAIT.value(), AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Wait"));
                incrementMusicNumberAndWrap();
            }
            //EMBARK
            if (musicNumber == 2 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_embark) {
                    player.playSound(EMBARK, AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Embark On A New Journey"));
                incrementMusicNumberAndWrap();
            }
            //SWEEPING CLOUDS
            if (musicNumber == 3 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_clouds) {
                    player.playSound(SWEEPING_CLOUDS_SOUND, AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Sweeping Through The Clouds"));
                incrementMusicNumberAndWrap();
            }
            //OTHERSIDE
            if (musicNumber == 4 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_otherside_disc) {
                    player.playSound(SoundEvents.MUSIC_DISC_OTHERSIDE.value(), AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Otherside"));
                incrementMusicNumberAndWrap();
            }
            //SUNSHINE
            if (musicNumber == 5 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_sunshine) {
                    player.playSound(SUNSHINE, AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing The First Ray Of Sunshine"));
                incrementMusicNumberAndWrap();
            }
            //PIGSTEP
            if (musicNumber == 6 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_pigstep_disc) {
                    player.playSound(SoundEvents.MUSIC_DISC_PIGSTEP.value(), AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Pigstep"));
                incrementMusicNumberAndWrap();
            }
            //
            if (musicNumber == 7 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_mellohi_disc) {
                    player.playSound(SoundEvents.MUSIC_DISC_MELLOHI.value(), AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Mellohi"));
                incrementMusicNumberAndWrap();
            }
            //FEELING
            if (musicNumber == 8 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_feeling) {
                    player.playSound(FEELING, AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing A Feeling Like Never Before"));
                incrementMusicNumberAndWrap();
            }
            //HOMESICK
            if (musicNumber == 9 && !isPlayingMusic) {
                if (AutomaticElytraConfig.HANDLER.instance().play_homesick) {
                    player.playSound(HOMESICK, AutomaticElytraConfig.HANDLER.instance().volume, 1.0f);
                    isPlayingMusic = true;
                }
                player.sendOverlayMessage(Component.literal("Playing Homesick"));
                incrementMusicNumberAndWrap();
            }

            isPlayingMusic = isSoundPlaying(EMBARK.location(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SWEEPING_CLOUDS_SOUND.location(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SUNSHINE.location(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SoundEvents.MUSIC_DISC_WAIT.key().registry(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SoundEvents.MUSIC_DISC_OTHERSIDE.key().registry(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SoundEvents.MUSIC_DISC_PIGSTEP.key().registry(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(SoundEvents.MUSIC_DISC_MELLOHI.key().registry(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(FEELING.location(), Minecraft.getInstance().getSoundManager().soundEngine)
                    || isSoundPlaying(HOMESICK.location(), Minecraft.getInstance().getSoundManager().soundEngine);
        }
        else {
            Minecraft.getInstance().player.sendOverlayMessage(Component.literal("Music cannot play; your master volume is 0%"));
        }
    }

    public static void stopAllMusic(){
        Minecraft.getInstance().getSoundManager().stop(SoundEvents.MUSIC_DISC_WAIT.key().registry(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(EMBARK.location(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(SWEEPING_CLOUDS_SOUND.location(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(SoundEvents.MUSIC_DISC_OTHERSIDE.key().registry(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(SUNSHINE.location(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(SoundEvents.MUSIC_DISC_PIGSTEP.key().registry(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(SoundEvents.MUSIC_DISC_MELLOHI.key().registry(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(FEELING.location(), SoundSource.MASTER);
        Minecraft.getInstance().getSoundManager().stop(HOMESICK.location(), SoundSource.MASTER);
        musicNumber = 1;

        isPlayingMusic = false;
    }

    public static boolean isPlayingMusic() {
        return isPlayingMusic;
    }

    public static boolean isSoundPlaying(Identifier id, SoundEngine soundSystem) {
        for (SoundInstance soundInstance : soundSystem.instanceToChannel.keySet()) {
            if (soundInstance.getIdentifier().equals(id)) {
                return true; // Found a matching sound identifier
            }
        }
        return false; // No matching sound identifier found
    }

    private static void incrementMusicNumberAndWrap(){
        musicNumber = (musicNumber + 1) % 9;
        if(musicNumber == 0) musicNumber = 9;
    }

}
