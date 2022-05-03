package com.seailz.orbitalapplication.commands.eco;

import com.seailz.orbitalapplication.OrbitalApplication;
import com.seailz.orbitalapplication.commands.eco.sub.Report;
import games.negative.framework.command.Command;
import games.negative.framework.command.annotation.CommandInfo;
import games.negative.framework.message.Message;
import org.bukkit.command.CommandSender;

@CommandInfo(
        name = "eco",
        aliases = {"economy"}
)
public class CommandEco extends Command {
    public CommandEco() {
        this.addSubCommands(
                new Report(OrbitalApplication.getInstance())
        );
    }
    @Override
    public void onCommand(CommandSender sender, String[] args) {
        new Message(
                "&b&n-------------",
                "&b       ECO",
                "&f Developed by &bSeailz",
                "   &bdev.seailz.com",
                "&b&n-------------"
        ).send(sender);
    }
}
