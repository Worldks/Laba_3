package com.bsu.rfe.java.group10.lab3.CharnetskyvVadymir.varC1;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private Double[] coefficients;
    // Объект диалогового окна для выбора файлов
// Компонент не создаѐтся изначально, т.к. может и не понадобиться
// пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;
    // Элементы меню вынесены в поля данных класса, так как ими необходимо
// манипулировать из разных мест
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem aboutProgramMenuItem;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    // Модель данных с результатами вычислений
    private GornerTableModel data;

    public MainFrame(Double[] coefficients) {       //Конструктор класса
// Обязательный вызов конструктора предка
        super("Табулирование многочлена на отрезке по схеме Горнера");
// Запомнить во внутреннем поле переданные коэффициенты
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
// Создать меню
        JMenuBar menuBar = new JMenuBar();
// Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
// Добавить в меню пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu Reference = new JMenu("Справка");
        menuBar.add(Reference);

        Action aboutProgramAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
                ImageIcon icon = new ImageIcon(kit.getImage("E:\\University\\Программирование\\Лабалаторные\\2 курс\\Laba_3\\Photo\\1.jpg"));
                JOptionPane.showMessageDialog(MainFrame.this,
                        new String[]{"- Чернецкий Владимир","- 10 группа"},
                        "О программе",JOptionPane.INFORMATION_MESSAGE,icon);
            }
        };
        aboutProgramMenuItem = Reference.add(aboutProgramAction);
        aboutProgramMenuItem.setEnabled(true);

// Создать новое "действие" по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
        public void actionPerformed(ActionEvent event) {
            if (fileChooser==null) {
                fileChooser = new JFileChooser();   // Если экземпляр диалогового окна "Открытьфайл" ещѐ не создан, то создать его
                fileChooser.setCurrentDirectory(new File(".")); // и инициализировать текущей директорией
            }
// Показать диалоговое окно
            if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                saveToTextFile(fileChooser.getSelectedFile());  // Если результат его показа успешный, сохранить данные в текстовый файл. Данный метод приинимает аргументы типа File
        }
    };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);    // Добавить соответствующий пункт подменю в меню "Файл"
        saveToTextMenuItem.setEnabled(false);   // По умолчанию пункт меню является недоступным (данных ещѐ нет)

        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {// Создать новое "действие" по сохранению в текстовый файл
        public void actionPerformed(ActionEvent event) {
            if (fileChooser==null) {
                fileChooser = new JFileChooser();   // Если экземпляр диалогового окна "Открыть файл" ещѐ не создан, то создать его
                fileChooser.setCurrentDirectory(new File(".")); // и инициализировать текущей директорией
            }
// Показать диалоговое окно
            if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
                saveToGraphicsFile(fileChooser.getSelectedFile());  // Если результат его показа успешный,сохранить данные в двоичный файл
        }
    };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);   // По умолчанию пункт меню является недоступным(данных ещѐ нет)

        Action searchValueAction = new AbstractAction("Найти значение многочлена") { // Создать новое действие по поиску значений многочлена
            public void actionPerformed(ActionEvent event) {// Запросить пользователя ввести искомую строку
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                        "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                 renderer.setNeedle(value);  // Установить введенное значение в качестве иголки
                getContentPane().repaint(); // Обновить таблицу
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);         // Добавить действие в меню "Таблица"
        searchValueMenuItem.setEnabled(false);  // По умолчанию пункт меню является недоступным (данных ещѐ нет)

        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());// Установить максимальный размер равный предпочтительному, чтобы предотвратить увеличение размера поля ввода
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());

        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));        // Задать для контейнера тип рамки "объѐмная"
        hboxRange.add(Box.createHorizontalGlue());// Добавить "клей" C1-H1
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());

// Установить предпочтительный размер области равным удвоенному
// минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(
                new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue()*2));

        getContentPane().add(hboxRange, BorderLayout.NORTH);// Установить область в верхнюю (северную) часть компоновки

// Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {// Задать действие на нажатие "Вычислить" и привязать к кнопке
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);// На основе считанных данных создать новый экземпляр модели таблицы
                    JTable table = new JTable(data);// Создать новый экземпляр таблицы __________________(Почему он так создаётся?)
// Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);// Установить размер строки таблицы в 30 пикселов
                    hBoxResult.removeAll();// Удалить все вложенные элементы из контейнера  hBoxResult
                    hBoxResult.add(new JScrollPane(table));// Добавить в hBoxResult таблицу, "обѐрнутую" в панель с полосами прокрутки
                    getContentPane().validate();// Обновить область содержания главного окна
                    saveToTextMenuItem.setEnabled(true);// Пометить ряд элементов меню как доступных
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {// В случае ошибки преобразования чисел показать сообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
// Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {// Задать действие на нажатие "Очистить поля" и привязать к кнопке
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");// Установить в полях ввода значения по умолчанию
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();// Удалить все вложенные элементы контейнера hBoxResult
                hBoxResult.add(new JPanel());// Добавить в контейнер пустую панель
                saveToTextMenuItem.setEnabled(false);// Пометить элементы меню как недоступные
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                getContentPane().validate();// Обновить область содержания главного окна
            }
        });

        Box hboxButtons = Box.createHorizontalBox();// Поместить созданные кнопки в контейнер
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());

// Установить предпочтительный размер области равным удвоенному минимальному, чтобы при
// компоновке окна область совсем не сдавили
        hboxButtons.setPreferredSize(new Dimension(new
                Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new
                Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2));

        getContentPane().add(hboxButtons, BorderLayout.SOUTH);// Разместить контейнер с кнопками в нижней (южной) области граничной компоновки

        hBoxResult = Box.createHorizontalBox();// Область для вывода результата пока что пустая
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);// Установить контейнер hBoxResult в главной (центральной) области граничной компоновки
    }
    protected void saveToGraphicsFile(File selectedFile) {
        try {
// Создать новый байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile));
// Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i<data.getRowCount(); i++) {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }
            out.close();// Закрыть поток вывода
        } catch (Exception e) {
// Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
// так как мы файл создаѐм, а не открываем для чтения
        }
    }
    protected void saveToTextFile(File selectedFile) {
        try {
// Создать новый символьный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);
// Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме Горнера");
                    out.print("Многочлен: ");
            for (int i=0; i<coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length-i-1));
                if (i!=coefficients.length-1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
// Записать в поток вывода значения в точках
            for (int i = 0; i<data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i,0)
                        + " равно " + data.getValueAt(i,1));
            }
            out.close();// Закрыть поток
        } catch (FileNotFoundException e) {
// Исключительную ситуацию "ФайлНеНайден" можно не
// обрабатывать, так как мы файл создаѐм, а не открываем
        }
    }

    public static void main(String[] args) {

        if (args.length==0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg: args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Ошибка преобразования строки '" +
                    args[i] + "' в число типа Double");
            System.exit(-2);
        }
        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Задать действие, выполняемое при закрытии окна
        frame.setVisible(true);
    }
}
