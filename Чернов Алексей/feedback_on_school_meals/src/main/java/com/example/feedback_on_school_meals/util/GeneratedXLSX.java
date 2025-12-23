package com.example.feedback_on_school_meals.util;

import com.example.feedback_on_school_meals.model.RatingFeedback;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GeneratedXLSX {
    public static void createXLSX(List<RatingFeedback> data) throws IOException {
        //Код создаёт файл с расширением xlsx после выполнение кода, try-with-resource закроет файл
        try (XSSFWorkbook file = new XSSFWorkbook()) {
            //Создаём книгу внутри файла
            XSSFSheet dataSheet = file.createSheet("Данные для диаграммы");

            XSSFSheet sheet = file.createSheet("Анализ отчётов");

            String feedbackDate = createRatingData(dataSheet, data);

            createColumnChart(dataSheet, sheet, data);
            createPieChart(dataSheet, sheet, data);

            saveReportWithPath(file, feedbackDate);
        }
    }

    //Метод для создания столбчатых диаграмм
    public static void createColumnChart(XSSFSheet dataSheet, XSSFSheet sheet, List<RatingFeedback> data) {
        //Создадим холст для рисования
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        //Задаются опорные точки на листе для диаграммы первые 4 аргумента-смещения от границ
        //Последующие 2-индексы левого верхнего угла и заключающие 2 правого нижнего
        //Холст будет находиться в 0:0 длинной 15 клеток и высотой 10 строк
        XSSFClientAnchor anchor =
                drawing.createAnchor(0, 0, 0, 0, 0, 0, 10,15);
        //Создаём диаграмму на холсте
        XSSFChart diagram = drawing.createChart(anchor);
        //Горизонтальная ось (категории) находиться снизу
        XDDFCategoryAxis bottomAxis = diagram.createCategoryAxis(AxisPosition.BOTTOM);
        //Вертикальная ось (значения) находиться слева
        XDDFValueAxis leftAxis = diagram.createValueAxis(AxisPosition.LEFT);

        //Создаём источник данных для названий блюд(какие значения брать) указывается лист в книге и положение клеточек
        XDDFDataSource<String> dishName = XDDFDataSourcesFactory.fromStringCellRange(
                dataSheet, new CellRangeAddress(1, data.size(), 0, 0));

        //Создаём источник данных для значений(средняя оценка) указывается лист в книге и положение клеточек
        XDDFNumericalDataSource<Double> dishRating = XDDFDataSourcesFactory.fromNumericCellRange(
                dataSheet, new CellRangeAddress(1, data.size(), 1, 1));

        //Создаём сам контейнер для данных диаграммы(столбчатая диаграмма BAR)
        XDDFChartData dataDiagram = diagram.createData(ChartTypes.BAR, bottomAxis, leftAxis);
        //Настройка диаграммы, приводим к конкретному типу и задаём направление столбцов(COL-вертикальные)
        XDDFBarChartData barData = (XDDFBarChartData) dataDiagram;
        barData.setBarDirection(BarDirection.COL);

        //Добавление серии данных в диаграмму, то-есть столбцов
        XDDFChartData.Series series = dataDiagram.addSeries(dishName, dishRating);
        //Настройка заголовка серии
        series.setTitle("Рейтинг", null);

        //Построение диаграммы с данными
        diagram.plot(dataDiagram);

        //Получение или создание легенды(Это цвет столбца и его расшифровка, какое блюдо)
        XDDFChartLegend legend = diagram.getOrAddLegend();
        //Расшифровку расположим сверху, справа
        legend.setPosition(LegendPosition.TOP_RIGHT);
    }

    //Метод построения круговой диаграммы
    public static void createPieChart(XSSFSheet dataSheet, XSSFSheet sheet, List<RatingFeedback> data) {
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 12, 0, 22, 15);
        XSSFChart chart = drawing.createChart(anchor);
        XDDFDataSource<String> dishName = XDDFDataSourcesFactory.fromStringCellRange(
                dataSheet, new CellRangeAddress(1, data.size(), 0, 0));
        XDDFNumericalDataSource<Double> dishRating = XDDFDataSourcesFactory.fromNumericCellRange(
                dataSheet, new CellRangeAddress(1, data.size(), 1, 1));

        XDDFChartData diagram = chart.createData(ChartTypes.PIE, null, null);
        XDDFChartData.Series series = diagram.addSeries(dishName, dishRating);
        series.setTitle("Доля", null);

        chart.plot(diagram);

        XDDFChartLegend legend = chart.getOrAddLegend();
        legend.setPosition(LegendPosition.TOP_RIGHT);
    }

    public static String createRatingData(XSSFSheet sheet, List<RatingFeedback> data) {
        XSSFRow header = sheet.createRow(0);
        header.createCell(0).setCellValue("Блюдо");
        header.createCell(1).setCellValue("Рейтинг");
        header.createCell(2).setCellValue("Дата");

        for (int i = 0; i < data.size(); i += 1) {
            XSSFRow dish = sheet.createRow(i + 1);
            RatingFeedback feedback = data.get(i);
            dish.createCell(0).setCellValue(feedback.getDishName());
            dish.createCell(1).setCellValue(feedback.getDishRating());
            dish.createCell(2).setCellValue(feedback.getRatingDate());
        }

        // Автоматически подбираем ширину колонок по содержимому
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
        return data.getFirst().getRatingDate();
    }

    public static boolean saveReportWithPath(XSSFWorkbook workbook, String feedbackDate) {
        try {
            // 1. Определяем путь
            Path desktop = Paths.get(System.getProperty("user.home"), "Desktop");
            Path reportsFolder = desktop.resolve("Отчёты по школьному питанию");

            // 2. Создаем папку если нет
            if (!Files.exists(reportsFolder)) {
                Files.createDirectory(reportsFolder);
                System.out.println("Создана папка: " + reportsFolder);
            }

            // 3. Создаем файл
            String fileName = String.format("analytics_report_%s.xlsx", feedbackDate);
            Path reportFile = reportsFolder.resolve(fileName);

            // 4. Сохраняем
            try (FileOutputStream fos = new FileOutputStream(reportFile.toFile())) {
                workbook.write(fos);
            }

            return true;

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            return false;
        }
    }
}
