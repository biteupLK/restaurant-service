package com.biteup.restaurant_service.service;


import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import java.io.IOException;
// import java.net.URL;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ImageUploaderService {

  private final Storage storage;

  public String uploadToGcs(String bucketName, MultipartFile file)
    throws IOException {
    String fileName =
      "restaurent/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

    BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName)
      .setContentType(file.getContentType())
      .build();

    storage.create(blobInfo, file.getBytes());

    return fileName;
  }
}
