package com.example.mywebservice.service;

import com.example.mywebservice.dto.PostDto;
import com.example.mywebservice.entity.Post;
import com.example.mywebservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // 게시글 작성하기
    public PostDto createPost(PostDto postDto) {
        Post post = new Post(postDto.getTitle(), postDto.getContent(), postDto.getAuthor());
        Post savePost = postRepository.save(post);

        return convertToDto(savePost);
    }

    // 게시글 목록 보기
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 게시글 상세보기
    public Optional<PostDto> getPostById(Long id) {
        return postRepository.findById(id).map(this::convertToDto);
    }

    // 게시글 수정하기
    public PostDto updatePost(Long id, PostDto updatedPostDto) {
        return postRepository.findById(id)
                .map(post -> {
                    post.update(updatedPostDto.getTitle(), updatedPostDto.getContent(), updatedPostDto.getAuthor());  // setter 대신 update 메서드 사용
                    return convertToDto(postRepository.save(post));
                })
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 게시글 삭제하기
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    private PostDto convertToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setAuthor(post.getAuthor());
        return postDto;
    }
}
