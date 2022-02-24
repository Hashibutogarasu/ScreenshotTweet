package com.hashibutogarasu.screenshottweet.mixin;

import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfig;
import com.hashibutogarasu.screenshottweet.Configs.ScreenshotTweetConfigScreenFactory;
import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import com.hashibutogarasu.screenshottweet.Threads.TwitterThread;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class TitleScreenMixin {
    @Inject(at = @At("HEAD"), method = "joinWorld")
    private void init(CallbackInfo ci){
        try{
            ScreenshotTweetConfigScreenFactory.twitterkeyconfig = ScreenshotTweetConfig.load();
            TwitterThread.Oauth();
        }
        catch(Exception e){

        }
    }
}
