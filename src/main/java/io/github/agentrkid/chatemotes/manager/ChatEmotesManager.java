package io.github.agentrkid.chatemotes.manager;

import io.github.agentrkid.chatemotes.ChatEmotes;
import io.github.agentrkid.chatemotes.listener.ChatListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatEmotesManager {
    @Getter private final Map<String, EmoteData> cachedEmotesMap = new HashMap<>();

    public ChatEmotesManager(ChatEmotes plugin) {
        Configuration config = plugin.getConfig();

        for (String key : config.getConfigurationSection("emotes").getKeys(false)) {
            String loc = "emotes." + key + ".";

            EmoteData emote = new EmoteData();

            if (!config.contains(loc + "replaceable")) {
                plugin.getLogger().warning("Didn't load " + key + " as it had no replaceable!");
                continue;
            }

            if (!config.contains(loc + "emote")) {
                plugin.getLogger().warning("Didn't load " + key + " as it had no emote!");
                continue;
            }

            emote.setName(key);
            emote.setEmote(ChatColor.translateAlternateColorCodes('&', config.getString(loc + "emote")));

            // We allow permissions for emotes
            // if they are permissible.
            if (config.contains(loc + "permissible") && config.getBoolean(loc + "permissible")) {
                emote.setPermission(loc + "permission");
            }

            // We allow multiple replaceables.
            List<String> replaceable = config.getStringList(loc + "replaceable");

            for (String replace : replaceable) {
                // Allow O(1) lookups by
                // their replaceable
                cachedEmotesMap.put(replace.toLowerCase(), emote);
            }
        }
        Bukkit.getPluginManager().registerEvents(new ChatListener(this), plugin);
    }
}
