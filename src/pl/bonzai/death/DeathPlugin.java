package pl.bonzai.death;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bonzai.death.handlers.PlayerHandler;
import pl.bonzai.death.json.Config;

import java.io.File;

public class DeathPlugin extends JavaPlugin {

    private static DeathPlugin plugin;

    @Override
    public void onLoad() {
        plugin=this;
    }

    @Override
    public void onEnable() {
        Config.init(new File("./plugins/" + getPlugin().getName()));
        Config.load("./plugins/" + getPlugin().getName() + "/config.json");
        Config.getInstance().toFile("./plugins/" + getPlugin().getName() + "/config.json");
        registerHandlers();
    }

    public void registerHandlers() {
        Bukkit.getPluginManager().registerEvents(new PlayerHandler(), this);
    }

    public static DeathPlugin getPlugin() {
        return plugin;
    }
}
