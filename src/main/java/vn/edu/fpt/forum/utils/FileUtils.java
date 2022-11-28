package vn.edu.fpt.forum.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.forum.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * @author : Hoang Lam
 * @product : Charity Management System
 * @project : Charity System
 * @created : 21/11/2022 - 14:39
 * @contact : 0834481768 - hoang.harley.work@gmail.com
 **/
@Slf4j
public class FileUtils {

    public static File convertMultipartToFile(MultipartFile multipartFile) {
        String fileName = UUID.randomUUID().toString();
        String[] splitFileName = multipartFile.getOriginalFilename().split(".");
        String extension = splitFileName[splitFileName.length-1];
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/"+fileName + "." + extension);
        try {
            multipartFile.transferTo(convFile);
            return convFile;
        } catch (IOException ex) {
            try {
                Files.delete(Path.of(convFile.getPath()));
            } catch (IOException e) {
                log.error("Exception when delete convFile: {}", e.getMessage());
                throw new BusinessException("Can't delete convFile: " + e.getMessage() );
            }
            throw new BusinessException("Can't transfer multipart file: "+ ex.getMessage());
        }
    }
}
