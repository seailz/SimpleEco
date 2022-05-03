package com.seailz.orbitalapplication.commands;

import com.seailz.orbitalapplication.OrbitalApplication;
import com.seailz.orbitalapplication.core.Locale;
import com.seailz.orbitalapplication.core.SQLUtil;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

@CommandInfo(
        name = "bal",
        aliases = {"balance", "whatismymoneypleasethankyou", "money"},
        playerOnly = true
)
public class CommandBalance extends Command {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            try {
                Locale.BALANCE.replace("%balance%", SQLUtil.getBalance((Player) sender)).send(sender);
            } catch (SQLException e) {
                OrbitalApplication.getInstance().getDebugLog().add(e.getMessage());
                OrbitalApplication.getInstance().addError(true);
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            OfflinePlayer o = Bukkit.getOfflinePlayer(args[0]);
            try {
                Locale.OTHER_BALANCE.replace("%player%", o.getName()).replace("%balance%", SQLUtil.getBalance(o)).send(sender);
            } catch (SQLException e) {
                Locale.ERROR_INVALID_PLAYER.send(sender);
            }
        }
    }
}
