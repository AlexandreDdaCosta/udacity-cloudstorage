package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileMapper fileMapper;

    public File getFile(Integer id, Integer userId) {
        return fileMapper.getFile(id, userId);
    }

    public Integer getFileById(Integer id, Integer userId) {
        return fileMapper.getFileId(id, userId);
    }

    public List<File> getUserFiles(Integer userId) {
        return fileMapper.getUserFiles(userId);
    }

    public void addFile(FileForm fileForm) throws IOException {
        MultipartFile multiPartFile = fileForm.getFileUpload();
        InputStream inputStream = multiPartFile.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int bytesRead;
        byte[] bufferedData = new byte[1024];
        while ((bytesRead = inputStream.read(bufferedData, 0, bufferedData.length)) != -1) {
            outputStream.write(bufferedData, 0, bytesRead);
        }
        outputStream.flush();
        File file = new File()
                .setName(multiPartFile.getOriginalFilename())
                .setContentType(multiPartFile.getContentType())
                .setFileSize(String.valueOf(multiPartFile.getSize()))
                .setData(outputStream.toByteArray())
                .setUserId(userMapper.getUserIdByName(fileForm.getAuthUsername()))
                .validate();
        fileMapper.addFile(file);
    }

    public void deleteFile(Integer id, Integer userId) {
        fileMapper.deleteFile(id, userId);
    }
}
