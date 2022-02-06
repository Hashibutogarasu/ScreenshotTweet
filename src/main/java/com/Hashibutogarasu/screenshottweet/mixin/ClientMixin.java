package com.Hashibutogarasu.screenshottweet.mixin;

import com.Hashibutogarasu.screenshottweet.ScreenshotTweetMod;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(MinecraftClient.class)
public class ClientMixin {
    @Inject(at = @At("HEAD"), method = "run()V")
    private void onEnable(CallbackInfo info) {

    }

    @Inject(at = @At("HEAD"), method = "stop()V")
    private void onDisable(CallbackInfo info) {

    }
}