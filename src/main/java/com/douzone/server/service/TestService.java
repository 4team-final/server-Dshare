package com.douzone.server.service;

import com.douzone.server.domain.repository.TestRepository;
import com.douzone.server.dto.TestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public List<TestDto> findAll() {
        return testRepository.findAll().stream().map(TestDto::new).collect(Collectors.toList());
    }
}
