package com.seailz.orbitalapplication.commands.eco.sub;

import com.seailz.orbitalapplication.OrbitalApplication;
import com.seailz.orbitalapplication.core.Logger;
import games.negative.framework.command.SubCommand;
import games.negative.framework.command.annotation.CommandInfo;
import games.negative.framework.message.Message;
import games.negative.framework.util.HasteBin;
import games.negative.framework.util.Utils;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@CommandInfo(
        name = "report",
        permission = "eco.report"
)
public class Report extends SubCommand {
    private final OrbitalApplication plugin;

    public Report(OrbitalApplication main) {
        this.plugin = main;
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        ArrayList<String> debugLog = plugin.getDebugLog();
        StringBuilder sb = new StringBuilder();
        debugLog.forEach(d -> sb.append(d).append("\n"));

        String[] array = new String[]{
                "===============================================================",
                "              Detailed Report of %ident% from OrbitalApplication".replace("%ident%", Objects.requireNonNull(plugin.getConfig().getString("server-identifier"))),
                "This is generally used to debug issues that are wrong with the plugin.",
                "               Severe Errors: %x% | Minor errors: %y%".replace("%x%", String.valueOf(plugin.getMinorErrors())).replace("%y%", String.valueOf(plugin.getSevereErrors())),
                "===============================================================",
                " ",
                " ",
                " ",
                "===============================================================",
                "Debug Logs [" + Utils.decimalFormat(debugLog.size()) + " entries]",
                sb.toString(),
                "===============================================================",
                " ",
                " ",
                " ",
        };
        StringBuilder urlBuilder = new StringBuilder();
        Arrays.stream(array).forEach(line -> urlBuilder.append(line).append("\n"));
        new Message("&aGenerating report..").send(sender);
        try {
            String url = new HasteBin().post(urlBuilder.toString(), false);
            new Message("&aDetailed Report: &e" + url).send(sender);
        } catch (IOException e) {
            new Message("&cFailed to generate report. Please check the Console.").send(sender);
            Logger.log(Logger.LogLevel.DEBUG, "Failed to generate report");
            Logger.log(Logger.LogLevel.ERROR, "Failed to generate report");
            Logger.log(Logger.LogLevel.ERROR, "Failed to generate report");
            plugin.addError(true);
            e.printStackTrace();
        }
    }
}
