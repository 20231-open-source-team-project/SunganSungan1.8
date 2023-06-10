package com.example.sungansungan12;
//조정동 담당
public class Post {
    private String name;
    private String description;
    private String price;
    private String available;
    private String imageUrl;

    public Post() {
        // 기본 생성자 (Firebase Realtime Database에서 필요)
    }

    public Post(String name, String description, String price, String available, String imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
        this.imageUrl = imageUrl;
    }

    // Getter 메서드
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailable() {
        return available;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    // Setter 메서드
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
