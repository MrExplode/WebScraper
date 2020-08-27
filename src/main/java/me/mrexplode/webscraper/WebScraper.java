package me.mrexplode.webscraper;

import lombok.Getter;
import me.mrexplode.webscraper.utils.InverseSemaphore;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

@Getter
public class WebScraper {
    private final String url;
    private final String output;
    private final boolean recursive;
    private final ExecutorService executor;
    private final InverseSemaphore inverseSemaphore = new InverseSemaphore();
    private final Logger logger;

    private final Set<URL> scrapedUrls = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Set<Future<Set<String>>> futures = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public WebScraper(String url, String output, boolean recursive) {
        this.url = url;
        this.output = output;
        this.recursive = recursive;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        this.logger = Logger.getLogger("WebScraper");
    }

    public void start() {
        //do the magic
    }

}
