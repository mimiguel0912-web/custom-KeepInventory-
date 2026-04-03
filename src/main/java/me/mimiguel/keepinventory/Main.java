package me.miguel.keepinventory;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private List<UUID> keepPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadData();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("kp").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                if (!keepPlayers.contains(p.getUniqueId())) {
                    keepPlayers.add(p.getUniqueId());
                    saveData();
                }
                p.sendMessage("§aKeepInventory ativado e SALVO!");
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                keepPlayers.remove(p.getUniqueId());
                saveData();
                p.sendMessage("§cKeepInventory desativado!");
                return true;
            }
        }
        p.sendMessage("§eUse: /kp <on/off>");
        return true;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if (keepPlayers.contains(p.getUniqueId())) {
            e.setKeepInventory(true);
            e.getDrops().clear();
            e.setDroppedExp(0);
            e.setKeepLevel(true);
        }
    }

    private void saveData() {
        List<String> list = new ArrayList<>();
        for (UUID uuid : keepPlayers) list.add(uuid.toString());
        getConfig().set("players", list);
        saveConfig();
    }

    private void loadData() {
        List<String> list = getConfig().getStringList("players");
        for (String s : list) keepPlayers.add(UUID.fromString(s));
    }
}
