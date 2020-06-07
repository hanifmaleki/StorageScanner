package com.hanifmaleki.assignment.storageScanner.rest;


import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.service.FileDataManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FileDataController {

    private final FileDataManager fileDataManager;

    @Autowired
    public FileDataController(FileDataManager fileDataManager) {
        this.fileDataManager = fileDataManager;
    }

    @GetMapping("/folders")
    List<FileData> getFolders() {
        return fileDataManager.getFolders();
    }

    @GetMapping("/filesizes")
    List<FileData> getFileSizesOfType(@RequestParam(name = "ftype", required = false) String type) {
        return fileDataManager.getSortedFilesOfType(type);
    }


}
