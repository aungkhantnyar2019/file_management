package file.manager.db;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.io.FileOutputStream;

@Entity
@Table(name = "files_db")
public class FilesInfo {
    @Id
    public ObjectId _id;
    private Binary fileData;
    private String extension;
    private String contentType;
    private String fileId;

    public String getId() {
        return _id.toHexString();
    }

    public void setFile(MultipartFile file) {
        try {
            this.fileData = new Binary(BsonBinarySubType.BINARY, file.getBytes());
        } catch (Exception ex) {

        }
    }

    public File getFile() {
        String name = "files/output_file" + getExtension();
        File a = new File(name);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(a);
            fileOutputStream.write(fileData.getData());
        } catch (Exception ex) {

        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return a;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        if (extension == null) {
            return "";
        }
        return "." + extension;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileId() {
        return fileId;
    }

}
