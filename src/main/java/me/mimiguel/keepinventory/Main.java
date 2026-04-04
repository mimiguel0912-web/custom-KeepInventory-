package me.miguel.keepinventory;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private boolean isGlobalKeepEnabled = false;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        // Carrega se o modo global estava ativado ou não
        isGlobalKeepEnabled = getConfig().getBoolean("keep-inventory-global", false);
        
        getServer().getPluginManager().registerEvents(this, this);
        if (getCommand("kp") != null) {
            getCommand("kp").setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage("§cSomente ADMs podem usar este comando!");
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("on")) {
                isGlobalKeepEnabled = true;
                saveData(true);
                sender.sendMessage("§a[KeepInv] ATIVADO para TODOS os jogadores!");
                return true;
            }
            if (args[0].equalsIgnoreCase("off")) {
                isGlobalKeepEnabled = false;
                saveData(false);
                sender.sendMessage("§c[KeepInv] DESATIVADO para todos.");
                return true;
            }
        }
        sender.sendMessage("§eUse: /kp <on/off>");
        return true;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        // Se o modo global estiver ativado, ninguém dropa itens
        if (isGlobalKeepEnabled) {
            e.setKeepInventory(true);
            e.getDrops().clear();
            e.setDroppedExp(0);
            e.setKeepLevel(true);
        }
    }

    private void saveData(boolean value) {
        getConfig().set("keep-inventory-global", value);
        saveConfig();
    }
}
