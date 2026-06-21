package io.github.autoinfelytra.autopilot;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import io.github.autoinfelytra.config.AutomaticElytraConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class Autopilot {
    private static BlockPos destination;
    private static BlockPos prevDestination;
    private static Player player;

    private static boolean doLanding = AutomaticElytraConfig.HANDLER.instance().do_landing;
    private static float targetYaw;
    private static boolean instantYaw;
    private static float turnAmount;
    private static final int destinationLeeway = 16;
    private static int lastDistanceToDestination = Integer.MAX_VALUE;

    private static ScheduledExecutorService executorService;

    private static boolean landing;

    public static void init() {
        player = Minecraft.getInstance().player;
        turnAmount = (float) (6+Math.random());
        landing = false;
    }


    public static float getTargetYaw(EntityAnchorArgument.Anchor anchorPoint, Vec3 target) {
        Vec3 vec3d = anchorPoint.apply(player);
        double d = target.x - vec3d.x;
        double f = target.z - vec3d.z;
        return (Mth.wrapDegrees((float)(Math.atan2(f, d) * 57.2957763671875) - 90.0f));
    }

    public static void initNewFlight(BlockPos blockPos, boolean instantYaw1){
        if(AutomaticElytraConfig.HANDLER.instance().record_analytics){
            Autopilot.init();
            destination = blockPos;

            FlightAnalytics.setStartTime(player.tickCount);
            FlightAnalytics.setDistance(((int) Math.sqrt(player.blockPosition().distSqr(destination))));
            FlightAnalytics.setStartDurability(player.getItemBySlot(EquipmentSlot.CHEST).get(DataComponents.DAMAGE));
            FlightAnalytics.startFlying();
        }
        instantYaw = instantYaw1;
        doLanding = AutomaticElytraConfig.HANDLER.instance().do_landing;
        setLocation(blockPos);
    }

    public static void setLocation(BlockPos blockPos) {
        Autopilot.init();
        destination = blockPos;
        prevDestination = destination.mutable();
        targetYaw = getTargetYaw(EntityAnchorArgument.Anchor.EYES, destination.getCenter());
    }

    public static void unsetLocation() {
        destination = null;
    }

    public static void stop() {
        destination = null;
        TraverseArea.stop();
    }

    public static void courseCorrection() {
        if (player.tickCount % 200 == 0) setLocation(destination);
    }

    public static void target() {
        if (!Mth.equal(Math.abs(player.getYRot() - targetYaw), 0.0)) {
            if(!instantYaw) {
                player.setYRot(Mth.wrapDegrees(player.getYRot() + turnAmount));
                if (Math.abs(player.getYRot() - targetYaw) <= turnAmount * 2) {
                    player.setYRot(targetYaw);
                }
            }
            else player.setYRot(targetYaw);
        }
        courseCorrection();
    }

    public static void tick() {
        if(player == null || player.blockPosition() == null || destination == null || !player.isFallFlying()){
            destination = null;
            targetYaw = Integer.MIN_VALUE;
            lastDistanceToDestination = Integer.MAX_VALUE;
            return;
        }
        if (destination == null) return;
        if(!landing)
            if(executorService != null) executorService.shutdown();

        destination = new BlockPos(destination.getX(), player.getBlockY(), destination.getZ());
        if (isAtDestination()) {
            destination = null;
            if(!TraverseArea.isTraversalInProgress()){
                player.sendOverlayMessage(Component.translatable("msg.autoinfelytra.autopilot.arrived").withStyle(ChatFormatting.GREEN));
                if(AutomaticElytraConfig.HANDLER.instance().record_analytics) {
                    FlightAnalytics.setTime((player.tickCount - FlightAnalytics.getStartTime()) / 20);
                    FlightAnalytics.setDurability_lost(player.getItemBySlot(EquipmentSlot.CHEST).get(DataComponents.DAMAGE) - FlightAnalytics.getStartDurability());
                    FlightAnalytics.flightDone();
                    if (AutomaticElytraConfig.HANDLER.instance().auto_send_analytics)
                        FlightAnalytics.printAnalytics(player);
                }

                if(shouldLand()) {
                    player.sendOverlayMessage(Component.translatable("msg.autoinfelytra.autopilot.landing").withStyle(ChatFormatting.GREEN));
                    landing = true;
                    initLanding();
                }
            }
            if(TraverseArea.isTraversalInProgress()) TraverseArea.tick();
        }
        else target();
    }

    public static void initLanding(){
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(Autopilot::land, 0, 20, TimeUnit.MILLISECONDS);
        if(!landing){
            executorService.shutdown();
        }
    }

    public static void land() {
        landing = shouldLand();
        if(landing){
            player.setYRot((float) (player.getYRot() + turnAmount / 1.8));
        }
    }

    public static boolean shouldLand(){
        return AutomaticInfiniteElytraClient.autoFlight
                && AutomaticElytraConfig.HANDLER.instance().do_landing
                && doLanding
                && player.isFallFlying()
                && !player.isInWater()
                && !player.isInLava();
    }

    public static boolean isAtDestination(){
        if(Math.sqrt(player.blockPosition().distSqr(destination)) > destinationLeeway) return false;
        if(Math.sqrt(player.blockPosition().distSqr(destination)) == 0) return true;
        else {
            if(lastDistanceToDestination < Math.sqrt(player.blockPosition().distSqr(destination))){
                return true;
            }
            else {
                lastDistanceToDestination = (int) Math.sqrt(player.blockPosition().distSqr(destination));
                return false;
            }
        }
    }


    public static boolean isAutopilotRunning(){
        return destination != null;
    }

    public static boolean isLanding(){
        return landing;
    }

    public static BlockPos getDestination(){
        return destination.mutable();
    }

    public static BlockPos getPrevDestination() {
        if(prevDestination != null) return prevDestination.mutable();
        else return null;
    }
}
