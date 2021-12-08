package com.scrappers.dbtraining.androidTerminal.terminal;

import com.scrappers.dbtraining.androidTerminal.command.Command;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandLineExecutor implements Terminal{

    private final List<Command> commands = new ArrayList<>();
    //default permission is regular user permission
    private Terminal.Permission permission = Permission.USER;
    //default run is the single run
    private Terminal.CommandRepetition commandRepetition = CommandRepetition.SINGLE_PHASE;


    @Override
    public void registerCommandSet(Command command) {
        if(command == null){
            return;
        }
        if(!commands.contains(command)){
            commands.add(command);
        }
    }

    @Override
    public void unRegisterCommandSet(Command command) {
        if(command == null){
            return;
        }
        commands.remove(command);
    }

    @Override
    public void startExecutingCommands() throws InterruptedException {
        for(final Command command : getTerminalCommands()){
            //entered command execution loop
            //check for the repetition flag
            switch (commandRepetition) {
                case SINGLE_PHASE:
                    //check for the first phase pass -> execute only one time
                    if (CommandRepetition.isFirstPhasePassed() && command == getTerminalCommands().get(getTerminalCommands().size() - 1)) {
                        return;
                    }
                    doExecute(Runtime.getRuntime(), command, permission);
                    CommandRepetition.setFirstPhasePassed(true);
                break;

                case REPEAT:
                    doExecute(Runtime.getRuntime(), command, permission);
                    //do a recursive call if this was the last command
                    final Command lastCommand = getTerminalCommands().get(getTerminalCommands().size() - 1);
                    if (command == lastCommand) {
                        startExecutingCommands();
                    }
                break;
            }
        }
    }

    @Override
    public List<Command> getTerminalCommands() {
        return commands;
    }

    @Override
    public void clearCommands() {
        commands.clear();
    }
    public abstract void doExecute(final Runtime jvmRuntime, final Command command, final Terminal.Permission permission) throws InterruptedException;

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setCommandRepetition(CommandRepetition commandRepetition) {
        this.commandRepetition = commandRepetition;
    }

    public CommandRepetition getCommandRepetition() {
        return commandRepetition;
    }
}
