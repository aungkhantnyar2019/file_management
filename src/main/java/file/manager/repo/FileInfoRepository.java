package file.manager.repo;

import file.manager.db.FilesInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileInfoRepository extends MongoRepository<FilesInfo, String> {
    FilesInfo findByFileId(String docId);
}
