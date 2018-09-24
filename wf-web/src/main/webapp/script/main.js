require([
    "dojo/_base/array",
    "script/layout/EmployeeLayout",
    "script/layout/GlobalLayout",
    "script/layout/OrgStructureLayout",
    "script/param/EmployeeParam",
    "script/param/OrgStructureParam",
    "script/request/EmployeeRequest",
    "script/request/OrgStructureRequest"
],
function(array, EmployeeLayout, GlobalLayout, OrgStructureLayout, EmployeeParam, OrgStructureParam, EmployeeRequest, OrgStructureRequest){

    /**
     *  Используется при обновлении каких-либо данных в бд.
     *  Спустя это время обновляются данные уже на странице
     */
    var TIMEOUT = 300;

    // добавляем слой приложения
    GlobalLayout.addAppLayout();
    // добавляем таб с рабочими и тулбар
    addEmployeesTabAndToolbar();
    // делаем запрос на поиск работников
    OrgStructureRequest.findEmployees(function(result) {
        addPersonTreeAndTable(result);
    });

    // добавление таба с работниками и тулбара
    function addEmployeesTabAndToolbar() {
        // создаем таб
        var tab = GlobalLayout.addTab(OrgStructureParam.title);
        // создаем тулбар
        GlobalLayout.addToolbar(OrgStructureParam.tools, function(o) {
            // при нажатии на кнопку тулбара применяется функция, которая находится в OrgStructureLayout
            // функции для OrgStructureLayout:
            //  - close
            //  - create
            if (OrgStructureLayout.hasOwnProperty(o)) {
                OrgStructureLayout[o](function() {
                    setTimeout(function() {
                        refresh()
                    }, TIMEOUT)
                });
            } else {
                console.log("Функция " + o + " отсутствует");
            }
        }).placeAt(tab);
    }

    // добавление таблицы с работниками и дерева
    function addPersonTreeAndTable(result) {

        // собираем коллекцию из работников
        var list = OrgStructureLayout.collect(result);

        console.log("Создаем таблицу и добавляем ее в таб");
        OrgStructureLayout.personTable = OrgStructureLayout.createTable(result, function(id) {
            // при нажатии на рабочего в таблице пробегаемся по списку рабочих и открываем нужную страницу
            for (var key in list) {
                if (list[key].hasOwnProperty("uid") && list[key].uid === id) {
                    onClickEmployees(list[key]);
                }
            }
        }).placeAt(GlobalLayout.currentTabs[OrgStructureParam.title]);
        console.log("Добавляем дерево");
        OrgStructureLayout.personTree = GlobalLayout.createTree(list, function(elem) {
            // обновляем данные по сотрудникам, если нажали на корневой элемент таблицы,
            // иначе открываем страницу с информацией по рабочему
            if (elem.id === "root") {
                refresh();
            } else {
                onClickEmployees(elem);
            }
        }).placeAt(GlobalLayout.leftTop);
    }

    // обновление данных по работникам в таблице и дереве
    function refresh() {

        var title = OrgStructureParam.title,
            tree = OrgStructureLayout.personTree,
            table = OrgStructureLayout.personTable;

        console.log("Попытка обновить данные по рабочим в дереве и табе");
        OrgStructureRequest.findEmployees(function(result) {
            // если таб закрыт, то открываем его
            if (GlobalLayout.currentTabs[title] == null) {
                addEmployeesTabAndToolbar()
            }
            // если дерево с рабочими есть, то удаляем
            if (tree != null) {
                tree.destroyRecursive();
                console.log("Найдено дерево, удаляем");
            }
            // если таблица есть, то удаляем
            if (table != null) {
                table.destroyRecursive();
                console.log("Найдена таблица с рабочими, удаляем");
            }
            // добавляем таблицу и дерево с рабочими
            addPersonTreeAndTable(result);
        });
    }

    // событие при клике на работника
    function onClickEmployees(elem) {

        console.log(elem);
        EmployeeRequest.findDocumentsByEmployeeId(elem.uid, function(result) {
            // если таб уже есть, то просто фокусируемся
            if (GlobalLayout.currentTabs.hasOwnProperty(elem.name)) {
                GlobalLayout.selectTab(elem.name);
                return;
            }
            var list = EmployeeLayout.collect(result),
                tree = GlobalLayout.createTree(list, function (elem) {
                    console.log(elem)
                }).placeAt(GlobalLayout.leftBottom),
                tab = GlobalLayout.addTab(elem.name, tree);
            // создаем тулбар
            GlobalLayout.addToolbar(EmployeeParam.tools, function(o) {
                // при нажатии на кнопку тулбара применяется функция, которая находится в EmployeeLayout
                // функции для OrgStructureLayout:
                //  - close
                //  - edit
                //  - remove
                if (EmployeeLayout.hasOwnProperty(o)) {
                    EmployeeLayout[o](elem, function() {
                        setTimeout(function() {
                            refresh()
                        }, TIMEOUT);
                    });
                } else {
                    console.log("Функция " + o + " отсутствует");
                }
            }).placeAt(tab);
            addEmployeeContent(elem);
        });
    }

    // контент по работникам
    function addEmployeeContent(elem) {

        var add = function(content, place) {
                return GlobalLayout.addSimpleContentPane(content).placeAt(place);
            },
            mainTab = GlobalLayout.currentTabs[elem.name],
            bioContainer = add("", mainTab);

        // добавляем картинку
        add("<div class='photo'><img src='img/no_photo.png'/></div>", mainTab);
        // создаем блоки типа: <div>Фамилия: <span>{его фамилия}</span></div>
        for (var key in EmployeeParam.param) {
            if (EmployeeParam.param[key].hasOwnProperty("title") && elem.hasOwnProperty(key)) {
                add(EmployeeParam.param[key].title + ": <span>" + elem[key] + "</span>", bioContainer)
            }
        }
    }
});