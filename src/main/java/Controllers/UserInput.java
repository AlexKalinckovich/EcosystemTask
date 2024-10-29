package Controllers;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UserInput {
    private static final Scanner scanner = new Scanner(System.in);
    public static int takeUserIntegerInput(){
        int result = 0;
        boolean isCorrect = false;
        while(!isCorrect){
            isCorrect = true;
            try{
                result = Integer.parseInt(scanner.nextLine());
            }catch(NumberFormatException e){
                isCorrect = false;
                System.out.println("Введено неверное значение.");
            }
        }
        return result;
    }

    public static int takeUserInputFromRange(int min, int max){
        int result = 0;
        boolean isCorrect = false;
        while(!isCorrect){
            isCorrect = true;
            result = takeUserIntegerInput();
            if(result < min || result > max){
                isCorrect = false;
                System.out.println("Введено значение вне допустимого диапазона.");
            }
        }
        return result;
    }

    public static String takeUserStringInput(){
        String result = "";
        final int MAX_STR_LENGTH = 20;
        boolean isCorrect = false;
        while(!isCorrect){
            isCorrect = true;
            result = scanner.nextLine();
            if(result.isEmpty()){
                System.out.println("Неверный ввод, строка пустая.");
                isCorrect = false;
            } else if (result.length() > MAX_STR_LENGTH) {
                System.out.println("Неверный ввод, строка слишком большая.");
                isCorrect = false;
            }
        }
        return result;
    }

    public static String createNewFile(String directory) {
        String result = "";
        boolean isCorrect = false;
        while (!isCorrect) {
            isCorrect = true;
            System.out.println("Введите имя файла для создания (с .txt):");
            result = takeUserStringInput();

            if (!result.endsWith(".txt")) {
                System.out.println("Ошибка: файл должен быть текстовым (.txt). Попробуйте снова.");
                isCorrect = false;
            } else {
                File file = new File(directory + result);

                // Проверяем, существует ли директория
                File dir = new File(directory);
                if (!dir.exists()) {
                    dir.mkdirs();  // Создаём директорию, если её нет
                }

                if (file.exists()) {
                    System.out.println("Файл с таким именем уже существует. Попробуйте другое имя.");
                    isCorrect = false;
                } else {
                    try {
                        file.createNewFile();
                        System.out.println("Файл успешно создан: " + file.getPath());
                    } catch (IOException e) {
                        System.out.println("Ошибка при создании файла: " + e.getMessage());
                        isCorrect = false;
                    }
                }
            }
        }
        return result;
    }

    // Метод для выбора существующего файла
    private static String selectExistingFile(String directory) {
        String result = "";
        boolean isCorrect = false;
        while (!isCorrect) {
            isCorrect = true;
            System.out.println("Введите имя существующего файла для записи (с .txt):");
            result = takeUserStringInput();

            if (!result.endsWith(".txt")) {
                System.out.println("Ошибка: файл должен быть текстовым (.txt). Попробуйте снова.");
                isCorrect = false;
            } else {
                File file = new File(directory + result);

                if (!file.exists()) {
                    System.out.println("Ошибка: файл не существует. Попробуйте снова.");
                    isCorrect = false;
                } else if (!file.canWrite()) {
                    System.out.println("Ошибка: файл недоступен для записи. Попробуйте снова.");
                    isCorrect = false;
                }
            }
        }
        return result;
    }

    public static String takeUserFilePathFromInput(String directory) {
        String result = "";
        boolean isCorrect = false;
        while (!isCorrect) {
            isCorrect = true;
            System.out.println("Хотите создать новый файл или записать в уже существующий?");
            System.out.println("1. Создать новый файл");
            System.out.println("2. Использовать существующий файл");
            int choice = takeUserInputFromRange(1, 2);

            if (choice == 1) {
                result = createNewFile(directory);
            } else {
                result = selectExistingFile(directory);
            }
        }
        return result;
    }

}
