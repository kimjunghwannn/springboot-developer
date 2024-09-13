package com.example.mywebservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="posts")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title; // 게시글 제목
    private String content; // 내용
    private String author; // 작성자
    private LocalDateTime createdAt; //작성일

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Post(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
