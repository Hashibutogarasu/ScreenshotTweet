package com.Hashibutogarasu.screenshottweet.mixin;

import com.Hashibutogarasu.screenshottweet.ScreenshotTweetMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ExampleMixin {
	@Shadow @Final private static Logger LOGGER;

	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		//ScreenshotTweetMod.LOGGER.info("This line is printed by an example mod mixin!");
	}
}
