package com.scrappers.dbtraining.androidTerminal.command;

import com.scrappers.dbtraining.androidTerminal.terminal.Terminal;

public interface Command {
    void asyncExecution(final Runtime jvmRuntime, final Terminal.Permission permission) throws InterruptedException;
    void setProcess(final Process process);
    void setCommands(final String[] commands);
    void getProcess();
    Terminal.Permission getPermission();
    String asyncStringRead();
    byte[] asyncBufferRead();
    void asyncStringWrite(final String values);
    void asyncBufferWrite(final byte[] buffer);
    void setPermission(final Terminal.Permission permission);
    void setId(final String id);
    String getId();
    void setIndex(int index);
    int getIndex();
    enum BufferSize{
        ZERO(0), ONE_KILOS(1024), TWO_KILOS(ONE_KILOS.memory * 2), FOUR_KILOS(TWO_KILOS.memory * 2), SIX_KILOS(TWO_KILOS.memory * 3),
        EIGHT_KILOS(TWO_KILOS.memory * 4), ONE_MEGA(ONE_KILOS.memory * 1024), TWO_MEGA(ONE_MEGA.memory * 2);
        private int memory;
        BufferSize(final int memory){
            this.memory = memory;
        }

        public int getMemory() {
            return memory;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }
    }
}
