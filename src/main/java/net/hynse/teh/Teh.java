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
        
        Entity entity = e.getEntity();
        double damage = e.getFinalDamage();
        TextDisplay display = entity.getWorld().spawn(entity.getLocation(), TextDisplay.class);
        
        // Configure text and appearance
        display.text(Component.text(String.format("%.1f", damage))
            .color(TextColor.color(255, 0, 0)));
        display.setSeeThrough(true);
        display.setPersistent(false);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
        display.setTransformationMatrix(new Matrix4f().scale(0.0f));
        
        
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
