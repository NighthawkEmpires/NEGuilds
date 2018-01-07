package net.nighthawkempires.guilds.scoreboard;

import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.scoreboard.Scoreboards;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class GuildScoreboards extends Scoreboards {

    private int task = 0;

    public String getName() {
        return "name";
    }

    public int getTaskID() {
        return task;
    }

    @SuppressWarnings("SameReturnValue")
    public int getNumber() {
        return 1;
    }

    public Scoreboard getFor(Player player) {
        final Scoreboard[] scoreboard = {Bukkit.getScoreboardManager().getNewScoreboard()};
        final Objective[] objective = {scoreboard[0].registerNewObjective("test", "dummy")};
        objective[0].setDisplaySlot(DisplaySlot.SIDEBAR);
        objective[0].setDisplayName(Lang.SCOREBOARD.getServerBoard());
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());
        Team guild = scoreboard[0].registerNewTeam("guild");
        guild.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.WHITE + "" + ChatColor.BOLD);
        guild.setPrefix("");
        guild.setSuffix("");
        Team rank = scoreboard[0].registerNewTeam("rank");
        rank.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.GREEN + "" + ChatColor.BOLD);
        rank.setPrefix("");
        rank.setSuffix("");
        Team power = scoreboard[0].registerNewTeam("power");
        power.addEntry(ChatColor.GRAY + " ➛   " + ChatColor.GOLD + "" + ChatColor.BOLD);
        power.setPrefix("");
        power.setSuffix("");

        objective[0].getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "------------")
                .setScore(10);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Guild" + ChatColor.GRAY + ": ").setScore(9);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.WHITE + "" + ChatColor.BOLD).setScore(8);
        try {
            guild.setSuffix(user.getGuild().get().getColor() + "" + ChatColor.BOLD + user.getGuild().get().getName());
        } catch (Exception e) {
            guild.setSuffix(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "NaN");
        }
        objective[0].getScore(ChatColor.DARK_PURPLE + " ").setScore(7);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Rank" + ChatColor.GRAY + ": ").setScore(6);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.GREEN + "" + ChatColor.BOLD).setScore(5);
        if (user.getGuild() != null) {
            rank.setSuffix(user.getType().getName());
        } else {
            rank.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
        }
        objective[0].getScore(ChatColor.YELLOW + "  ").setScore(4);
        objective[0].getScore(ChatColor.GRAY + "" + ChatColor.BOLD + " Power" + ChatColor.GRAY + ": ").setScore(3);
        objective[0].getScore(ChatColor.GRAY + " ➛   " + ChatColor.GOLD + "" + ChatColor.BOLD).setScore(2);
        power.setSuffix(user.getPower() + "");
        objective[0].getScore(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "-----------")
                .setScore(1);

        this.task = Bukkit.getScheduler().scheduleSyncRepeatingTask(NECore.getPlugin(), () -> {
            try {
                guild.setSuffix(
                        user.getGuild().get().getColor() + "" + ChatColor.BOLD + user.getGuild().get().getName());
            } catch (Exception e) {
                guild.setSuffix(ChatColor.YELLOW + "" + ChatColor.BOLD + "NaN");
            }
            if (user.getGuild() != null) {
                rank.setSuffix(user.getType().getName());
            } else {
                rank.setSuffix(ChatColor.GRAY + "" + ChatColor.BOLD + "NaN");
            }
            power.setSuffix(user.getPower() + "");
        }, 0, 5);
        Bukkit.getScheduler()
                .scheduleSyncDelayedTask(NEGuilds.getPlugin(), () -> Bukkit.getScheduler().cancelTask(this.task),
                        14 * 20);
        return scoreboard[0];
    }
}
