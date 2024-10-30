package net.hynse.teh;

import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Matrix4f;
import org.bukkit.entity.TextDisplay;

public class DisplayAnimator {
    // Animation phase percentages
    private static final double POP_UP_PERCENT = 0.05;      // 5% of total duration
    private static final double BOUNCE_PERCENT = 0.10;      // 10% of total duration
    private static final double STABILIZE_PERCENT = 0.35;   // 35% of total duration
    private static final double FADE_PERCENT = 0.50;        // 50% of total duration
    // 5+10+35+50 = 100
    
    private final TextDisplay display;
    private final JavaPlugin plugin;
    private final int duration;
    private final float targetScale;
    private final double targetYOffset;
    
    /**
     * Creates a new display animator with dynamic parameters
     * @param display The text display to animate
     * @param plugin The plugin instance
     * @param duration Total animation duration in ticks
     * @param targetScale Final scale before fade out
     * @param targetYOffset Total Y movement distance
     */
    public DisplayAnimator(TextDisplay display, JavaPlugin plugin, int duration, float targetScale, double targetYOffset) {
        this.display = display;
        this.plugin = plugin;
        this.duration = duration;
        this.targetScale = targetScale;
        this.targetYOffset = targetYOffset;
    }
    
    /**
     * Starts the complete animation sequence
     */
    public void start() {
        // Initial state
        display.setTransformationMatrix(new Matrix4f().scale(0.0f));
        
        // Calculate phase durations
        int popUpDuration = (int)(duration * POP_UP_PERCENT);
        int bounceDuration = (int)(duration * BOUNCE_PERCENT);
        int stabilizeDuration = (int)(duration * STABILIZE_PERCENT);
        int fadeDuration = (int)(duration * FADE_PERCENT);
        
        // Calculate delays (cumulative)
        int popUpDelay = 2;
        int bounceDelay = popUpDelay + popUpDuration;
        int stabilizeDelay = bounceDelay + bounceDuration;
        int fadeDelay = stabilizeDelay + stabilizeDuration;
        
        // Execute animation sequence
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            animate(targetScale * 1.5f, targetYOffset * 0.3, popUpDuration, popUpDelay);
            animate(targetScale * 1.2f, targetYOffset * 0.2, bounceDuration, bounceDelay);
            animate(targetScale, targetYOffset * 0.3, stabilizeDuration, stabilizeDelay);
            animate(0.1f, targetYOffset * 0.2, fadeDuration, fadeDelay);
        }, 2);
        
        // Remove at the end
        plugin.getServer().getScheduler().runTaskLater(plugin, display::remove, duration);
    }
    
    private void animate(float scale, double yOffset, int duration, int delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            display.setInterpolationDelay(0);
            display.setInterpolationDuration(duration);
            display.setTeleportDuration(duration);
            display.setTransformationMatrix(new Matrix4f().scale(scale));
            display.teleportAsync(display.getLocation().add(0, yOffset, 0));
        }, delay);
    }
}
