package org.badmus.settlement.utils;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MultipartUtils {

    public static final String EXPORTS = "exports";
    public static final String UPLOADS = "uploads";


    private FileManagementUtil createDirectory(String name) {
        FileManagementUtil fileManagementUtil = new FileManagementUtil(name);
        fileManagementUtil.createDirectory();
        return fileManagementUtil;
    }

    public <K> List<K> convertExcelToDTO(ByteArrayResource byteArrayResource, Class<K> type, String excelType) {
        log.info("converting excel to DTO for {}...", type.getName());

        List<K> excelDTO = new ArrayList<>();
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


    private void deleteAllFilesInFolders(String... folderNames) {
        for (String folderName : folderNames) {
            FileManagementUtil fileManagementUtil = new FileManagementUtil(folderName);
            fileManagementUtil.deleteAllFileInFolder();
        }
    }
}
