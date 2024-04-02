<!DOCTYPE html>
<html>
<head>
    <title>Merge</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        #backButton {
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 20px;
            padding: 10px 20px;
            text-align: center;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            cursor: pointer;
            position: fixed;
            top: 20px;
            left: 20px;
        }
        #backButton:hover {
            background-color: #0056b3;
        }
        .container {
            width: 80%;
            max-width: 600px;
            padding: 40px;
            background-color: #fff;
            border-radius: 20px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1 {
            color: #333;
            margin-bottom: 20px;
        }

        hr {
            margin: 20px 0;
            border: none;
            border-top: 1px solid #ccc;
        }

        .meeting {
            background-color: #fff;
            border-radius: 5px;
            padding: 10px;
            margin-bottom: 20px;
            transition: all 0.3s;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .meeting p {
            margin: 0;
            flex-grow: 1;
        }

        .button-group {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 10px;
        }

        .button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 20px;
            transition: background-color 0.3s;
            margin-right: 5px;
        }

        .button:last-child {
            margin-right: 0;
        }

        .button:hover {
            background-color: #0056b3;
        }

        .delete-button {
            padding: 10px 20px;
            background-color: #dc3545;
            color: #fff;
            border: none;
            border-radius: 20px;
            transition: background-color 0.3s;
            cursor: pointer;
        }

        .delete-button:hover {
            background-color: #c82333;
        }

        .create-button,
        .invitation-button {
            margin-bottom: 20px;
        }

        .create-button a,
        .invitation-button a {
            display: inline-block;
            padding: 12px 24px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 20px;
            transition: background-color 0.3s;
        }

        .create-button a:hover,
        .invitation-button a:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Merge</h1>
    <hr>
    <#list meetings as meeting>
        <div class="meeting">
            <p><b>${meeting.description}</b></p>
            <div class="button-group">
                <a href="/meeting/${meeting.id}" class="button">Подробнее</a>
                <a href="/meeting/update/${meeting.id}" class="button">Редактировать</a>
                <form action="/meeting/delete/${meeting.id}" method="post" style="display:inline;">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <button type="submit" class="delete-button">Удалить</button>
                </form>
            </div>
        </div>
    </#list>
    <#if meetings?size == 0>
        <h3>Созданных встреч нет</h3>
    </#if>
    <#list invmeetings as meeting>
        <div class="meeting">
            <p><b>${meeting.description}</b></p>
            <div class="button-group">
                <a href="/meeting/${meeting.id}" class="button">Подробнее</a>
            </div>
        </div>
    </#list>
    <#if invmeetings?size == 0>
        <h3>Принятых встреч нет</h3>
    </#if>
    <div class="button-group">
        <div class="create-button">
            <a href="/meeting/create">Создать</a>
        </div>
        <div class="invitation-button">
            <a href="/invitation">Приглашения</a>
        </div>
    </div>
</div>
</body>
</html>
