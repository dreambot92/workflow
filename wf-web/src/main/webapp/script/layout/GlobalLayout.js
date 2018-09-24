/**
 *  Работа c визуалкой приложения
 */
define([
    "dojo/_base/array",
    "dijit/form/Button",
    "dijit/form/Form",
    "dijit/form/ValidationTextBox",
    "dijit/layout/BorderContainer",
    "dijit/layout/ContentPane",
    "dijit/layout/TabContainer",
    "dijit/MenuBar",
    "dijit/MenuBarItem",
    "dijit/Tree",
    "dijit/tree/ObjectStoreModel",
    "dojo/dom-style",
    "dojo/store/Memory"
],
function(array, Button, Form, ValidationTextBox, BorderContainer, ContentPane, TabContainer, MenuBar, MenuBarItem, Tree, ObjectStoreModel, domStyle, Memory) {

    var TREE = "tree";

    // Общий слой
    var appLayout = new BorderContainer({
            design: "headline",
            style: "height: 100%;"
        }, "appLayout"),
        // TabContainer приложения
        tabContainer = new TabContainer({
            id: "contentTabs",
            region: "center",
            tabPosition: "top"
        }),
        // Лефтбар
        leftCol = new BorderContainer({
            region: "left",
            design: "headline",
            style: "overflow: hidden; width: 14em;",
            splitter: true
        }),
        // Правая и нижняя часть лефтбара (разные блоки)
        leftTop = new ContentPane({
            style: "height: 20em",
            class: "centerPanel",
            region: "top",
            splitter: true
        }),
        leftBottom = new ContentPane({
            class: "centerPanel",
            region: "center"
        }),
        // Текущие открытые табы
        currentTabs = {};

    // добавление слоя приложения
    function addAppLayout() {

        // Добавляем на страницу табконтейнер и лефтбар
        appLayout.addChild(tabContainer);
        console.log("Добавляем табконтейнер");
        // Добавляем лефтбар
        appLayout.addChild(leftCol);
        console.log("Добавляем лефтбар");
        appLayout.startup();
        // Делим лефтбар на 2 части
        leftCol.addChild(leftTop);
        leftCol.addChild(leftBottom);
        console.log("Разделяем на две части лефтбар");
    }

    // возвращает новый таб
    function addTab(title, tree) {

        // если в базе нету таба с таким заголовком, то создаем
        if (currentTabs[title] == null) {
            currentTabs[title] = new ContentPane({
                title: title,
                closable: true,
                // при переключении на этот таб показываем прикрепленное к нему дерево
                onShow: function () {
                    hideAllTree();
                    if (this.hasOwnProperty(TREE) && this[TREE] != null) {
                        showTree(this[TREE])
                    }
                },
                // при закрытии удаляем таб из базы и удаляем его дерево
                onClose: function () {
                    closeTab(title);
                }
            }).placeAt(tabContainer);
            // прикрепляем дерево к табу
            currentTabs[title][TREE] = tree;
        }
        // фокусируемся
        selectTab(title);
        return currentTabs[title]
    }

    // фокусировка таба по его заголовку
    function selectTab(title) {
        tabContainer.selectChild(currentTabs[title])
    }

    // закрытие таба по его заголовку и удаления его из коллекции текущих табов
    function closeTab(title) {
        tabContainer.removeChild(currentTabs[title]);
        delete currentTabs[title];
    }

    // возвращает новое дерево
    // на входе необходимый список и функция onClick
    function createTree(list, onClick) {

        return new Tree({
            model: new ObjectStoreModel({
                store: new Memory({
                    data: list,
                    getChildren: function (obj) {
                        return this.query({parent: obj.id});
                    }
                }),
                query: {id: 'root'}
            }),
            style: "overflow-x: hidden",
            onOpenClick: true,
            autoExpand: true,
            onClick: function(o) {
                if (onClick != null) {
                    onClick(o)
                }
            }
        })
    }

    // скрыть все деревья открытых табов
    // скрываем с помощью display:none
    function hideAllTree() {

        // смысла перебирать нету, если нету открытых табов
        if (currentTabs != null) {
            // начинаем перебор
            for (var key in currentTabs) {
                console.log("Проверка параметра " + key + " на наличие параметра tree");
                // нас интересует не только наличие свойства tree, но и наличие domNod-ы у дерева
                if (currentTabs[key]["tree"] != null && currentTabs[key]["tree"].hasOwnProperty("domNode")) {
                    console.log("Параметр tree найден. Скрываем");
                    // скрываем
                    domStyle.set(currentTabs[key][TREE].domNode, "display", "none");
                }
            }
        }
    }

    // показываем нужное дерево
    function showTree(tree) {

        console.log("Показываем скрытое дерево");
        domStyle.set(tree.domNode, "display", "block");
    }

    // создать тулбар
    function addToolbar(tools, onClick) {

        var menuBar = new MenuBar({});
        array.forEach(tools, function(o) {
            menuBar.addChild(new MenuBarItem({
                label: o.label,
                onClick: function() {
                    if (onClick != null) {
                        onClick(o.name)
                    }
                }
            }))
        });
        return menuBar;
    }

    return {
        /** Здесь хранятся открытые табы */
        currentTabs: currentTabs,
        /** TabContainer приложения. Виджет для работы с окошками */
        tabContainer: tabContainer,
        /** Окошко слева сверху */
        leftTop: leftTop,
        /** Окошко слева снизу */
        leftBottom: leftBottom,
        /** Создать рабочую зону. Табконтейнер по центру и разделенный лефтбар */
        addAppLayout: function() {
            addAppLayout();
        },
        /**
         *  Создание таба
         *  @param title заголовок таба
         *  @param tree дерево, которое прикрепляется к табу, либо {@code = null}, если дерева нет
         *  @return ContentPane этого таба
         */
        addTab: function(title, tree) {
            return addTab(title, tree);
        },
        /**
         *  Фокусировка на табе
         *  @param title заголовок таба
         */
        selectTab: function(title) {
            selectTab(title);
        },
        /**
         *  Закрытие таба
         *  @param title заголовк таба
         */
        closeTab: function(title) {
            closeTab(title);
        },
        /**
         *  Создание дерева
         *  @param list коллекция объектов, которые будут занесены в дерево
         *  @param onClick функция, которая вызывается при нажатии на элемент
         *  @return тот самый Tree
         */
        createTree: function(list, onClick) {
            return createTree(list, onClick)
        },
        /** Скрыть все деревья */
        hideAllTree: function() {
            hideAllTree()
        },
        /**
         *  Показать конкретное дерево
         *  @param tree то самое дерево
         */
        showTree: function(tree) {
            showTree(tree)
        },
        /**
         *  Создать простой блок
         *  @param content то, что будет внутри блока
         *  @return этот ContentPane
         */
        addSimpleContentPane: function(content) {
            return new ContentPane({
                content: content
            })
        },
        /**
         *  Создание Toolbar-а
         *  @param tools массив объектов, в котором каждый элемент содержит:
         *      - name указатель на функцию
         *      - label визуальное текстовое отображение
         *  @param onClick функция, которая срабатывает при нажатии на элемент тулбара
         *  @return тот самый MenuBar, который и представляет собой Toolbar
         */
        addToolbar: function(tools, onClick) {
            return addToolbar(tools, onClick)
        },
        /**
         *  Создание ValidationTextBox
         *  @param value то, что будет введено в поле
         *  @result тот самый ValidationTextBox
         */
        addValidationTextBox: function(value) {

            return new ValidationTextBox({
                value: value,
                pattern: '[0-9А-я ]+'
            });
        },
        /**
         *  Создать форму
         *  @param onSuccessSubmit функция, которая вызывается при нажатии на кнопку submit в форме
         *  @return эта Form
         */
        addSimpleForm: function(onSuccessSubmit) {
            return new Form({
                onSubmit: function() {
                    if (this.validate()) {
                        onSuccessSubmit()
                    }
                    return false;
                }
            })
        },
        /**
         *  Создать кнопку на подтверждение формы
         *  @param label текст на кнопке
         *  @return этот Button
         */
        addSubmitButton: function(label) {
            return new Button({
                label: label,
                type: "submit"
            });
        }
    }
});