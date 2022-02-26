package com.hashibutogarasu.screenshottweet.Configs;

import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

import static com.hashibutogarasu.screenshottweet.Ids.Id.MOD_ID;

public class ScreenshotTweetConfig {
    private transient File file;
    public String apikey = "";
    public String apikeysecret = "";
    public String accesstoken = "";
    public String accesstokensecret = "";
    public Boolean showscreenname = true;
    public Boolean showlogsintweetscreen = true;

    private ScreenshotTweetConfig() { }

    public static ScreenshotTweetConfig load() {
        File file = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                MOD_ID + ".toml"
        );

        ScreenshotTweetConfig config;
        if (file.exists()) {
            Toml configTOML = new Toml().read(file);
            config = configTOML.to(ScreenshotTweetConfig.class);
            config.file = file;
        } else {
            config = new ScreenshotTweetConfig();
            config.file = file;
            config.save();
        }
        return config;
    }

    public void save() {
        TomlWriter writer = new TomlWriter();
        try {
            writer.write(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
