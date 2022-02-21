package com.hashibutogarasu.screenshottweet;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModmenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ScreenshotTweetConfigScreenFactory::genConfig;
    }
}