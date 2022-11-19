package com.mycloudstorage.service;

import com.mycloudstorage.model.File;
import com.mycloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int create(File file) {
        return fileMapper.insert(file);
    }

    public List<File> getByUserId(Integer userId) {
        return fileMapper.getFileByUserId(userId);
    }

    public File getByFilename(String filename) {
        return fileMapper.getFileByFilename(filename);
    }

    public File getById(Integer fileId) {
        return fileMapper.getFileByFileId(fileId);
    }

    public void delete(Integer fileId) {
        fileMapper.delete(fileId);
    }
}
