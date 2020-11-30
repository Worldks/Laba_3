package com.bsu.rfe.java.group10.lab3.CharnetskyvVadymir.varC1;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.math.BigInteger;

public class GornerTableCellRenderer implements TableCellRenderer {

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private String needle = null;   // Ищем ячейки, строковое представление которых совпадает с needle
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();  // Получение числового формата и преобразование его к десятичному
    private Boolean simlpeNumbers= false;

    public GornerTableCellRenderer() {          // Конструктор класса
        formatter.setMaximumFractionDigits(5);  // Показывать только 5 знаков после запятой
        formatter.setGroupingUsed(false);       // Т.е. показывать число как "1000", а не "1 000" или "1,000"
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();    // Установить в качестве разделителя дробной части точку, а не запятую.
        dottedDouble.setDecimalSeparator('.');      // Т.к. по дефолту стоит запятая для России и Беларуси
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);// Разместить надпись внутри панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));// Установить выравнивание надписи по левому краю панели
    }

    public Component getTableCellRendererComponent(JTable table/*ссылку на экземпляр таблицы, ячейка которой отображается*/, Object value/*флаг выделения ячейки*/, boolean isSelected/*флаг наличия фокуса ввода в ячейке*/, boolean hasFocus, int row, int col) {

        String formattedDouble = formatter.format(value);   // Преобразовать double в строку с помощью форматировщика
        label.setText(formattedDouble); // Установить текст надписи равным строковому представлению числа

        if(Double.parseDouble(formattedDouble) < 0){
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        }else if((Double.parseDouble(formattedDouble) > 0)){
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        }else if(Double.parseDouble(formattedDouble) == 0){
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        }

        if (col == 1 && needle != null && needle.equals(formattedDouble)) {
            panel.setBackground(Color.RED);
        } else {
            panel.setBackground(Color.WHITE);   // Иначе - в обычный белый
        }

        if(simlpeNumbers){
            final double error = 0.1;
            double numberFromTable = Double.parseDouble(formattedDouble);
            Integer bIntegerPart =  (int)numberFromTable;
            Integer eIntegerPart = (int)numberFromTable+1;
            // Если наше число отклоняеться от целой части больше чем на 0.1
            // То это число точно не будет входить в погрешность 0.1 для целых чисел
            if(bIntegerPart >= numberFromTable-error && bIntegerPart >= 1.9){
                // Проверим число на простоту, выполнив тест на простоту
                // В Java уже реализован тест Рабина-Миллера в классe BigInteger
                BigInteger bigInteger = BigInteger.valueOf(bIntegerPart);
                boolean simpleNumber = bigInteger.isProbablePrime((int)Math.log(bIntegerPart));
                // Если число простое окрасим его поле в оранжевый
                if(simpleNumber) panel.setBackground(Color.ORANGE);
                else panel.setBackground(Color.WHITE);
            }else if(eIntegerPart <= numberFromTable+error && eIntegerPart >= 1.9){
                BigInteger bigInteger = BigInteger.valueOf(eIntegerPart);
                boolean simpleNumber = bigInteger.isProbablePrime((int)Math.log(eIntegerPart));
                if(simpleNumber) panel.setBackground(Color.ORANGE);
                else panel.setBackground(Color.WHITE);
            }else panel.setBackground(Color.WHITE);
        }

        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }
    public void findSimple(boolean flag){ this.simlpeNumbers = flag; }

}