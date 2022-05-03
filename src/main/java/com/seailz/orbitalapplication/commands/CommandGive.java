package com.seailz.orbitalapplication.commands;

import com.seailz.orbitalapplication.OrbitalApplication;
import com.seailz.orbitalapplication.core.Locale;
import com.seailz.orbitalapplication.core.SQLUtil;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

@CommandInfo(
        name = "give",
        args = {"player", "amount"}
)
public class CommandGive extends Command {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if (!sender.isOp()) return;
        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException ex){
            Locale.NAN.send(sender);
            return;
        }
        try {
            SQLUtil.setBalance(String.valueOf(Integer.parseInt(SQLUtil.getBalance(Bukkit.getOfflinePlayer(args[0]))) + Integer.parseInt(args[1])), Bukkit.getOfflinePlayer(args[0]));
            Locale.ADDED_OTHER.replace("%amount%", args[1]).replace("%player%", args[0]).send(sender);
        } catch (SQLException e) {
            e.printStackTrace();
            OrbitalApplication.getInstance().addError(true);
            OrbitalApplication.getInstance().getDebugLog().add(e.getMessage());
        }
    }
}
