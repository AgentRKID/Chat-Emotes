package io.github.agentrkid.chatemotes.listener;

import io.github.agentrkid.chatemotes.manager.ChatEmotesManager;
import io.github.agentrkid.chatemotes.manager.EmoteData;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ChatEmotesManager emotesManager;

    public ChatListener(ChatEmotesManager emotesManager) {
        this.emotesManager = emotesManager;
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
}
