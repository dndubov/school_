package ru.hogwarts.school_.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@Slf4j
public class CalculationService {

    public int sumMillionNumbers() {
        log.info("Вычисление суммы чисел от 1 до 1_000_000 начато (параллельно)");
        int sum = IntStream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()          // делаем стрим параллельным
                .reduce(0, Integer::sum);
        log.info("Вычисление завершено. Сумма = {}", sum);
        return sum;
    }
}
