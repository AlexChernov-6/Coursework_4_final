package com.example.feedback_on_school_meals.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDate;

@Entity
@Table(name = "feedback", schema = "public")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id")
    private Long feedbackID;
    @ManyToOne @JoinColumn(name = "dish_id")
    private Dish dish;
    @Column(name = "feedback_rating")
    private Integer feedbackRating;
    @Column(name = "feedback_comment")
    private String feedbackComment;
    @Column(name = "feedback_date", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private LocalDate feedbackDate;
    @Column(name = "student_is_class")
    private Integer studentIsClass;
    private boolean anonymous;

    public Long getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(Long feedbackID) {
        this.feedbackID = feedbackID;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Integer getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(Integer feedbackRating) {
        this.feedbackRating = feedbackRating;
    }

    public String getFeedbackComment() {
        return feedbackComment;
    }

    public void setFeedbackComment(String feedbackComment) {
        this.feedbackComment = feedbackComment;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public Integer getStudentIsClass() {
        return studentIsClass;
    }

    public void setStudentIsClass(Integer studentIsClass) {
        this.studentIsClass = studentIsClass;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
}