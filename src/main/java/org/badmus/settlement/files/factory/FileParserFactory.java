package org.badmus.settlement.files.factory;

import lombok.RequiredArgsConstructor;
import org.badmus.settlement.files.service.FileParser;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileParserFactory {

    private final Map<String, FileParser> fileParsers;

    // get file type
    public FileParser getFileParser(String fileType) {
        FileParser fileParser = fileParsers.get(fileType);
        if (Objects.isNull(fileParser)) {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        return fileParser;
    }
}
