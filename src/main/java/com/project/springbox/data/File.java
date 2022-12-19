package com.project.springbox.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer fileId;

    @Lob
    private String fileName;

    @JsonIgnore
    private byte[] data;

    private String contentType;

    @ManyToOne
    private User user;

    public File(User user, String fileName, String contentType, byte[] data) {
        this.user = user;
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
    }
}
