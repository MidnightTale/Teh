package net.hynse.teh;

import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;

public final class Teh extends JavaPlugin implements Listener {

    public static Teh instance;
    
    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled()) return;
        
        double damage = e.getFinalDamage();
        if (damage == 0) return;
        
        // Format damage text with minus sign
        String damageText = String.format("-%.1f", damage);
        spawnDamageDisplay(e.getEntity(), damageText, TextColor.color(255, 0, 0));
    }

    @EventHandler
    void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (e.isCancelled()) return;
        
        double heal = e.getAmount();
        if (heal <= 0) return;
        
        // Format heal text with plus sign
        String healText = String.format("+%.1f", heal);
        spawnDamageDisplay(e.getEntity(), healText, TextColor.color(0, 255, 0));
    }

    private void spawnDamageDisplay(Entity entity, String text, TextColor color) {
        // Initial setup
        double randomX = getRandomOffset();
        double randomZ = getRandomOffset();
        
        // Spawn and configure display
        TextDisplay display = entity.getWorld().spawn(
            entity.getLocation().add(randomX, 2.2, randomZ), 
            TextDisplay.class
        );
        
        // Configure display properties
        display.text(Component.text(text).color(color));
        display.setSeeThrough(true);
        display.setPersistent(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        
        // Create and start animator with dynamic parameters
        new DisplayAnimator(display, this, 16, 1.3f, 0.6).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    private double getRandomOffset() {
        return ThreadLocalRandom.current().nextDouble() * 0.5 - 0.25;
    }
}
