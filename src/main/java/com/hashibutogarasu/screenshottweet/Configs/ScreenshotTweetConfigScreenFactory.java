package com.hashibutogarasu.screenshottweet.Configs;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.atomic.AtomicReference;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;

public class ScreenshotTweetConfigScreenFactory {
    public static ScreenshotTweetConfig twitterkeyconfig;
    public static AtomicReference<String> twitterapikey;
    public static AtomicReference<String> twitterapikeysecret;
    public static AtomicReference<String> twitteraccesstoken;
    public static AtomicReference<String> twitteraccesstokensecret;
    public static AtomicReference<Boolean> showscreenname;
    public static AtomicReference<Boolean> showlogsintweetscreen;

    public static ConfigurationBuilder cb = new ConfigurationBuilder();
    public static TwitterFactory tf = new TwitterFactory(cb.build());
    public static Twitter twitter = tf.getInstance();

    public ScreenshotTweetConfigScreenFactory() {
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikey)
                .setOAuthConsumerSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.apikeysecret)
                .setOAuthAccessToken(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstoken)
                .setOAuthAccessTokenSecret(ScreenshotTweetConfigScreenFactory.twitterkeyconfig.accesstokensecret);
    }

    public static Screen genConfig(Screen parent){

        try{
            twitterkeyconfig = ScreenshotTweetConfig.load();
            twitterapikey = new AtomicReference<>(twitterkeyconfig.apikey);
            twitterapikeysecret = new AtomicReference<>(twitterkeyconfig.apikeysecret);
            twitteraccesstoken = new AtomicReference<>(twitterkeyconfig.accesstoken);
            twitteraccesstokensecret = new AtomicReference<>(twitterkeyconfig.accesstokensecret);
            showscreenname = new AtomicReference<>(twitterkeyconfig.showscreenname);
            showlogsintweetscreen = new AtomicReference<>(twitterkeyconfig.showlogsintweetscreen);
        }
        catch (Exception ignored){

        }

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(new KeyConfigScreen(new TranslatableText("gui." + MOD_ID + ".title")))
                .setTitle(new TranslatableText("gui." + MOD_ID + ".title"));

        builder.setSavingRunnable(() -> {
            try{
                twitterkeyconfig.apikey = twitterapikey.get();
                twitterkeyconfig.apikeysecret = twitterapikeysecret.get();
                twitterkeyconfig.accesstoken = twitteraccesstoken.get();
                twitterkeyconfig.accesstokensecret = twitteraccesstokensecret.get();
                twitterkeyconfig.showscreenname = showscreenname.get();
                twitterkeyconfig.showlogsintweetscreen = showlogsintweetscreen.get();
                twitterkeyconfig.save();
                ScreenshotTweetConfig.load();
            }
            catch(Exception ignored){

            }
        });

        ConfigCategory general = builder.getOrCreateCategory(new TranslatableText("category." + MOD_ID + ".general"));
        builder.setParentScreen(parent);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option." + MOD_ID + ".apikey"), twitterkeyconfig.apikey)
                .setDefaultValue(twitterkeyconfig.apikey) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitterapikey.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option."+ MOD_ID + ".apikeysecret"), twitterkeyconfig.apikeysecret)
                .setDefaultValue(twitterkeyconfig.apikeysecret) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitterapikeysecret.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option." + MOD_ID + ".accesstoken"), twitterkeyconfig.accesstoken)
                .setDefaultValue(twitterkeyconfig.accesstoken) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitteraccesstoken.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startStrField(new TranslatableText("option."+ MOD_ID + ".accesstokensecret"), twitterkeyconfig.accesstokensecret)
                .setDefaultValue(twitterkeyconfig.accesstokensecret) // Recommended: Used when user click "Reset"
                .setSaveConsumer(newValue -> twitteraccesstokensecret.set(newValue)) // Recommended: Called when user save the config
                .build()); // Builds the option entry for cloth config

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option."+ MOD_ID + ".showscreenname"),twitterkeyconfig.showscreenname)
                .setDefaultValue(twitterkeyconfig.showscreenname)
                .setSaveConsumer(newValue -> showscreenname.set(newValue))
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("option."+ MOD_ID + ".showlogsintweetscreen"),twitterkeyconfig.showlogsintweetscreen)
                .setDefaultValue(twitterkeyconfig.showlogsintweetscreen)
                .setSaveConsumer(newValue -> showlogsintweetscreen.set(newValue))
                .build());

        return builder.build();
    }
}
