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
    void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        double damage = event.getFinalDamage();
        
        Location loc = entity.getLocation().add(
            (random.nextDouble() - 0.5) * 0.5,
            1.5,
            (random.nextDouble() - 0.5) * 0.5
        );

        // Spawn and configure text display
        TextDisplay textDisplay = entity.getWorld().spawn(loc, TextDisplay.class);
        textDisplay.text(Component.text(String.format("%.1f", damage))
            .color(NamedTextColor.RED));
        textDisplay.setSeeThrough(true);
        textDisplay.setPersistent(false);
        textDisplay.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        textDisplay.setAlignment(TextDisplay.TextAlignment.CENTER);
        textDisplay.setBillboard(Display.Billboard.CENTER);

        Matrix4f scale = new Matrix4f().scale(0.0f);
        Matrix4f scale2 = new Matrix4f().scale(1.3f);
        Matrix4f scale3 = new Matrix4f().scale(0.0f);

        textDisplay.setInterpolationDelay(0);
        textDisplay.setTransformationMatrix(scale);

        getServer().getScheduler().runTaskLater(this, () -> {
            textDisplay.setInterpolationDuration(8); 
            textDisplay.setTransformationMatrix(scale2);
        }, 3L);
        // getServer().getScheduler().runTaskLater(this, () -> {
        //     textDisplay.setInterpolationDuration(20 * 4); 
        //     textDisplay.setTransformationMatrix(scale3);
        // },20L * 6L);
    }

    //     // Remove the text display after 2 seconds
    //     getServer().getScheduler().runTaskLater(this, () -> {
    //         textDisplay.remove();
    //     }, 40L); // 40 ticks = 2 seconds
    // }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
