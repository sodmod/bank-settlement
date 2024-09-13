package org.badmus.settlement.utils;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class MultipartUtils {

    public static final String EXPORTS = "exports";
    public static final String UPLOADS = "uploads";
    private static final List<String> EXPECTED_COLUMNS = Arrays.asList("Column1", "Column2", "Column3");


    private FileManagementUtil createDirectory(String name) {
        FileManagementUtil fileManagementUtil = new FileManagementUtil(name);
        fileManagementUtil.createDirectory();
        return fileManagementUtil;
    }

    public <K> List<K> convertExcelToDTO(ByteArrayResource byteArrayResource, Class<K> type, String excelType) {
        log.info("converting excel to DTO for {}...", type.getName());

        List<K> excelDTO;
        try {
            excelDTO = switch (excelType) {
                case "xlsx", "xls", "csv" -> Poiji.fromExcel(byteArrayResource.getInputStream(),
                        PoijiExcelType.valueOf(excelType.toUpperCase()), type);
                default -> throw new Exception("Excel type not supported");
            };

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while parsing excel");
        }

        return excelDTO;
    }

    public int countExcelRows(MultipartFile file) {
        int rowCount = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while (reader.readLine() != null) {
                rowCount++;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("The file is empty");
        }
        // exclude the header row
        return rowCount - 1;
    }

    public void checkExcelSheetHeaders(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine();  // Read the first line (headers)
            if (headerLine != null) {
                List<String> headers = Arrays.asList(headerLine.split(","));  // Split by comma for CSV columns
                if (!new HashSet<>(headers).containsAll(EXPECTED_COLUMNS)) {
                    throw new IllegalArgumentException("CSV file does not contain the required columns.");
                }
            } else {
                throw new IllegalArgumentException("The file is empty.");
            }
        } catch (IOException exception) {
            throw new IllegalArgumentException("The file is empty");
        }
    }


    private void deleteAllFilesInFolders(String... folderNames) {
        for (String folderName : folderNames) {
            FileManagementUtil fileManagementUtil = new FileManagementUtil(folderName);
            fileManagementUtil.deleteAllFileInFolder();
        }
    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        return "";
    }
}
