package dev.noah.combatpoints;

import dev.noah.combatpoints.util.DataUtils;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CombatPointsCommand implements CommandExecutor {


    private final CombatPoints plugin;
    public CombatPointsCommand(CombatPoints plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        MiniMessage miniMessage = MiniMessage.miniMessage();
        if(strings.length == 0){
            //check if its a player
            if(commandSender instanceof Player){
                if(!commandSender.hasPermission("combatpoints.command.self")){
                    commandSender.sendMessage(miniMessage.deserialize(plugin.getConfig().get("messages.no-permission").toString()));
                    return true;
                }
                Player player = (Player) commandSender;
                int points = DataUtils.getPoints(player);
                player.sendMessage(miniMessage.deserialize(plugin.getConfig().get("messages.points.self").toString().replace("%points%",String.valueOf(points))));
                return true;
            }else{
                commandSender.sendMessage(miniMessage.deserialize(plugin.getConfig().get("messages.no-console").toString()));
                return true;
            }
        }else{
            if(!(commandSender.hasPermission("combatpoints.command.other"))) {
                commandSender.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getConfig().get("messages.no-permission").toString()));
                return true;
            }
                Player target = plugin.getServer().getPlayer(strings[0]);
                if(target == null){
                    commandSender.sendMessage("Player not found.");
                    return true;
                }
                int points = DataUtils.getPoints(target);
                commandSender.sendMessage(miniMessage.deserialize(plugin.getConfig().get("messages.points.other").toString().replace("%player%",target.getName()).replace("%points%",String.valueOf(points))));
                return true;
            }

    }
}
