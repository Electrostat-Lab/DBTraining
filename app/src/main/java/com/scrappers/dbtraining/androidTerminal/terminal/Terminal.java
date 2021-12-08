package com.scrappers.dbtraining.androidTerminal.terminal;

import com.scrappers.dbtraining.androidTerminal.command.Command;

import java.util.List;

public interface Terminal {
    enum Permission{
        USER(""), SU("su");

        final private String permission;
        Permission(final String permission){
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }
    }
    enum CommandRepetition{
        SINGLE_PHASE, REPEAT;
        private static boolean firstPhasePassed;
        public static void setFirstPhasePassed(final boolean value){
            firstPhasePassed = value;
        }

        public static boolean isFirstPhasePassed() {
            return firstPhasePassed;
        }
    }
    void registerCommandSet(final Command command);
    void unRegisterCommandSet(final Command command);
    void startExecutingCommands() throws InterruptedException;
    List<Command> getTerminalCommands();
    void clearCommands();
}
