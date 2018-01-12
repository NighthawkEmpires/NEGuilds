package net.nighthawkempires.guilds.listener;

import com.google.common.collect.Lists;
import net.nighthawkempires.core.NECore;
import net.nighthawkempires.core.language.Lang;
import net.nighthawkempires.core.utils.*;
import net.nighthawkempires.guilds.NEGuilds;
import net.nighthawkempires.guilds.guild.GuildModel;
import net.nighthawkempires.guilds.guild.GuildRank;
import net.nighthawkempires.guilds.user.UserModel;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UserModel user = NEGuilds.getUserRegistry().getUser(player.getUniqueId());

        try {
            if (ChatColor.stripColor(event.getClickedInventory().getName()).equals("Guild Color")) {
                Optional<GuildModel> opGuild = user.getGuild();
                if (opGuild.isPresent() && (user.getType() == GuildRank.LEADER ||
                        user.getType() == GuildRank.OFFICER)) {
                    if (event.isShiftClick()) {
                        event.setCancelled(true);
                        return;
                    }

                    GuildModel guild = opGuild.get();

                    event.setCancelled(true);
                    if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                        return;
                    }
                    String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    ChatColor color = ChatColor.DARK_GRAY;
                    for (ChatColor colors : ChatColor.values()) {
                        if (colors.name().replaceAll("_", " ").toLowerCase().equals(itemName.toLowerCase())) {
                            color = colors;
                        }
                    }
                    if (guild.getColor() == color) {
                        player.closeInventory();
                        player.sendMessage(Lang.CHAT_TAG
                                .getServerMessage(ChatColor.RED + "That is already the current color of the guild!"));
                        return;
                    }

                    player.closeInventory();
                    if (MathUtil.lessThan(NECore.getUserManager().getUser(player.getUniqueId()).getTokens(), 10)) {
                        player.sendMessage(Lang.CHAT_TAG
                                .getServerMessage(ChatColor.RED + "You do not have enough tokens to do this!"));
                        return;
                    }

                    guild.setColor(color);
                    NECore.getUserManager().getUser(player.getUniqueId())
                            .setTokens(NECore.getUserManager().getUser(player.getUniqueId()).getTokens() - 10);
                    player.sendMessage(Lang.CHAT_TAG.getServerMessage(
                            ChatColor.GRAY + "You have set the color of your guild to " + color + color.name() +
                                    ChatColor.GRAY + "."));
                    SoundUtil.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
                    for (UUID uuid : guild.getMembers()) {
                        if (!uuid.toString().equals(player.getUniqueId().toString())) {
                            if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(uuid))) {
                                Bukkit.getPlayer(uuid).sendMessage(Lang.CHAT_TAG.getServerMessage(
                                        ChatColor.BLUE + player.getName() + ChatColor.GRAY +
                                                " has changed the guild color to " + color + color.name()
                                                + ChatColor.GRAY + "."));
                            }
                        }
                    }
                    event.setCancelled(true);
                }
            }
        } catch (Exception ignored) {
        }
    }

    public Inventory inventoryGuildColor(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 3 * 9,
                ChatColor.RED + "" + ChatColor.BOLD + "" + ChatColor.ITALIC + "Guild Color");
        ArrayList<String> meta =
                Lists.newArrayList("  ", ChatColor.GRAY + "Costs " + ChatColor.GOLD + 10 + ChatColor.GRAY + " Tokens");
        ItemStack[] items = new ItemStack[]{
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.RED, ChatColor.DARK_RED + "Dark Red",
                        meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE_POWDER, ItemUtil.Color.RED, ChatColor.RED + "Red",
                        meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.YELLOW, ChatColor.GOLD + "Gold", meta,
                        1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE_POWDER, ItemUtil.Color.YELLOW,
                        ChatColor.YELLOW + "Yellow", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.LIME,
                        ChatColor.DARK_GREEN + "Dark Green", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE_POWDER, ItemUtil.Color.LIME, ChatColor.GREEN + "Green",
                        meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.CYAN,
                        ChatColor.DARK_AQUA + "Dark Aqua", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE_POWDER, ItemUtil.Color.LIGHT_BLUE,
                        ChatColor.AQUA + "Aqua", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.BLUE,
                        ChatColor.DARK_BLUE + "Dark Blue", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE_POWDER, ItemUtil.Color.BLUE, ChatColor.BLUE + "Blue",
                        meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.PURPLE,
                        ChatColor.DARK_PURPLE + "Dark Purple", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.PINK,
                        ChatColor.LIGHT_PURPLE + "Light Purple", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.WHITE, ChatColor.WHITE + "White", meta,
                        1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.LIGHT_GRAY, ChatColor.GRAY + "Gray",
                        meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.GRAY,
                        ChatColor.DARK_GRAY + "Dark Gray", meta, 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.CONCRETE, ItemUtil.Color.BLACK, ChatColor.BLACK + "Black", meta,
                        1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
                ItemUtil.coloredItem(ItemUtil.Colorable.STAINED_GLASS_PANE, ItemUtil.Color.BLACK, "",
                        Lists.newArrayList(), 1),
        };
        inventory.setContents(items);
        return inventory;
    }
}
