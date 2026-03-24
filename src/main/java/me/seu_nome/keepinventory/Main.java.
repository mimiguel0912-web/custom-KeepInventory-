package me.seu_nome.keepinventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

    private boolean globalKeepInventory = false; // Chave Mestra (Todos)
    private final Set<UUID> individualKeep = new HashSet<>(); // Lista Individual

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("keepinventory").setExecutor(this);
        getLogger().info("KeepInventory Custom 1.21.1 Ativado!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        UUID uuid = player.getUniqueId();

        // Se a chave global estiver ON OU se o jogador estiver na lista individual
        if (globalKeepInventory || individualKeep.contains(uuid)) {
            event.setKeepInventory(true);
            event.setKeepLevel(true);
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("keepinventory.admin")) {
            sender.sendMessage(ChatColor.RED + "Sem permissão!");
            return true;
        }

        if (args.length == 1) {
            // COMANDO GERAL: /kp on ou /kp off
            if (args[0].equalsIgnoreCase("on")) {
                globalKeepInventory = true;
                Bukkit.broadcastMessage(ChatColor.GREEN + "KeepInventory ATIVADO para todos!");
            } else if (args[0].equalsIgnoreCase("off")) {
                globalKeepInventory = false;
                individualKeep.clear(); // Limpa as exceções ao desligar o geral
                Bukkit.broadcastMessage(ChatColor.RED + "KeepInventory DESATIVADO para todos!");
            } else {
                sender.sendMessage(ChatColor.YELLOW + "Use: /kp <on/off> ou /kp <player> <on/off>");
            }
            return true;
        }

        if (args.length == 2) {
            // COMANDO INDIVIDUAL: /kp <player> <on/off>
            Player target = Bukkit.getPlayer(args[0]);
            String status = args[1];

            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Jogador offline!");
                return true;
            }

            if (status.equalsIgnoreCase("on")) {
                individualKeep.add(target.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "KeepInventory ativado para " + target.getName());
            } else {
                individualKeep.remove(target.getUniqueId());
                sender.sendMessage(ChatColor.RED + "KeepInventory desativado para " + target.getName());
            }
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Use: /kp <on/off> ou /kp <player> <on/off>");
        return true;
    }
}
