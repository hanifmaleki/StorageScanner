package com.hanifmaleki.assignment.storageScanner.core.repository;

import java.io.File;
import java.util.Date;
import java.util.List;

public interface FileRepository {

    List<File> getListOfFilesForPath(File path);

    Long getSizeOf(File file);

    Boolean isDirectory(File file);

    String getAbsolutePath(File file);

    Date getLastModified(File file);

    String getName(File file);

    String getExtension(File file);

    boolean exists(File file);
}

