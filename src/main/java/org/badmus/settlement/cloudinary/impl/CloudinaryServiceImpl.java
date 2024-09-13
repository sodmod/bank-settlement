package org.badmus.settlement.cloudinary.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.badmus.settlement.dto.SettlementDTO;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.badmus.settlement.utils.GeneralUtils.generateString;


@Slf4j
@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final static int CHUNK_SIZE = 100 * 1024 * 1024;

    private final Cloudinary cloudinary;


    @Override
    public <K> Map<String, K> genericUpload(SettlementDTO settlementDTO, Class<K> classType) {
        Map<Object, Object> mapObjectObject = new HashMap<>();
        Map<String, K> responseDTOMap = new HashMap<>();

        String public_id;

        if (settlementDTO.getPublicId().isEmpty()) {
            public_id = settlementDTO.getPublicId();
        } else {
            public_id = generateString();
        }

        // Create an instance of the generic class `K`
        K instance = null;
        try {
            mapObjectObject.put("folder", settlementDTO.getFile().getOriginalFilename());
            mapObjectObject.put("public_id", public_id);
            mapObjectObject.put("resource_type", "auto");
            mapObjectObject.put("chunk_size", CHUNK_SIZE);

            Map map = cloudinary.uploader().uploadLarge(settlementDTO.getFile().getBytes(), mapObjectObject);

            instance = classType.getDeclaredConstructor().newInstance();

            setProperty(instance, classType, "setSecureUrl", map.get("secure_url").toString());
            setProperty(instance, classType, "setFileId", map.get("public_id").toString());
            setProperty(instance, classType, "setResponseMessage", "SUCCESS");

            responseDTOMap.put("response", instance);
            log.info("uploaded successfully");
        } catch (Exception e) {
            if (instance != null) {
                try {
                    log.info("There is an error but updating map property ResponseMessage to FAILURE");
                    setProperty(instance, classType, "setResponseMessage", "FAILURE");
                    responseDTOMap.put("response", instance);
                } catch (Exception reflectionException) {
                    log.error("Reflection error: {}", reflectionException.getMessage());
                }
            }
            log.error("There is an error {}", e.getMessage());
            throw new RuntimeException("could not upload file");
        }
        return responseDTOMap;
    }

    @Override
    public ByteArrayResource download(String cloudUrl) {
        try {
            // Get a ByteArrayResource from the URL
            URL url = new URL(cloudUrl);
            InputStream inputStream = url.openStream();
            byte[] out = toByteArray(inputStream);

            return new ByteArrayResource(out);

        } catch (Exception ex) {
            log.error("FAILED to download the file: {}", "");
            return null;
        }
    }

    @Override
    public void deleteFolder(String folderName) {
        try {
            cloudinary.api().deleteFolder(folderName, new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException("");
        }
    }

    @Override
    public void deleteSingleAsset(String public_id) {
        try {
            cloudinary.uploader().destroy(public_id, new HashMap<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <K> void setProperty(K instance, Class<K> classType, String methodName, String value) throws Exception {
        Method method = classType.getMethod(methodName, String.class);
        method.invoke(instance, value);
    }

}
