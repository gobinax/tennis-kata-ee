package org.example.tennis.infra.controller;

import org.example.tennis.data.MatchDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MatchController {

    @PostMapping("/tennis/match")
    public MatchDto createMatch(@RequestBody MatchDto matchDto) {
        matchDto.id = 1L;
        return matchDto;
    }
}
