package net.hynse.teh;

import org.bukkit.event.Listener;
import me.nahu.scheduler.wrapper.FoliaWrappedJavaPlugin;
import me.nahu.scheduler.wrapper.WrappedScheduler;

import net.hynse.teh.event.TehEventListener;
import net.hynse.teh.command.ReloadCommand;

public final class Teh extends FoliaWrappedJavaPlugin implements Listener {

    public static Teh instance;
    public WrappedScheduler scheduler;
    @Override
    public void onEnable() {
        instance = this;
        scheduler = getScheduler();
        ConfigManager.reload(this);
        getServer().getPluginManager().registerEvents(new TehEventListener(), this);
        getCommand("tehreload").setExecutor(new ReloadCommand());
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
