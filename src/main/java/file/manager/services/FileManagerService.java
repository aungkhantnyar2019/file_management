package file.manager.services;

import file.manager.db.FilesInfo;
import file.manager.repo.FileInfoRepository;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class FileManagerService {
    private static final Logger log = LoggerFactory.getLogger(FileManagerService.class);

    @Autowired
    private FileInfoRepository fileInfoRepository;

    public ResponseEntity uploadFile(MultipartFile file) {
        FilesInfo filesInfo = new FilesInfo();
        filesInfo.setFileId(generateUniqueDocId());
        setInfoFromMultipartFile(filesInfo, file);
        filesInfo = fileInfoRepository.insert(filesInfo);
        log.info("File saved successfully!");
        return new ResponseEntity(filesInfo.getFileId(), HttpStatus.CREATED);
    }

    private String generateUniqueDocId() {
        String uniqueDocId = RandomStringUtils.randomAlphanumeric(20);
        FilesInfo filesInfo = fileInfoRepository.findByFileId(uniqueDocId);
        if (filesInfo == null) {
            return uniqueDocId;
        }
        return generateUniqueDocId();
    }

    public ResponseEntity getFile(String fileId) {
        FilesInfo filesInfo = fileInfoRepository.findByFileId(fileId);
        if (filesInfo == null) {
            log.error("File not foune with id : {}", fileId);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        try {
            File f = filesInfo.getFile();
            byte[] document = FileCopyUtils.copyToByteArray(f);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.valueOf(filesInfo.getContentType()));
            ResponseEntity<File> responseEntity =
                    new ResponseEntity(document, header, HttpStatus.OK);
            log.info("File found!");
            return responseEntity;
        } catch (Exception ex) {
            log.error("ERROR here! {}", ex);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity removeFile(String fileId) {
        FilesInfo filesInfo = fileInfoRepository.findByFileId(fileId);
        if (filesInfo == null) {
            log.error("File not foune with id : {}", fileId);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        fileInfoRepository.deleteById(filesInfo._id.toHexString());
        log.info("File removed with id: {}", fileId);
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NO_CONTENT);
        return responseEntity;
    }

    public ResponseEntity updateFile(String fileId, MultipartFile file) {

        FilesInfo filesInfo = fileInfoRepository.findByFileId(fileId);
        if (filesInfo == null) {
            log.error("File not foune with id : {}", fileId);
            ResponseEntity responseEntity = new ResponseEntity(HttpStatus.NOT_FOUND);
            return responseEntity;
        }
        setInfoFromMultipartFile(filesInfo, file);
        fileInfoRepository.save(filesInfo);
        log.info("File updated with id: {} by {}", fileId, file.getOriginalFilename());
        ResponseEntity responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return responseEntity;
    }

    private void setInfoFromMultipartFile(FilesInfo filesInfo, MultipartFile file) {
        String contentType = file.getContentType();
        String extension = getExtension(file.getOriginalFilename());
        filesInfo.setFile(file);
        filesInfo.setContentType(contentType);
        filesInfo.setExtension(extension);
    }

    private String getExtension(String fileName) {
        int lastPeriod = fileName.lastIndexOf('.');
        if (lastPeriod == -1) {
            return null;
        }
        return fileName.substring(lastPeriod + 1);
    }
}
