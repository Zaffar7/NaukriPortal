package naukri.portal.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class fileUploadutil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadpath = Paths.get(uploadDir);
        if(!Files.exists(uploadpath)){
            Files.createDirectories(uploadpath);
        }

        try(InputStream inputStream = multipartFile.getInputStream()){
            Path path= uploadpath.resolve(fileName);
            System.out.println("filepath" +path);
            System.out.println("filename"+fileName);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
    catch(IOException ioe){
            throw new IOException("could not save image"+fileName,ioe);
        }
}
}

