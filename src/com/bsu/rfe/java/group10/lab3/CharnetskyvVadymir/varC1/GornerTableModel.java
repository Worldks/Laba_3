package com.bsu.rfe.java.group10.lab3.CharnetskyvVadymir.varC1;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }
    public Double getTo() {
        return to;
    }
    public Double getStep() {
        return step;
    }

    @Override           // переопределение
    public int getColumnCount() {
        return 2;
    }
    @Override
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1; // Данная функция устарела но она работает
    }
    @Override
    public Object getValueAt(int row, int col) {
// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        double x = from + step*row;
        if (col==0) {
            return x;
        } else {
            Double result = 0.0;
            // Вычисление значения в точке по схеме Горнера.
            // Вспомнить 1-ый курс и реализовать
            for (int i = 0; i < coefficients.length; i++) {
                result = result * x + coefficients[i];
            }
            return result;
        }
    }
    @Override
    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
    @Override
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            default:
                return "Значение многочлена";
        }
    }

}
