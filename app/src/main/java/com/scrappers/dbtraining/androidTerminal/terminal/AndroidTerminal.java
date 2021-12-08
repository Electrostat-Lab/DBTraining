package com.scrappers.dbtraining.androidTerminal.terminal;

import com.scrappers.dbtraining.androidTerminal.command.Command;

public class AndroidTerminal extends CommandLineExecutor{
    public static final Object terminalMutex = new Object();

    public AndroidTerminal(){
        synchronized (terminalMutex){
            terminalMutex.notifyAll();
        }
    }

    @Override
    public void doExecute(Runtime jvmRuntime, Command command, Permission permission) throws InterruptedException {
            command.asyncExecution(jvmRuntime, permission);
    }
}
