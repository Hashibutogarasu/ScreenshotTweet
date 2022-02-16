package com.Hashibutogarasu.screenshottweet;

import com.Hashibutogarasu.screenshottweet.guis.TweetScreenGUI;
import com.Hashibutogarasu.screenshottweet.guis.Tweetscreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class ScreenshotTweetModClient implements ClientModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("ScreenshotTweet");

    @Override
    public void onInitializeClient() {

        var keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.screenshottweet.tweet", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_N, // The keycode of the key
                "category.screenshottweet.keybinds" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {

                MinecraftClient.getInstance().setScreen(new Tweetscreen(new TweetScreenGUI(Paths.get("screenshots").toAbsolutePath().toString())));//GUI呼び出し
                //client.player.sendSystemMessage(new LiteralText("Key 1 was pressed!"), UUID.randomUUID());
            }
        });
    }
}


