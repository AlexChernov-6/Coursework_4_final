package com.example.feedback_on_school_meals.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints", schema = "public")
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long complaintID;
    @Column(name = "complaint_date", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private LocalDate complaintDate;
    @Column(name = "student_is_class")
    private Integer studentIsClass;
    @Column(name = "complaint_text")
    private String complaintText;
    @Column(name = "complaint_photo")
    private byte[] complaintPhoto;
    @Column(name = "complaint_status", insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private String complaintStatus;
    private String response;
    @Column(name = "responded_at")
    private LocalDateTime respondedAt;
    @ManyToOne @JoinColumn(name = "dish_id")
    private Dish dish;

    public Long getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(Long complaintID) {
        this.complaintID = complaintID;
    }

    public LocalDate getComplaintDate() {
        return complaintDate;
    }

    public Integer getStudentIsClass() {
        return studentIsClass;
    }

    public void setStudentIsClass(Integer studentIsClass) {
        this.studentIsClass = studentIsClass;
    }

    public String getComplaintText() {
        return complaintText;
    }

    public void setComplaintText(String complaintText) {
        this.complaintText = complaintText;
    }

    public byte[] getComplaintPhoto() {
        return complaintPhoto;
    }

    public void setComplaintPhoto(byte[] complaintPhoto) {
        this.complaintPhoto = complaintPhoto;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}