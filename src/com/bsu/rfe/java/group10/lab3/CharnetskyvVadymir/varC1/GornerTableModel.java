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

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1; // Данная функция устарела но она работает(Перечеркнутый дабл)
    }

    public Object getValueAt(int row, int col) {
        double x = from + step*row;// Вычислить значение X как НАЧАЛО_ОТРЕЗКА + ШАГ*НОМЕР_СТРОКИ
        switch (col) {
            case 0:
                return x;
            case 1:{
                Double result = 0.0;
                for (int i = 0; i < coefficients.length; i++) {
                    result = result * x + coefficients[i];
                }// схема Горнера
                return result;
            }
            case 2:{
                Double result =0.0;
                for (int i = 0; i < coefficients.length; i++) {
                    result += Math.pow(x,coefficients.length-i-1)*coefficients[i];
                }// Обычный подсчёт с помощью pow()
                return result;
            }
            default: {
                //
                return Math.abs((Double)getValueAt(row,1)-(Double)getValueAt(row,2));
            }
        }
    }

    public Class<?> getColumnClass(int col) {
        return Double.class;
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            case 2:
                return "С помощью Math.pow()";
            default:
                return "Разница между значениями";
        }
    }

}
