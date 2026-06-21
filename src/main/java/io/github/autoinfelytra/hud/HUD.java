package io.github.autoinfelytra.hud;

import io.github.autoinfelytra.AutomaticInfiniteElytraClient;
import io.github.autoinfelytra.config.AutomaticElytraConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Objects;
@Environment(EnvType.CLIENT)
public class HUD {

    // Hardcoding because easy
    public static final int RED_HUD_COLOR = 0xFFFF5555;
    public static final int YELLOW_HUD_COLOR = 0xFFFFFF55;
    public static final int GREEN_HUD_COLOR = 0xFF55FF55;

    public static ArrayList<Component> hudArray;
    public static int hudColor = RED_HUD_COLOR;
    public static final int HUD_ELEMENTS = 5;

    public static void tick(){
        if (AutomaticInfiniteElytraClient.showHud && AutomaticElytraConfig.HANDLER.instance().render_hud) {
            hudArray = HUDHelper.generateHUD(hudArray, HUD_ELEMENTS);
            if (AutomaticInfiniteElytraClient.autoFlight) hudColor = GREEN_HUD_COLOR;
            else hudColor = YELLOW_HUD_COLOR;
        } else hudArray = null;
    }
    public static void drawHUD(GuiGraphicsExtractor drawContext, DeltaTracker renderTickCounter) {
        //drawContext.draw();
        
        Minecraft client = Minecraft.getInstance();
        int sw = client.getWindow().getGuiScaledWidth();
        int sh = client.getWindow().getGuiScaledHeight();

        int x = (sw * AutomaticElytraConfig.HANDLER.instance().x_coordinates_of_hud) / 2000;
        int y = (sh * AutomaticElytraConfig.HANDLER.instance().y_coordinates_of_hud) / 2000;

        //MinecraftClient.getInstance().player.sendMessage(Text.literal(String.valueOf((MinecraftClient.getInstance().getWindow().getHeight()))), true);

        //FLIGHT MODE
        if(hudArray != null && hudArray.size() >= 1) drawContext.text(
                Minecraft.getInstance().font,
                hudArray.get(0),
                x,
                (int) (y + AutomaticElytraConfig.HANDLER.instance().distance_between_sentences * -2),
                hudColor,
                true);

        //ALTITUDE
        if(hudArray != null && hudArray.size() >= 2) drawContext.text(
                Minecraft.getInstance().font,
                hudArray.get(1),
                x,
                (int) (y + AutomaticElytraConfig.HANDLER.instance().distance_between_sentences * -1),
                hudColor,
                true);

        //SPEED
        if(hudArray != null && hudArray.size() >= 3) drawContext.text(
                Minecraft.getInstance().font,
                hudArray.get(2),
                x,
                (int) (y + AutomaticElytraConfig.HANDLER.instance().distance_between_sentences * 0),
                hudColor,
                true);

        //ELYTRA DURABILITY
        if(hudArray != null && hudArray.size() >= 4) drawContext.text(
                Minecraft.getInstance().font,
                hudArray.get(3),
                x,
                (int) (y + AutomaticElytraConfig.HANDLER.instance().distance_between_sentences * 1),
                hudColor,
                true);

        //AUTOPILOT
        if(hudArray != null && hudArray.size() >= 5) drawContext.text(
                Minecraft.getInstance().font,
                hudArray.get(4),
                x,
                (int) (y + AutomaticElytraConfig.HANDLER.instance().distance_between_sentences * 2),
                hudColor,
                true);
    }
}

