package com.lucferreira.myanimeback.service.scraper;

import org.jsoup.select.Elements;

public record DocElement(Elements elements, DocSelector docSelector, int position) {

}
