package net.nighthawkempires.guilds.listener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.events.UserDeathEvent;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.utils.ChunkUtil;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.user.UserModel;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

public class GuildListener implements Listener {

    private ConcurrentMap<Player, GuildModel> locationMap = Maps.newConcurrentMap();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();
        if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (isInteractiveBlock(event.getClickedBlock().getType().name())) {
                Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
                if (opGuild.isPresent()) {
                    GuildModel guild = opGuild.get();
                    if (!NEGuilds.getGuildTempData().adminBypass.contains(player.getUniqueId())) {
                        if (!user.getGuild().isPresent()) {
                            event.setCancelled(true);
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                    ChatColor.RED + "You're not allowed to interact inside of " +
                                            guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                            "territory."));
                            return;
                        }

                        GuildModel guildModel = user.getGuild().get();
                        if (!guildModel.getUUID().toString().equals(guild.getUUID().toString())) {
                            if (!guildModel.isAlly(guild)) {
                                event.setCancelled(true);
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                        ChatColor.RED + "You're not allowed to interact inside of " +
                                                guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                                "territory."));
                            }
                        }
                    }
                }
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (player.getInventory().getItemInMainHand().getType() == Material.ARMOR_STAND ||
                    player.getInventory().getItemInOffHand().getType() == Material.ARMOR_STAND ||
                    player.getInventory().getItemInMainHand().getType() == Material.ITEM_FRAME ||
                    player.getInventory().getItemInOffHand().getType() == Material.ITEM_FRAME) {
                if (notAllowedBuild(chunk, player, user)) {
                    event.setCancelled(true);
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
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();
        if (isInteractiveEntity(event.getRightClicked().getType().name())) {
            Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
            if (opGuild.isPresent()) {
                GuildModel guild = opGuild.get();
                if (!NEGuilds.getGuildTempData().adminBypass.contains(player.getUniqueId())) {
                    if (!user.getGuild().isPresent()) {
                        event.setCancelled(true);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                ChatColor.RED + "You're not allowed to interact inside of " +
                                        guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                        "territory."));
                        return;
                    }

                    GuildModel guildModel = user.getGuild().get();
                    if (!guildModel.getUUID().toString().equals(guild.getUUID().toString())) {
                        if (!guildModel.isAlly(guild)) {
                            event.setCancelled(true);
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                    ChatColor.RED + "You're not allowed to interact inside of " +
                                            guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                            "territory."));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();

        boolean inTerritory = false;
        Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
        if (opGuild.isPresent()) {
            GuildModel guild = opGuild.get();
            if (!locationMap.containsKey(player) || locationMap.get(player) != guild) {
                locationMap.put(player, guild);
                NECore.getCodeHandler().sendTitleToPlayer(player, guild.getColor() + guild.getName(),
                        ChatColor.GRAY + guild.getDescription(), 10, 30, 10);
            }
            inTerritory = true;
        }

        if (!inTerritory) {
            locationMap.remove(player);
        }
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        if (notAllowedBuild(chunk, player, user)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        if (notAllowedBuild(chunk, player, user)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(UserDeathEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        if (event.getEntityKiller() != null && event.getEntityKiller() instanceof Player) {
            Player killer = (Player) event.getEntityKiller();
            UserModel kuser = NEGuilds.getUserRegistry().getUser(killer.getUniqueId());

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
            player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                    ChatColor.GRAY + "Your power is now " + ChatColor.GOLD + user.getPower() + ChatColor.GRAY + "."));
        } else {
            if (user.getPower() == 0) {
                user.setPower(0);
            } else {
                user.setPower(user.getPower() - 1);
            }
            player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                    ChatColor.GRAY + "Your power is now " + ChatColor.GOLD + user.getPower() + ChatColor.GRAY + "."));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            UserModel user = NEGuilds.getUserRegistry().getUser(damaged.getUniqueId());
            Player damager = (Player) event.getDamager();
            UserModel duser = NEGuilds.getUserRegistry().getUser(damager.getUniqueId());

            if (notAllowedHurt(damaged, user.getGuild(), damager, duser.getGuild())) {
                event.setCancelled(true);
                event.setDamage(0.0);
            }
        } else if (event.getEntity() instanceof Player && event.getDamager() instanceof Projectile) {
            Player damaged = (Player) event.getEntity();
            UserModel user = NEGuilds.getUserRegistry().getUser(damaged.getUniqueId());
            Projectile damager = (Projectile) event.getDamager();
            if (damager.getShooter() instanceof Player) {
                Player damagerr = (Player) damager.getShooter();
                UserModel duser = NEGuilds.getUserRegistry().getUser(damagerr.getUniqueId());

                if (notAllowedHurt(damaged, user.getGuild(), damager, duser.getGuild())) {
                    event.setCancelled(true);
                    event.setDamage(0.0);
                }
            }
        }

        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

            Chunk chunk = player.getLocation().getChunk();
            if (isInteractiveEntity(event.getEntity().getType().name())) {
                Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
                if (opGuild.isPresent()) {
                    GuildModel guild = opGuild.get();
                    if (!NEGuilds.getGuildTempData().adminBypass.contains(player.getUniqueId())) {
                        if (!user.getGuild().isPresent()) {
                            event.setCancelled(true);
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                    ChatColor.RED + "You're not allowed to interact inside of " +
                                            guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                            "territory."));
                            return;
                        }

                        GuildModel guildModel = user.getGuild().get();
                        if (!guildModel.getUUID().toString().equals(guild.getUUID().toString())) {
                            if (!guildModel.isAlly(guild)) {
                                event.setCancelled(true);
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                        ChatColor.RED + "You're not allowed to interact inside of " +
                                                guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                                "territory."));
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onManipulate(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        Chunk chunk = player.getLocation().getChunk();
        if (isInteractiveEntity(event.getRightClicked().getType().name())) {
            Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
            if (opGuild.isPresent()) {
                GuildModel guild = opGuild.get();
                if (!NEGuilds.getGuildTempData().adminBypass.contains(player.getUniqueId())) {
                    if (!user.getGuild().isPresent()) {
                        event.setCancelled(true);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                ChatColor.RED + "You're not allowed to interact inside of " +
                                        guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                        "territory."));
                        return;
                    }

                    GuildModel guildModel = user.getGuild().get();
                    if (guildModel.isAlly(guild)) {
                        event.setCancelled(true);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                ChatColor.RED + "You're not allowed to interact inside of " +
                                        guild.getColor() + guild.getName() + "'s " + ChatColor.RED +
                                        "territory."));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();

        if (user.getGuild().isPresent()) {
            Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
            if (opGuild.isPresent() && !user.getGuild().get().getUUID().equals(opGuild.get().getUUID())) {
                GuildModel guild = opGuild.get();
                if (user.getGuild().get().isEnemy(guild) || guild.isEnemy(user.getGuild().get())) {
                    if (event.getMessage().startsWith("/home") || event.getMessage().startsWith("/spawn") ||
                            event.getMessage().startsWith("/tpa") || event.getMessage().startsWith("/tpaccept")
                            || event.getMessage().startsWith("/warp")) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                                ChatColor.RED + "You're not allowed to teleport inside of enemy territory."));
                    }
                }
            }
        }
    }

    private boolean notAllowedBuild(Chunk chunk, Player player, UserModel user) {
        Optional<GuildModel> opGuild = NEGuilds.getGuildRegistry().getGuild(chunk);
        if (opGuild.isPresent()) {
            if (!NEGuilds.getGuildTempData().adminBypass.contains(player.getUniqueId())) {
                GuildModel guild = opGuild.get();
                Optional<GuildModel> opUserGuild = user.getGuild();
                if (!opUserGuild.isPresent()) {
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() +
                                    guild.getName() + "'s " + ChatColor.RED + "territory."));
                    return true;
                }

                if (!opUserGuild.get().getUUID().toString().equals(guild.getUUID().toString())) {
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to build inside of " + guild.getColor() +
                                    guild.getName() + "'s " + ChatColor.RED + "territory."));
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private boolean notAllowedHurt(Entity damaged, Optional<GuildModel> opUserGuild, Entity damager,
                                   Optional<GuildModel> opDamagerGuild) {
        if (opUserGuild.isPresent()) {
            GuildModel userGuild = opUserGuild.get();
            if (opDamagerGuild.isPresent()) {
                GuildModel damagerGuild = opDamagerGuild.get();
                if (userGuild.getUUID().equals(damagerGuild.getUUID())) {
                    damager.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to hurt other members of your guild!"));
                    return true;
                } else if (userGuild.isAlly(damagerGuild)) {
                    damager.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to hurt players you're allied to!"));
                    return true;
                } else if (userGuild.isTruce(damagerGuild)) {
                    damager.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to hurt players you're in a truce with!"));
                    return true;
                } else if (userGuild.isNeutral(damagerGuild)) {
                    if (userGuild.getTerritory().contains(ChunkUtil.getChunkString(damaged.getLocation().getChunk()))) {
                        damager.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED +
                                "You're not allowed to hurt players in their own territory while neutral!"));
                        return true;
                    }
                }
            } else {
                if (userGuild.getTerritory().contains(ChunkUtil.getChunkString(damaged.getLocation().getChunk()))) {
                    damager.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.RED + "You're not allowed to hurt players in their own territory!"));
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isInteractiveBlock(String string) {
        List<String> keywords =
                Lists.newArrayList("door", "chest", "bed", "gate", "lever", "button", "diode", "comparator", "minecart",
                        "boat", "anvil", "workbench", "furnace", "brewing", "jukebox", "note", "shulker_box", "hopper",
                        "dispenser", "dropper", "enchantment");
        for (String words : keywords) {
            if (string.toLowerCase().contains(words.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private boolean isInteractiveEntity(String string) {
        List<String> keywords =
                Lists.newArrayList("stand", "frame");
        for (String words : keywords) {
            if (string.toLowerCase().contains(words.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
