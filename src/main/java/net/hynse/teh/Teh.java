package net.hynse.teh;

import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
        
import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import me.nahu.scheduler.wrapper.WrappedScheduler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;

public final class Teh extends FoliaWrappedJavaPlugin implements Listener {

    public static Teh instance;
    public WrappedScheduler scheduler;
    private static final double MIN_WIDTH_OFFSET = 0.4;
    private static final double MAX_WIDTH_EXTRA = 0.04;
    private static final double MIN_HEIGHT_OFFSET = 0.03;
    private static final double MAX_HEIGHT_EXTRA = 0.06;
    private static final TextColor DAMAGE_COLOR = TextColor.color(255, 0, 0);
    private static final TextColor HEAL_COLOR = TextColor.color(0, 255, 0);
    private static final int ANIMATION_DURATION = 40;
    private static final float ANIMATION_SCALE = 1.3f;
    private static final double ANIMATION_Y_OFFSET = 0.7;
    
    @Override
    public void onEnable() {
        instance = this;
        scheduler = getScheduler();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    void onEntityDamage(EntityDamageEvent e) {
        if (e.isCancelled() || e.getFinalDamage() == 0 || e.getEntity().getType() == org.bukkit.entity.EntityType.ALLAY) return;
        spawnDamageDisplay(e.getEntity(), 
            String.format("-%.1f", e.getFinalDamage()), 
            DAMAGE_COLOR);
    }

    @EventHandler
    void onEntityRegainHealth(EntityRegainHealthEvent e) {
        if (e.isCancelled() || e.getAmount() <= 0 || e.getEntity().getType() == org.bukkit.entity.EntityType.ALLAY) return;
        spawnDamageDisplay(e.getEntity(),
            String.format("+%.1f", e.getAmount()),
            HEAL_COLOR);
    }

    private void spawnDamageDisplay(Entity entity, String text, TextColor color) {
        org.bukkit.util.Vector direction = entity.getLocation().getDirection();
        double sideOffset = getRandomOffset(entity.getWidth());
        
        if (ThreadLocalRandom.current().nextBoolean()) {
            sideOffset = -sideOffset;
        }

        TextDisplay display = entity.getWorld().spawn(
            entity.getLocation().add(
                -direction.getZ() * sideOffset,
                getRandomHeightOffset(entity.getHeight()),
                direction.getX() * sideOffset
            ),
            TextDisplay.class
        );
        
        configureDisplay(display, text, color);
        
        new DisplayAnimator(
            display, 
            ANIMATION_DURATION, 
            ANIMATION_SCALE, 
            ANIMATION_Y_OFFSET
        ).start();
    }

    private void configureDisplay(TextDisplay display, String text, TextColor color) {
        display.text(Component.text(text).color(color));
        display.setSeeThrough(false);
        display.setPersistent(false);
        display.setShadowed(true);
        display.setBackgroundColor(Color.fromARGB(0, 0, 0, 0));
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBillboard(Display.Billboard.CENTER);
    }

    private double getRandomOffset(double entityWidth) {
        return (entityWidth + MIN_WIDTH_OFFSET) + 
            ThreadLocalRandom.current().nextDouble() * MAX_WIDTH_EXTRA;
    }

    private double getRandomHeightOffset(double entityHeight) {
        return (entityHeight - MIN_HEIGHT_OFFSET) + 
            ThreadLocalRandom.current().nextDouble() * MAX_HEIGHT_EXTRA;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
