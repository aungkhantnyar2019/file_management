package file.manager.rest.end.point;

import file.manager.services.FileManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/documents")
public class FileREST {
    private static final Logger log = LoggerFactory.getLogger(FileREST.class);

    @Autowired
    private FileManagerService fileManagerService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity createNewFile(
            @RequestParam(value = "file") MultipartFile file
    ) {
        log.info("Received request to save file: {}", file.getOriginalFilename());
        return fileManagerService.uploadFile(file);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.PUT)
    public ResponseEntity updateFile(
            @PathVariable String fileId,
            @RequestParam("file") MultipartFile file
    ) {
        log.info("Received request to get a file with id: {}", fileId);
        return fileManagerService.updateFile(fileId, file);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
    public ResponseEntity getFile(
            @PathVariable String fileId
    ) {
        log.info("Received request to update a file with id: {}", fileId);
        return fileManagerService.getFile(fileId);
    }

    @RequestMapping(value = "/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteFile(
            @PathVariable String fileId
    ) {
        log.info("Received request to update a file with id: {}", fileId);
        return fileManagerService.removeFile(fileId);
    }
}
