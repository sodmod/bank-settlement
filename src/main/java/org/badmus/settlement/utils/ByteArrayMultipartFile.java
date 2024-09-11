package org.badmus.settlement.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ByteArrayMultipartFile implements MultipartFile {


    private final ByteArrayResource byteArrayResource;
    private final String originalFilename;
    private final String contentType;

    public ByteArrayMultipartFile(ByteArrayResource byteArrayResource, String originalFilename, String contentType) {
        this.byteArrayResource = byteArrayResource;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return originalFilename; // Can return original filename or any identifier
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return byteArrayResource.getByteArray().length == 0;
    }

    @Override
    public long getSize() {
        return byteArrayResource.getByteArray().length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return byteArrayResource.getByteArray();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(byteArrayResource.getByteArray());
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("Not implemented");
    }
}
