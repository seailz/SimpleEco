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
        name = "setbal",
        args = {"player", "amount"}
)
public class CommandSetBal extends Command {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        try {
            Integer.parseInt(args[1]);
        } catch (NumberFormatException ex){
            Locale.NAN.send(sender);
            return;
        }
        if (!sender.isOp()) return;
        try {
            SQLUtil.setBalance(args[1], Bukkit.getOfflinePlayer(args[0]));
            Locale.SET_OTHER.replace("%player%", args[0]).replace("%amount%", args[1]).send(sender);
        } catch (SQLException e) {
            e.printStackTrace();
            OrbitalApplication.getInstance().addError(true);
            OrbitalApplication.getInstance().getDebugLog().add(e.getMessage());
            Locale.ERROR_BALANCE.send(sender);
        }
    }
}
