package com.Sarthak.course_recommender.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.Sarthak.course_recommender.service.exception.DormFullException;
import com.Sarthak.course_recommender.service.exception.GenderMismatchException;
import com.Sarthak.course_recommender.service.exception.HostelCapacityExceededException;
import com.Sarthak.course_recommender.service.exception.ResourceNotFoundException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntime(RuntimeException ex) {
        return ex.getMessage();
    }
      @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(DormFullException.class)
    public String handleDormFull(DormFullException ex, Model model) {
        model.addAttribute("errorTitle", "Dorm Full");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(HostelCapacityExceededException.class)
    public String handleCapacity(HostelCapacityExceededException ex, Model model) {
        model.addAttribute("errorTitle", "Hostel Full");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(GenderMismatchException.class)
    public String handleGenderMismatch(GenderMismatchException ex, Model model) {
        model.addAttribute("errorTitle", "Gender Mismatch");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Something went wrong");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/general";
    }
}