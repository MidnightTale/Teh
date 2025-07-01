package net.hynse.teh.command;

import net.hynse.teh.Teh;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Teh.instance.reloadConfig();
        net.hynse.teh.ConfigManager.reload(Teh.instance);
        sender.sendMessage("§aTeh config reloaded!");
        return true;
    }
} 