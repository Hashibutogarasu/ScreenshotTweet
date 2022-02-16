package com.hashibutogarasu.screenshottweet.Threads;

import com.hashibutogarasu.screenshottweet.ScreenshotTweetModClient;
import com.hashibutogarasu.screenshottweet.Utils.CommandExecutor;

import java.util.Arrays;

public class ThreadRun implements Runnable{

    String Fullpath = "";

    public ThreadRun(String fullpath){
        Fullpath = fullpath;

        ScreenshotTweetModClient.LOGGER.info("Command start [start " + Fullpath + "");
        try{
            CommandExecutor.execute("start", Arrays.asList(Fullpath), 10);
        }
        catch(Exception e){

        }

    }

    public void run(){

    }
}