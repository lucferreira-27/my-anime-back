package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.TopList;
import com.lucferreira.myanimeback.util.DateParse;
import com.lucferreira.myanimeback.util.Regex;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TopListFactory {
    public TopList createTopList(String url) throws ArchiveScraperException {

        final TopList.TopType topType = url.contains("topanime.php") ? TopList.TopType.ANIME : TopList.TopType.MANGA;
        String datePart = Regex.match(url, "\\d{14}"); // Match 14 digits representing the date
        Date date = DateParse.stringToDate(datePart);

        if(url.endsWith(".php")){
            return new TopList(topType,TopList.TopSubtype.ALL,url,date);
        }

        final String urlSubType = Regex.match(url,"(?<=type=)[^&]+");
        final List<TopList.TopSubtype> subtypes = List.of(TopList.TopSubtype.values());
        final TopList.TopSubtype subtype = getSubType(urlSubType, subtypes);
        return new TopList(topType,subtype,url,date);

    }

    private TopList.TopSubtype getSubType(String urlSubType, List<TopList.TopSubtype> subtypes) {
        for (TopList.TopSubtype subtype: subtypes) {
            if(subtype.getPossiblesName().contains(urlSubType)){
                return subtype;
            }
        }
        return null;
    }
}
