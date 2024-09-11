package org.badmus.settlement.files.service.impl;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.files.factory.FileParserFactory;
import org.badmus.settlement.files.service.FileParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileParserService {

    private final FileParserFactory fileParserFactory;

    public void parse(MultipartFile multipartFile, String fileType){
        FileParser fileParser = fileParserFactory.getFileParser(fileType);
        fileParser.parse(multipartFile);
    }
}
