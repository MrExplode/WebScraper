package me.mrexplode.webscraper;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.*;

public class Bootstrap {

    public static void main(String[] args) {
        Logger logger = initLogger(Level.INFO);
        OptionParser parser = new OptionParser();
        parser.allowsUnrecognizedOptions();
        parser.accepts("recursive");
        parser.accepts("r");

        OptionSpec<String> urlSpec = parser.accepts("url").withRequiredArg();
        OptionSpec<String> outputSpec = parser.accepts("output").withRequiredArg();
        OptionSpec<String> ignored = parser.nonOptions();

        OptionSet optionSet = parser.parse(args);
        List<String> ignoredArgs = optionSet.valuesOf(ignored);
        logger.info("Ignored arguments: " + ignoredArgs);

        if (!optionSet.has(urlSpec)) {
            logger.severe("Missing argument: url");
            return;
        }
        boolean recursive = optionSet.has("r") || optionSet.has("recursive");
        String url = optionSet.valueOf(urlSpec);
        String output = optionSet.has(outputSpec) ? optionSet.valueOf(outputSpec) : "output.txt";

        new WebScraper(url, output, recursive).start();
    }

    private static Logger initLogger(Level level) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Logger logger = Logger.getLogger("WebScraper");
        logger.setUseParentHandlers(false);
        logger.setLevel(level);

        ConsoleHandler consoleManager = new ConsoleHandler() {
            @Override
            protected synchronized void setOutputStream(OutputStream out) throws SecurityException {
                super.setOutputStream(System.out);
            }
        };

        consoleManager.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                Date date = new Date(record.getMillis());

                return "[" + simpleDateFormat.format(date) + "] [" + record.getLevel().getName() + "] " + record.getMessage() + "\n";
            }
        });
        logger.addHandler(consoleManager);
        return logger;
    }
}
