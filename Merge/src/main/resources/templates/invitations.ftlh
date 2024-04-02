<!DOCTYPE html>
<html>
<head>
    <title>Приглашения</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            margin: 0;
            padding: 20px;
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
            background-color: #fff;
            padding: 20px;
            border-radius: 20px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        h1, h2 {
            color: #333;
            margin-bottom: 20px;
        }

        ul {
            list-style-type: none;
            padding: 0;
        }

        li {
            margin-bottom: 20px;
        }

        form {
            display: inline;
        }

        input[type="submit"] {
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 20px;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-left: 10px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }

        a {
            display: inline-block;
            padding: 10px 20px;
            background-color: #007bff;
            color: #fff;
            text-decoration: none;
            border-radius: 20px;
            transition: background-color 0.3s;
            margin-top: 20px;
        }

        a:hover {
            background-color: #0056b3;
        }

        p {
            text-align: center;
        }
    </style>
</head>
<body>
<button id="backButton" onclick="goBack()">Назад</button>

<script>
    function goBack() {
        window.history.back();
    }
</script>
<div class="container">
    <h2>Вас пригласили:</h2>
    <#if invitations?has_content>
        <ul>
            <#list invitations as invitation>
                <li>
                    <b>Встреча:</b> ${invitation.meeting.description}<br>
                    <b>Отправитель:</b> ${invitation.creator.email}<br>
                    <b>Статус:</b> ${invitation.status}<br>
                    <form action="/invitation/accept/${invitation.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="submit" value="Принять">
                    </form>

                    <form action="/invitation/reject/${invitation.id}" method="post">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="submit" value="Отклонить">
                    </form>
                </li>
            </#list>
        </ul>
    <#else>
        <p>Приглашений нет</p>
    </#if>

    <h2>Отправленные приглашения:</h2>
    <#if sentInvitations?has_content>
        <ul>
            <#list sentInvitations as invitation>
                <li>
                    <b>Встреча:</b> ${invitation.meeting.description}<br>
                    <b>Получатель:</b> ${invitation.invitedUser.email}
                </li>
            </#list>
        </ul>
    <#else>
        <p>Отправленных приглашений нет</p>
    </#if>
    <a href="/invitation/create">Отправить приглашение</a>
</div>
</body>
</html>
