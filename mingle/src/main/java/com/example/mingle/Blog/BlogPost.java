package com.example.mingle.Blog;

public class BlogPost {
    private final int id;
    private final String title;
    private final String content;
    private final String image;
    private final String summary;

    public BlogPost(int id, String title, String content, String image, String summary) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.summary = summary;
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getImage() { return image; }
    public String getSummary() { return summary; }
}
