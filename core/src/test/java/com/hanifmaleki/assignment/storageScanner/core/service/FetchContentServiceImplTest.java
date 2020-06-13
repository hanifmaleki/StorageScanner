package com.hanifmaleki.assignment.storageScanner.core.service;


import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import com.hanifmaleki.assignment.storageScanner.core.model.TheInputPathIsIncorrectException;
import com.hanifmaleki.assignment.storageScanner.core.repository.FileRepository;
import org.junit.jupiter.api.DisplayName;
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

import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@UnitTest
@ExtendWith(MockitoExtension.class)
class FetchContentServiceImplTest {

    @Mock
    FileRepository fileRepository;


    final FetchContentService fetchContentService = new FetchContentServiceImpl(fileRepository);


    @Test
    @DisplayName("Make a file structure and check the size of root folder to be calculated correctly")
    public void fetchContentTest() throws TheInputPathIsIncorrectException {
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
        FileData fileData = fetchContentService.getContent();


        assertEquals(3, fileData.getChildren().size());
        assertEquals(1750, fileData.getFileSize());
        FileData folder1 = fileData.getChildren().stream().filter(it -> it.getFileType() == FOLDER).collect(Collectors.toList()).get(0);
        assertEquals(1350, folder1.getFileSize());
    }

    @Test
    @DisplayName("Fetch content of a non-existing path andexpect TheInputPathIsIncorrect exception")
    public void fetchNotExistingPath() throws TheInputPathIsIncorrectException {
        File notExistPath = new File("notExistPath");

        Mockito.when(fileRepository.exists(any(File.class)))
                .thenReturn(false);
        fetchContentService.setFileRepository(fileRepository);
        fetchContentService.setSearchPath(notExistPath.getAbsolutePath());
        assertThrows(TheInputPathIsIncorrectException.class, () -> fetchContentService.getContent());
    }

    private void createTreeStructure() {
        File varFolder = new File("/var");
        File file1 = createFileAndSetMock(varFolder, "file1", "ext", 150L);
        File file2 = createFileAndSetMock(varFolder, "file2", "ext", 250L);
        File folder1 = createFolderAndSetMock(varFolder, "folder1");

        File file3 = createFileAndSetMock(varFolder, "file3", "ext", 350L);
        File folder2 = createFolderAndSetMock(folder1, "folder2.ext");
        File folder3 = createFolderAndSetMock(folder1, "folder3.ext");

        File file4 = createFileAndSetMock(folder3, "file4", "ext", 450L);
        File file5 = createFileAndSetMock(folder3, "file5", "", 550L);


        List<File> listFile1 = Arrays.asList(file1, file2, folder1);
        List<File> listFile2 = Arrays.asList(file3, folder2, folder3);
        List<File> listFile3 = Arrays.asList(file4, file5);


        Mockito.when(fileRepository.getListOfFilesForPath(varFolder))
                .thenReturn(listFile1);
        Mockito.when(fileRepository.isDirectory(varFolder))
                .thenReturn(true);
        Mockito.when(fileRepository.getListOfFilesForPath(folder1))
                .thenReturn(listFile2);
        Mockito.when(fileRepository.getListOfFilesForPath(folder2))
                .thenReturn(Collections.emptyList());
        Mockito.when(fileRepository.getListOfFilesForPath(folder3))
                .thenReturn(listFile3);
        Mockito.when(fileRepository.exists(any(File.class)))
                .thenReturn(true);
    }

    private File createFileAndSetMock(File path, String filename, String extension, Long size) {
        String fileName = String.format("%s.%s", filename, extension);
        File file = new File(path, fileName);
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
        Mockito.lenient().doReturn(file.getAbsolutePath())
                .when(fileRepository)
                .getAbsolutePath(file);
        return file;
    }

    private File createFolderAndSetMock(File path, String filename) {
        File folder = new File(path, filename);
        Mockito.lenient().doReturn(filename)
                .when(fileRepository)
                .getName(folder);
        Mockito.lenient().doReturn(new Date())
                .when(fileRepository)
                .getLastModified(folder);
        Mockito.lenient().doReturn(true)
                .when(fileRepository)
                .isDirectory(folder);
        Mockito.lenient().doReturn(folder.getAbsolutePath())
                .when(fileRepository)
                .getAbsolutePath(folder);
        return folder;
    }


}