define([
    "dojo/request"
], function(request) {

    var HTTP_BAD_REQUEST = 400,
        HTTP_SERVER_ERROR = 503;

    // поиск документов конкретного рабочего
    function findDocumentsByEmployeeId(id, onSuccess) {

        var req = "ecm/employees/" + id;
        console.log("Выполняется следующий GET запрос: " + req);
        request(req, {
            method: "GET",
            handleAs: "json"
        }).response.then(
            function(o) {
                if (onSuccess != null) {
                    onSuccess(o.data);
                }
            },
            function(e) {
                let httpCode = e.response.status;
                if (httpCode === HTTP_BAD_REQUEST) {
                    console.log("Используются некорректные символы или один из параметров пуст");
                }
                if (httpCode === HTTP_SERVER_ERROR) {
                   console.log("При обработке запроса произошла ошибка со стороны сервера");
                }
            }
        )
    }

    // создание рабочего
    function createEmployee(obj, onSuccess) {

        var req = "ecm/employees/";
        request(req, {
            data: JSON.stringify(obj),
            method: "PUT",
            handleAs: "json",
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json;charset=utf-8'
            }
        }).response.then(
            function(o) {
                console.log("Рабочий успешно создан");
                if (onSuccess != null) {
                    onSuccess(o)
                }
            },
            function(e) {
                let httpCode = e.response.status;
                if (httpCode === HTTP_BAD_REQUEST) {
                    console.log("Используются некорректные символы или один из параметров пуст");
                }
                if (httpCode === HTTP_SERVER_ERROR) {
                    console.log("При обработке запроса произошла ошибка со стороны сервера");
                }
            }
        )
    }

    // редактирование рабочего
    function editEmployee(obj, onSuccess) {

        var req = "ecm/employees/" + obj.id;
        request(req, {
            data: JSON.stringify(obj),
            method: "PUT",
            handleAs: "json",
            headers : {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json;charset=utf-8'
            }
        }).response.then(
            function(o) {
                if (onSuccess != null) {
                    onSuccess(o)
                }
            },
            function(e) {
                let httpCode = e.response.status;
                if (httpCode === HTTP_BAD_REQUEST) {
                    console.log("Используются некорректные символы или один из параметров пуст");
                }
                if (httpCode === HTTP_SERVER_ERROR) {
                    console.log("При обработке запроса произошла ошибка со стороны сервера");
                }
            }
        )
    }

    // удаление рабочего
    function removeEmployee(id, onSuccess) {

        var req = "ecm/employees/" + id;
        request(req, {
            method: "DELETE",
            handleAs: "json"
        }).response.then(
            function() {
                console.log("Работник успешно удален");
                if (onSuccess != null) {
                    onSuccess()
                }
            },
            function(e) {
                let httpCode = e.response.status;
                if (httpCode === HTTP_BAD_REQUEST) {
                    console.log("Используются некорректные символы или один из параметров пуст");
                }
                if (httpCode === HTTP_SERVER_ERROR) {
                    console.log("При обработке запроса произошла ошибка со стороны сервера");
                }
            }
        )
    }

    return {

        /**
         *  Поиск документов рабочего по его id
         *  @param id идентификатор рабочего
         *  @param onSuccess функция, которая вызывается, если запрос обработан успешно
         */
        findDocumentsByEmployeeId: function(id, onSuccess) {
            findDocumentsByEmployeeId(id, onSuccess)
        },
        /**
         *  Отправить запрос на сохранение рабочего в БД
         *  @param obj рабочий, которого нужно создать
         *  @param onSuccess функция, которая вызывается, если запрос обработан успешно
         */
        createEmployee: function(obj, onSuccess) {
            createEmployee(obj, onSuccess);
        },
        /**
         *  Перезаписать информацию по рабочему
         *  @param obj рабочий, которого надо перезаписать
         *  @param onSuccess функция, которая вызывается, если запрос обработан успешно
         */
        editEmployee: function(obj, onSuccess) {
            editEmployee(obj, onSuccess)
        },
        /**
         *  Отправить запрос на удаление рабочего из БД
         *  @param id идентификатор рабочего
         *  @param onSuccess функция, которая вызывается, если запрос обработан успешно
         */
        removeEmployee: function(id, onSuccess) {
            removeEmployee(id, onSuccess)
        }
    }
});