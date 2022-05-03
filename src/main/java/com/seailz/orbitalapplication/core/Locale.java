package com.seailz.orbitalapplication.core;
import games.negative.framework.message.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Locale {

    BALANCE("balance", Collections.singletonList("&b&lBALANCE&f Your balance is &b$%balance%&f!")),
    ERROR_BALANCE("error.balance", Collections.singletonList("&c&lERROR&f Unable to contact database! Please contact an admin. The most likely explanation is that the MySQL credentials are &bincorrect&f")),
    ERROR_INVALID_PLAYER("error.invalidplayer", Collections.singletonList("&c&lERROR&f That player is invalid!")),
    OTHER_BALANCE("otherbalance", Collections.singletonList("&b&lBALANCE&f %player%'s balance: &b%balance%&f!")),
    ERROR_COOLDOWN("error.cooldown", Collections.singletonList("&c&lERROR&f That command is still on cooldown!")),
    ERROR_EARN("error.earn", Collections.singletonList("&c&lERROR&F There was an error adding the money to your account! Please contact an admin!")),
    ADDED("added", Collections.singletonList("&b&lBALANCE&f We have added &b$%amount%&f to your account!")),
    ADDED_OTHER("addedother", Collections.singletonList("&b&lBANK&f We have added &b$%amount%&f to &b%player%'s&f account!")),
    NAN("error.nan", Collections.singletonList("&c&lERROR&f Please input a number!")),
    SET_OTHER("setother", Collections.singletonList("&b&lBANK&F We have set &b%player%'s&f balance to &b$%amount%&f!"));

    private final String id;
    private final List<String> defaultMessage;
    private Message message;

    /**
     * Register the messages.yml file
     *
     * @param plugin The main class
     */
    @SneakyThrows
    public static void init(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "messages.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        if (!configFile.exists()) {
            Arrays.stream(values()).forEach(locale -> {
                String id = locale.getId();
                List<String> defaultMessage = locale.getDefaultMessage();

                config.set(id, defaultMessage);
            });

        } else {
            Arrays.stream(values()).filter(locale -> {
                String id = locale.getId();
                return (config.get(id, null) == null);
            }).forEach(locale -> config.set(locale.getId(), locale.getDefaultMessage()));

        }
        config.save(configFile);

        // Creates the message objects
        Arrays.stream(values()).forEach(locale ->
                locale.message = new Message(config.getStringList(locale.getId())
                        .toArray(new String[0])));
    }

    public void send(CommandSender sender) {
        message.send(sender);
    }

    public void send(List<Player> players) {
        message.send((CommandSender) players);
    }

    public void broadcast() {
        message.broadcast();
    }

    public Message replace(Object o1, Object o2) {
        return message.replace((String) o1, (String) o2);
    }
}