package net.nighthawkempires.guilds.command;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.utils.MathUtil;
import net.nighthawkempires.essentials.NEEssentials;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.guild.rank.RankType;
import net.nighthawkempires.guilds.guild.relation.RelationType;
import net.nighthawkempires.guilds.user.User;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class GuildCommand implements CommandExecutor {

    private String[] adminHelp1, adminHelp2, adminHelp3, help1, help2, help3;

    public GuildCommand() {
        adminHelp1 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "1" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "claim", "Claim the chunk you're in"),//
                Lang.CMD_HELP.getCommand("g", "create [name]", "Create a guild"),
                Lang.CMD_HELP.getCommand("g", "help <page>", "Show guild help"),//
                Lang.CMD_HELP.getCommand("g", "join [guild]", "Join a guild"),
                Lang.CMD_HELP.getCommand("g", "invite [player]", "Invite a player to the guild"),
                Lang.CMD_HELP.getCommand("g", "show|info <guild>", "Show a guilds info"),//
                Lang.CMD_HELP.getCommand("g", "kick [player]", "Kick a player from the guild"),
                Lang.CMD_HELP.getCommand("g", "list", "Show a list of guilds"),//
                Lang.CMD_HELP.getCommand("g", "name [name] <guild>", "Change the name of a guild"),
                Lang.CMD_HELP.getCommand("g", "sethome", "Set the home for the guild"),//
                Lang.FOOTER.getMessage()
        };
        adminHelp2 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "2" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "disband <guild>", "Disband a guild"),//
                Lang.CMD_HELP.getCommand("g", "color", "Open Guild Color Shop"),//
                Lang.CMD_HELP.getCommand("g", "home <guild>", "Teleport to a guild's home"),//
                Lang.CMD_HELP.getCommand("g", "ally [guild]", "Ally another guild"),
                Lang.CMD_HELP.getCommand("g", "truce [guild]", "Truce another guild"),
                Lang.CMD_HELP.getCommand("g", "neutral [guild}", "Neutral another guild"),
                Lang.CMD_HELP.getCommand("g", "enemy [guild]", "Enemy another guild"),
                Lang.CMD_HELP.getCommand("g", "unclaim <all>", "Unclaim territory"),//
                Lang.CMD_HELP.getCommand("g", "desc [desc]", "Set the description for the guild"),//
                Lang.CMD_HELP.getCommand("g", "leave", "Leave a guild"),//
                Lang.FOOTER.getMessage(),
        };
        adminHelp3 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "3" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "leader [player]", "Give a member guild leadership"),
                Lang.CMD_HELP.getCommand("g", "promote <player>", "Promote a player in your guild"),
                Lang.CMD_HELP.getCommand("g", "demote <player>", "Demote a player in your guild"),
                Lang.CMD_HELP.getCommand("g", "rank [player] [rank]", "Set the guild rank of a member"),
                Lang.CMD_HELP.getCommand("g", "admin", "Enable Admin bypass mode"),//
                Lang.FOOTER.getMessage(),
        };
        help1 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "1" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "claim", "Claim the chunk you're in"),
                Lang.CMD_HELP.getCommand("g", "create [name]", "Create a guild"),
                Lang.CMD_HELP.getCommand("g", "help <page>", "Show guild help"),
                Lang.CMD_HELP.getCommand("g", "join [guild]", "Join a guild"),
                Lang.CMD_HELP.getCommand("g", "invite [player]", "Invite a player to the guild"),
                Lang.CMD_HELP.getCommand("g", "show|info <guild>", "Show a guilds info"),
                Lang.CMD_HELP.getCommand("g", "kick [player]", "Kick a player from the guild"),
                Lang.CMD_HELP.getCommand("g", "list", "Show a list of guilds"),
                Lang.CMD_HELP.getCommand("g", "name [name]", "Change the name of a guild"),
                Lang.CMD_HELP.getCommand("g", "sethome", "Set the home for the guild"),
                Lang.FOOTER.getMessage()
        };
        help2 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "2" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "disband", "Disband a guild"),
                Lang.CMD_HELP.getCommand("g", "color", "Open Guild Color Shop"),
                Lang.CMD_HELP.getCommand("g", "home", "Teleport to a guild's home"),
                Lang.CMD_HELP.getCommand("g", "ally [guild]", "Ally another guild"),
                Lang.CMD_HELP.getCommand("g", "truce [guild]", "Truce another guild"),
                Lang.CMD_HELP.getCommand("g", "neutral [guild}", "Neutral another guild"),
                Lang.CMD_HELP.getCommand("g", "enemy [guild]", "Enemy another guild"),
                Lang.CMD_HELP.getCommand("g", "unclaim <all>", "Unclaim territory"),
                Lang.CMD_HELP.getCommand("g", "desc [desc]", "Set the description for the guild"),
                Lang.CMD_HELP.getCommand("g", "leave", "Leave a guild"),
                Lang.FOOTER.getMessage(),
        };
        help3 = new String[]{
                Lang.HEADER.getServerHeader(),
                Lang.CMD_NAME.getCommandName("Guilds" + ChatColor.DARK_GRAY + " - Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + "3" + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + "3"),
                Lang.FOOTER.getMessage(),
                Lang.CMD_HELP.getCommand("g", "leader [player]", "Give a member guild leadership"),
                Lang.CMD_HELP.getCommand("g", "promote <player>", "Promote a player in your guild"),
                Lang.CMD_HELP.getCommand("g", "demote <player>", "Demote a player in your guild"),
                Lang.FOOTER.getMessage(),
        };
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = NEGuilds.getUserManager().getUser(player.getUniqueId());

            if (!sender.hasPermission("ne.guilds")) {
                player.sendMessage(Lang.NO_PERM.getServerMessage());
                return true;
            }

            boolean inGuild = true;
            if (user.getGuild() == null) {
                inGuild = false;
            }
            GuildModel guild = user.getGuild();
            RankType rank = user.getType();
            if (args.length == 0) {
                if (sender.hasPermission("ne.admin")) {
                    player.sendMessage(adminHelp1);
                    return true;
                }
                player.sendMessage(help1);
                return true;
            } else if (args.length == 1) {
                if (args[0].toLowerCase().equals("claim")) {
                    if (!inGuild || rank == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.OFFICER && rank != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    Chunk chunk = player.getLocation().getChunk();
                    if (guild.getTerritory().contains(chunk)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "Your guild already owns this land!"));
                        return true;
                    }

                    if (MathUtil.greaterThanEqualTo(user.getGuild().getTerritory().size(), user.getGuild().getMembers().size() * 5)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You do not have enough Land Power to claim this!"));
                        return true;
                    }

                    for (GuildModel guilds : NEGuilds.getGuildRegistry().getRegistered()) {
                        if (guilds.getTerritory().contains(chunk)) {
                            int power = 0;
                            for (UUID uuid : user.getGuild().getMembers()) {
                                User temp = NEGuilds.getUserManager().getTempUser(uuid);
                                power = power + temp.getPower();
                            }
                            power = power / user.getGuild().getMembers().size();

                            int powerRival = 0;
                            for (UUID uuid : guild.getMembers()) {
                                User temp = NEGuilds.getUserManager().getTempUser(uuid);
                                powerRival = powerRival + temp.getPower();
                            }
                            powerRival = powerRival / guild.getMembers().size();

                            if (MathUtil.greaterThan(power, (int) Math.ceil((double) powerRival * 2.5))) {
                                guilds.removeTerritory(chunk);
                                for (Player players : Bukkit.getOnlinePlayers()) {
                                    if (guilds.getMembers().contains(players.getUniqueId())) {
                                        players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " claimed over your territory at " + ChatColor.DARK_GRAY + "["
                                                + ChatColor.GOLD + chunk.getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + chunk.getZ() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "."));
                                    }
                                }
                                if (guilds.getHome().getChunk() == chunk) {
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        if (guilds.getMembers().contains(players.getUniqueId())) {
                                            players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The home of your guild has been unset due to the territory it was overclaimed by " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "This land is already owned by " + guilds.getColor() + guilds.getName() + ChatColor.RED + "."));
                                return true;
                            }
                        }
                    }

                    guild.addTerritory(chunk);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have claimed land at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + chunk.getX() + ChatColor.DARK_GRAY
                            + ", " + ChatColor.GOLD + chunk.getZ() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " for your guild."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(player.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " claimed land at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + chunk.getX() + ChatColor.DARK_GRAY
                                        + ", " + ChatColor.GOLD + chunk.getZ() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " for your guild."));
                            }
                        }
                    }

                } else if (args[0].toLowerCase().equals("help")) {
                    if (sender.hasPermission("ne.admin")) {
                        player.sendMessage(adminHelp1);
                        return true;
                    }
                    player.sendMessage(help1);
                    return true;
                } else if (args[0].toLowerCase().equals("show") || args[0].toLowerCase().equals("info")) {
                    if (user.getGuild() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in a guild!"));
                        return true;
                    }

                    int size = user.getGuild().getMembers().size();
                    int power = 0;
                    for (UUID uuid : user.getGuild().getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        power = power + temp.getPower();
                    }
                    int maxpower = size * 10;

                    StringBuilder builder = new StringBuilder();
                    if (!user.getGuild().getRelations().keySet().isEmpty()) {
                        for (UUID guildId : user.getGuild().getRelations().keySet()) {
                            GuildModel guilds = NEGuilds.getGuildRegistry().getGuild(guildId);
                            if (user.getGuild().isAlly(guilds)) {
                                builder.append(ChatColor.BLUE).append(guilds.getName()).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        }
                    } else {
                        builder.append(ChatColor.RED).append("This guild does not have any allies...");
                    }
                    if (builder.length() == 0) {
                        builder.append(ChatColor.RED).append("This guild does not have any allies...");
                    }
                    String allies = builder.substring(0, builder.length() - 2);
                    builder = new StringBuilder();
                    List<GuildModel> enemiee = Lists.newArrayList();
                    if (!user.getGuild().getRelations().keySet().isEmpty()) {
                        for (UUID guildId : user.getGuild().getRelations().keySet()) {
                            GuildModel guilds = NEGuilds.getGuildRegistry().getGuild(guildId);
                            if (user.getGuild().isEnemy(guilds)) {
                                builder.append(ChatColor.RED).append(guilds.getName()).append(ChatColor.DARK_GRAY).append(", ");
                                enemiee.add(guilds);
                            }
                        }

                        for (GuildModel guilds : NEGuilds.getGuildRegistry().getRegistered()) {
                            if (user.getGuild().isEnemy(guilds)) {
                                if (!enemiee.contains(guilds))
                                    builder.append(ChatColor.RED).append(guilds.getName()).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        }
                    } else {
                        builder.append(ChatColor.RED).append("This guild does not have any enemies...");
                    }
                    if (builder.length() == 0) {
                        builder.append(ChatColor.RED).append("This guild does not have any enemies...");
                    }
                    String enemies = builder.substring(0, builder.length() - 2);
                    builder = new StringBuilder();
                    String prefix;
                    ChatColor color;
                    for (UUID uuid : user.getGuild().getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.LEADER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else color = ChatColor.RED;

                            prefix = "★★ ";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : user.getGuild().getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.OFFICER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else color = ChatColor.RED;

                            prefix = "★ ";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : user.getGuild().getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.MEMBER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else color = ChatColor.RED;

                            prefix = "";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : user.getGuild().getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.RECRUIT) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else color = ChatColor.RED;

                            prefix = "";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    String members = builder.substring(0, builder.length() - 2);

                    String[] info = new String[]{
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(user.getGuild().getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + user.getGuild().getName(),
                            ChatColor.DARK_GRAY + "Description" + ChatColor.GRAY + ": " + ChatColor.GREEN + user.getGuild().getDescription(),
                            ChatColor.DARK_GRAY + "Color" + ChatColor.GRAY + ": " + user.getGuild().getColor() + user.getGuild().getColor().name(),
                            ChatColor.DARK_GRAY + "Size" + ChatColor.GRAY + ": " + ChatColor.GOLD + user.getGuild().getMembers().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + 15,
                            ChatColor.DARK_GRAY + "Power" + ChatColor.GRAY + ": " + ChatColor.GOLD + power + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + maxpower,
                            ChatColor.DARK_GRAY + "Land Power" + ChatColor.GRAY + ": " + ChatColor.GOLD + user.getGuild().getTerritory().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + user.getGuild().getMembers().size() * 5,
                            ChatColor.DARK_GRAY + "Allies" + ChatColor.GRAY + ": " + ChatColor.BLUE + "" + allies,
                            ChatColor.DARK_GRAY + "Enemies" + ChatColor.GRAY + ": " + ChatColor.RED + "" + enemies,
                            ChatColor.DARK_GRAY + "Members" + ChatColor.GRAY + ": " + ChatColor.RED + "" + members,
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(info);
                } else if (args[0].toLowerCase().equals("home")) {
                    if (!inGuild) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (guild.getHome() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "Your guild does not currently have a home set!"));
                        return true;
                    }

                    if (!player.hasPermission("ne.no.cmdwarmup")) {
                        NEEssentials.getData().dontMove.add(player.getUniqueId());
                        player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "Executing " + ChatColor.AQUA + "/g" + ChatColor.DARK_AQUA + " home" + ChatColor.GRAY + " in "
                                + ChatColor.GOLD + "4 seconds.");
                        Bukkit.getScheduler().scheduleSyncDelayedTask(NEEssentials.getPlugin(), () -> {
                            if (NEEssentials.getData().dontMove.contains(player.getUniqueId())) {
                                NEEssentials.getData().lastLocation.put(player.getUniqueId(), player.getLocation());
                                player.teleport(user.getGuild().getHome());
                                player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have teleported to your guild's home!");
                                NEEssentials.getData().dontMove.remove(player.getUniqueId());
                            }
                        }, 80L);
                        return true;
                    }
                    NEEssentials.getData().lastLocation.put(player.getUniqueId(), player.getLocation());
                    player.teleport(user.getGuild().getHome());
                    player.sendMessage(Lang.CHAT_TAG.getServerChatTag() + ChatColor.GRAY + "You have teleported to your guild's home!");
                } else if (args[0].toLowerCase().equals("sethome")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.OFFICER && rank != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    Chunk chunk = player.getLocation().getChunk();
                    if (!guild.getTerritory().contains(chunk)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be in your own territory to set the home of the guild!"));
                        return true;
                    }

                    guild.setHome(player.getLocation());
                    int x = player.getLocation().getBlockX(), y = player.getLocation().getBlockY(), z = player.getLocation().getBlockZ();
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have set the home of the guild at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + x + ChatColor.DARK_GRAY + ", "
                            + ChatColor.GOLD + y + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + z + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " set the home of the guild at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD
                                        + x + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + y + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + z + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("leave")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (guild.getMembers().size() == 1 && guild.getMembers().contains(player.getUniqueId())) {
                        for (UUID uuid : guild.getMembers()) {
                            User temp = NEGuilds.getUserManager().getTempUser(uuid);
                            temp.setType(null);
                            temp.setGuild(null);

                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (players.getUniqueId().toString().equals(uuid.toString())) {
                                    if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                        players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has disbanded the guild you were in!"));
                                        return true;
                                    }
                                }
                            }

                            user.setType(null);
                            user.setGuild(null);
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The guild you were in has been disbanded since you were the last member of the guild.."));
                            NEGuilds.getGuildRegistry().deleteGuild(guild.getUUID());
                        }
                        return true;
                    }

                    if (rank == RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "In order to leave the guild, you must pass leadership onto another member of the guild.  If you wish to disband the" +
                                " guild use /guild disband."));
                        return true;
                    }

                    guild.removeMember(player.getUniqueId());
                    user.setGuild(null);
                    user.setType(null);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have left " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has left the guild!"));
                                return true;
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("unclaim")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    Chunk chunk = player.getLocation().getChunk();
                    if (!guild.getTerritory().contains(chunk)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "Your guild does not currently own this territory!"));
                        return true;
                    }

                    if (guild.getHome() != null) {
                        if (guild.getHome().getChunk() == chunk) {
                            guild.setHome(null);
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (guild.getMembers().contains(players.getUniqueId())) {
                                    if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The home of the guild was removed due to the territory it was in was unclaimed!"));
                                    }
                                }
                            }
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The home of the guild was removed due to the territory it was in was unclaimed!"));
                        }
                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has unclaimed territory at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD
                                        + chunk.getX() + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + chunk.getZ() + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + "."));
                            }
                        }
                    }
                    guild.removeTerritory(chunk);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have unclaimed territory at " + ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + chunk.getX()
                            + ChatColor.DARK_GRAY + ", " + ChatColor.GOLD + chunk.getZ() + ChatColor.DARK_GRAY + "}" + ChatColor.GRAY + ".")));
                } else if (args[0].toLowerCase().equals("color")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have opened Guild Color Shop."));
                    player.openInventory(NEGuilds.getInventoryListener().inventoryGuildColor(player));
                } else if (args[0].toLowerCase().equals("admin")) {
                    if (!player.hasPermission("ne.admin")) {
                        player.sendMessage(Lang.NO_PERM.getServerMessage());
                        return true;
                    }

                    if (!NEGuilds.getGuildData().adminBypass.contains(player.getUniqueId())) {
                        NEGuilds.getGuildData().adminBypass.add(player.getUniqueId());
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have " + ChatColor.GREEN + "" + ChatColor.UNDERLINE + "enabled" + ChatColor.GRAY + " Admin bypass."));
                        return true;
                    } else {
                        NEGuilds.getGuildData().adminBypass.remove(player.getUniqueId());
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have " + ChatColor.RED + "" + ChatColor.UNDERLINE + "disabled" + ChatColor.GRAY + " Admin bypass."));
                        return true;
                    }
                } else if (args[0].toLowerCase().equals("list")) {
                    sendPage(player, 1);
                } else if (args[0].toLowerCase().equals("disband")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be the leader of the guild to do this!"));
                        return true;
                    }

                    for (UUID uuid : guild.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        temp.setType(null);
                        temp.setGuild(null);
                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has disbanded the guild you were in!"));
                            }
                        }
                    }
                    user.setType(null);
                    user.setGuild(null);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You disbanded the guild you were in!"));
                    NEGuilds.getGuildRegistry().deleteGuild(guild.getUUID());
                } else if (args[0].toLowerCase().equals("desc") || args[0].toLowerCase().equals("description")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (guild.getDescription().equals("") || guild.getDescription().equals(null)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "Your guild does not currently have a description set"));
                        return true;
                    }

                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The description of your guild is: " + guild.getDescription() + "."));
                } else {
                    player.sendMessage(Lang.SYNTAX_ERROR.getServerMessage());
                    return true;
                }
            } else if (args.length == 2) {
                //CREATE, HELP, JOIN, INVITE, SHOW, KICK, NAME, DISBAND, HOME, ALLY, TRUCE, NEUTRAL, ENEMY, UNCLAIM, DESC, LEADER, PROMOTE, DEMOTE
                if (args[0].toLowerCase().equals("create")) {
                    if (inGuild) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must leave your current guild first in order to do this!"));
                        return true;
                    }

                    String name = args[1];
                    if (name.length() > 15) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That name is too long!"));
                        return true;
                    }

                    if (NEGuilds.getGuildRegistry().containsBannedWord(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That name contains a banned word!"));
                        return true;
                    }

                    if (NEGuilds.getGuildRegistry().guildExists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "A guild with that name already exists!"));
                        return true;
                    }

                    UUID uuid = NEGuilds.getGuildRegistry().createGuild(name, player.getUniqueId());
                    user.setGuild(NEGuilds.getGuildRegistry().getGuild(uuid));
                    user.setType(RankType.LEADER);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have created a guild by the name of " + user.getGuild().getColor() + user.getGuild().getName() + ChatColor.GRAY + "."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has crated a guild by the name of "
                                + user.getGuild().getColor() + user.getGuild().getName() + ChatColor.GRAY + "."));
                    }
                } else if (args[0].toLowerCase().equals("help")) {
                    String preInt = args[1];
                    if (!NumberUtils.isDigits(preInt)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That is not a valid number!"));
                        return true;
                    }

                    int page = Integer.parseInt(preInt);
                    if (page < 1) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "The page number must be at most 1-3!"));
                        return true;
                    } else if (page > 3) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "The page number must be at most 1-3!"));
                        return true;
                    }

                    if (page == 1) {
                        if (player.hasPermission("ne.admin")) {
                            player.sendMessage(adminHelp1);
                            return true;
                        }
                        player.sendMessage(help1);
                    } else if (page == 2) {
                        if (player.hasPermission("ne.admin")) {
                            player.sendMessage(adminHelp2);
                            return true;
                        }
                        player.sendMessage(help2);
                    } else if (page == 3) {
                        if (player.hasPermission("ne.admin")) {
                            player.sendMessage(adminHelp3);
                            return true;
                        }
                        player.sendMessage(help3);
                    }
                } else if (args[0].toLowerCase().equals("join")) {
                    if (inGuild) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must leave your current guild first in order to do this!"));
                        return true;
                    }
                    String name = args[1];
                    if (!NEGuilds.getGuildRegistry().guildExists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "A guild with that name does not exists!"));
                        return true;
                    }

                    GuildModel guildd = NEGuilds.getGuildRegistry().getGuild(name);
                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist!"));
                        return true;
                    }

                    if (!guildd.getInvites().contains(player.getUniqueId())) {
                        if (!player.hasPermission("ne.admin")) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not invited to that guild!"));
                            for (Player players : Bukkit.getOnlinePlayers()) {
                                if (guildd.getMembers().contains(players.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " tried to join the guild."));
                                }
                            }
                        }
                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guildd.getMembers().contains(players.getUniqueId())) {
                            players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has joined the guild."));
                        }
                    }
                    guildd.addMember(player.getUniqueId());
                    user.setGuild(guildd);
                    user.setType(RankType.RECRUIT);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have joined " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                    if (guildd.getInvites().contains(player.getUniqueId())) {
                        guildd.removeInvite(player.getUniqueId());
                    }
                } else if (args[0].toLowerCase().equals("invite") || args[0].toLowerCase().equals("inv")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.OFFICER && rank != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    OfflinePlayer invitee = Bukkit.getOfflinePlayer(args[1]);

                    if (guild.getMembers().contains(invitee.getUniqueId())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is already in your guild!"));
                        return true;
                    }

                    if (!guild.getInvites().contains(invitee.getUniqueId())) {
                        guild.addInvite(invitee.getUniqueId());
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have invited " + ChatColor.BLUE + invitee.getName() + ChatColor.GRAY + " to the guild!"));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has invited " + ChatColor.BLUE + invitee.getName()
                                            + ChatColor.GRAY + " to the guild!"));
                                }
                            } else {
                                if (invitee.getUniqueId().toString().equals(players.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has invited you to " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    } else {
                        guild.removeInvite(invitee.getUniqueId());
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have revoked " + ChatColor.BLUE + invitee.getName() + "'s " + ChatColor.GRAY + "invitation to the guild!"));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has revoked " + ChatColor.BLUE + invitee + "'s "
                                            + ChatColor.GRAY + "invitation to the guild!"));
                                }
                            } else {
                                if (invitee.getUniqueId().toString().equals(players.getUniqueId().toString())) {
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has revoked your invitation to " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("show") || args[0].toLowerCase().equals("info")) {
                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }


                    int size = guildd.getMembers().size();
                    int power = 0;
                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        power = power + temp.getPower();
                    }
                    int maxpower = size * 10;

                    StringBuilder builder = new StringBuilder();
                    if (!guildd.getRelations().keySet().isEmpty()) {
                        for (UUID guildsId : guildd.getRelations().keySet()) {
                            GuildModel guilds = NEGuilds.getGuildRegistry().getGuild(guildsId);
                            if (guildd.isAlly(guilds)) {
                                builder.append(ChatColor.BLUE).append(guilds.getName()).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        }
                    } else {
                        builder.append(ChatColor.RED).append("This guild does not have any allies...");
                    }
                    if (builder.length() == 0) {
                        builder.append(ChatColor.RED).append("This guild does not have any allies...");
                    }
                    String allies = builder.substring(0, builder.length() - 2);
                    builder = new StringBuilder();
                    List<GuildModel> enemiee = Lists.newArrayList();
                    if (!guildd.getRelations().keySet().isEmpty()) {
                        for (UUID guild1Id : guildd.getRelations().keySet()) {
                            GuildModel guild1 = NEGuilds.getGuildRegistry().getGuild(guild1Id);
                            if (guildd.isEnemy(guild1)) {
                                enemiee.add(guild1);
                                builder.append(ChatColor.RED).append(guild1.getName()).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        }

                        for (GuildModel guilds : NEGuilds.getGuildRegistry().getRegistered()) {
                            if (guildd.isEnemy(guilds)) {
                                if (!enemiee.contains(guilds))
                                    builder.append(ChatColor.RED).append(guilds.getName()).append(ChatColor.DARK_GRAY).append(", ");
                            }
                        }
                    } else {
                        builder.append(ChatColor.RED).append("This guild does not have any enemies...");
                    }
                    if (builder.length() == 0) {
                        builder.append(ChatColor.RED).append("This guild does not have any enemies...");
                    }
                    String enemies = builder.substring(0, builder.length() - 2);
                    builder = new StringBuilder();
                    String prefix;
                    ChatColor color;
                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.LEADER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else {
                                color = ChatColor.RED;
                            }

                            prefix = "★★ ";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.OFFICER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else {
                                color = ChatColor.RED;
                            }

                            prefix = "★ ";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.MEMBER) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else {
                                color = ChatColor.RED;
                            }

                            prefix = "";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        if (temp.getType() == RankType.RECRUIT) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                color = ChatColor.GREEN;
                            } else {
                                color = ChatColor.RED;
                            }

                            prefix = "";
                            builder.append(color).append(prefix).append(temp.getName()).append(ChatColor.DARK_GRAY).append(", ");
                        }
                    }
                    String members = builder.substring(0, builder.length() - 2);

                    String[] info = new String[]{
                            Lang.HEADER.getServerHeader(),
                            Lang.INFO.getInfo(guildd.getName()),
                            Lang.FOOTER.getMessage(),
                            ChatColor.DARK_GRAY + "Name" + ChatColor.GRAY + ": " + guildd.getName(),
                            ChatColor.DARK_GRAY + "Description" + ChatColor.GRAY + ": " + ChatColor.GREEN + guildd.getDescription(),
                            ChatColor.DARK_GRAY + "Color" + ChatColor.GRAY + ": " + guildd.getColor() + guildd.getColor().name(),
                            ChatColor.DARK_GRAY + "Size" + ChatColor.GRAY + ": " + ChatColor.GOLD + guildd.getMembers().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + 15,
                            ChatColor.DARK_GRAY + "Power" + ChatColor.GRAY + ": " + ChatColor.GOLD + power + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + maxpower,
                            ChatColor.DARK_GRAY + "Land Power" + ChatColor.GRAY + ": " + ChatColor.GOLD + guildd.getTerritory().size() + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + guildd.getMembers().size() * 5,
                            ChatColor.DARK_GRAY + "Allies" + ChatColor.GRAY + ": " + ChatColor.BLUE + "" + allies,
                            ChatColor.DARK_GRAY + "Enemies" + ChatColor.GRAY + ": " + ChatColor.RED + "" + enemies,
                            ChatColor.DARK_GRAY + "Members" + ChatColor.GRAY + ": " + ChatColor.RED + "" + members,
                            Lang.FOOTER.getMessage(),
                    };
                    player.sendMessage(info);
                } else if (args[0].toLowerCase().equals("kick")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                    }

                    if (user.getType() != RankType.LEADER && user.getType() != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    String name = args[1];
                    OfflinePlayer kickee = Bukkit.getOfflinePlayer(name);
                    User temp = NEGuilds.getUserManager().getTempUser(kickee.getUniqueId());
                    if (kickee.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not kick yourself from the guild!"));
                        return true;
                    }
                    if (!guild.getMembers().contains(kickee.getUniqueId())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is not in your guild!"));
                        return true;
                    }

                    if (rank == RankType.OFFICER && temp.getType() == RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not kick other officers from the guild!"));
                        return true;
                    }

                    if (rank == RankType.OFFICER && temp.getType() == RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not kick the leader from the guild!"));
                        return true;
                    }

                    guild.removeMember(kickee.getUniqueId());
                    temp.setGuild(null);
                    temp.setType(null);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have kicked " + ChatColor.BLUE + kickee.getName() + ChatColor.GRAY + " from the guild!"));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has kicked " + ChatColor.BLUE + kickee.getName() + ChatColor.GRAY + " from the guild!"));
                            }
                        } else if (players.getUniqueId().toString().equals(kickee.getUniqueId().toString())) {
                            players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has kicked you from the guild!"));
                        }
                    }
                } else if (args[0].toLowerCase().equals("name")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be the leader of the guild to do this!"));
                        return true;
                    }

                    String name = args[1];
                    if (name.length() > 15) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That name is too long!"));
                        return true;
                    }

                    if (NEGuilds.getGuildRegistry().guildExists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "A guild with that name already exists!"));
                        return true;
                    }

                    guild.setName(name);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have set the name of the guild to " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has set the name of the guild to " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("disband")) {
                    if (!player.hasPermission("ne.admin")) {
                        player.sendMessage(Lang.NO_PERM.getServerMessage());
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    for (UUID uuid : guildd.getMembers()) {
                        User temp = NEGuilds.getUserManager().getTempUser(uuid);
                        temp.setType(null);
                        temp.setGuild(null);
                    }

                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guildd.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has disbanded the guild you were in!"));
                            }
                        }
                    }

                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have disbanded guild " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                    NEGuilds.getGuildRegistry().deleteGuild(guildd.getUUID());
                } else if (args[0].toLowerCase().equals("home")) {
                    if (!player.hasPermission("ne.admin")) {
                        player.sendMessage(Lang.NO_PERM.getServerMessage());
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    if (guildd.getHome() == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not have a home set!"));
                        return true;
                    }
                    player.teleport(guildd.getHome());
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have teleported to " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + " guild home."));
                } else if (args[0].toLowerCase().equals("ally")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    if (guild == guildd) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not send a relation wish to your own guild!"));
                        return true;
                    }

                    if (guildd.getRelations().containsKey(guild.getUUID()) && guildd.getRelations().get(guild.getUUID()) == RelationType.ALLY) {
                        if (guild.isAlly(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already allied with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.ALLY) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.ALLY);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be allies."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be allies."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has accepted your wish to be allies"));
                            }
                        }
                    } else {
                        if (guild.isAlly(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already allied with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd) && guild.getRelations().get(guildd) == RelationType.ALLY) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.ALLY);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have requested to be allies with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has requested to be allies with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has requested to be allies"));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("truce")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    if (guild == guildd) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not send a relation wish to your own guild!"));
                        return true;
                    }

                    if (guildd.getRelations().containsKey(guild.getUUID()) && guildd.getRelations().get(guild.getUUID()) == RelationType.TRUCE) {
                        if (guild.isTruce(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already truced with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.TRUCE) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.TRUCE);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be truced."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be truced."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has accepted your wish to be truced"));
                            }
                        }
                    } else {
                        if (guild.isTruce(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already allied with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.TRUCE) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.TRUCE);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have requested to be truced with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has requested to be truced with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has requested to be truced"));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("neutral")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    if (guild == guildd) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not send a relation wish to your own guild!"));
                        return true;
                    }

                    if (guildd.getRelations().containsKey(guild.getUUID()) && guildd.getRelations().get(guild.getUUID()) == RelationType.NEUTRAL) {
                        if (guild.isNeutral(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already neutral with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.NEUTRAL) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.NEUTRAL);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be neutral."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has accepted " + guildd.getColor() + guildd.getName() + "'s " + ChatColor.GRAY + "wish to be neutral."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has accepted your wish to be neutral."));
                            }
                        }
                    } else {
                        if (guild.isNeutral(guildd)) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already neutral with that guild!"));
                            return true;
                        } else if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.NEUTRAL) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.NEUTRAL);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have requested to be neutral with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has requested to be neutral with  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has requested to be neutral."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("enemy")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[1])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[1]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[1]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    if (guild == guildd) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not send a relation wish to your own guild!"));
                        return true;
                    }

                    if (guildd.getRelations().containsKey(guild.getUUID()) && guildd.getRelations().get(guild.getUUID()) == RelationType.ENEMY) {
                    } else {
                        if (guild.getRelations().containsKey(guildd.getUUID()) && guild.getRelations().get(guildd.getUUID()) == RelationType.ENEMY) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You already have that relation wish set with that guild!"));
                            return true;
                        }

                        guild.addRelation(guildd.getUUID(), RelationType.ENEMY);
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have enemied " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has enemied  " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                                }
                            } else if (guildd.getMembers().contains(players.getUniqueId())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(guild.getColor() + guild.getName() + ChatColor.GRAY + " has enemied you."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("unclaim")) {
                    if (args[1].toLowerCase().equals("all")) {
                        if (!inGuild || guild == null) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                            return true;
                        }

                        if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                            return true;
                        }

                        for (Chunk chunk : guild.getTerritory()) {
                            if (guild.getHome() != null) {
                                if (guild.getHome().getChunk() == chunk) {
                                    guild.setHome(null);
                                    for (Player players : Bukkit.getOnlinePlayers()) {
                                        if (guild.getMembers().contains(players.getUniqueId())) {
                                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The home of the guild was removed due to the territory it was in was unclaimed!"));
                                            }
                                        }
                                    }
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "The home of the guild was removed due to the territory it was in was unclaimed!"));
                                }
                            }
                            guild.removeTerritory(chunk);
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have unclaimed all of your guild's territory!"));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has unclaimed all of your guild's territory!"));
                                }
                            }
                        }
                    } else {
                        player.sendMessage(Lang.SYNTAX_ERROR.getServerMessage());
                    }
                } else if (args[0].toLowerCase().equals("desc") || args[0].toLowerCase().equals("description")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                    }

                    if (user.getType() != RankType.LEADER && user.getType() != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    StringBuilder builder = new StringBuilder("");
                    for (String string : args) {
                        builder.append(string).append(" ");
                    }
                    String desc = builder.substring(args[0].length() + 1, builder.length() - 1);

                    guild.setDescription(desc);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have changed your guild's description to " + ChatColor.GREEN + desc + ChatColor.GRAY + "."));
                    for (UUID uuid : user.getGuild().getMembers()) {
                        if (!uuid.toString().equals(player.getUniqueId().toString())) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                Bukkit.getPlayer(uuid).sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has changed the guild description to " + ChatColor.GREEN
                                        + desc + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("leader")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (user.getType() != RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be the leader of the guild to do this!"));
                        return true;
                    }

                    String name = args[1];
                    OfflinePlayer leader = Bukkit.getOfflinePlayer(name);
                    if (leader.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're already the leader of the guild!"));
                        return true;
                    }
                    User temp = NEGuilds.getUserManager().getTempUser(leader.getUniqueId());
                    if (!guild.getMembers().contains(leader.getUniqueId())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is not in your guild!"));
                        return true;
                    }

                    temp.setType(RankType.LEADER);
                    guild.setLeader(temp.getUUID());
                    user.setType(RankType.OFFICER);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have made " + ChatColor.BLUE + leader.getName() + ChatColor.GRAY + " the leader of the guild!"));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players.getUniqueId().toString().equals(leader.getUniqueId().toString())) {
                            players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has made you the leader of the guild!"));
                        }
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has made " + ChatColor.BLUE + leader.getName() + ChatColor.GRAY + " the leader of the guild!"));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("promote")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    String name = args[1];
                    OfflinePlayer off = Bukkit.getOfflinePlayer(name);
                    User temp = NEGuilds.getUserManager().getTempUser(off.getUniqueId());
                    if (!guild.getMembers().contains(off.getUniqueId())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is not in your guild!"));
                    }
                    if (rank == RankType.LEADER) {
                        switch (temp.getType()) {
                            case RECRUIT:
                                temp.setType(RankType.MEMBER);
                                break;
                            case MEMBER:
                                temp.setType(RankType.OFFICER);
                                break;
                            case OFFICER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "To make that player the leader of the guild do /guild leader [player]."));
                                return true;
                            case LEADER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That member is already the leader of the guild!"));
                                return true;
                            default:
                                temp.setType(RankType.RECRUIT);
                                break;
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have promoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (players.getUniqueId().toString().equals(off.getUniqueId().toString())) {
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have been promoted to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has promoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    } else {
                        switch (temp.getType()) {
                            case RECRUIT:
                                temp.setType(RankType.MEMBER);
                                break;
                            case MEMBER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not make other members an officer of the guild!"));
                            case OFFICER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not change the leader of the guild!"));
                                return true;
                            case LEADER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That member is already the leader of the guild!"));
                                return true;
                            default:
                                temp.setType(RankType.RECRUIT);
                                break;
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have promoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (players.getUniqueId().toString().equals(off.getUniqueId().toString())) {
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have been promoted to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has promoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("demote")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    String name = args[1];
                    OfflinePlayer off = Bukkit.getOfflinePlayer(name);
                    User temp = NEGuilds.getUserManager().getTempUser(off.getUniqueId());
                    if (!guild.getMembers().contains(off.getUniqueId())) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is not in your guild!"));
                    }
                    if (rank == RankType.LEADER) {
                        switch (temp.getType()) {
                            case RECRUIT:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "This player already has the lowest guild rank!"));
                                return true;
                            case MEMBER:
                                temp.setType(RankType.RECRUIT);
                                break;
                            case OFFICER:
                                temp.setType(RankType.MEMBER);
                                break;
                            case LEADER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That member is already the leader of the guild!"));
                                return true;
                            default:
                                temp.setType(RankType.RECRUIT);
                                break;
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have demoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (players.getUniqueId().toString().equals(off.getUniqueId().toString())) {
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have been demoted to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has demoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    } else {
                        switch (temp.getType()) {
                            case RECRUIT:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "This player already has the lowest guild rank!"));
                                return true;
                            case MEMBER:
                                temp.setType(RankType.RECRUIT);
                                break;
                            case OFFICER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not demote other officers of the guild!"));
                                return true;
                            case LEADER:
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not demote the leader of the guild!"));
                                return true;
                            default:
                                temp.setType(RankType.RECRUIT);
                                break;
                        }
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have demoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (guild.getMembers().contains(players.getUniqueId())) {
                                if (players.getUniqueId().toString().equals(off.getUniqueId().toString())) {
                                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have been demoted to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                                if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                    players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has demoted " + ChatColor.BLUE + off.getName() + ChatColor.GRAY + " to " + ChatColor.BLUE + temp.getType().getName() + ChatColor.GRAY + "."));
                                }
                            }
                        }
                    }
                }
            } else if (args.length == 3) {
                //NAME, RANK, DESC
                if (args[0].toLowerCase().equals("name")) {
                    if (!player.hasPermission("ne.admin")) {
                        player.sendMessage(Lang.NO_PERM.getServerMessage());
                        return true;
                    }

                    GuildModel guildd = null;
                    if (NEGuilds.getGuildRegistry().guildExists(args[2])) {
                        guildd = NEGuilds.getGuildRegistry().getGuild(args[2]);
                    } else {
                        if (NEGuilds.getUserManager().userExists(Bukkit.getOfflinePlayer(args[2]).getUniqueId())) {
                            guildd = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(args[2]).getUniqueId()).getGuild();
                        }
                    }

                    if (guildd == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That guild does not exist or that player is not in a guild!"));
                        return true;
                    }

                    String name = args[1];
                    if (name.length() > 15) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That name is too long!"));
                        return true;
                    }

                    if (NEGuilds.getGuildRegistry().guildExists(name)) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "A guild with that name already exists!"));
                        return true;
                    }

                    guildd.setName(name);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have set the name of the guild " + guildd.getColor() + guildd.getName() + ChatColor.GRAY +
                            " to " + guildd.getColor() + guildd.getName() + ChatColor.GRAY + "."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (guild.getMembers().contains(players.getUniqueId())) {
                            if (!players.getUniqueId().toString().equals(player.getUniqueId().toString())) {
                                players.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has set the name of the guild to " + guild.getColor() + guild.getName() + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else if (args[0].toLowerCase().equals("rank")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                        return true;
                    }

                    if (rank != RankType.LEADER && rank != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }
                    String name = args[1];

                    RankType type;
                    type = RankType.valueOf(args[2].toUpperCase());

                    if (type == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That is not a valid rank!"));
                        return true;
                    }

                    if (type == RankType.LEADER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "To transfer leadership of the guild do /g leader [player]."));
                        return true;
                    }

                    if (user.getType() == RankType.LEADER) {
                        if (user.getGuild().getMembers().contains(Bukkit.getOfflinePlayer(name).getUniqueId())) {
                            User temp = NEGuilds.getUserManager().getTempUser(Bukkit.getOfflinePlayer(name).getUniqueId());
                            if (temp == user) {
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You can not change your guild rank unless you transfer leadership of the guild!"));
                                return true;
                            }

                            if (temp.getType() == RankType.LEADER) {
                                player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is already the leader!"));
                                return true;
                            }
                            temp.setType(type);
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(name))) {
                                Bukkit.getPlayer(name).sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "Your guild rank has been set to " + ChatColor.BLUE + type.getName() + ChatColor.GRAY + "."));
                            }
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have set " + ChatColor.BLUE + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + " guild rank to "
                                    + ChatColor.BLUE + type.getName() + ChatColor.GRAY + "."));
                            for (UUID uuid : user.getGuild().getMembers()) {
                                if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                    if (!uuid.toString().equals(player.getUniqueId().toString()) && !uuid.toString().equals(Bukkit.getOfflinePlayer(name).getUniqueId().toString())) {
                                        Bukkit.getPlayer(uuid).sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has set " + ChatColor.BLUE
                                                + Bukkit.getOfflinePlayer(name).getName() + "'s " + ChatColor.GRAY + " guild rank to " + ChatColor.BLUE + type.getName() + ChatColor.GRAY + "."));
                                    }
                                }
                            }
                        } else {
                            player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "That player is not in the guild!"));
                            return true;
                        }
                    }
                } else if (args[0].toLowerCase().equals("desc") || args[0].toLowerCase().equals("description")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                    }

                    if (user.getType() != RankType.LEADER && user.getType() != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    StringBuilder builder = new StringBuilder("");
                    for (String string : args) {
                        builder.append(string).append(" ");
                    }
                    String desc = builder.substring(args[0].length() + 1, builder.length() - 1);

                    guild.setDescription(desc);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have changed your guild's description to " + ChatColor.GREEN + desc + ChatColor.GRAY + "."));
                    for (UUID uuid : user.getGuild().getMembers()) {
                        if (!uuid.toString().equals(player.getUniqueId().toString())) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                Bukkit.getPlayer(uuid).sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has changed the guild description to " + ChatColor.GREEN
                                        + desc + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else {
                    player.sendMessage(Lang.SYNTAX_ERROR.getServerMessage());
                    return true;
                }
            } else {
                if (args[0].toLowerCase().equals("desc") || args[0].toLowerCase().equals("description")) {
                    if (!inGuild || guild == null) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You're not currently in any guild!"));
                    }

                    if (user.getType() != RankType.LEADER && user.getType() != RankType.OFFICER) {
                        player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "You must be an officer or leader of the guild to do this!"));
                        return true;
                    }

                    StringBuilder builder = new StringBuilder("");
                    for (String string : args) {
                        builder.append(string).append(" ");
                    }
                    String desc = builder.substring(args[0].length() + 1, builder.length() - 1);

                    guild.setDescription(desc);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.GRAY + "You have changed your guild's description to " + ChatColor.GREEN + desc + ChatColor.GRAY + "."));
                    for (UUID uuid : user.getGuild().getMembers()) {
                        if (!uuid.toString().equals(player.getUniqueId().toString())) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                Bukkit.getPlayer(uuid).sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.BLUE + player.getName() + ChatColor.GRAY + " has changed the guild description to " + ChatColor.GREEN
                                        + desc + ChatColor.GRAY + "."));
                            }
                        }
                    }
                } else {
                    player.sendMessage(Lang.SYNTAX_ERROR.getServerMessage());
                    return true;
                }
            }
        } else if (sender instanceof ConsoleCommandSender) {
            ConsoleCommandSender console = (ConsoleCommandSender) sender;
            console.sendMessage(Lang.CHAT_TAG.getServerMessage(ChatColor.RED + "This command is not available from the console!"));
            return true;
        }
        return true;
    }

    private int getTotalPages() {
        Collection<GuildModel> guilds = NEGuilds.getGuildRegistry().getRegistered();

        int totalPages = (int) Math.ceil((double) guilds.size() / 10);
        return totalPages;
    }

    private void sendPage(Player player, int page) {
        int diplayPage = page;
        page = page - 1;

        Collection<GuildModel> guilds = NEGuilds.getGuildRegistry().getRegistered();

        int totalPages = getTotalPages();

        int start = 10 * page;
        int finish;

        if (start + 10 > guilds.size()) {
            finish = guilds.size();
        } else {
            finish = start + 10;
        }
        List<Integer> members = Lists.newArrayList();

        for (GuildModel guild : guilds) {
            members.add(guild.getMembers().size());
        }

        Collections.sort(members);
        Collections.reverse(members);

        List<Integer> memberCount = Lists.newArrayList();
        for (int i = start; i < finish; i++) {
            memberCount.add(members.get(i));
        }

        String[] help = new String[]{
                Lang.HEADER.getServerHeader(),
                ChatColor.GRAY + "Guilds List " + ChatColor.DARK_GRAY + "- Page" + ChatColor.GRAY + ": " + ChatColor.GOLD + diplayPage + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + totalPages,
                Lang.FOOTER.getMessage(),
        };
        player.sendMessage(help);
        int pos = 1;
        List<GuildModel> used = Lists.newArrayList();
        for (Integer integer : memberCount) {
            for (GuildModel guild : guilds) {
                if (!used.contains(guild)) {
                    if (guild.getMembers().size() == integer) {
                        StringBuilder spacer = new StringBuilder();
                        int spaces = 15 - guild.getName().length();
                        for (int i = 0; i < spaces; i++) {
                            spacer.append(" ");
                        }

                        player.sendMessage(ChatColor.GOLD + "" + start + pos + ". " + guild.getColor() + guild.getName() + ChatColor.DARK_GRAY + spacer + " - Members" + ChatColor.GRAY + ": " + ChatColor.GOLD
                                + integer + ChatColor.DARK_GRAY + "/" + ChatColor.GOLD + 15);
                        pos++;
                        used.add(guild);
                        break;
                    }
                }
            }
        }
        player.sendMessage(Lang.FOOTER.getMessage());
    }
}