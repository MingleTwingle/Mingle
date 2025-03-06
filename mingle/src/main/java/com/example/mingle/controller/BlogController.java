package com.example.mingle.controller;

import com.example.mingle.Blog.BlogPost;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class BlogController {

    @GetMapping("/bottom/blog/blog")
    public String showBlogList(Model model) {
        List<BlogPost> posts = List.of(
                new BlogPost(1, "첫 번째 블로그 글", "내용1", "/images/blog1.jpg", "요약1"),
                new BlogPost(2, "두 번째 블로그 글", "내용2", "/images/blog2.jpg", "요약2")
        );

        model.addAttribute("posts", posts);
        return "bottom/blog/blog"; // templates/bottom/blog/blog.html
    }
}
