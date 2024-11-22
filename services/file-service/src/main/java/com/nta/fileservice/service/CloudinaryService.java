package com.nta.fileservice.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.nta.fileservice.enums.ErrorCode;
import com.nta.fileservice.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CloudinaryService {
    Cloudinary cloudinary;
    public String uploadImage(final byte[] base64) {
        try {
            return this.cloudinary
                    .uploader()
                    .upload(base64, ObjectUtils.asMap("resource_type", "auto"))
                    .get("secure_url")
                    .toString();
        } catch (Exception e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }
    }

    public List<String> uploadImages(final List<byte []> base64List) {
        final List<CompletableFuture<String>> futures = new ArrayList<>();

        for (byte[] base64 : base64List) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> this.uploadImage(base64));
            futures.add(future);
        }
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        try {
            allOf.get(); // Chờ tất cả các tác vụ hoàn thành
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot upload file: ", e);
            throw new AppException(ErrorCode.UPLOAD_FILE_FAILED);
        }

        // Trả về kết quả theo thứ tự ban đầu
        List<String> resultUrls = new ArrayList<>();
        for (CompletableFuture<String> future : futures) {
            resultUrls.add(future.join()); // Lấy kết quả của từng task
        }

        return resultUrls;
    }
}
