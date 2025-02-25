package com.gafarov.bastion.service.impl;

import com.gafarov.bastion.entity.user.Activity;
import com.gafarov.bastion.entity.user.Role;
import com.gafarov.bastion.entity.user.User;
import com.gafarov.bastion.exception.BadRequestException;
import com.gafarov.bastion.model.FormDto;
import com.gafarov.bastion.model.FullFormDto;
import com.gafarov.bastion.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FormServiceImpl {
    private final UserRepository userRepository;
    private final ResumeServiceImpl resumeService;
    private final QuizServiceImpl quizService;

    public List<FormDto> getPaginationForm(Pageable pageable, Activity filterParam,Role role) {
        Page<User> page;
        if (filterParam == null) page = userRepository.findAllByRole(pageable, role);
        else page = userRepository.findAllByActivityAndRole(pageable, filterParam, role);
        List<User> users = page.getContent();
        return users.stream().map(this::mapUserToForm).sorted(Comparator.comparing(FormDto::getId).reversed()).toList();
    }

    private FormDto mapUserToForm(User user) {
        String activity = "";
        switch (user.getActivity()) {
            case REGISTERED -> activity += "Зарегистрировался";
            case RESUME -> activity += "Заполнил резюме/";
            case DEMO_TEST -> activity += "Заполнил резюме/Выполнил демо тест/";
            case WAITING_INTERVIEW -> activity += "Заполнил резюме/Выполнил демо тест/";
            case INTERVIEW -> activity += "Заполнил резюме/Выполнил демо тест/Выдан доступ к финальному тесту";
            case WAITING_RESULT -> activity += "Заполнил резюме/Выполнил демо тест/Выполнил финальный тест/";
            case CASE_STUDY -> activity+= "Выполняет чертежное задание";
            case CASE_DONE -> activity+= "Выполнил чертежное задание";
        }
        return new FormDto(user.getId(), user.getFirstname(), user.getLastname(), user.getActivity().name(), activity,
                user.getCreatedDate(),user.getLastActivityDate(), user.isViewed());
    }

    public FullFormDto getUserInfo(Integer id) {
        FullFormDto fullForm = new FullFormDto();
        Optional<User> u = userRepository.findById(id);
        if (u.isEmpty()) throw new BadRequestException("no users with this id");
        fullForm.setUser(mapUserToForm(u.get()));
        fullForm.setResumeDto(resumeService.getResume(u.get()));
        fullForm.setQuizResult(quizService.getUserResult(id));
        var user  = u.get();
        user.setViewed(true);
        userRepository.save(user);
        return fullForm;
    }


}
