package org.badmus.settlement.cloudinary.impl;

import com.cloudinary.Cloudinary;
import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.badmus.settlement.cloudinary.dto.CloudinaryResponseDTO;
import org.badmus.settlement.dto.SettlementDTO;
import org.badmus.settlement.cloudinary.CloudinaryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
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

        if(settlementDTO.getPublicId().isEmpty()){
            public_id = settlementDTO.getPublicId();
        }else{
            public_id = generateString();
        }

        try {
            mapObjectObject.put("folder", settlementDTO.getFile().getOriginalFilename());
            mapObjectObject.put("public_id", public_id);
            mapObjectObject.put("resource_type", "auto");
            mapObjectObject.put("chunk_size", CHUNK_SIZE);

            Map map = cloudinary.uploader().uploadLarge(settlementDTO.getFile().getBytes(), mapObjectObject);

            // Create an instance of the generic class `K`
            K instance = classType.getDeclaredConstructor().newInstance();

            Method setFileId = classType.getMethod("setFileId", String.class);
            setFileId.invoke(instance, map.get("public_id").toString());

            Method setSecureUrl = classType.getMethod("setSecureUrl", String.class);
            setSecureUrl.invoke(instance, map.get("secure_url").toString());

            responseDTOMap.put("response", instance);

            log.info("uploaded successfully");
        } catch (IOException e) {
            //  send failure message
            log.error(e.getMessage());
            throw new RuntimeException("could not upload file");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
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
            ByteArrayResource resource = new ByteArrayResource(out);

           return resource;

        } catch (Exception ex) {
            log.error("FAILED to download the file: {}" , "");
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



}
