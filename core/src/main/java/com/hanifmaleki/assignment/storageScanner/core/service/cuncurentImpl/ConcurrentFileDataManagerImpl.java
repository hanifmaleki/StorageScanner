package com.hanifmaleki.assignment.storageScanner.core.service.cuncurentImpl;

import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import com.hanifmaleki.assignment.storageScanner.core.service.AbstractFileDataManager;
import com.hanifmaleki.assignment.storageScanner.core.service.FetchContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hanifmaleki.assignment.storageScanner.core.Profiles.MULTI_THREAD;

@Service
@Profile(MULTI_THREAD)
public class ConcurrentFileDataManagerImpl extends AbstractFileDataManager {
    private final Logger logger = LoggerFactory.getLogger(ConcurrentFileDataManagerImpl.class);

    private final FetchContentService fetchContentService;

    @Autowired
    protected ConcurrentFileDataManagerImpl(FileDataRepository fileDataRepository, FetchContentService fetchContentService) {
        super(fileDataRepository);
        this.fetchContentService = fetchContentService;
    }

    @Override
    @Scheduled(fixedDelay = 300 * 1000)
    @Transactional
    public void getLatestFilesAndStore() throws TheInputPathIsIncorrectException {
        fetchContentService.getContent();
    }


}
