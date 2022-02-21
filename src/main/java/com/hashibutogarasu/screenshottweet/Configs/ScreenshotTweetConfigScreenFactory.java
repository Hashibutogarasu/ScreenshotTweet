package com.hashibutogarasu.screenshottweet.Configs;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

import java.util.concurrent.atomic.AtomicReference;

public class ScreenshotTweetConfigScreenFactory {
    public static ScreenshotTweetConfig twitterkeyconfig;
    public static AtomicReference<String> twitterapikey;
    public static AtomicReference<String> twitterapikeysecret;
    public static AtomicReference<String> twitteraccesstoken;
    public static AtomicReference<String> twitteraccesstokensecret;

    public ScreenshotTweetConfigScreenFactory() { }

    public static Screen genConfig(Screen parent){

        try{
            twitterkeyconfig = ScreenshotTweetConfig.load();
            twitterapikey = new AtomicReference<>(twitterkeyconfig.apikey);
            twitterapikeysecret = new AtomicReference<>(twitterkeyconfig.apikeysecret);
            twitteraccesstoken = new AtomicReference<>(twitterkeyconfig.accesstoken);
            twitteraccesstokensecret = new AtomicReference<>(twitterkeyconfig.accesstokensecret);
        }
        catch (Exception ignored){

        }

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(new KeyConfigScreen(new TranslatableText("gui.screenshottweet.title")))
                .setTitle(new TranslatableText("gui.screenshottweet.title"));

        builder.setSavingRunnable(() -> {
            try{
                twitterkeyconfig.apikey = twitterapikey.get();
                twitterkeyconfig.apikeysecret = twitterapikeysecret.get();
                twitterkeyconfig.accesstoken = twitteraccesstoken.get();
                twitterkeyconfig.accesstokensecret = twitteraccesstokensecret.get();
                twitterkeyconfig.save();
            }
            catch(Exception ignored){

            }
        });

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category.screenshottweet.general"));
        builder.setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option.screenshottweet.apikey"), twitterkeyconfig.apikey)
                .setDefaultValue(twitterkeyconfig.apikey) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitterapikey.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option.screenshottweet.apikeysecret"), twitterkeyconfig.apikeysecret)
                .setDefaultValue(twitterkeyconfig.apikeysecret) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitterapikeysecret.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option.screenshottweet.accesstoken"), twitterkeyconfig.accesstoken)
                .setDefaultValue(twitterkeyconfig.accesstoken) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitteraccesstoken.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option.screenshottweet.accesstokensecret"), twitterkeyconfig.accesstokensecret)
                .setDefaultValue(twitterkeyconfig.accesstokensecret) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitteraccesstokensecret.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        return builder.build();
    }
}
