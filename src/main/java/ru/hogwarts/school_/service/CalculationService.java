package ru.hogwarts.school_.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CalculationService {

    public int sumMillionNumbers() {
        log.info("Вычисление суммы чисел от 1 до 1_000_000 начато");

        int n = 1_000_000;
        int sum = n * (n + 1) / 2;

        log.info("Вычисление завершено. Сумма = {}", sum);
        return sum;
    }
}
