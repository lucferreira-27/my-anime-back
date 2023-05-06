package com.lucferreira.myanimeback.service.scraper;

import com.lucferreira.myanimeback.exception.ScrapeConnectionError;
import com.lucferreira.myanimeback.exception.SelectorQueryException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ScrapeHelper {

    public Connection.Response connectToUrl(String url) throws ScrapeConnectionError {
        try {
            Connection.Response response = Jsoup.connect(url).followRedirects(true).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ScrapeConnectionError("Error scraping data from URL: " + url + ". The connection failed. Please check the url and try again later.");
        }
    }
    public Document jsoupParse(Connection.Response response) throws ScrapeConnectionError {
        String url = response.url().toString();
        try {
            return response.parse();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ScrapeConnectionError("Error scraping data from URL: " + url + ". The connection failed. Please check the url and try again later.");
        }
    }


    public Optional<DocElement> queryElements(Document doc, Element parentElement, List<DocSelector> docSelectors) throws SelectorQueryException {

        for (DocSelector docSelector : docSelectors) {
            String selector = docSelector.getSelector();
            Elements element = docSelector.isParentSelector() ? parentElement.select(selector) : doc.select(selector);
            if (!element.isEmpty()) {
                return Optional.of(new DocElement(element,docSelector));
            }
        }
        return  Optional.empty();
    }
}
