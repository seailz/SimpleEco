package com.seailz.orbitalapplication.listeners;

import com.seailz.orbitalapplication.core.SQLUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.util.Objects;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        try {
            if (!Objects.equals(SQLUtil.getBalance(e.getPlayer()), "0")) return;
        } catch (SQLException ex) {
            try {
                SQLUtil.setBalance("0", e.getPlayer());
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }
}
