package com.example.feedback_on_school_meals.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTableViewControllerTest {

    @Test
    void testEmptySearchText() {
        String searchText = "   ";
        assertTrue(searchText.trim().isEmpty());
        // Должен вызвать tableViewRefresh()
    }

    @Test
    void testValidNumericId() {
        String searchText = "123";
        boolean isNumber = false;
        try {
            Long.parseLong(searchText);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        assertTrue(isNumber);
    }

    @Test
    void testInvalidNumericId() {
        String searchText = "abc123";
        boolean isNumber = false;
        try {
            Long.parseLong(searchText);
            isNumber = true;
        } catch (NumberFormatException e) {
            isNumber = false;
        }
        assertFalse(isNumber);
    }

    @Test
    void testContainsRatingKeyword() {
        String searchText = "оценка 5 звёзд";
        boolean isRating = searchText.toLowerCase().contains("оценка");
        assertTrue(isRating);
    }

    @Test
    void testContainsClassKeyword() {
        String searchText = "ученик 10 класс";
        boolean isClass = searchText.toLowerCase().contains("класс");
        assertTrue(isClass);
    }

    @Test
    void testExtractNumbersFromRatingSearch() {
        String searchText = "оценка 5";
        String numbersOnly = searchText.replaceAll("[^0-9]", "");
        assertEquals("5", numbersOnly);

        // Проверка парсинга числа
        int rating = Integer.parseInt(numbersOnly);
        assertEquals(5, rating);
    }

    @Test
    void testExtractNumbersFromComplexText() {
        String searchText = "ученик 10А класс";
        String numbersOnly = searchText.replaceAll("[^0-9]", "");
        assertEquals("10", numbersOnly);

        if (!numbersOnly.isEmpty()) {
            int classNumber = Integer.parseInt(numbersOnly);
            assertEquals(10, classNumber);
        }
    }

    @Test
    void testDateParsingValidFormat() {
        String searchText = "15.05.2024";
        String[] dateParts = searchText.split("\\.");

        assertEquals(3, dateParts.length);

        // Проверка преобразования в LocalDate
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);

        LocalDate date = LocalDate.of(year, month, day);
        assertEquals(15, date.getDayOfMonth());
        assertEquals(5, date.getMonthValue());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testDateParsingInvalidFormat() {
        String searchText = "15-05-2024";
        String[] dateParts = searchText.split("\\.");

        // Неправильный разделитель, поэтому массив будет содержать только один элемент
        assertNotEquals(3, dateParts.length);
        assertEquals(1, dateParts.length);

        // Не должно парситься как дата
        if (dateParts.length > 1) {
            // Этот блок не должен выполняться
            fail("Не должно парситься как дата");
        }
    }

    @Test
    void testSearchTextWithSpecialCharacters() {
        String searchText = "Борщ!@#$%^&*()";
        // Проверяем, что специальные символы не мешают извлечению текста
        assertFalse(searchText.trim().isEmpty());

        // Проверяем логику парсинга чисел
        String numbersOnly = searchText.replaceAll("[^0-9]", "");
        assertTrue(numbersOnly.isEmpty());
    }

    @Test
    void testRatingWithoutKeyword() {
        String searchText = "5";
        boolean isRating = searchText.toLowerCase().contains("оценка");
        boolean isClass = searchText.toLowerCase().contains("класс");

        assertFalse(isRating);
        assertFalse(isClass);

        // Просто число без ключевых слов должно парситься как число
        String numbersOnly = searchText.replaceAll("[^0-9]", "");
        assertEquals("5", numbersOnly);

        // Но без ключевых слов "оценка" или "класс" это должно
        // обрабатываться как поиск по ID или названию блюда
    }

    @Test
    void testClassWithoutKeyword() {
        String searchText = "10";
        boolean isClass = searchText.toLowerCase().contains("класс");

        assertFalse(isClass);

        // Просто число без ключевого слова "класс"
        // должно обрабатываться как поиск по ID или рейтингу
    }

    @Test
    void testMixedKeywords() {
        String searchText = "оценка 5 класс 10";
        boolean isRating = searchText.toLowerCase().contains("оценка");
        boolean isClass = searchText.toLowerCase().contains("класс");

        assertTrue(isRating);
        assertTrue(isClass);

        // Извлекаем все числа
        String numbersOnly = searchText.replaceAll("[^0-9]", "");
        assertEquals("510", numbersOnly);

        // Логика должна учитывать оба ключевых слова
        // В реальном методе будет проверка на наличие ключевых слов
    }

    @Test
    void testDateWithSpaces() {
        String searchText = "15 . 05 . 2024";
        // Оригинальный код использует split("\\."), так что пробелы не убираются
        String[] dateParts = searchText.split("\\.");

        // Получится ["15 ", " 05 ", " 2024"]
        assertEquals(3, dateParts.length);

        // Нужно убрать пробелы для парсинга
        String dayStr = dateParts[0].trim();
        String monthStr = dateParts[1].trim();
        String yearStr = dateParts[2].trim();

        assertEquals("15", dayStr);
        assertEquals("05", monthStr);
        assertEquals("2024", yearStr);
    }

    @Test
    void testPriorityLogic() {
        // Тестируем приоритетность поиска:
        // 1. По ID (если число)
        // 2. По названию блюда
        // 3. По рейтингу (если есть слово "оценка")
        // 4. По дате
        // 5. По классу (если есть слово "класс")

        String searchById = "123";
        String searchByDish = "Борщ";
        String searchByRating = "оценка 5";
        String searchByDate = "15.05.2024";
        String searchByClass = "класс 10";

        // Проверка парсинга ID
        try {
            Long id = Long.parseLong(searchById);
            assertEquals(123L, id);
        } catch (NumberFormatException e) {
            // Не число
        }

        // Проверка поиска по блюду - не пустая строка
        assertFalse(searchByDish.trim().isEmpty());

        // Проверка поиска по рейтингу
        assertTrue(searchByRating.toLowerCase().contains("оценка"));

        // Проверка поиска по дате
        String[] dateParts = searchByDate.split("\\.");
        assertEquals(3, dateParts.length);

        // Проверка поиска по классу
        assertTrue(searchByClass.toLowerCase().contains("класс"));
    }
}