package io.github.autoinfelytra.autopilot;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class CollisionDetectionUtil {
    public static final int scanAheadTicks = 5;

    public static void cancelFlightIfObstacleDetected(Player player, Level world) {
        Vec3 playerPos = player.position();
        Vec3 velocity = player.getDeltaMovement();

        Vec3 scanVelocity = velocity.scale(scanAheadTicks);
        Vec3 futurePos = playerPos.add(scanVelocity);

        Vec3i vec3i = new Vec3i((int) futurePos.x, (int) futurePos.y, (int) futurePos.z);
        BlockPos blockPos = new BlockPos(vec3i);
        if (world.getBlockState(blockPos).isSolid()) {
            player.sendSystemMessage(Component.literal("[Collision Detection Utility] ").withStyle(ChatFormatting.AQUA).append(Component.literal("Flight aborted due to obstacle ahead!"))); // Send a message to the player
            player.sendSystemMessage(Component.literal("Consider using fireworks to boost your height before enabling automatic flight"));
            AutomaticInfiniteElytraClient.rotating = true;
        }
    }
}
