package io.github.autoinfelytra.hud;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import io.github.autoinfelytra.autopilot.Autopilot;
import io.github.autoinfelytra.config.AutomaticElytraConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static io.github.autoinfelytra.AutomaticInfiniteElytraClient.autoFlight;
import static io.github.autoinfelytra.AutomaticInfiniteElytraClient.getCurrentVelocity;

@Environment(EnvType.CLIENT)
public class HUDHelper {
    private static final MinecraftClient minecraftClient = MinecraftClient.getInstance();
    private static int altitude = 0;
    private static boolean isRunning = false;
    private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    public static void init(){
        service.scheduleWithFixedDelay(() -> {
            if(minecraftClient.player != null && AutomaticInfiniteElytraClient.showHud && !isRunning) {
                isRunning = true;
                altitude = altitude(minecraftClient.player);
                isRunning = false;
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public static void exit(){
        service.shutdownNow();
        service = Executors.newSingleThreadScheduledExecutor();
    }
    public static ArrayList<Text> generateHUD(ArrayList<Text> hudArray, int HUD_ELEMENTS){
        assert minecraftClient.player != null;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        /*if(!isRunning) executorService.submit(() -> {
            altitude = altitude(minecraftClient.player);
        });
        executorService.shutdown();*/

        ItemStack itemStack = minecraftClient.player.getEquippedStack(EquipmentSlot.CHEST);
        Text[] hudString = new Text[HUD_ELEMENTS];
        if (hudArray == null) hudArray = new ArrayList<Text>();
        else hudArray.clear();

        if(AutomaticElytraConfig.HANDLER.instance().render_flight_mode) hudString[0] = Text.translatable("hud.autoinfelytra.flight_mode", autoFlight ? Text.translatable("hud.autoinfelytra.mode.automatic") : Text.translatable("hud.autoinfelytra.mode.manual"));
        if(Autopilot.isLanding()) hudString[0] = Text.translatable("hud.autoinfelytra.flight_mode.landing", hudString[0]);
        if(Autopilot.isAutopilotRunning()) hudString[0] = Text.translatable("hud.autoinfelytra.autopilot.running", hudString[0]);

        if(AutomaticElytraConfig.HANDLER.instance().render_altitude) hudString[1] = Text.translatable("hud.autoinfelytra.altitude", altitude);
        if(AutomaticElytraConfig.HANDLER.instance().render_speed) hudString[2] = Text.translatable("hud.autoinfelytra.speed", String.format("%.2f", getCurrentVelocity() * 20));
        if(AutomaticElytraConfig.HANDLER.instance().render_elytra_durability) hudString[3] = Text.translatable("hud.autoinfelytra.durability", itemStack.getMaxDamage() - itemStack.getDamage());
        if(Autopilot.isAutopilotRunning() && AutomaticElytraConfig.HANDLER.instance().render_autopilot_coords) hudString[4] = Text.translatable("hud.autoinfelytra.autopilot_coords", Autopilot.getDestination().getX(), Autopilot.getDestination().getZ(), Math.round(Math.pow(Autopilot.getDestination().getSquaredDistance(minecraftClient.player.getBlockPos()), 0.5)));

        for(int i = 0; i < HUD_ELEMENTS; i++){
            if(hudString[i] != null) hudArray.add(hudString[i]);
        }
        return hudArray;
    }

    private static int altitude(PlayerEntity player){
        World world = player.getWorld();
        BlockPos blockPos = player.getBlockPos();
        int counter = 0;
        while(world.getBlockState(blockPos).isAir() && !isOverVoid(blockPos)){
            blockPos = blockPos.down();
            counter++;
            if(isOverVoid(blockPos)) return player.getBlockY() - blockPos.getY();
            if(counter >= 20000) return Integer.MAX_VALUE;
        }
        return player.getBlockY() - blockPos.getY();
    }

    public static int getAltitude() {
        return altitude;
    }

    private static boolean isOverVoid(BlockPos blockPos){
        return blockPos.getY() < -64;
    }
}
