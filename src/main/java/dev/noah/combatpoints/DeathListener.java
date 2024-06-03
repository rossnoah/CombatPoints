package dev.noah.combatpoints;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class DeathListener implements Listener {


    private HashMap<String,Long> lastKillTimeMap = new HashMap<>();
    /*
    This may use a decent amount of memory with a large number of players and unique kills.
    Its should be fine, probably.
    8bytes per long, 32 bytes per key (16UUID x 2). Figure double that because JVM weirdness.

    1000 players, 1000 unique kills each = 1000*1000*(32+8)*2 = 80MB maximum before server restart
    In practice it will be much much less than this.
     */

    private CombatPoints plugin;

    public DeathListener(CombatPoints plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        //check if there is a killer
        if (event.getEntity().getKiller() == null) {
            return;
        }

        Player killed = event.getEntity();
        Player killer = killed.getKiller();


        //check if they have the same IP, then return as this is likely abuse
        if (isSameIP(killed, killer)) {
            return;
        }

        if(hasKilledRecently(killer,killed)){
            return;
        }


        int pointsEarned = calculatePoints(killed.getInventory());
        if(pointsEarned == 0){
            return;
            //don't log or do anything if no points earned
        }

        addKill(killer,killed);
        int currentPoints = PDCUtils.getPoints(killer);
        PDCUtils.setPoints(killer,currentPoints+pointsEarned);

        if(plugin.getConfig().getBoolean("send-on-kill")) {
            killer.sendMessage(MiniMessage.miniMessage().deserialize(plugin.getConfig().getString("messages.onkill").replace("%points%", String.valueOf(pointsEarned)).replace("%player%", killed.getName())));
        }

    }





    private int calculatePoints(PlayerInventory killedPlayerInv){

        int points = 0;
        //armor items
        ItemStack[] armor = killedPlayerInv.getArmorContents();

        for(ItemStack item : armor){
            if(item == null){
                continue;
            }
            //do something with the item
            int enchantPoints = calcEnchantPoints(item);
            int materialPoints = calcMaterialPoints(item);

            points += enchantPoints;
            points += materialPoints;
        }

        return points;
    }

    private int calcMaterialPoints(ItemStack item){
        //check if it's a piece of diamond armor (boots, leggings, chestplate, helmet)
        if(item.getType() == Material.DIAMOND_BOOTS || item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_HELMET){
            return 50;
        }
        //200 points for netherite armor
        if(item.getType() == Material.NETHERITE_BOOTS || item.getType() == Material.NETHERITE_LEGGINGS || item.getType() == Material.NETHERITE_CHESTPLATE || item.getType() == Material.NETHERITE_HELMET){
            return 100;
        }

        return 0;
    }

    private int calcEnchantPoints(ItemStack item){

        int points = 0;

        Map<Enchantment,Integer> enchanatsMap = item.getEnchantments();


        for(Map.Entry<Enchantment,Integer> entry : enchanatsMap.entrySet()){
            Enchantment enchantment = entry.getKey();
            if(enchantment == Enchantment.VANISHING_CURSE || enchantment == Enchantment.BINDING_CURSE){
                continue;
                //skip this and dont give any points
            }
            int level = entry.getValue();

            if(enchantment == Enchantment.PROTECTION_EXPLOSIONS || enchantment == Enchantment.PROTECTION_ENVIRONMENTAL){
                points += level * 30;
                //double points for protection and blast protection
            }else{
                points += level * 10;
                //normal 100 points per level
            }

        }
        return points;
    }


    private String makeKey(Player killer, Player killed){
        return killer.getUniqueId().toString()+killed.getUniqueId().toString();
    }

    private void addKill(Player killer, Player killed){
        String key = makeKey(killer,killed);
        lastKillTimeMap.put(key,System.currentTimeMillis());
    }


    private boolean hasKilledRecently(Player killer, Player killed){
        String key = makeKey(killer,killed);
        if(!lastKillTimeMap.containsKey(key)){
            return false;
        }
        long lastKillTime = lastKillTimeMap.get(key);
        long TWENTY_FOUR_HOURS_AGO = System.currentTimeMillis() - 24*60*60*1000;

        //if the last kill time is less than 24 hours ago (value greater), return true
        return lastKillTime > TWENTY_FOUR_HOURS_AGO;

    }



    private boolean isSameIP(Player player1, Player player2) {
        return player1.getAddress().getAddress().getHostAddress().equals(player2.getAddress().getAddress().getHostAddress());
    }
}
