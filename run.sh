#!/bin/bash


if ! command -v java &> /dev/null
then
    echo "Java не установлена. Пожалуйста, установите Java 11 или выше."
    exit 1
fi


if [ ! -d "src" ]; then
    echo "Директория 'src' не найдена. Убедитесь, что исходный код находится в папке 'src'."
    exit 1
fi


mkdir -p out


echo "Компиляция проекта..."
javac -d out $(find src -name "*.java")


if [ $? -ne 0 ]; then
    echo "Ошибка компиляции."
    exit 1
fi


cd out || exit


echo "Запуск программы..."
java Main