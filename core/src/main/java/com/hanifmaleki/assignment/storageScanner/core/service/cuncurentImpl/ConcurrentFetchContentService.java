package com.hanifmaleki.assignment.storageScanner.core.service.cuncurentImpl;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import com.hanifmaleki.assignment.storageScanner.core.service.AbstractFetchContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.hanifmaleki.assignment.storageScanner.core.Profiles.MULTI_THREAD;
import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;

@Service
@Profile(MULTI_THREAD)
public class ConcurrentFetchContentService extends AbstractFetchContentService {

    private final Logger logger = LoggerFactory.getLogger(ConcurrentFileDataManagerImpl.class);

    private final FileDataRepository fileDataRepository;

    private final FileRepository fileRepository;

    @Autowired
    public ConcurrentFetchContentService(FileRepository fileRepository, FileDataRepository fileDataRepository) {
        super(fileRepository);
        this.fileDataRepository = fileDataRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    protected FileData getFileDataTreeOfFolder(File folder) {

        FileData fileData = createFileDataAndSaveItWithoutSizeAndChildrenOfFolder(folder);

        List<FileData> fileDataList = scanAsynchronouslyForChildren(folder);

        updateChildrenAndSizeOfFolder(fileData, fileDataList);

        return fileData;
    }

    private void updateChildrenAndSizeOfFolder(FileData fileData, List<FileData> fileDataList) {
        fileDataList.forEach(it -> it.setParent(fileData));
        fileData.setChildren(fileDataList);
        Long fileSize = fileDataList.stream().map(FileData::getFileSize).reduce(0L, Long::sum);
        fileData.setFileSize(fileSize);
        fileDataRepository.save(fileData);

        if (fileSize > 10000000) {
            NumberFormat nf = NumberFormat.getInstance(new Locale("sk", "SK"));
            logger.debug("Second saving of folder {} of size {}", fileData.getAbsolutePath(), nf.format(fileData.getFileSize()));
            logger.debug("Number of threads {}", Thread.activeCount());
        }
    }

    private List<FileData> scanAsynchronouslyForChildren(File file) {
        return fileRepository.getListOfFilesForPath(file).stream()
                .map(it -> createCompletableFuture(it)
                        .join()
                )
                .collect(Collectors.toList());
    }

    private FileData createFileDataAndSaveItWithoutSizeAndChildrenOfFolder(File file) {
        FileData fileData = getCommonData(file);
        fileData.setFileType(FOLDER);
        fileDataRepository.save(fileData);
        return fileData;
    }

    private CompletableFuture<FileData> createCompletableFuture(File file) {
        CompletableFuture<FileData> fileDataCompletableFuture = CompletableFuture.supplyAsync(() -> fetchContent(file))
                .thenApply(fileData -> {
                    fileDataRepository.save(fileData);
                    return fileData;
                });
        return fileDataCompletableFuture;
    }

}
