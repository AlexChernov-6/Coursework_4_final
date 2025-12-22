package com.example.feedback_on_school_meals.model;

import java.time.*;

import jakarta.persistence.*;

@Entity
@Table(name = "daily_menus", schema = "public")
public class DailyMenus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_menu_id")
    private Integer dailyMenuId;
    @Column(name = "daily_menu_date")
    private LocalDate dailyMenuDate;
    @Column(name = "approved_by")
    private String approvedBy;

    public Integer getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(Integer dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

    public LocalDate getDailyMenuDate() {
        return dailyMenuDate;
    }

    public void setDailyMenuDate(LocalDate dailyMenuDate) {
        this.dailyMenuDate = dailyMenuDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }
}