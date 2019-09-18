var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#device-commands").prop("hidden", !connected);
    // $("#privateMessages").prop("hidden", !connected);
    // $("#name").prop("readonly", connected);
    // $("#disconnect").prop("disabled", !connected);
    // if (connected) {
    //     $("#conversation").show();
    // }
    // else {
    //     $("#conversation").hide();
    // }
    $("#received-messages").html("");
}

function connect() {
    var socket = new SockJS('/was-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {

        setConnected(true);
        sendClientId();

        stompClient.subscribe('/topic/connect', function (connectMessage) {
            displayMessage(JSON.parse(connectMessage.body).text);
        });
        stompClient.subscribe('/topic/command', function (commandMessage) {
            displayMessage(JSON.parse(commandMessage.body).text);
        });
        stompClient.subscribe('/topic/stats', function (deviceStatsMessage) {
            displayMessage(JSON.parse(deviceStatsMessage.body).text);
        });
        // stompClient.subscribe('/topic/disconnect', function (greeting) {
        //     displayMessage(JSON.parse(greeting.body).content);
        // });
        // var name = $("#name").val();
        // stompClient.subscribe('/topic/privateMessage/' + name, function (greeting) {
        //     showPrivateMessage(greeting.body);
        // });

    });
}

// function disconnect() {
//     if (stompClient != null) {
//         sendDisconnectMessage()
//         stompClient.disconnect();
//     }
//     setConnected(false);
//     console.log("Disconnected");
// }
//
function sendClientId() {
    stompClient.send("/app/connect/" + $("#name").val(), {}, null);
}
//
function sendDeviceCommand() {
    var command = $("#device-command");
    stompClient.send("/app/command/" + command.val(), {}, JSON.stringify({'command': command.val()}));
    command.val("");
}
//
// function sendPrivateMessage() {
//     var message = $("#privateMessage")
//     var receiver = $("#privateMessageTo")
//     stompClient.send("/app/privateMessage", {}, JSON.stringify({'message': message.val(), 'receiver': receiver.val()}));
//     message.val("");
//     receiver.val("");
// }
//
// function sendDisconnectMessage() {
//     var message = $("#message")
//     stompClient.send("/app/disconnect", {}, JSON.stringify({'message': "Left the room"}));
//     message.val("");
// }

function displayMessage(message) {
    console.log("Received {}", message);
    $("#received-messages").append("<tr><td>" + message + "</td></tr>");
}

// function showPrivateMessage(message) {
//     $("#received-messages").append("<tr><td style='background-color: chartreuse'>" + message + "</td></tr>");
// }

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    // $("#disconnect").click(function () {
    //     disconnect();
    // });
    // $("#send").click(function () {
    //     sendName();
    // });
    $("#send-device-command").click(function () {
        sendDeviceCommand();
    });
    // $("#sendPrivateMessage").click(function () {
    //     sendPrivateMessage();
    // });
});

