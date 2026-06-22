package io.github.autoinfelytra.autopilot;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class FlightAnalytics {
    private static int startTime;
    private static int startDurability;

    private static int distance;
    private static int time;
    private static int durability_lost;

    private static boolean completedFlight = false;

    public static void setDistance(int distance1) {
        distance = distance1;
    }

    public static void setTime(int time1) {
        time = time1;
    }

    public static void setStartTime(int startTime1) {
        startTime = startTime1;
    }

    public static void setStartDurability(int startDurability1) {
        startDurability = startDurability1;
    }

    public static void setDurability_lost(int durability_lost1) {
        durability_lost = durability_lost1;
    }

    public static int getStartTime(){
        return startTime;
    }

    public static int getStartDurability() {
        return startDurability;
    }

    public static boolean isCompletedFlight(){
        return completedFlight;
    }

    public static void startFlying(){
        completedFlight = false;
    }

    public static void flightDone(){
        completedFlight = true;
    }


    public static String getSeperator(int count, String with) {
        return new String(new char[count]).replace("\0", with);
    }

    public static int[] getPresentableTime(int t){
        int min = 0;
        int sec = 0;
        if(t >= 60) {
            min = (t / 60);
        }
        sec = t % 60;

        return new int[]{min, sec};
    }

    public static void printAnalytics(PlayerEntity player){
        int width = (int) Math.floor(MinecraftClient.getInstance().options.getChatWidth().getValue());
        player.sendMessage(Text.literal(getSeperator(width, "-")));
        player.sendMessage(Text.translatable("msg.autoinfelytra.analytics.title").formatted(Formatting.AQUA));
        player.sendMessage(Text.translatable("msg.autoinfelytra.analytics.distance", distance).formatted(Formatting.WHITE));
        int[] minSec = getPresentableTime(time);
        player.sendMessage(Text.translatable("msg.autoinfelytra.analytics.time", minSec[0], minSec[1]).formatted(Formatting.WHITE));
        player.sendMessage(Text.translatable("msg.autoinfelytra.analytics.avg_speed", (distance/time)).formatted(Formatting.WHITE));
        player.sendMessage(Text.translatable("msg.autoinfelytra.analytics.durability", durability_lost).formatted(Formatting.WHITE));
        player.sendMessage(Text.literal(getSeperator(width, "-")));

    }
}
