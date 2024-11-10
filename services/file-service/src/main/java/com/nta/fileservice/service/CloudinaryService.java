package com.nta.fileservice.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nta.fileservice.enums.ErrorCode;
import com.nta.fileservice.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryService {
    Cloudinary cloudinary;

    public Map upload(MultipartFile file) {
        try {
            return this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
        } catch (Exception e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public Map upload(byte[] b) {
        try {
            return this.cloudinary.uploader().upload(b, ObjectUtils.asMap("resource_type", "auto"));
        } catch (Exception e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public String uploadImage(final String base64) {
        byte[] bytes = Base64.getDecoder().decode(base64.getBytes());
        try {
            return this.cloudinary.uploader().upload(bytes, ObjectUtils.asMap("resource_type", "auto")).get("secure_url").toString();
        } catch (Exception e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public List<String> uploadImages(final List<String> base64List) {
        final List<CompletableFuture<String>> futures = new ArrayList<>();

        for (String base64 : base64List) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> this.uploadImage(base64));
            futures.add(future);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.get();  // Chờ tất cả các tác vụ hoàn thành
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }

        // Trả về kết quả theo thứ tự ban đầu
        List<String> resultUrls = new ArrayList<>();
        for (CompletableFuture<String> future : futures) {
            resultUrls.add(future.join());  // Lấy kết quả của từng task
        }

        return resultUrls;
    }
}
