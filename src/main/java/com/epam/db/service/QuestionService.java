package com.epam.db.service;

import com.epam.telegram.entity.Question;

import java.util.HashSet;

public interface QuestionService {
    Question getQuestionWithoutRepetition(HashSet<Question> ids);
}
