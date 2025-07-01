package net.hynse.teh.command;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public class ToggleTehCommand implements CommandExecutor {
    private static final NamespacedKey KEY = new NamespacedKey("teh", "indicator_enabled");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use /toggleteh!", NamedTextColor.RED));
            return true;
        }
        boolean enabled = isIndicatorEnabled(player);
        boolean newState = !enabled;
        player.getPersistentDataContainer().set(KEY, PersistentDataType.INTEGER, newState ? 1 : 0);
        player.sendActionBar(Component.text(
            "Health Indicator: " + (newState ? "ENABLED" : "DISABLED"),
            TextColor.fromHexString(newState ? "#00FF00" : "#FF0000")
        ));
        return true;
    }

    public static boolean isIndicatorEnabled(Player player) {
        Integer value = player.getPersistentDataContainer().get(KEY, PersistentDataType.INTEGER);
        return value != null && value == 1;
    }
} 