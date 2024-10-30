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
    
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled() || e.getFinalDamage() <= 0) return;
        Random random = new Random();
        double randomX = random.nextDouble() * 0.5 - 0.25;  
        double randomZ = random.nextDouble() * 0.5 - 0.25;
        Entity entity = e.getEntity();
        double damage = e.getFinalDamage();
        TextDisplay display = entity.getWorld().spawn(entity.getLocation().add(randomX, 1.5, randomZ), TextDisplay.class);
        
        // Configure initial display properties
        display.text(Component.text(String.format("%.1f", damage))
            .color(TextColor.color(255, 0, 0)));
        display.setSeeThrough(true);
        display.setPersistent(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        display.setTransformationMatrix(new Matrix4f().scale(0.0f));
        display.setTextOpacity((byte) 0);
        
        // Create smooth animation
        new BukkitRunnable() {
            private int ticks = 0;
            private final int TOTAL_DURATION = 80;
            
            @Override
            public void run() {
                if (ticks >= TOTAL_DURATION) {
                    display.remove();
                    this.cancel();
                    return;
                }
                
                float progress = (float) ticks / TOTAL_DURATION;
                
                // Scale animation
                float scale;
                if (progress < 0.1f) {
                    // Quick pop-in (0-10% of animation)
                    scale = progress * 10 * 2.0f;  // Faster scale up to 2.0x
                } else {
                    // Logarithmic scale down
                    float normalizedProgress = (progress - 0.1f) / 0.9f;  // Normalize remaining progress to 0-1
                    scale = 2.0f * (float)(1 - Math.log1p(normalizedProgress * 9) / Math.log1p(9));
                }
                display.setInterpolationDelay(0);
                display.setInterpolationDuration(2);
                display.setTransformationMatrix(new Matrix4f().scale(scale));
                
                // Opacity animation
                byte opacity;
                if (progress < 0.1f) {
                    // Fade in
                    opacity = (byte) (progress * 10f * 255);
                } else if (progress > 0.8f) {
                    // Fade out
                    opacity = (byte) (255 * (1.0f - ((progress - 0.8f) / 0.2f)));
                } else {
                    // Full opacity
                    opacity = (byte) 255;
                }
                display.setTextOpacity(opacity);
                
                // Position animation
                if (ticks % 2 == 0) {
                    float heightMultiplier = 1.0f - (progress * 0.7f); // Gradually decreases from 1.0 to 0.3
                    float baseHeight = 0.05f; // Initial jump height
                    float currentJumpHeight = baseHeight * heightMultiplier;
                    
                    display.setTeleportDuration(2);
                    display.teleportAsync(display.getLocation().add(0, currentJumpHeight, 0));
                }
                
                ticks++;
                getLogger().info("Ticks: " + ticks);
                getLogger().info("Progress: " + progress);
                getLogger().info("Scale: " + scale);
                getLogger().info("Opacity: " + opacity);
                getLogger().info("Position: " + display.getLocation().getY());
                getLogger().info("--------------------------------");

            }
        }.runTaskTimer(this, 2L, 1L);
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
