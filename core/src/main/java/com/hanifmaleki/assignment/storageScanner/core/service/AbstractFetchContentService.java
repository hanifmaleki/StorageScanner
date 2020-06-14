package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.FileType;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.Date;

public abstract class AbstractFetchContentService implements FetchContentService {

    private final Logger logger = LoggerFactory.getLogger(AbstractFetchContentService.class);

    @Value("${search.path}")
    private String searchPath;

    protected FileRepository fileRepository;

    public AbstractFetchContentService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileData getContent() throws TheInputPathIsIncorrectException {
        logger.debug("Fetching files and folder of path {}", searchPath);
        File folder = new File(searchPath);
        if (fileRepository.exists(folder)) {
            return fetchContent(folder);
        }
        String message = String.format("Could not find the path %s. The input path is not a directory", searchPath);
        logger.error(message);
        throw new TheInputPathIsIncorrectException(message);
    }

    protected FileData fetchContent(File fileEntry) {
        if (fileRepository.isDirectory(fileEntry)) {
            return getFileDataTreeOfFolder(fileEntry);
        }
        return setFileSpecificProperties(fileEntry);
    }

    protected abstract FileData getFileDataTreeOfFolder(File folder);

    protected FileData getCommonData(File fileEntry) {
        FileData fileData = new FileData();
        fileData.setModificationDate(fileRepository.getLastModified(fileEntry));
        fileData.setFileName(fileRepository.getName(fileEntry));
        fileData.setExtension(fileRepository.getExtension(fileEntry));
        fileData.setAbsolutePath(fileRepository.getAbsolutePath(fileEntry));
        fileData.setScanDate(new Date());
        return fileData;
    }

    private FileData setFileSpecificProperties(File fileEntry) {
        FileData fileData = getCommonData(fileEntry);

        fileData.setFileSize(fileRepository.getSizeOf(fileEntry));
        fileData.setFileType(FileType.FILE);

        return fileData;
    }

    @Override
    public String getSearchPath() {
        return searchPath;
    }

    @Override
    public void setSearchPath(String searchPath) {
        this.searchPath = searchPath;
    }

    @Override
    public FileRepository getFileRepository() {
        return fileRepository;
    }

    @Override
    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
}
