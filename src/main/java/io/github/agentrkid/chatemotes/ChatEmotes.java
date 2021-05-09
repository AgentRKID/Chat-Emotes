package io.github.agentrkid.chatemotes;

import io.github.agentrkid.chatemotes.manager.ChatEmotesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatEmotes extends JavaPlugin {
    private ChatEmotesManager emotesManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        emotesManager = new ChatEmotesManager(this);
    }

    @Override
    public void onDisable() {
        if (emotesManager != null) {
            emotesManager.getCachedEmotesMap().clear();
        }
    }
}
