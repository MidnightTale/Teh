package net.hynse.teh.display;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import java.util.concurrent.ThreadLocalRandom;
import net.hynse.teh.ConfigManager;
import org.bukkit.entity.Player;
import net.hynse.teh.command.ToggleTehCommand;

public class DamageDisplayUtil {
    private static final double MIN_WIDTH_OFFSET = 0.05;
    private static final double MAX_WIDTH_EXTRA = 0.15;
    private static final double MIN_HEIGHT_OFFSET = 0.0;
    private static final double MAX_HEIGHT_EXTRA = 0.6;

    public static void spawnDamageDisplay(Entity entity, Component text) {
        org.bukkit.util.Vector direction = entity.getLocation().getDirection();
        double sideOffset = getRandomOffset(entity.getWidth());
        double heightOffset = getRandomHeightOffset(entity.getHeight() - 0.5);
        if (ThreadLocalRandom.current().nextBoolean()) {
            sideOffset = -sideOffset;
        }
        TextDisplay display = entity.getWorld().spawn(
            entity.getLocation().add(
                -direction.getZ() * sideOffset,
                heightOffset,
                direction.getX() * sideOffset
            ),
            TextDisplay.class
        );
        configureDisplay(display, text);

        // Only show to players who have indicators enabled
        var enabledPlayers = entity.getWorld().getPlayers().stream()
            .filter(ToggleTehCommand::isIndicatorEnabled)
            .toList();
        if (enabledPlayers.isEmpty()) {
            display.remove();
            return;
        }
        for (Player viewer : enabledPlayers) {
            viewer.showEntity(net.hynse.teh.Teh.instance, display);
        }

        new net.hynse.teh.display.DisplayAnimator(
            display,
            40,
            1.3f,
            1.5
        ).start();
    }

    public static void configureDisplay(TextDisplay display, Component text) {
        display.text(text);
        display.setSeeThrough(ConfigManager.getDisplayConfig().seeThrough);
        display.setPersistent(false);
        display.setShadowed(ConfigManager.getDisplayConfig().shadowed);
        display.setBackgroundColor(org.bukkit.Color.fromARGB(ConfigManager.getDisplayConfig().backgroundColorARGB));
        try {
            display.setAlignment(TextDisplay.TextAlignment.valueOf(ConfigManager.getDisplayConfig().alignment.toUpperCase()));
        } catch (Exception ignored) {
            display.setAlignment(TextDisplay.TextAlignment.CENTER);
        }
        try {
            display.setBillboard(Display.Billboard.valueOf(ConfigManager.getDisplayConfig().billboard.toUpperCase()));
        } catch (Exception ignored) {
            display.setBillboard(Display.Billboard.CENTER);
        }
    }

    private static double getRandomOffset(double entityWidth) {
        return (entityWidth + MIN_WIDTH_OFFSET) + ThreadLocalRandom.current().nextDouble() * MAX_WIDTH_EXTRA;
    }

    private static double getRandomHeightOffset(double entityHeight) {
        return (entityHeight - MIN_HEIGHT_OFFSET) + ThreadLocalRandom.current().nextDouble() * MAX_HEIGHT_EXTRA;
    }
} 