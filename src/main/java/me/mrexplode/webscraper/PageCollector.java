package me.mrexplode.webscraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class PageCollector implements Callable<Set<String>> {
    @NonNull private final WebClient webClient;
    @NonNull private final HtmlPage page;
    @NonNull private final WebScraper webScraper;
    private URL url;

    @Override
    public Set<String> call() {
        long start = System.currentTimeMillis();
        webScraper.getLogger().info("Started scraping page: " + url.toString());
        url = page.getUrl();
        webScraper.getScrapedUrls().add(url);

        Set<String> texts = new HashSet<>(Arrays.asList(page.getBody().asText().split(" ")));
        page.getAnchors().forEach(anchor -> {
            HtmlPage referencedPage = anchor.getHtmlPageOrNull();
            if (referencedPage != null && page.getBaseURL().equals(referencedPage.getBaseURL()) && !webScraper.getScrapedUrls().contains(referencedPage.getUrl()))
                webScraper.getFutures().add(webScraper.getExecutor().submit(new PageCollector(webClient, anchor.getHtmlPageOrNull(), webScraper)));
        });
        webScraper.getLogger().info("Finished scraping page \"" + page.getTitleText() + "\" in " + (System.currentTimeMillis() - start) + " ms");
        return texts;
    }
}
