package net.nighthawkempires.guilds.guild;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.nighthawkempires.core.chat.tag.PlayerTag;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.user.User;
import org.bukkit.entity.Player;

public class GuildTag extends PlayerTag {

    public String getName() {
        return "guild";
    }

    public TextComponent getComponentFor(Player player) {
        User user = NEGuilds.getUserManager().getUser(player.getUniqueId());
        if (user.getGuild() == null) {
            return null;
        }
        GuildModel guild = user.getGuild();
        TextComponent tag = new TextComponent("[");
        tag.setColor(ChatColor.DARK_GRAY);
        TextComponent mid = new TextComponent(guild.getName());
        mid.setColor(ChatColor.valueOf(guild.getColor().name()));
        tag.addExtra(mid);
        tag.addExtra("]");
        tag.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ChatColor.GRAY + user.getType().name().substring(0, 1).toUpperCase()
                + user.getType().name().substring(1, user.getType().name().length()).toLowerCase() + " of " + ChatColor.valueOf(guild.getColor().name()) + guild.getName())));
        return tag;
    }

    public int getPriority() {
        return 2;
    }
}
