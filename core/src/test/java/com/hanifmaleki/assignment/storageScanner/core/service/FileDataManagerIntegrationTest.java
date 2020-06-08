package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.repository.FileDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@IntegrationTest
public class FileDataManagerIntegrationTest {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Autowired
    private FetchContentService fetchContentService;

    @Autowired
    private FileDataManager fileDataManager;

    @TempDir
    File tempDir;

    private final int lineBreakLength = System.lineSeparator().length();


    @Test
    void getFilesTest() throws IOException {

        createNewFileWithSomeStringInsideAndReturnSize(tempDir, "file1");

        File folder1 = new File(tempDir, "folder1");
        folder1.mkdir();

        int sum = createNewFileWithSomeStringInsideAndReturnSize(folder1, "file2");

        sum += createNewFileWithSomeStringInsideAndReturnSize(folder1, "file3");

        fetchContentService.setSearchPath(tempDir.getAbsolutePath());
        fileDataManager.getLatestFilesAndStore();

        assertEquals(4, fileDataRepository.findAll().size());

        assertEquals(sum, fileDataManager.getFolders().get(0).getFileSize());


    }

    private int createNewFileWithSomeStringInsideAndReturnSize(File path, String name) throws IOException {
        String fileName = format("%s.txt", name);
        File file = new File(path, fileName);
        List<String> lines = produceRandomNumberOfStrings();
        Files.write(file.toPath(), lines);
        return lines.stream().map(String::length).reduce(0, Integer::sum) + lineBreakLength * lines.size();
    }

    private List<String> produceRandomNumberOfStrings() {
        Random random = new Random();

        return IntStream.range(1, random.nextInt(30))
                .mapToObj(it -> getRandomStringOfSize(random.nextInt(30)))
                .collect(Collectors.toList());
    }

    private String getRandomStringOfSize(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}


