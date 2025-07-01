package net.hynse.teh;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.hynse.teh.command.ToggleTehCommand;

public class TehExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "teh";
    }

    @Override
    public @NotNull String getAuthor() {
        return "MidnightTale";
    }

    @Override
    public @NotNull String getVersion() {
        return Teh.instance.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String identifier) {
        if (offlinePlayer == null) return "";
        Player player = offlinePlayer.getPlayer();
        if (player == null) return "";
        switch (identifier.toLowerCase()) {
            case "enabled":
                return ToggleTehCommand.isIndicatorEnabled(player) ? "Enabled" : "Disabled";
            default:
                return null;
        }
    }
} 