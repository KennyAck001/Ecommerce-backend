package com.ecommerce.EcomProj.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileHandler {
    public String uploadImage(String path, MultipartFile file) throws IOException;
}
