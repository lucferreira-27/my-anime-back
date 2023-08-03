package com.lucferreira.myanimeback.service.scraper.mal.top;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public record ListElementID(Elements elements, Document doc, Integer selectorId) {
}
