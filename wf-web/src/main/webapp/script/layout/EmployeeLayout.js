/** Функции для работы со списками работников */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    "script/layout/GlobalLayout",
    "script/param/EmployeeParam",
    "script/param/GlobalParam",
    "script/request/EmployeeRequest"
], function(array, lang, GlobalLayout, EmployeeParam, GlobalParam, EmployeeRequest) {

    // собрать коллекцию, чтобы в дальнейшем использовать его в дереве
    function collect(oldList) {
        // клонируем дефолтные поля для массива, который будет использоваться для создания дерева
        var newList = lang.clone(EmployeeParam.defaultView);

        array.forEach(oldList, function(o) {
            newList.push({
                id: "documents_" + ++GlobalParam.counter,
                name: lang.replace("{name} - {registrationNumber}", o),
                parent: o.type
            });
        });
        return newList;
    }

    // функция, которая вызывется при нажатии на кнопку в Тулбаре - edit
    function edit(elem, onSuccess) {
        // клонируем параметры рабочего
        var obj = lang.clone(EmployeeParam.param),
            title = lang.replace("Редактировать {surname} {firstName}", elem),
            editTab = GlobalLayout.addTab(title),
            // создаем форму и определяем событие, которое происходит при submit-е формы
            form = GlobalLayout.addSimpleForm(function() {
                var employee = {
                    id: elem.uid
                };
                // копируем значения с input-ов
                for (var key in obj) {
                    employee[key] = obj[key]["input"].displayedValue;
                }
                // записываем в базу
                EmployeeRequest.editEmployee(employee, function() {
                    GlobalLayout.closeTab(title);
                    onSuccess()
                });
            }).placeAt(editTab);
        // закрываем таб просмотра информации по рабочему
        GlobalLayout.closeTab(elem.name);
        // добавляем input-ы на страницу
        for (var key in obj) {
            obj[key]["pane"] = GlobalLayout.addSimpleContentPane(obj[key]["title"]).placeAt(form);
            obj[key]["input"] = GlobalLayout.addValidationTextBox(elem[key]).placeAt(obj[key]["pane"]);
        }
        GlobalLayout.addSubmitButton("Сохранить").placeAt(form)
    }

    return {

        /**
         *  Собрать коллекцию по документам конкретного рабочего, чтобы в дальнейшем использовать его в дереве
         *  @param oldList в приложении это результат GET-запроса
         *  @return новая коллекция, которая может использоваться в дереве
         */
        collect: function(oldList) {
            return collect(oldList);
        },
        /**
         *  Закрытие таба. Нажатие на кнопку close
         *  @param elem рабочий
         */
        close: function(elem) {
            GlobalLayout.closeTab(elem.name);
        },
        /**
         *  Редактирование рабочего. Нажатие на кнопку edit.
         *  Создание таба с формой, валидацией и кнопкой
         *  @param elem рабочий
         *  @param onSuccess функция, которая вызывается при успешном завершении функции edit
         */
        edit: function(elem, onSuccess) {
            edit(elem, onSuccess);
        },
        /**
         *  Удаление рабочего. Нажатие на кнопку remove
         *  @param elem рабочий
         *  @param onSuccess функция, которая вызывается при успешном завершении функции remove
         */
        remove: function(elem, onSuccess) {
            EmployeeRequest.removeEmployee(elem.uid, function() {
                GlobalLayout.closeTab(elem.name);
                onSuccess();
            });
        }
    }
});