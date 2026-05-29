package com.Sarthak.course_recommender.controller;

import com.Sarthak.course_recommender.model.Notice;
import com.Sarthak.course_recommender.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public String listNotices(Model model) {
        model.addAttribute("notices", noticeService.getAllNotices());
        model.addAttribute("notice", new Notice());
        return "notices/list";
    }

    @PostMapping("/add")
    public String addNotice(@ModelAttribute Notice notice,
                            RedirectAttributes redirectAttributes) {
        noticeService.createNotice(notice);
        redirectAttributes.addFlashAttribute("success", "Notice posted.");
        return "redirect:/notices";
    }

    @PostMapping("/{id}/delete")
    public String deleteNotice(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        noticeService.deleteNotice(id);
        redirectAttributes.addFlashAttribute("success", "Notice deleted.");
        return "redirect:/notices";
    }
}