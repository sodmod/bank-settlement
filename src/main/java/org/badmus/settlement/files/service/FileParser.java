package org.badmus.settlement.files.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileParser {

    void parse(MultipartFile multipartFile);
}
