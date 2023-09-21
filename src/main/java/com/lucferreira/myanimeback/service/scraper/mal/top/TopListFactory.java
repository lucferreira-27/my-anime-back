package com.lucferreira.myanimeback.service.scraper.mal.top;

import com.lucferreira.myanimeback.exception.ArchiveScraperException;
import com.lucferreira.myanimeback.model.record.TopListRecord;
import com.lucferreira.myanimeback.util.DateParse;
import com.lucferreira.myanimeback.util.Regex;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TopListFactory {
    public TopListRecord createTopList(String url) throws ArchiveScraperException {

        final TopListRecord.TopType topType = url.contains("topanime.php") ? TopListRecord.TopType.ANIME : TopListRecord.TopType.MANGA;
        String datePart = Regex.match(url, "\\d{14}"); // Match 14 digits representing the date
        Date date = DateParse.stringToDate(datePart);

        if(url.endsWith(".php")){
            return new TopListRecord(topType,TopListRecord.TopSubtype.ALL,url,date);
        }

        final String urlSubType = Regex.match(url,"(?<=type=)[^&]+");
        final List<TopListRecord.TopSubtype> subtypes = List.of(TopListRecord.TopSubtype.values());
        final TopListRecord.TopSubtype subtype = getSubType(urlSubType, subtypes);
        return new TopListRecord(topType,subtype,url,date);

    }

    private TopListRecord.TopSubtype getSubType(String urlSubType, List<TopListRecord.TopSubtype> subtypes) {
        for (TopListRecord.TopSubtype subtype: subtypes) {
            if(subtype.getPossiblesName().contains(urlSubType)){
                return subtype;
            }
        }
        return null;
    }
}
