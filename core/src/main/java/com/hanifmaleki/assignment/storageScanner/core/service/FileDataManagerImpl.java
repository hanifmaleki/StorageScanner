package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;

@Service
public class FileDataManagerImpl implements FileDataManager {

    private final Logger logger = LoggerFactory.getLogger(FileDataManager.class);

    private final FetchContentService fetchContentService;

    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileDataManagerImpl(FetchContentService fetchContentService, FileDataRepository fileDataRepository) {
        this.fetchContentService = fetchContentService;
        this.fileDataRepository = fileDataRepository;
    }


    @Override
    @Scheduled(fixedDelay = 60 * 1000)
    @Transactional
    public void getLatestFilesAndStore() {
        logger.debug("Fetching files");
        List<FileData> content = fetchContentService.getContent();
        content.forEach(this::updateOrSave);
        logger.debug("{} files has been fetched", content.size());
    }

    public void updateOrSave(FileData fileData) {
        FileData attachedFileData = fileDataRepository.findById(fileData.getAbsolutePath()).orElse(null);
        if (attachedFileData == null) {
            logger.debug("Save {}", fileData.getAbsolutePath());
            fileDataRepository.save(fileData);
        } else {
            logger.debug("Update {}", fileData.getAbsolutePath());
            attachedFileData.setFileSize(fileData.getFileSize());
            attachedFileData.setFileName(fileData.getFileName());
            attachedFileData.setExtension(fileData.getExtension());
            attachedFileData.setFileType(fileData.getFileType());
            attachedFileData.setModificationDate(fileData.getModificationDate());
            attachedFileData.setParent(fileData.getParent());
            attachedFileData.setScanDate(fileData.getScanDate());
        }
    }

    @Override
    public List<FileData> getFolders() {
        return fileDataRepository.findByFileType(FOLDER, Sort.by("fileName"));
    }


    @Override
    public List<FileData> getSortedFilesOfType(String type) {
        if (type == null || type.isEmpty()) {
            return fileDataRepository.findByFileType(FOLDER, Sort.by("fileSize"));
        }
        return fileDataRepository.findByFileTypeOrExtension(FOLDER, type, Sort.by("fileSize"));
    }

}
