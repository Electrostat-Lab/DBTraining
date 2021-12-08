package com.scrappers.dbtraining.androidTerminal.command;

import android.view.View;
import java.io.File;
import java.util.logging.Logger;

public interface WritableData extends Command{
    void writeToFile(final File file);
    void writeToConsole(final Logger logger);
    void printToWidget(final View view);
}
