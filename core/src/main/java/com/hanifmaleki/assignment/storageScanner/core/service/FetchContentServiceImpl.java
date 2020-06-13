package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.FileType;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;

@Service
public class FetchContentServiceImpl implements FetchContentService {
    final Logger logger = LoggerFactory.getLogger(FetchContentServiceImpl.class);

    @Value("${search.path}")
    private String searchPath;

    private FileRepository fileRepository;

    @Autowired
    public FetchContentServiceImpl(FileRepository fileRepository) {
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

    private FileData fetchContent(File fileEntry) {
        if (fileRepository.isDirectory(fileEntry)) {
            return getFolderSpecificationAndItsContent(fileEntry);
        }
        return setFileSpecificProperties(fileEntry);
    }


    private FileData getCommonData(File fileEntry) {
        FileData fileData = new FileData();
        fileData.setModificationDate(fileRepository.getLastModified(fileEntry));
        fileData.setFileName(fileRepository.getName(fileEntry));
        fileData.setExtension(fileRepository.getExtension(fileEntry));
        fileData.setAbsolutePath(fileRepository.getAbsolutePath(fileEntry));
        fileData.setScanDate(new Date());
        return fileData;
    }

    private FileData getFolderSpecificationAndItsContent(File folder) {
        FileData fileData = getCommonData(folder);

        List<FileData> children = fileRepository.getListOfFilesForPath(folder)
                .stream()
                .map(this::fetchContent)
                .peek(it -> it.setParent(fileData))
                .collect(Collectors.toList());

        Long folderSize = children.stream().map(it -> it.getFileSize()).reduce(0L, Long::sum);

        fileData.setChildren(children);
        fileData.setFileSize(folderSize);
        fileData.setFileType(FOLDER);
        return fileData;
    }


    private FileData setFileSpecificProperties(File fileEntry) {
        FileData fileData = getCommonData(fileEntry);

        fileData.setFileSize(fileRepository.getSizeOf(fileEntry));
        fileData.setFileType(FileType.FILE);

        return fileData;
    }

}
