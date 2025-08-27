package ru.hogwarts.school_.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school_.service.CalculationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculation")
public class CalculationController {

    private final CalculationService calculationService;

    @GetMapping("/sum-million")
    public int getSumMillion() {
        return calculationService.sumMillionNumbers();
    }
}
