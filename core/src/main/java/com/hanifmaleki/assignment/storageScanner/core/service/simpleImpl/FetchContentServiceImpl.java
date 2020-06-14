package com.hanifmaleki.assignment.storageScanner.core.service.simpleImpl;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import com.hanifmaleki.assignment.storageScanner.core.service.AbstractFetchContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static com.hanifmaleki.assignment.storageScanner.core.Profiles.SINGLE_THREAD;
import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;

@Service
@Profile(SINGLE_THREAD)
public class FetchContentServiceImpl extends AbstractFetchContentService {


    @Autowired
    public FetchContentServiceImpl(FileRepository fileRepository) {
        super(fileRepository);
        this.fileRepository = fileRepository;
    }

    public FileData getFileDataTreeOfFolder(File folder) {
        FileData fileData = getCommonData(folder);

        List<FileData> children = fileRepository.getListOfFilesForPath(folder)
                .stream()
                .map(this::fetchContent)
                .peek(it -> it.setParent(fileData))
                .collect(Collectors.toList());

        Long folderSize = children.stream().map(FileData::getFileSize).reduce(0L, Long::sum);

        fileData.setChildren(children);
        fileData.setFileSize(folderSize);
        fileData.setFileType(FOLDER);
        return fileData;
    }

}
