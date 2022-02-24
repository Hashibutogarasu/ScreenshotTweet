package com.hashibutogarasu.screenshottweet;

import com.hashibutogarasu.screenshottweet.guis.TweetScreenGUI;
import com.hashibutogarasu.screenshottweet.guis.Tweetscreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class ScreenshotTweetModClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotTweet");
    public static String MOD_ID = "ScreenshotTweet";

    @Override
    public void onInitializeClient() {

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.screenshottweet.tweet",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.screenshottweet.keybinds"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new Tweetscreen(new TweetScreenGUI()));
            }
        });
    }
}


