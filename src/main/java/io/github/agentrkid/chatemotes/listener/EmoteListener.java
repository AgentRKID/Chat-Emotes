package io.github.agentrkid.chatemotes.listener;

import io.github.agentrkid.chatemotes.ChatEmotes;
import io.github.agentrkid.chatemotes.manager.ChatEmotesManager;
import io.github.agentrkid.chatemotes.manager.EmoteData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EmoteListener implements Listener {
    private final ChatEmotesManager emotesManager;
    private final ChatEmotes plugin;

    public EmoteListener(ChatEmotesManager emotesManager, ChatEmotes plugin) {
        this.emotesManager = emotesManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // Split by spaces.
        String[] messageSplit = event.getMessage().split(" ");

        int index = 0;

        for (String str : messageSplit) {
            EmoteData data = emotesManager.getCachedEmotesMap().get(str.toLowerCase());

            if (data != null) {
                // Emotes can have permissions
                // so we need to make sure the
                // player actually has the permission
                // for the emote.
                if (data.getPermission() != null && !data.getPermission().isEmpty()
                        && !event.getPlayer().hasPermission(data.getPermission())) {
                    continue;
                }

                // We make sure to reset the chat color from any emote.
                messageSplit[index] = data.getEmote() + ChatColor.RESET;
            }
            ++index;
        }
        event.setMessage(StringUtils.join(messageSplit, " "));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSignChange(SignChangeEvent event) {
        if (plugin.getConfig().getBoolean("settings.handle-emotes-on-signs")) {
            int index = 0;

            for (String line : event.getLines()) {
                int splitIndex = 0;
                String[] lineSplit = line.split(" ");

                for (String splitLine : lineSplit) {
                    EmoteData data = emotesManager.getCachedEmotesMap().get(splitLine.toLowerCase());

                    if (data != null) {
                        // Emotes can have permissions
                        // so we need to make sure the
                        // player actually has the permission
                        // for the emote.
                        if (data.getPermission() != null && !data.getPermission().isEmpty()
                                && !event.getPlayer().hasPermission(data.getPermission())) {
                            continue;
                        }

                        // We make sure to reset the chat color from any emote.
                        lineSplit[splitIndex] = data.getEmote() + ChatColor.RESET;
                    }
                    ++splitIndex;
                }

                // Replace the line with
                // our corrected line.
                event.setLine(index++, StringUtils.join(lineSplit, " "));
            }
        }
    }
}
