package com.example.restapi.config;

import com.example.restapi.common.CSVUtil;
import com.example.restapi.service.ConfigurationService;
import com.example.restapi.service.TalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private final ConfigurationService configurationService;
    private final TalkService talkService;

    @Value("${data-file.path}")
    private String datafilePath;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        loadData();
    }

    private void loadData() {
        log.info("Start loading data file...");
        if (!configurationService.isDataLoadedSuccessfully()) {
            List<List<String>> csvData = CSVUtil.readCSV(datafilePath, "title", "author", "date", "views", "likes", "link");
            csvData.forEach(
                    data -> talkService.loadTalk(
                            data.get(0),
                            data.get(1),
                            data.get(2),
                            NumberUtils.isDigits(data.get(3)) ?
                                    Long.parseLong(data.get(3)) : 0,
                            NumberUtils.isDigits(data.get(4)) ? Long.parseLong(data.get(4)) : 0,
                            data.get(5)
                    )
            );
            configurationService.dataLoadedSuccessfully();
            log.info("Data file loaded successfully!");

        } else {
            log.info("Data file already processed successfully!");
        }
    }
}
