package com.scrappers.dbtraining.androidTerminal.command;

import android.view.View;

import com.scrappers.dbtraining.androidTerminal.terminal.AndroidTerminal;
import com.scrappers.dbtraining.androidTerminal.terminal.CommandLineExecutor;
import com.scrappers.dbtraining.androidTerminal.terminal.Terminal;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandSet implements Command, WritableData{

    private String[] commands;
    private String id;
    private Process process;
    private static final Object mutex0 = new Object();
    private Terminal.Permission permission;
    private boolean firstCommandSet = false;
    private static final Logger logger = Logger.getLogger(CommandSet.class.getName());
    private BufferSize bufferSize = BufferSize.ONE_KILOS;

    public CommandSet(final CommandLineExecutor commandLineExecutor, final String setId, final BufferSize bufferSize){
        //use the permissions provided by the android terminal class as the default permission.
        this.permission = commandLineExecutor.getPermission();
        this.id = setId;
        this.bufferSize = bufferSize;
        synchronized (mutex0) {
            mutex0.notify();
        }
    }

    public void setFirstCommandSet(boolean firstCommandSet) {
        this.firstCommandSet = firstCommandSet;
    }

    public boolean isFirstCommandSet() {
        return firstCommandSet;
    }

    @Override
    public void asyncExecution(Runtime jvmRuntime, Terminal.Permission permission) throws InterruptedException {
        //set the new permission specifically for this command set.
        permission = this.permission;
        //calling the synchronized on hte mutex obj would make the thread own this object monitor.
        synchronized (mutex0) {
            try {
                mutex0.wait();
                logger.log(Level.FINE, "Started executing command set with id : " + CommandSet.this.getId());
                //expand to accommodate --the 2 vital commands--
                final String[] finalCommands = new String[commands.length + 2];
                //copy values to the final commands args
                finalCommands[0] = permission.getPermission();
                finalCommands[1] = "-c";
                for (int i = 0; i < commands.length; i++) {
                    //copy commands to the final commands args
                    finalCommands[i + 2] = commands[i];
                }
                //assign the process to be used by input streams.
                CommandSet.this.process = jvmRuntime.exec(finalCommands);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //use this object as a mutex
            //calling the synchronized on hte mutex obj would make the thread own this object monitor.
            mutex0.notifyAll();
            logger.log(Level.FINE, "Finished executing command set with id : " + CommandSet.this.getId());
        }
    }

    @Override
    public void setProcess(Process process) {

    }

    @Override
    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    @Override
    public void getProcess() {

    }

    @Override
    public Terminal.Permission getPermission() {
        return permission;
    }

    @Override
    public String asyncStringRead() {
        try {
            return Executors.newSingleThreadExecutor().submit(new AsyncStringReader()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException("Cannot read from Streams. " + e.getMessage());
        }
    }

    @Override
    public byte[] asyncBufferRead() {
        try {
            return (byte[]) Executors.newSingleThreadExecutor().submit(new AsyncByteReader()).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException("Cannot read from Streams. " + e.getMessage());
        }
    }

    @Override
    public void asyncStringWrite(String values) {

    }

    @Override
    public void asyncBufferWrite(byte[] buffer) {

    }

    @Override
    public void setPermission(Terminal.Permission permission) {
        this.permission = permission;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setIndex(int index) {

    }

    @Override
    public int getIndex() {
        return 0;
    }

    @Override
    public void writeToFile(File file) {

    }

    @Override
    public void writeToConsole(Logger logger) {

    }

    @Override
    public void printToWidget(View view) {

    }

    private class AsyncStringReader extends Reader<String> {
        @Override
        public String call() throws Exception {
                logger.log(Level.FINE, "Started reading command set output with id : "+CommandSet.this.getId());
                 //use this object as a mutex
                waitForCommandExecution();
                final Process process = CommandSet.this.process;
                final InputStream inputStream = process.getInputStream();
                //use at least 2kbs buffer memory
                byte[] buffer = new byte[Math.max(bufferSize.getMemory(), 2048)];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) inputStream.read();
                }
                buffer = clampBufferLen(buffer);
                releaseLocks();
                logger.log(Level.FINE, "Finished reading command set output with id : "+CommandSet.this.getId());
            return new String(buffer);
        }
    }
    private class AsyncByteReader extends Reader<byte[]> {
        @Override
        public byte[] call() throws Exception {
                logger.log(Level.FINE, "Started reading command set output with id : "+CommandSet.this.getId());
                //use this object as a mutex
                waitForCommandExecution();
                final Process process = CommandSet.this.process;
                final InputStream inputStream = process.getInputStream();
                byte[] buffer = new byte[2048];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) inputStream.read();
                }
                buffer = clampBufferLen(buffer);
                releaseLocks();
                logger.log(Level.FINE, "Finished reading command set output with id : "+CommandSet.this.getId());
            return buffer;
        }
    }
    protected class AsyncWriter extends Thread{
        @Override
        public void run() {
            //use this object as a mutex
            waitForCommandExecution();
        }
        private void waitForCommandExecution(){
            try {
                synchronized (mutex0) {
                    mutex0.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private abstract static class Reader<T> implements Callable<T>{
        protected void waitForCommandExecution() throws InterruptedException{
            synchronized (mutex0) {
                mutex0.wait();
            }
        }
        protected void releaseLocks(){
            synchronized (mutex0) {
                mutex0.notifyAll();
            }
        }
        /**
         * Clamps the buffer length to the only viable readable alphabetic based bytes.
         * @param buffer the byte buffer to clamp.
         * @return a new clamped buffer.
         */
        protected synchronized byte[] clampBufferLen(byte[] buffer){
            final byte[] tempBuffer = new byte[buffer.length];
            int byteIndex = 0;
            int bufferLen = 0;
            //deep copy available values to a temporary buffer & recalculate the length of the initial buffer
            for(final byte val : buffer){
                if(val >= 0){
                    //collect viable values & arrange them serially
                    tempBuffer[byteIndex++] = val;
                    ++bufferLen;
                }
            }
            final byte[] newBuffer = new byte[bufferLen];
            for(int i = 0; i < newBuffer.length; i++){
                //deep copy data into the new buffer
                newBuffer[i] = tempBuffer[i];
            }
            //return a new clamped buffer to the available data
            return newBuffer;
        }
    }

}
