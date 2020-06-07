package com.hanifmaleki.assignment.storageScanner.core.service;


import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@UnitTest
@ExtendWith(MockitoExtension.class)
class FetchContentServiceImplTest {

    @Mock
    FileRepository fileRepository;


    final FetchContentService fetchContentService = new FetchContentServiceImpl(fileRepository);


    @Test
    public void fetchContentTest() {
        createTreeStructure();
        /*
        /var
          - file1.ext (150)
          - file2.ext (250)
          - folder1
            - file3.ext (350)
            - folder2.ext
            - folder3.ext
                - file4.ext (450)
                - file5.ext (550)
         */

        fetchContentService.setFileRepository(fileRepository);
        fetchContentService.setSearchPath("/var");
        List<FileData> content = fetchContentService.getContent();

        List<FileData> folder1List = content.stream().filter(it -> it.getAbsolutePath().equals("/var/folder1")).collect(Collectors.toList());
        assertEquals(8, content.size());
        assertEquals(1, folder1List.size());
        assertEquals(1350, folder1List.get(0).getFileSize());
    }

    private void createTreeStructure() {
        File file1 = createFileAndSetMock("/var/", "file1", "ext", 150L);
        File file2 = createFileAndSetMock("/var/", "file2", "ext", 250L);
        File folder1 = createFolderAndSetMock("/var/", "folder1");

        File file3 = createFileAndSetMock("/var/", "file3", "ext", 350L);
        File folder2 = createFolderAndSetMock("/var/folder/", "folder2.ext");
        File folder3 = createFolderAndSetMock("/var/folder/", "folder3.ext");


        File file4 = createFileAndSetMock("/var/folder/folder4.ext/", "file4", "ext", 450L);
        File file5 = createFileAndSetMock("/var/folder/folder5.ext/", "file5", "", 550L);


        List<File> listFile1 = Arrays.asList(file1, file2, folder1);
        List<File> listFile2 = Arrays.asList(file3, folder2, folder3);
        List<File> listFile3 = Arrays.asList(file4, file5);


        Mockito.when(fileRepository.getListOfFilesForPath(new File("/var")))
                .thenReturn(listFile1);
        Mockito.when(fileRepository.getListOfFilesForPath(folder1))
                .thenReturn(listFile2);
        Mockito.when(fileRepository.getListOfFilesForPath(folder2))
                .thenReturn(Collections.emptyList());
        Mockito.when(fileRepository.getListOfFilesForPath(folder3))
                .thenReturn(listFile3);
        Mockito.when(fileRepository.exists(any(File.class)))
                .thenReturn(true);
    }

    private File createFileAndSetMock(String path, String filename, String extension, Long size) {
        String pathName = String.format("%s%s.%s", path, filename, extension);
        File file = new File(pathName);
        Mockito.lenient().doReturn(size)
                .when(fileRepository)
                .getSizeOf(file);
        Mockito.lenient().doReturn(filename)
                .when(fileRepository)
                .getName(file);
        Mockito.lenient().doReturn(extension)
                .when(fileRepository)
                .getExtension(file);
        Mockito.lenient().doReturn(new Date())
                .when(fileRepository)
                .getLastModified(file);
        Mockito.lenient().doReturn(false)
                .when(fileRepository)
                .isDirectory(file);
        Mockito.lenient().doReturn(pathName)
                .when(fileRepository)
                .getAbsolutePath(file);
        return file;
    }

    private File createFolderAndSetMock(String path, String filename) {
        String pathName = String.format("%s%s", path, filename);
        File folder = new File(pathName);
        Mockito.lenient().doReturn(filename)
                .when(fileRepository)
                .getName(folder);
        Mockito.lenient().doReturn(new Date())
                .when(fileRepository)
                .getLastModified(folder);
        Mockito.lenient().doReturn(true)
                .when(fileRepository)
                .isDirectory(folder);
        Mockito.lenient().doReturn(pathName)
                .when(fileRepository)
                .getAbsolutePath(folder);
        return folder;
    }


}