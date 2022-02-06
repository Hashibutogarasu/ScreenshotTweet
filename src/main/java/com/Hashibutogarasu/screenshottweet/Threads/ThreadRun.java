package com.Hashibutogarasu.screenshottweet.Threads;

import com.Hashibutogarasu.screenshottweet.ScreenshotTweetMod;
import com.Hashibutogarasu.screenshottweet.Utils.CommandExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class ThreadRun implements Runnable{

    String Fullpath = "";

    public ThreadRun(String fullpath){
        Fullpath = fullpath;

        ScreenshotTweetMod.LOGGER.info("Command start [start " + Fullpath + "");
        try{
            CommandExecutor.execute("start", Arrays.asList(Fullpath), 10);
        }
        catch(Exception e){

        }

    }

    public void run(){

    }
}