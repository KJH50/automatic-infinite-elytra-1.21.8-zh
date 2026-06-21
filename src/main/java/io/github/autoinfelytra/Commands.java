package io.github.autoinfelytra;

import com.mojang.brigadier.CommandDispatcher;
import dev.xpple.clientarguments.arguments.CColumnPosArgument;
import io.github.autoinfelytra.autopilot.Autopilot;
import io.github.autoinfelytra.autopilot.FlightAnalytics;
import io.github.autoinfelytra.autopilot.TraverseArea;
import io.github.autoinfelytra.config.AutomaticElytraConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColumnPos;

public class Commands {
    @Environment(EnvType.CLIENT)
    public static void registerCommands(){
        ClientCommandRegistrationCallback.EVENT.register(Commands::SetDestinationCommand);
        ClientCommandRegistrationCallback.EVENT.register(Commands::SetLastDestinationCommand);
        CommandRegistrationCallback.EVENT.register(Commands::unsetDestinationCommand);
        ClientCommandRegistrationCallback.EVENT.register(Commands::traverseAreaCommand);
        CommandRegistrationCallback.EVENT.register(Commands::analyticsCommand);
    }

    private static void SetLastDestinationCommand(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("setDestination")
            .executes(context -> {
                assert context.getSource().getPlayer() != null;
                BlockPos pos = Autopilot.getPrevDestination();
                if(pos != null){
                    if(AutomaticInfiniteElytraClient.autoFlight) {
                        Autopilot.initNewFlight(pos, false);
                        TraverseArea.stop();
                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.autopilot.set", pos.getX(), pos.getZ()).formatted(Formatting.GREEN));
                    }
                    else context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.error.not_flying").formatted(Formatting.RED));
                }
                else context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.error.no_prev_dest").formatted(Formatting.RED));
                return 1;
            }));
    }

    private static void SetDestinationCommand(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("setDestination")
            .then(ClientCommandManager.argument("destination", CColumnPosArgument.columnPos())
                .executes(context -> {
                    assert context.getSource().getPlayer() != null;
                    //BlockPos pos = CBlockPosArgument.getBlockPos(context, "Z");
                    ColumnPos columnPos = CColumnPosArgument.getColumnPos(context, "destination");
                    BlockPos pos = AutomaticInfiniteElytra.blockPos(columnPos);
                    if(true){
                        if(AutomaticInfiniteElytraClient.autoFlight) {
                            Autopilot.initNewFlight(pos, false);
                            TraverseArea.stop();
                            context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.autopilot.set", pos.getX(), pos.getZ()).formatted(Formatting.GREEN));
                        }
                        else context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.error.not_flying").formatted(Formatting.RED));
                    }
                    else context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.error.autopilot_disabled").formatted(Formatting.RED));
                   return 1;
        })));
    }

    private static void unsetDestinationCommand(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("removeDestination")
                .executes(context -> {
                    Autopilot.unsetLocation();
                    context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.autopilot.deactivated"));
                    return 0;
                }));
    }

    private static void traverseAreaCommand(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(ClientCommandManager.literal("exploreArea")
                .then(ClientCommandManager.argument("starting", CColumnPosArgument.columnPos())
                        .then(ClientCommandManager.argument("ending", CColumnPosArgument.columnPos())
                            .executes(context -> {
                                assert context.getSource().getPlayer() != null;
                                //BlockPos pos = CBlockPosArgument.getBlockPos(context, "Z");
                                ColumnPos starting = CColumnPosArgument.getColumnPos(context, "starting");
                                ColumnPos ending = CColumnPosArgument.getColumnPos(context, "ending");
                                    if(AutomaticInfiniteElytraClient.autoFlight) {
                                        TraverseArea.init(starting, ending);
                                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.area.traversal_started").formatted(Formatting.GREEN));
                                    }
                                    else context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.error.not_flying").formatted(Formatting.RED));
                                return 1;
                            }))));
    }


    private static void analyticsCommand(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("flightanalytics")
                .executes(context -> {
                    if(FlightAnalytics.isCompletedFlight() && AutomaticElytraConfig.HANDLER.instance().record_analytics){
                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.analytics.separator"));
                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.analytics.header"));
                        FlightAnalytics.printAnalytics(context.getSource().getPlayer());
                    }
                    else {
                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.analytics.unavailable").formatted(Formatting.RED));
                        context.getSource().getPlayer().sendMessage(Text.translatable("msg.autoinfelytra.analytics.unavailable_hint").formatted(Formatting.WHITE));
                    }
                    return 0;
                }));
    }
}
