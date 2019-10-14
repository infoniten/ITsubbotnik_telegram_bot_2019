package com.epam.db.service;

import com.epam.telegram.entity.Question;

import java.util.List;

public interface QuestionService {
    Question getQuestionWithoutRepetition(List<Integer> ids);
}
