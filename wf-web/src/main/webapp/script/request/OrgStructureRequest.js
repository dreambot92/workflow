define([
    "dojo/request"
], function(request) {

    var HTTP_BAD_REQUEST = 400,
        HTTP_SERVER_ERROR = 503;

    // поиск всех рабочих и вызов функции к результату запроса
    function findEmployees(onSuccess) {

        var req = "ecm/employees/";
        console.log("Выполняется следующий GET запрос: " + req);
        request(req, {
            method: "GET",
            handleAs: "json"
        }).response.then(
            function(o) {
                // если функция есть, то выполняем
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

    return {

        /**
         *  Поиск всех рабочих
         *  @param onSuccess функция, которая применяется к результату запроса
         */
        findEmployees: function(onSuccess) {
            return findEmployees(onSuccess)
        }
    }
});