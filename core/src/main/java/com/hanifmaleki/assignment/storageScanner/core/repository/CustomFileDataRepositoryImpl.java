package com.hanifmaleki.assignment.storageScanner.core.repository;

import com.hanifmaleki.assignment.storageScanner.core.model.FileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.hanifmaleki.assignment.storageScanner.core.model.FileType.FILE;

@Repository
public class CustomFileDataRepositoryImpl implements CustomFileDataRepository {
    @Autowired
    EntityManager entityManager;

    @Override
    public FileData saveHierarchy(FileData fileData) {
        if (fileData.getFileType() == FILE) {
            return entityManager.merge(fileData);
        } else {
            fileData.setParent(null);
            List<FileData> children = fileData.getChildren();
            fileData.setChildren(null);
            entityManager.merge(fileData);
            children.stream()
                    .peek(it -> it.setParent(fileData))
                    .forEach(this::saveHierarchy);
            fileData.setChildren(children);
            return entityManager.merge(fileData);
        }
    }
}
