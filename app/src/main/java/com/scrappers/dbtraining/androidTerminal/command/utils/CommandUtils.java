package com.scrappers.dbtraining.androidTerminal.command.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import com.scrappers.dbtraining.androidTerminal.command.Command;
import com.scrappers.dbtraining.androidTerminal.command.CommandSet;
import com.scrappers.dbtraining.androidTerminal.terminal.AndroidTerminal;
import com.scrappers.dbtraining.androidTerminal.terminal.Terminal;

/**
 * Summarizes the common commands used by superusers, including dumping commands, profiling ones, permissive commands.
 * @author pavl_g.
 */
public final class CommandUtils {
    private CommandUtils(){
    }

    private static Command createCommand(final String[] commands, final Terminal.Permission permission, final String name, final Command.BufferSize bufferMemory) throws InterruptedException {
            final AndroidTerminal terminal = new AndroidTerminal();
            terminal.setPermission(permission);
            final Command command = new CommandSet(terminal, name, bufferMemory);
            command.setCommands(commands);
            command.setPermission(permission);
            command.asyncExecution(Runtime.getRuntime(), command.getPermission());
        return command;
    }

    public static Command dumpGLESStats() throws InterruptedException {
        return CommandUtils.createCommand(new String[]{"dumpsys | grep GLES"}, Terminal.Permission.SU, "GLES Stats", Command.BufferSize.ONE_KILOS);
    }
    public static Command dumpCpuInfo() throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"dumpsys | grep cpu"}, Terminal.Permission.SU, "device cpu info", Command.BufferSize.ONE_KILOS);
    }
    public static Command dumpAppPackageInfo(final Context context) throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"dumpsys | grep " + context.getPackageName()}, Terminal.Permission.SU, "appPackage info", Command.BufferSize.TWO_KILOS);
    }
    public static Command dumpAppGfxInfo(final Context context) throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"dumpsys gfxinfo "+ context.getPackageName()}, Terminal.Permission.SU, "App gfxinfo", Command.BufferSize.TWO_MEGA);
    }
    public static Command dumpGfxFramesInfo(final Context context) throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"dumpsys gfxinfo "+ context.getPackageName() + " framestats"}, Terminal.Permission.SU, "FrameStats gfxinfo", Command.BufferSize.TWO_MEGA);
    }
    public static Command uninstallApp(final String packageName) throws InterruptedException {
        return CommandUtils.createCommand(new String[]{"pm uninstall " + packageName}, Terminal.Permission.SU, "Uninstall Package App", Command.BufferSize.ZERO);
    }
    public static Command captureScreenShot(final Context context, final String fileName) throws InterruptedException {
        String screenShotDir = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Android/data" + context.getPackageName() + "files/Screenshots";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            screenShotDir = context.getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS).getAbsolutePath();
        }
        return CommandUtils.createCommand(new String[]{"screencap " + (screenShotDir + "/" + fileName)}, Terminal.Permission.SU, "Uninstall Package App", Command.BufferSize.ZERO);
    }
    public static Command killAllProcesses() throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"am kill-all"}, Terminal.Permission.SU, "kill processses",Command.BufferSize.ZERO);
    }
    public static Command listDeviceFeatures() throws InterruptedException{
        return CommandUtils.createCommand(new String[]{"pm list features"}, Terminal.Permission.SU, "list device features", Command.BufferSize.TWO_KILOS);
    }
    public static Command grantPermission(final Context context, final String permission) throws InterruptedException {
        return CommandUtils.createCommand(new String[]{"pm grant " + context.getPackageName() +" "+ permission}, Terminal.Permission.SU, "Grant Permission", Command.BufferSize.ONE_KILOS);
    }
    public static Command revokePermission(final Context context, final String permission) throws InterruptedException {
        return CommandUtils.createCommand(new String[]{"pm revoke " + context.getPackageName() +" "+ permission}, Terminal.Permission.SU, "Grant Permission", Command.BufferSize.ONE_KILOS);
    }
    public static Command collectExecutionProfile(final Context context) throws InterruptedException {
        return CommandUtils.createCommand(new String[]{"cmd package dump-profiles " + context.getPackageName()}, Terminal.Permission.SU, "Dump Profile", Command.BufferSize.TWO_KILOS);
    }
}
