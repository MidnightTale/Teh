package net.hynse.teh;

import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Vector3f;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.hover.content.Text;

import java.util.Random;
import org.joml.Matrix4f;

import org.bukkit.Color;

public final class Teh extends JavaPlugin implements Listener {
    private final Random random = new Random();
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        double damage = e.getFinalDamage();
        
        Location baseLocation = entity.getLocation();
        Location displayLocation = baseLocation.clone().add(
            (random.nextDouble() - 0.5) * 0.5,
            1.5,
            (random.nextDouble() - 0.5) * 0.5
        );

        TextDisplay display = createDamageDisplay(entity, displayLocation, damage);
        getLogger().info("display created");
        // First animate in
        animateEaseIn(display, 1.3f, 8, 3);
        
        // Then schedule the fade out animation after a delay
        getServer().getScheduler().runTaskLater(this, () -> {
            animateEaseOut(display, 1.3f, 16, 6);
        }, 9L); // Wait 1.5 seconds before starting fade out
        getLogger().info("fade out scheduled");
    }
        /**
     * Creates a configured TextDisplay entity for damage numbers
     * @param entity Source entity for the world reference
     * @param location Location to spawn the display
     * @param damage Damage amount to display
     * @return Configured TextDisplay entity
     */
    private TextDisplay createDamageDisplay(Entity entity, Location location, double damage) {
        TextDisplay display = entity.getWorld().spawn(location, TextDisplay.class);
        
        // Configure text and appearance
        display.text(Component.text(String.format("%.1f", damage))
            .color(NamedTextColor.RED));
        display.setSeeThrough(true);
        display.setPersistent(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        display.setTransformationMatrix(new Matrix4f().scale(0.0f));
        
        return display;
    }

    /**
     * Animates a Display entity with dynamic ease-in scaling
     * @param display The Display entity to animate
     * @param targetScale The final scale to reach
     * @param durationTicks Total duration in ticks (20 ticks = 1 second)
     * @param steps Number of animation steps to use
     */
    public void animateEaseIn(Display display, float targetScale, int durationTicks, int steps) {
        getLogger().info("animateEaseIn");
        // Calculate time between steps
        int stepDuration = Math.max(1, durationTicks / steps);
        
        // Calculate scale values using easeOutQuad function
        float[] scaleSteps = calculateEaseOutScales(0.0f, targetScale, steps);
        
        // Calculate interpolation duration for smooth transitions
        int interpolationDuration = Math.max(1, stepDuration / 2);
        
        BukkitRunnable animation = new BukkitRunnable() {
            private int currentStep = 0;
            
            @Override
            public void run() {
                if (currentStep >= steps) {
                    this.cancel();
                    // Remove display after animation completes
                    getServer().getScheduler().runTaskLater(
                        Teh.this, 
                        display::remove, 
                        20L // Keep visible for 1 second after animation
                    );
                    return;
                }
                
                display.setInterpolationDuration(interpolationDuration);
                display.setInterpolationDelay(0);
                display.setTransformationMatrix(
                    new Matrix4f().scale(scaleSteps[currentStep])
                );
                getLogger().info("scale" + currentStep + " = " + scaleSteps[currentStep]);
                
                currentStep++;
            }
        };
        
        // Start animation with calculated timing
        animation.runTaskTimer(this, 2L, stepDuration);
    }

    /**
     * Calculates scale values using easeOutQuad easing function
     * @param startScale Starting scale value
     * @param endScale Target scale value
     * @param steps Number of steps to calculate
     * @return Array of scale values
     */
    private float[] calculateEaseOutScales(float startScale, float endScale, int steps) {
        float[] scales = new float[steps];
        float change = endScale - startScale;
        
        for (int i = 0; i < steps; i++) {
            float progress = (float) i / (steps - 1);
            // EaseOutQuad formula: 1 - (1 - progress) * (1 - progress)
            float easedProgress = 1 - (1 - progress) * (1 - progress);
            scales[i] = startScale + (change * easedProgress);
        }
        
        return scales;
    }

    /**
     * Animates a Display entity with dynamic ease-out scaling (shrinking effect)
     * @param display The Display entity to animate
     * @param startScale The initial scale to start from
     * @param durationTicks Total duration in ticks (20 ticks = 1 second)
     * @param steps Number of animation steps to use
     */
    public void animateEaseOut(Display display, float startScale, int durationTicks, int steps) {
        getLogger().info("animateEaseOut");
        // Calculate time between steps
        int stepDuration = Math.max(1, durationTicks / steps);
        
        // Calculate scale values using easeInQuad function (reverse of easeOutQuad)
        float[] scaleSteps = calculateEaseInScales(startScale, 0.0f, steps);
        
        // Calculate interpolation duration for smooth transitions
        int interpolationDuration = Math.max(1, stepDuration / 2);
        
        BukkitRunnable animation = new BukkitRunnable() {
            private int currentStep = 0;
            
            @Override
            public void run() {
                if (currentStep >= steps) {
                    this.cancel();
                    display.remove();
                    return;
                }
                
                display.setInterpolationDuration(interpolationDuration);
                display.setInterpolationDelay(0);
                display.setTransformationMatrix(
                    new Matrix4f().scale(scaleSteps[currentStep])
                );
                getLogger().info("scale" + currentStep + " = " + scaleSteps[currentStep]);
                currentStep++;
            }
        };
        
        // Start animation with calculated timing
        animation.runTaskTimer(this, 2L, stepDuration);
    }

    /**
     * Calculates scale values using easeInQuad easing function
     * Creates a shrinking effect from start to end
     * @param startScale Starting scale value
     * @param endScale Target scale value (usually 0)
     * @param steps Number of steps to calculate
     * @return Array of scale values
     */
    private float[] calculateEaseInScales(float startScale, float endScale, int steps) {
        float[] scales = new float[steps];
        float change = endScale - startScale;
        
        for (int i = 0; i < steps; i++) {
            float progress = (float) i / (steps - 1);
            // EaseInQuad formula: progress * progress
            float easedProgress = progress * progress;
            scales[i] = startScale + (change * easedProgress);
        }
        
        return scales;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
