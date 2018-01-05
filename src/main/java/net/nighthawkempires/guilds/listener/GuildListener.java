package net.nighthawkempires.guilds.listener;

import com.google.common.collect.Maps;
import jdk.management.resource.internal.inst.FileOutputStreamRMHooks;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.events.UserDeathEvent;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.utils.BlockUtil;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.guild.relation.RelationType;
import net.nighthawkempires.guilds.user.User;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class GuildListener implements Listener {

    private ConcurrentMap<Player, GuildModel> locationMap = Maps.newConcurrentMap();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();
        if (BlockUtil.isInteractiveBlock(event.getClickedBlock().getType())) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                for (GuildModel guild : NEGuilds.getGuildRegistry().getRegistered()) {
                    if (guild.getTerritory().contains(chunk)) {
                        if (!NEGuilds.getGuildData().adminBypass.contains(player.getUniqueId())) {
                            if (user.getGuild() == null) {
                                event.setCancelled(true);
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to interact inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                                return;
                            }

                            GuildModel guildModel = user.getGuild();
                            if (!guildModel.getUUID().toString().equals(guild.getUUID().toString())) {
                                for (UUID uuid : guildModel.getRelations().keySet()) {
                                    if (guildModel.getRelations().get(uuid) != RelationType.ALLY) {
                                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to interact inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock().getType() == Material.SOIL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();

        boolean inTerritory = false;
        for (GuildModel guild : NEGuilds.getGuildRegistry().getRegistered()) {
            if (guild.getTerritory().contains(chunk)) {
                if (!locationMap.containsKey(player) || locationMap.get(player) != guild) {
                    locationMap.put(player, guild);
                    NECore.getCodeHandler().sendTitleToPlayer(player, guild.getColor() + guild.getName() + "'s", ChatColor.GRAY + "Territory", 10, 30, 10);
                }
                inTerritory = true;
            }
        }

        if (!inTerritory) {
            locationMap.remove(player);
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        for (GuildModel guild : NEGuilds.getGuildRegistry().getRegistered()) {
            if (guild.getTerritory().contains(chunk)) {
                if (!NEGuilds.getGuildData().adminBypass.contains(player.getUniqueId())) {
                    if (user.getGuild() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                        event.setCancelled(true);
                        return;
                    }

                    if (!user.getGuild().getUUID().toString().equals(guild.getUUID().toString())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        for (GuildModel guild : NEGuilds.getGuildRegistry().getRegistered()) {
            if (guild.getTerritory().contains(chunk)) {
                if (!NEGuilds.getGuildData().adminBypass.contains(player.getUniqueId())) {
                    if (user.getGuild() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                        event.setCancelled(true);
                        return;
                    }

                    if (!user.getGuild().getUUID().toString().equals(guild.getUUID().toString())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() + guild.getName() + "'s " + ChatColor.RED + "territory."));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(UserDeathEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());

        if (event.getEntityKiller() != null && event.getEntityKiller() instanceof Player) {
            Player killer = (Player) event.getEntityKiller();
            User kuser = NEGuilds.getUserManager().getUser(killer.getUniqueId());

            if (user.getPower() == 0 || user.getPower() == 1) {
                user.setPower(0);
            } else {
                user.setPower(user.getPower() - 2);
            }

            if (kuser.getPower() == 10) {
                kuser.setPower(10);
            } else {
                kuser.setPower(kuser.getPower() + 1);
            }
            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "Your power is now " + ChatColor.GOLD + user.getPower() + ChatColor.GRAY + "."));
        } else {
            if (user.getPower() == 0) {
                user.setPower(0);
            } else {
                user.setPower(user.getPower() - 1);
            }
            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "Your power is now " + ChatColor.GOLD + user.getPower() + ChatColor.GRAY + "."));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            User user = NEGuilds.getUserManager().getUser(damaged.getUniqueId());
            Player damager = (Player) event.getDamager();
            User duser = NEGuilds.getUserManager().getUser(damager.getUniqueId());

            boolean playerInGuild = true, damagerInGuild = true;
            if (user.getGuild() == null) playerInGuild = false;
            if (duser.getGuild() == null) damagerInGuild = false;
            if (playerInGuild) {
                if (damagerInGuild) {
                    if (user.getGuild() == duser.getGuild()) {
                        damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt other members of your guild!"));
                        event.setCancelled(true);
                        event.setDamage(0.0);
                    } else if (user.getGuild().isAlly(duser.getGuild())) {
                        damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players you're allied to!"));
                        event.setCancelled(true);
                        event.setDamage(0.0);
                    } else if (user.getGuild().isTruce(duser.getGuild())) {
                        damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players you're in a truce with!"));
                        event.setCancelled(true);
                        event.setDamage(0.0);
                    } else if (user.getGuild().isNeutral(duser.getGuild())) {
                        if (user.getGuild().getTerritory().contains(damaged.getLocation().getChunk())) {
                            damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players in their own territory while neutral!"));
                            event.setCancelled(true);
                            event.setDamage(0.0);
                        }
                    }
                } else {
                    if (user.getGuild().getTerritory().contains(damaged.getLocation().getChunk())) {
                        damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players in their own territory!"));
                        event.setCancelled(true);
                        event.setDamage(0.0);
                    }
                }
            } else {

            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Player damaged = (Player) event.getEntity();
            User user = NEGuilds.getUserManager().getUser(damaged.getUniqueId());
            Projectile damager = (Projectile) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                Player damagerr = (Player) damager.getShooter();
                User duser = NEGuilds.getUserManager().getUser(damagerr.getUniqueId());

                boolean playerInGuild = true, damagerInGuild = true;
                if (user.getGuild() == null) playerInGuild = false;
                if (duser.getGuild() == null) damagerInGuild = false;
                if (playerInGuild) {
                    if (damagerInGuild) {
                        if (user.getGuild() == duser.getGuild()) {
                            damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt other members of your guild!"));
                            event.setCancelled(true);
                            event.setDamage(0.0);
                        } else if (user.getGuild().isAlly(duser.getGuild())) {
                            damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players you're allied to!"));
                            event.setCancelled(true);
                            event.setDamage(0.0);
                        } else if (user.getGuild().isTruce(duser.getGuild())) {
                            damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players you're in a truce with!"));
                            event.setCancelled(true);
                            event.setDamage(0.0);
                        } else if (user.getGuild().isNeutral(duser.getGuild())) {
                            if (user.getGuild().getTerritory().contains(damaged.getLocation().getChunk())) {
                                damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players in their own territory while neutral!"));
                                event.setCancelled(true);
                                event.setDamage(0.0);
                            }
                        }
                    } else {
                        if (user.getGuild().getTerritory().contains(damaged.getLocation().getChunk())) {
                            damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to hurt players in their own territory!"));
                            event.setCancelled(true);
                            event.setDamage(0.0);
                        }
                    }
                } else {

                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        for (GuildModel guild : NEGuilds.getGuildRegistry().getRegistered()) {
            if (guild.getTerritory().contains(chunk)) {
                if (user.getGuild() != guild) {
                    if (user.getGuild().isEnemy(guild) || guild.isEnemy(user.getGuild())) {
                        if (event.getMessage().startsWith("/home") || event.getMessage().startsWith("/spawn") || event.getMessage().startsWith("/tpa") || event.getMessage().startsWith("/tpaccept")
                                || event.getMessage().startsWith("/warp")) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not allowed to teleport inside of enemy territory."));
                            return;
                        }
                    }
                }
            }
        }
    }
}
