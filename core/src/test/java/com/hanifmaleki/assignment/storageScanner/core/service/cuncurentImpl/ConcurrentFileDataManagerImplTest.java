package com.hanifmaleki.assignment.storageScanner.core.service.cuncurentImpl;

import com.hanifmaleki.assignment.storageScanner.core.IntegrationTest;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import com.hanifmaleki.assignment.storageScanner.core.service.FetchContentService;
import com.hanifmaleki.assignment.storageScanner.core.service.FileDataManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.hanifmaleki.assignment.storageScanner.core.Profiles.MULTI_THREAD;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@IntegrationTest
@ActiveProfiles(MULTI_THREAD)
@Disabled
class ConcurrentFileDataManagerImplTest {

    @Autowired
    private FetchContentService fetchContentService;

    @Autowired
    private FileDataRepository fileDataRepository;


    @Test
        //TODO complete te test
    void getLatestFilesAndStore() throws TheInputPathIsIncorrectException {
        fetchContentService.setSearchPath("/");
        FileDataManager fileDataManager = new ConcurrentFileDataManagerImpl(fileDataRepository, fetchContentService);

        fileDataManager.getLatestFilesAndStore();
    }
}