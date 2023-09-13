package com.springboot.blog.service.imp;

import com.springboot.blog.dto.PostDto;
import com.springboot.blog.dto.PostResponse;
import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//this annotation will make the class available for autodetection while component scanning and autowire this class
@Service
public class PostServiceImp implements PostService {
    private ModelMapper mapper;
    private PostRepository postRepository;
    private CategoryRepository categoryRepository;

    // inject repository into constructor
    public PostServiceImp(PostRepository postRepository,
                          ModelMapper mapper,
                          CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public PostDto createPost(PostDto postDto) {

        // we have a set of category in PostDto so we need to manually insert it, first step extract the category id from postDto and search for it in repo
        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        Post post = mapToEntity(postDto);
        // second step is to insert the object into the postdto
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPost(int pageNo, int pageSize, String sortBy, String sortDir) {
        // check if ASC is true or false
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy)); // add sort argument, choose method that accepts 3 parameters

        // Page types can not be shown to the user
        Page<Post> posts = postRepository.findAll(pageable);

        // extract content from page object
        List<Post> listOfPosts = posts.getContent();

        // create stream out of content to pass data, use lambda to convert all post entity to dto, collect into content
        List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        // extract information from page onto something a user can see
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalPages());
        // true or false - is user on the last page
        postResponse.setLast(posts.isLast());

        return postResponse;
    }

    @Override
    public PostDto getPostBy(long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        return mapToDTO(post);
    }

    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        Category category = categoryRepository.findById(postDto.getCategoryId())
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);

        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }

    @Override
    public Void deletePostByID(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));
        postRepository.delete(post);
        return null;
    }

    @Override
    public List<PostDto> getPostsByCategory(long categoryId) {
        // check to see the object category with the id exist in database
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "id", categoryId));
        // if it does exist, under 1 category id there can be many posts, use list
        List<Post> posts = postRepository.findByCategoryId(categoryId);


        return posts.stream().map((post) -> mapToDTO(post))
                .collect(Collectors.toList());
    }

    //this will be the convertor from entity to dto
    private PostDto mapToDTO(Post post){
        // convert argument post into a dto using mapper, post -> PostDto
        PostDto postDto = mapper.map(post, PostDto.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return postDto;
    }

    private Post mapToEntity(PostDto postDto){
        // convert argument postDto into an entity using mapper, postDto -> Post
        Post post = mapper.map(postDto, Post.class);
//        Post post = new Post();
//        //the reason we do not set an id here is because the entity has to autogenerate it
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return post;
    }
}
