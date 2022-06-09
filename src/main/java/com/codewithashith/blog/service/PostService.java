package com.codewithashith.blog.service;

import com.codewithashith.blog.dto.PostDto;
import com.codewithashith.blog.exception.PostNotFoundException;
import com.codewithashith.blog.model.Post;
import com.codewithashith.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private AuthService authService;

    @Autowired
    private PostRepository postRepository;

    public void createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        User user = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user logged in"));
        post.setUsername(user.getUsername());
        post.setCreatedOn(Instant.now());
        postRepository.save(post);
    }

    public List<PostDto> getAllPost() {
//        List<PostDto> postDtos = new ArrayList<>();
//        List<Post> posts = postRepository.findAll();
//        for (int i = 0; i < posts.size(); i++) {
//            PostDto postDto = new PostDto();
//            postDto.setId(posts.get(i).getId());
//            postDto.setTitle(posts.get(i).getTitle());
//            postDto.setContent(posts.get(i).getContent());
//            postDto.setUsername(posts.get(i).getUsername());
//            postDtos.add(postDto);
//        }
        return postRepository.findAll().stream().map(this::mapFromPostToDto).collect(Collectors.toList());
    }

    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found for id " + id));
        return mapFromPostToDto(post);
    }

    private PostDto mapFromPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setUsername(post.getUsername());
        return postDto;
    }

    private Post mapFromDtoToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        User user = authService.getCurrentUser().orElseThrow(() ->
                new IllegalArgumentException("No user Found"));
        post.setUsername(user.getUsername());
        post.setCreatedOn(Instant.now());
        post.setUpdatedOn(Instant.now());
        return post;
    }


}
