package com.gafarov.bastion.controller;

import com.gafarov.bastion.entity.user.User;
import com.gafarov.bastion.model.QuizAnswer;
import com.gafarov.bastion.model.QuizDto;
import com.gafarov.bastion.model.ResultDto;
import com.gafarov.bastion.service.impl.QuizServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
@AllArgsConstructor

public class QuizController extends BaseController {
    private final QuizServiceImpl quizService;
    @CrossOrigin(origins = "http://94.241.140.221:8080")
    @GetMapping("/{id}")
    public QuizDto getQuiz(
            @PathVariable Integer id,
            @AuthenticationPrincipal User user
    ) {
        return quizService.getQuiz(id, user);
    }
    @CrossOrigin(origins = "http://94.241.140.221:8080")
    @PostMapping("/{id}")
    public List<ResultDto> sendResult(
            @PathVariable Integer id,
            @RequestBody List<QuizAnswer> answers,
            @AuthenticationPrincipal User user
    ) {
        return quizService.checkResult(answers, user, id);
    }
}
