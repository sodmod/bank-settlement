package org.badmus.settlement.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileManagementUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileManagementUtil.class);

    File file;

    public FileManagementUtil(String fileName) {
        String activeDir = System.getProperty("user.dir");
        activeDir = activeDir + File.separator + fileName;
        file = new File(activeDir);
    }

    public void createDirectory() {
        if (!file.exists()) {
            if (file.mkdirs()) {
                logger.info("Directory is created!");
            } else {
                logger.error("Cant Create Export Directory");
            }
        }
    }

    public void deleteFile() {
        if (file.exists()) {
            if (file.delete()) {
                logger.info("File is Deleted!");
            } else {
                logger.error("Cant Create File");
            }
        }
    }

    public File transferUploaded(MultipartFile multipartFile) {
        long mills = new Date().getTime();
        try {
            File fileLocation = new File(file.getAbsolutePath() + File.separator + mills + "_settlementUpload.xlsx");
            multipartFile.transferTo(fileLocation);
            return fileLocation;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllFileInFolder() {
        logger.info("Deleting all files in directory");
        try {
            FileUtils.cleanDirectory(file);
        } catch (IOException e) {
            logger.info("Error deleting files");
            e.printStackTrace();
        }
        logger.info("Deleted all files in directory");
    }
}
