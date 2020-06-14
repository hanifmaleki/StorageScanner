package com.hanifmaleki.assignment.storageScanner.core.service.simpleImpl;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import com.hanifmaleki.assignment.storageScanner.core.service.AbstractFileDataManager;
import com.hanifmaleki.assignment.storageScanner.core.service.FetchContentService;
import com.hanifmaleki.assignment.storageScanner.core.service.FileDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanifmaleki.assignment.storageScanner.core.Profiles.SINGLE_THREAD;

@Service
@Profile(SINGLE_THREAD)
public class FileDataManagerImpl extends AbstractFileDataManager {

    private final Logger logger = LoggerFactory.getLogger(FileDataManager.class);

    private final FetchContentService fetchContentService;

    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileDataManagerImpl(FetchContentService fetchContentService, FileDataRepository fileDataRepository) {
        super(fileDataRepository);
        this.fetchContentService = fetchContentService;
        this.fileDataRepository = fileDataRepository;
    }


    @Override
    @Scheduled(fixedDelay = 60 * 1000)
    @Transactional
    public void getLatestFilesAndStore() throws TheInputPathIsIncorrectException {
        logger.debug("Fetching files");
        FileData fileData = fetchContentService.getContent();
        if (fileData != null) {
            fileDataRepository.saveHierarchy(fileData);
            logger.debug("file {} has been fetched with total size {}", fileData.getAbsolutePath(), fileData.getFileName());
        }
    }

}
