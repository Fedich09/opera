package com.dev.cinema.controller;

import com.dev.cinema.model.dto.shoppingcart.ShoppingCartResponseDto;
import com.dev.cinema.service.MovieSessionService;
import com.dev.cinema.service.ShoppingCartService;
import com.dev.cinema.service.UserService;
import com.dev.cinema.service.mapper.ShoppingCartMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shopping-carts")
public class ShoppingCartController {
    private final ShoppingCartMapper cartMapper;
    private final ShoppingCartService cartService;
    private final MovieSessionService movieSessionService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartMapper cartMapper,
                                  ShoppingCartService cartService,
                                  MovieSessionService movieSessionService,
                                  UserService userService) {
        this.cartMapper = cartMapper;
        this.cartService = cartService;
        this.movieSessionService = movieSessionService;
        this.userService = userService;
    }

    @PostMapping("/movie-sessions")
    public void addMovieSession(Authentication auth, @RequestParam Long movieSessionId) {
        Object principal = auth.getPrincipal();
        UserDetails details = (UserDetails) principal;
        cartService.addSession(movieSessionService.get(movieSessionId),
                userService.findByEmail(details.getUsername()));
    }

    @GetMapping("/by-user")
    public ShoppingCartResponseDto getByUser(Authentication auth) {
        Object principal = auth.getPrincipal();
        UserDetails details = (UserDetails) principal;
        return cartMapper.toDto(cartService.getByUser(userService
                .findByEmail(details.getUsername())));
    }
}