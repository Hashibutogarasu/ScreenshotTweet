package com.Hashibutogarasu.screenshottweet.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class CommandExecutor{

    public static int execute(String cmd, List<String> params, long timeoutSec) {
        List<String> cmdAndParams = new LinkedList<>();
        cmdAndParams.add(cmd);
        cmdAndParams.addAll(params);

        return execute(cmdAndParams, timeoutSec);
    }

    public static int execute(String cmd, long timeoutSec) {
        return execute(Arrays.asList(cmd), timeoutSec);
    }

    private static int execute(List<String> cmdAndParams, long timeoutSec) {
        ProcessBuilder builder = new ProcessBuilder(cmdAndParams);
        builder.redirectErrorStream(true);  // 標準エラー出力の内容を標準出力にマージする

        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new CommandExecuteFailedException("Command launch failed. [cmd: " + cmdAndParams + "]", e);
        }

        int exitCode;
        try {
            // 標準出力をすべて読み込む
            new Thread(() -> {
                try (InputStream is = process.getInputStream()) {
                    while (is.read() >= 0);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }).start();

            boolean end = process.waitFor(timeoutSec, TimeUnit.SECONDS);
            if (end) {
                exitCode = process.exitValue();
            } else {
                throw new CommandExecuteFailedException("Command timeout. [CommandPath: " + cmdAndParams + "]");
            }

        } catch (InterruptedException e) {
            throw new CommandExecuteFailedException("Command interrupted. [CommandPath: " + cmdAndParams + "]", e);
        } finally {
            if (process.isAlive()) {
                process.destroy(); // プロセスを強制終了
            }
        }

        return exitCode;
    }

    private CommandExecutor() {
    }
}
