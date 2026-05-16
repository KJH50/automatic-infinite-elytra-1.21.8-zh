package io.github.autoinfelytra.autopilot;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

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

    public static String getPresentableTime(int t){
        int min = 0;
        int sec = 0;
        if(t >= 60) {
            min = (t / 60);
        }
        sec = t % 60;

        return min + " min " + sec + " sec";
    }

    public static void printAnalytics(Player player){
        int width = (int) Math.floor(Minecraft.getInstance().options.chatWidth().get());
        player.sendSystemMessage(Component.literal(getSeperator(width, "-")));
        player.sendSystemMessage(Component.literal("Flight Analytics: ").withStyle(ChatFormatting.AQUA));
        player.sendSystemMessage(Component.literal("Distance Travelled: " + distance).withStyle(ChatFormatting.WHITE));
        player.sendSystemMessage(Component.literal("Time Taken: " + getPresentableTime(time)).withStyle(ChatFormatting.WHITE));
        player.sendSystemMessage(Component.literal("Average Speed: " + (distance/time) + " m/s").withStyle(ChatFormatting.WHITE));
        player.sendSystemMessage(Component.literal("Elytra Durability Used: " + durability_lost).withStyle(ChatFormatting.WHITE));
        player.sendSystemMessage(Component.literal(getSeperator(width, "-")));

    }
}
