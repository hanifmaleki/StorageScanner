package com.hanifmaleki.assignment.storageScanner.core.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
@Table(name = "FILE_DATA")
public class FileData {
    @Id
    private String absolutePath;
    private String fileName;
    private String extension;
    private long fileSize;
    private Date modificationDate;
    private Date scanDate;
    @ManyToOne
    private FileData parent;
    @OneToMany
    private List<FileData> children;
    @Enumerated(EnumType.STRING)
    private FileType fileType;
}
