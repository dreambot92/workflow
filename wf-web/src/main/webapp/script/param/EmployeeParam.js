/** Параметры рабочих */
define([
], function() {

    return {
        /** Заголовок для создания рабочего */
        createTitle: "Создать рабочего",
        /** Эта часть всегда присутствует в списке, которая создается для дерева */
        defaultView: [
            {id: 'root', name: 'Документы'},
            {id: 'outgoing', name: 'Исходящие', parent: 'root'},
            {id: 'incoming', name: 'Входящие', parent: 'root'},
            {id: 'task', name: 'Поручения', parent: 'root'}
        ],
        /** Утилитки для тулбара */
        tools: [
            {name: "close", label: "Закрыть"},
            {name: "edit", label: "Редактировать"},
            {name: "remove", label: "Удалить"}
        ],
        /** То, что хранится в объекте: Рабочий */
        param: {
            surname: {title: "Фамилия"},
            firstName: {title: "Имя"},
            patronymic: {title: "Отчество"},
            post: {title: "Должность"}
        }
    }
});