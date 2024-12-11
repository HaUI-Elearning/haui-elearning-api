package com.elearning.haui.domain.request;

public class AddCourseToCartRequest {
    private Long courseId;
    private int quantity = 1;

    // Getters và Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
