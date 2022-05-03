package com.seailz.orbitalapplication;

import com.seailz.orbitalapplication.commands.CommandBalance;
import com.seailz.orbitalapplication.commands.CommandEarn;
import com.seailz.orbitalapplication.commands.CommandGive;
import com.seailz.orbitalapplication.commands.CommandSetBal;
import com.seailz.orbitalapplication.commands.eco.CommandEco;
import com.seailz.orbitalapplication.core.Locale;
import com.seailz.orbitalapplication.core.Logger;
import com.seailz.orbitalapplication.listeners.PlayerJoin;
import games.negative.framework.BasePlugin;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class OrbitalApplication extends BasePlugin {

    @Getter
    @Setter
    public static OrbitalApplication instance;
    @Getter
    boolean debug;
    @Getter
    private int minorErrors;
    @Getter
    private int severeErrors;
    @Getter
    private ArrayList<String> debugLog;
    @Getter
    private YamlConfiguration dataConfig;
    private File dataConfigFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        long start = System.currentTimeMillis();
        setInstance(this);
        Locale.init(this);
        minorErrors = 0;
        severeErrors = 0;
        debugLog = new ArrayList<>();
        this.debug = getConfig().getBoolean("debug");
        saveDefaultConfig();

        File dataFile = new File(this.getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);

        dataConfigFile = dataFile;

        registerListeners(
                new PlayerJoin()
        );

        registerCommands(
                new CommandEco(),
                new CommandBalance(),
                new CommandEarn(),
                new CommandSetBal(),
                new CommandGive()
        );

        long finish = System.currentTimeMillis();
        long time = finish - start;
        Logger.log(Logger.LogLevel.SUCCESS, "OrbitalApplication successfully started in %t%ms".replace("%t%", String.valueOf(time)));
    }

    public void addError(boolean severe) {
        if (severe) {
            severeErrors++;
        } else {
            minorErrors++;
        }
    }

    public void saveDataConfig() throws IOException {
        this.getDataConfig().save(this.dataConfigFile);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
