package com.dev.cinema.controller;

import com.dev.cinema.model.MovieSession;
import com.dev.cinema.model.dto.MovieSessionRequestDto;
import com.dev.cinema.model.dto.MovieSessionResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.mapper.impl.MovieSessionMapperImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private final MovieSessionService movieSessionService;
    private final MovieSessionMapperImpl sessionMapper;

    public MovieSessionController(MovieSessionService movieSessionService,
                                  MovieSessionMapperImpl sessionMapper) {
        this.movieSessionService = movieSessionService;
        this.sessionMapper = sessionMapper;
    }

    @PostMapping
    public void create(@RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        movieSessionService.add(sessionMapper.toEntity(movieSessionRequestDto));
    }

    @RequestMapping("/available")
    public List<MovieSessionResponseDto> getAvailableSession(@RequestParam(value =
            "movieId") Long id, @RequestParam(value = "date") String showTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return movieSessionService.findAvailableSessions(id,
                LocalDate.parse(showTime, formatter)).stream()
                .map(sessionMapper::toDto)
                .collect(Collectors.toList());
    }

    @PutMapping
    public void update(@RequestBody MovieSessionRequestDto movieSessionRequestDto,
                       @RequestParam Long id) {
        MovieSession movieSession = sessionMapper.toEntity(movieSessionRequestDto);
        movieSession.setId(id);
        movieSessionService.update(movieSession);
    }

    @DeleteMapping
    public void delete(@RequestParam Long id) {
        movieSessionService.delete(id);
    }
}