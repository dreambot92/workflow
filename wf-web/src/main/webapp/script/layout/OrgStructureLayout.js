/**
 *  Функции для работы со слоями со списками работников
 */
define([
    "dojo/_base/array",
    "dojo/_base/lang",
    'dojo/data/ItemFileWriteStore',
    'dojox/grid/DataGrid',
    "script/layout/GlobalLayout",
    "script/param/GlobalParam",
    "script/param/EmployeeParam",
    "script/param/OrgStructureParam",
    "script/request/EmployeeRequest"
], function(array, lang, ItemFileWriteStore, DataGrid, GlobalLayout, GlobalParam, EmployeeParam, OrgStructureParam, EmployeeRequest) {

    // собрать коллекцию, чтобы в дальнейшем использовать его в дереве
    function collect(oldList) {
        // клонируем дефолтные поля для массива, который будет использоваться для создания дерева
        var newList = lang.clone(OrgStructureParam.defaultView);
        // Используемые id рабочих. В дальнейшем будет использоваться для создания уникальных айди для рабочих
        GlobalParam.usedId = [];

        array.forEach(oldList, function(o) {
            newList.push({
                id: "person_" + ++GlobalParam.counter,
                uid: o.uid,
                name: lang.replace("{surname} {firstName} {patronymic}", o),
                surname: o.surname,
                firstName: o.firstName,
                patronymic: o.patronymic,
                post: o.post,
                parent: "root"
            });
            OrgStructureParam.usedId.push(o.uid)
        });
        return newList;
    }

    // создать дерево
    function createTable(list, onClick) {

        var data = {
            identifier: "id",
            items: []
        };
        for (var i = 0; i < list.length; i++) {
            data.items.push(lang.mixin({ id: i+1 }, list[i]));
        }

        console.log(list);
        var store = new ItemFileWriteStore({data: data}),
            layout = [[
                {'name': 'id', 'field': 'id', 'width': '12%'},
                {'name': 'Фамилия', 'field': 'surname', 'width': '22%'},
                {'name': 'Имя', 'field': 'firstName', 'width': '22%'},
                {'name': 'Отчество', 'field': 'patronymic', 'width': '22%'},
                {'name': 'Должность', 'field': 'post', 'width': '22%'}
            ]],
            grid = new DataGrid({
                store: store,
                structure: layout,
                onStyleRow: function(row) {
                    myStyleRow(row)
                },
                // функция, которая вызывается при нажатии на конкретный элемент таблицы
                onRowClick: function(e) {
                    onClick(this.getItem(e.rowIndex).uid[0]);
                },
                rowSelector: '20px'
            });

        // настраивание стиля
        function myStyleRow(row){
            var item = grid.getItem(row.index);
            if(item){
                var type = store.getValue(item, "id", null);
                // настраиваем стиль для четных элементов
                if(type % 2 === 0){
                    row.customStyles += "color:blue;";
                }
            }
            grid.focus.styleRow(row);
            grid.edit.styleRow(row);
        }
        return grid;
    }

    // вызов функции create из тулбара
    function create(onSuccess) {

        var obj = lang.clone(EmployeeParam.param),
            createTab = GlobalLayout.addTab(EmployeeParam.createTitle),
            form = GlobalLayout.addSimpleForm(function() {
                // Для уникальности идентификаторов берем максимальный элемент из массива usedId,
                // в котором находятся все идентифкаторы рабочих, и увеличиваем на единицу
                var id = Math.max.apply(null, OrgStructureParam.usedId),
                    employee = {
                        id: id + 1
                    };
                for (var key in obj) {
                    employee[key] = obj[key]["input"].displayedValue;
                }
                // добавляем этот идентификатор в массив
                OrgStructureParam.usedId.push(id + 1);
                // добавляем в базу данных
                EmployeeRequest.createEmployee(employee, function() {
                    GlobalLayout.closeTab(EmployeeParam.createTitle);
                    setTimeout(onSuccess, 300)
                });
            }).placeAt(createTab);

        // добавляем input-ы на страницу
        for (var key in obj) {
            obj[key]["pane"] = GlobalLayout.addSimpleContentPane(obj[key]["title"]).placeAt(form);
            obj[key]["input"] = GlobalLayout.addValidationTextBox().placeAt(obj[key]["pane"]);
        }
        GlobalLayout.addSubmitButton("Сохранить").placeAt(form)
    }

    return {

        /** Дерево с рабочими */
        personTree: null,
        /** Таблица с рабочими */
        personTable: null,
        /**
         *  Собрать коллекцию, чтобы в дальнейшем использовать его в дереве
         *  @param oldList в приложении это результат GET-запроса
         *  @return новая коллекция, которая может использоваться в дереве
         */
        collect: function(oldList) {
            return collect(oldList);
        },
        /**
         *  Создать таблицу по работникам. Использовать коллекцию от возвращаемого значения collect()
         *  @param list список рабочих
         *  @param onClick функция, которая вызывается при нажатии на элемент в таблице
         *  @return таблица DataGrid с данными из списка list
         */
        createTable: function(list, onClick) {
            return createTable(list, onClick);
        },
        /** Закрытие таба. Нажатие на кнопку close */
        close: function() {
            GlobalLayout.closeTab(OrgStructureParam.title);
        },
        /**
         *  Создание рабочего. Нажатие на кнопку create.
         *  Создание таба с формой, валидацией и кнопкой
         *  @param onSuccess функция, которая вызывается при успешном завершении функции create
         */
        create: function(onSuccess) {
            create(onSuccess)
        }
    }
});