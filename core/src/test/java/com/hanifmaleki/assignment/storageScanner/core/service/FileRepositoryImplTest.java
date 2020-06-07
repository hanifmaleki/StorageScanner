package com.hanifmaleki.assignment.storageScanner.core.service;

import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepositoryImpl;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;


@UnitTest
class FileRepositoryImplTest {

    final FileRepository fileRepository = new FileRepositoryImpl();


    @ParameterizedTest(name = "getName and getExtension on inout {0} return {1} as name and {2} as extension")
    @MethodSource("filedataAndExpectedResultProvider")
    void getNamedAndTypedFileData(File file, String name, String extension) {
        assertEquals(name, fileRepository.getName(file));
        assertEquals(extension, fileRepository.getExtension(file));
    }

    static Stream<Arguments> filedataAndExpectedResultProvider() {
        return Stream.of(
                arguments(new File("autoexec.bat"), "autoexec", "bat"),
                arguments(new File(".gitignore"), "", "gitignore"),
                arguments(new File("fileWithoutExtension."), "fileWithoutExtension", ""),
                arguments(new File("fileWithoutExtension2"), "fileWithoutExtension2", ""),
                arguments(new File("."), ".", ""),
                arguments(new File("file.with.multiple.points"), "file.with.multiple", "points")
        );
    }


}