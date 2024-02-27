package com.pblintern.web.Batch.Processor;

import com.pblintern.web.Payload.Requests.CSVRequest;
import org.springframework.batch.item.ItemProcessor;

public class CSVExportProcessor implements ItemProcessor<CSVRequest, CSVRequest> {
    @Override
    public CSVRequest process(CSVRequest item) throws Exception {
        return item;
    }
}
