package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class WebController {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "http://localhost:8080";

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam String accountId, Model model) {

        try {
            AccountDto account =
                    restTemplate.getForObject(
                            BASE_URL + "/accounts/" + accountId,
                            AccountDto.class
                    );

            model.addAttribute("account", account);

        } catch (Exception e) {
            model.addAttribute("error", "Account not found.");
        }

        return "index";
    }

    @PostMapping("/transaction")
    public String transaction(@RequestParam Long accountId,
                              @RequestParam String type,
                              @RequestParam String amount,
                              Model model) {

        try {
            restTemplate.put(
                    BASE_URL + "/accounts/" + accountId + "/" + type + "?amount=" + amount,
                    null
            );

            AccountDto updated =
                    restTemplate.getForObject(
                            BASE_URL + "/accounts/" + accountId,
                            AccountDto.class
                    );

            model.addAttribute("account", updated);

        } catch (Exception e) {
            model.addAttribute("error", "Transaction failed.");
        }

        return "index";
    }
}