package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.FileType;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
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
    public List<FileData> getContent() {
        logger.debug("Fetching files and folder of path {}", searchPath);
        List<FileData> fileDataList = fetchContent(searchPath);
        logger.debug("{} items has been found ", fileDataList.size());
        return fileDataList;
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

    private List<FileData> fetchContent(String folder) {
        File searchingFolder = new File(folder);
        if (!fileRepository.exists(searchingFolder)) {
            return Collections.emptyList();
        }
        return fileRepository.getListOfFilesForPath(searchingFolder).stream()
                .map(this::createFileData)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<FileData> createFileData(File fileEntry) {
        FileData fileData = getCommonData(fileEntry);
        if (fileRepository.isDirectory(fileEntry)) {
            return getFolderSpecificationAndItsContent(fileData);
        }
        setFileSpecificProperties(fileEntry, fileData);
        return Collections.singletonList(fileData);
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

    private List<FileData> getFolderSpecificationAndItsContent(FileData fileData) {
        List<FileData> contents = fetchContent(fileData.getAbsolutePath());
        Long folderSize = contents.stream().filter(it -> it.getParent() == null)
                .peek(it -> it.setParent(fileData))
                .map(FileData::getFileSize).reduce(0L, Long::sum);
        fileData.setFileSize(folderSize);
        fileData.setFileType(FOLDER);
        List<FileData> fileDataList = new ArrayList<>();
        fileDataList.add(fileData);
        fileDataList.addAll(new ArrayList<>(contents));
        return fileDataList;
    }


    private void setFileSpecificProperties(File fileEntry, FileData fileData) {
        fileData.setFileSize(fileRepository.getSizeOf(fileEntry));
        fileData.setFileType(FileType.FILE);
    }

}
