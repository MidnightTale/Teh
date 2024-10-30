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
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.degrees;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

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
        if (e.isCancelled() || e.getFinalDamage() <= 0) return;
        
        Entity entity = e.getEntity();
        double damage = e.getFinalDamage();
        
        Location displayLocation = getRandomDisplayLocation(entity);
        TextDisplay display = createDamageDisplay(entity, displayLocation, damage);
        
        // Start both animations
        animateScale(display, 1.5f, 20 * 3, 5);
        animateMovement(display, 1.5, 20 * 3, 4);
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
            .color(TextColor.color(255, 0, 0)));
        display.setSeeThrough(true);
        display.setPersistent(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        display.setTransformationMatrix(new Matrix4f().scale(0.0f));
        
        return display;
    }

    /**
     * Animates a Display entity with smooth scale animation
     * @param display The Display entity to animate
     * @param maxScale The maximum scale to reach
     * @param totalDuration Total duration in ticks (20 ticks = 1 second)
     * @param steps Number of animation steps for each phase
     */
    public void animateScale(Display display, float maxScale, int totalDuration, int steps) {
        // Calculate durations for each phase (should add up to totalDuration)
        int easeInDuration = totalDuration / 10;     // 16.7% for quick ease-in
        int holdDuration = totalDuration / 8;       // 50% for hold
        int easeOutDuration = totalDuration / 2;    // 33.3% for slow ease-out
        
        // Calculate step timings
        int stepDuration = Math.max(1, Math.min(easeInDuration, easeOutDuration) / steps);
        int interpolationDuration = Math.max(1, stepDuration / 2);
        
        BukkitRunnable animation = new BukkitRunnable() {
            private int currentStep = 0;
            private AnimationPhase phase = AnimationPhase.EASE_IN;
            private float currentScale = 0;
            
            @Override
            public void run() {
                boolean shouldContinue = true;
                
                switch (phase) {
                    case EASE_IN:
                        if (currentStep >= steps) {
                            phase = AnimationPhase.HOLD;
                            currentStep = 0;
                            currentScale = maxScale;  // Maintain max scale when transitioning to hold
                        } else {
                            float progress = (float) currentStep / (steps - 1);
                            float easedProgress = 1 - (1 - progress) * (1 - progress);
                            currentScale = maxScale * easedProgress;
                        }
                        break;
                        
                    case HOLD:
                        if (currentStep >= holdDuration) {
                            phase = AnimationPhase.EASE_OUT;
                            currentStep = 0;
                        }
                        // Keep the current scale during hold phase
                        break;
                        
                    case EASE_OUT:
                        if (currentStep >= steps) {
                            shouldContinue = false;
                        } else {
                            float progress = (float) currentStep / (steps - 1);
                            float easedProgress = progress * progress;
                            currentScale = maxScale * (1 - easedProgress);
                        }
                        break;
                    default:
                        break;
                }
                
                if (!shouldContinue) {
                    this.cancel();
                    display.remove();
                    return;
                }
                if (currentScale != maxScale) {
                    display.setInterpolationDuration(interpolationDuration);
                    display.setInterpolationDelay(0);
                    display.setTransformationMatrix(new Matrix4f().scale(currentScale));
                }
                getLogger().info(String.format("Phase: %s, Step: %d, Scale: %.2f", phase, currentStep, currentScale));
                
                currentStep++;
            }
        };
        
        animation.runTaskTimer(this, 2L, stepDuration);
    }

    private enum AnimationPhase {
        EASE_IN,
        HOLD,
        EASE_OUT,
    }

    public void animateMovement(Display display, double maxMovement, int totalDuration, int steps) {
        // Calculate durations for each phase (should add up to totalDuration)
        int easeInDuration = totalDuration / 10;     // 16.7% for quick ease-in
        int holdDuration = totalDuration / 8;       // 50% for hold
        int easeOutDuration = totalDuration / 2;    // 33.3% for slow ease-out
        
        // Calculate step timings
        int stepDuration = Math.max(1, Math.min(easeInDuration, easeOutDuration) / steps);
        int interpolationDuration = Math.max(1, stepDuration / 2);
        
        BukkitRunnable animation = new BukkitRunnable() {
            private int currentStep = 0;
            private AnimationPhase phase = AnimationPhase.EASE_IN;
            private double currentLocation = display.getLocation().getY();
            
            @Override
            public void run() {
                boolean shouldContinue = true;
                
                switch (phase) {
                    case EASE_IN:
                        if (currentStep >= steps) {
                            phase = AnimationPhase.HOLD;
                            currentStep = 0;
                            currentLocation = maxMovement;  // Maintain max scale when transitioning to hold
                        } else {
                            float progress = (float) currentStep / (steps - 1);
                            float easedProgress = 1 - (1 - progress) * (1 - progress);
                            currentLocation = maxMovement * easedProgress;
                        }
                        break;
                        
                    case HOLD:
                        if (currentStep >= holdDuration) {
                            phase = AnimationPhase.EASE_OUT;
                            currentStep = 0;
                        }
                        // Keep the current scale during hold phase
                        break;
                        
                    case EASE_OUT:
                        if (currentStep >= steps) {
                            shouldContinue = false;
                        } else {
                            float progress = (float) currentStep / (steps - 1);
                            float easedProgress = progress * progress;
                            currentLocation = maxMovement * (1 - easedProgress);
                        }
                        break;
                    default:
                        break;
                }
                
                if (!shouldContinue) {
                    this.cancel();
                    display.remove();
                    return;
                }
                if (currentLocation != maxMovement) {
                    display.setTeleportDuration(interpolationDuration);
                    display.setInterpolationDelay(0);
                    display.teleport(new Location(display.getWorld(), display.getLocation().getX(), currentLocation, display.getLocation().getZ()));
                }
                getLogger().info(String.format("Phase: %s, Step: %d, Location: %.2f, %.2f, %.2f", phase, currentStep, currentLocation, display.getLocation().getY(), display.getLocation().getZ()));
                
                currentStep++;
            }
        };
        
        animation.runTaskTimer(this, 2L, stepDuration);
    }

    private Location getRandomDisplayLocation(Entity entity) {
        // Get entity dimensions
        double entityWidth = entity.getWidth();
        double entityHeight = entity.getHeight();
        
        Location baseLocation = entity.getLocation();
        
        // Get entity's direction vector
        double directionYaw = baseLocation.getYaw();
        double directionX = -Math.sin(Math.toRadians(directionYaw));
        double directionZ = Math.cos(Math.toRadians(directionYaw));
        
        // Pick a random side (left or right relative to facing direction)
        boolean rightSide = random.nextBoolean();
        double sideMultiplier = rightSide ? 1 : -1;
        
        // Calculate perpendicular offset (cross product with up vector)
        double offsetX = -directionZ * sideMultiplier;
        double offsetZ = directionX * sideMultiplier;
        
        // Apply offset scaled by entity width
        double xOffset = (entityWidth / 2 + 0.2) * offsetX;
        double zOffset = (entityWidth / 2 + 0.2) * offsetZ;
        
        // Random height between 1/3 and 2/3 of entity height
        double yOffset = entityHeight * (0.33 + random.nextDouble() * 0.33);
        
        return baseLocation.clone().add(xOffset, yOffset, zOffset);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
