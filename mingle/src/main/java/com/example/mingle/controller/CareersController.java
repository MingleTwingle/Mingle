/*
package com.example.mingle.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/bottom/careers")
public class CareersController {

    // 채용 공고 목록 페이지
    @GetMapping
    public String showCareers(Model model) {
        List<BatchProperties.Job> jobs = List.of(
                new BatchProperties.Job(1, "웹 개발자", "React와 Spring Boot를 이용한 웹 개발"),
                new BatchProperties.Job(2, "백엔드 개발자", "Spring Boot 기반 REST API 개발")
        );
        model.addAttribute("jobs", jobs);
        return "bottom/careers/careers"; // 템플릿 폴더 위치
    }

    // 특정 직무 지원 페이지
    @GetMapping("/apply")
    public String showApplyForm(@RequestParam("job") int jobId, Model model) {
        model.addAttribute("jobId", jobId);
        return "bottom/careers/apply"; // 템플릿 폴더 위치
    }

    // 지원서 제출 처리
    @PostMapping("/apply")
    public String submitApplication(@RequestParam("jobId") int jobId,
                                    @RequestParam("name") String name,
                                    @RequestParam("email") String email,
                                    @RequestParam("phone") String phone,
                                    @RequestParam("cover_letter") String coverLetter,
                                    @RequestParam("resume") MultipartFile resume) {
        System.out.println("지원 완료: " + name + " - " + email + " - 직무 ID: " + jobId);
        return "redirect:/bottom/careers"; // 지원 후 공고 페이지로 이동
    }
}
*/
