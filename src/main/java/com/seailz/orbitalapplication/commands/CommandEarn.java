package com.seailz.orbitalapplication.commands;

import com.seailz.orbitalapplication.OrbitalApplication;
import com.seailz.orbitalapplication.core.Locale;
import com.seailz.orbitalapplication.core.SQLUtil;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;

@CommandInfo(
        name = "earn",
        playerOnly = true,
        aliases = {"givememoneynowustipidcommandpleaseokcoolbye"}
)
public class CommandEarn extends Command {
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        // Check cooldown
        boolean moveOn = OrbitalApplication.getInstance().getDataConfig().getString(p.getUniqueId() + ".canEarm") == null;
        if (!moveOn && OrbitalApplication.getInstance().getDataConfig().getString(p.getUniqueId() + ".canEarn").equals("false")) {
            Locale.ERROR_COOLDOWN.send(p);
            return;
        }

        Random r = new Random();
        int low = 1;
        int high = 5;
        int result = r.nextInt(high-low) + low;

        try {
            SQLUtil.setBalance(String.valueOf(Integer.parseInt(SQLUtil.getBalance(p)) + result), p);
            Locale.ADDED.replace("%amount%", String.valueOf(result)).send(p);
            OrbitalApplication.getInstance().getDataConfig().set(p.getUniqueId() + ".canEarn", "false");
            OrbitalApplication.getInstance().saveDataConfig();

            OrbitalApplication.getInstance().getServer().getScheduler().runTaskLater(OrbitalApplication.getInstance(), new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    OrbitalApplication.getInstance().getDataConfig().set(p.getUniqueId() + ".canEarn", "true");
                    OrbitalApplication.getInstance().saveDataConfig();
                }
            }, 1200);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Locale.ERROR_EARN.send(p);
            OrbitalApplication.getInstance().addError(true);
            OrbitalApplication.getInstance().getDebugLog().add(e.getMessage());
        }
    }
}
