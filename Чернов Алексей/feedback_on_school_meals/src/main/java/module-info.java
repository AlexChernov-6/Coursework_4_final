module com.example.feedback_on_school_meals {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.postgresql.jdbc;
    requires javafx.base;
    requires java.naming;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    opens com.example.feedback_on_school_meals to javafx.fxml;
    opens com.example.feedback_on_school_meals.controller to javafx.fxml;
    opens com.example.feedback_on_school_meals.model to org.hibernate.orm.core, javafx.base, com.fasterxml.jackson.databind;
    exports com.example.feedback_on_school_meals;
    exports com.example.feedback_on_school_meals.controller;
    exports com.example.feedback_on_school_meals.model;
    exports com.example.feedback_on_school_meals.service;
    exports com.example.feedback_on_school_meals.util;
}