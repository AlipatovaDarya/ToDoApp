package com.example.todoapp3.utils

import com.example.todoapp3.model.Importance
import com.example.todoapp3.model.TodoItem
import java.time.LocalDate

class TodoItemsListSource{

    companion object {
        val sourceList = mutableListOf(
            TodoItem(
                "1",
                "Купить тетрадь1",
                Importance.MEDIUM,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                null
            ),
            TodoItem(
                "2",
                "Сдать лабораторные работы №1, 2, 3 по информатике",
                Importance.MEDIUM,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "3",
                "Полить фиалку",
                Importance.HIGH,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                null
            ),
            TodoItem(
                "4",
                "Полить розу Pierre de Ronsard — сорт плетистых крупноцветковых роз. Первый" +
                        " сорт серии роз «Romantica», сочетающих густомахровые цветки «старинной» формы" +
                        " с силой и здоровьем современных сортов. Назван в честь Пьера де Ронсара, " +
                        "французского поэта XVI века, 400-летие со дня смерти которого отмечали в " +
                        "Париже в 1985 году.",
                Importance.HIGH,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),

                ),
            TodoItem(
                "5",
                "Пойти туда, не знаю, куда, найти то, не знаю, что",
                Importance.LOW,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "6",
                "Купить хлеб",
                Importance.MEDIUM,
                null,
                true,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "7",
                "Подготовиться к экзамену по теории вероятностей и математической статистике",
                Importance.LOW,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "8",
                "дз №n",
                Importance.MEDIUM,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "9",
                "Покраситься в вишнёвый",
                Importance.LOW,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "10",
                "Научиться писать код",
                Importance.HIGH,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                null
            ),

            TodoItem(
                "11",
                "Самостоятельно доказать теорему Ферма",
                Importance.HIGH,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),

                ),

            TodoItem(
                "12",
                "Подготовиться к походу с палатками",
                Importance.LOW,
                null,
                true,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),

            TodoItem(
                "13",
                "привет",
                Importance.LOW,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),

            TodoItem(
                "14",
                "Пожелать проверяющему хорошего настроения",
                Importance.HIGH,
                null,
                true,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),

            TodoItem(
                "15",
                "Сдать дз по интерфейсам в шмр",
                Importance.HIGH,
                LocalDate.of(2024, 5, 15),
                true,
                LocalDate.of(2024, 5, 15),
                null,
            ),
            TodoItem(
                "16",
                "Узнать, что такое инъекция зависимостей, о библиотеках для которой спорят в чате",
                Importance.MEDIUM,
                LocalDate.of(2024, 5, 15),
                false,
                LocalDate.of(2024, 5, 15),
                null
            ),
            TodoItem(
                "17",
                "Придумать ещё один текст для todoItems",
                Importance.LOW,
                LocalDate.of(2024, 5, 15),
                true,
                LocalDate.of(2024, 5, 15),
                null
            ),
            TodoItem(
                "18",
                "о",
                Importance.LOW,
                LocalDate.of(2024, 5, 15),
                true,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "19",
                "Посадить дерево: Берёза пови́слая, или берёза бородавчатая — вид растений рода" +
                        " Берёза семейства Берёзовые. Более точно устаревшее синонимичное название" +
                        " Берёза бородавчатая отсносится к основному подвиду Betula pendula subsp." +
                        " pendula, выделяя его среди пяти признаваемых современной " +
                        "классификацией подвидов.",
                Importance.MEDIUM,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2024, 5, 15),
            ),
            TodoItem(
                "20",
                "Построить дом: Котте́дж — индивидуальный городской или сельский малоэтажный " +
                        "жилой дом с небольшим участком прилегающей земли для постоянного или " +
                        "временного проживания одной нуклеарной семьи.",
                Importance.MEDIUM,
                null,
                false,
                LocalDate.of(2024, 5, 15),
                null
            ),
        )
    }

}