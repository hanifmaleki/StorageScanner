package com.hanifmaleki.assignment.storageScanner.core.repository;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Repository
public class FileRepositoryImpl implements FileRepository {
    @Override
    public List<File> getListOfFilesForPath(File path) {
        return Arrays.asList(path.listFiles());
    }

    @Override
    public Long getSizeOf(File file) {
        return file.length();
    }

    @Override
    public Boolean isDirectory(File file) {
        return file.isDirectory();
    }

    @Override
    public String getAbsolutePath(File file) {
        return file.getAbsolutePath();
    }

    @Override
    public Date getLastModified(File file) {
        return new Date(file.lastModified());
    }

    @Override
    public String getName(File file) {
        String filename = file.getName();

        if (isDirectory(file)) {
            return filename;
        }

        if (!filename.contains(".")) {
            return filename;
        }
        int lastPointIndex = filename.lastIndexOf(".");
        if (lastPointIndex < 1) {
            return "";
        }
        return filename.substring(0, lastPointIndex);
    }

    @Override
    public String getExtension(File file) {
        String filename = file.getName();

        if (isDirectory(file)) {
            return "";
        }

        if (!filename.contains(".")) {
            return "";
        }

        int length = filename.length();
        int lastPointIndex = filename.lastIndexOf(".");

        if (lastPointIndex == length - 1) {
            return "";
        }

        return filename.substring(lastPointIndex + 1);
    }

    @Override
    public boolean exists(File file) {
        return file.exists();
    }


}
