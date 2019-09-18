var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#messages").prop("hidden", !connected);
    $("#privateMessages").prop("hidden", !connected);
    $("#name").prop("readonly", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/was-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        var suffix = frame.headers['queue-suffix'];

        setConnected(true);
        sendName();
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/message', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        stompClient.subscribe('/topic/disconnect', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
        var name = $("#name").val();
        stompClient.subscribe('/topic/privateMessage/' + name, function (greeting) {
            showPrivateMessage(greeting.body);
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        sendDisconnectMessage()
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function sendMessage() {
    var message = $("#message")
    stompClient.send("/app/message", {}, JSON.stringify({'message': message.val()}));
    message.val("");
}

function sendPrivateMessage() {
    var message = $("#privateMessage")
    var receiver = $("#privateMessageTo")
    stompClient.send("/app/privateMessage", {}, JSON.stringify({'message': message.val(), 'receiver': receiver.val()}));
    message.val("");
    receiver.val("");
}

function sendDisconnectMessage() {
    var message = $("#message")
    stompClient.send("/app/disconnect", {}, JSON.stringify({'message': "Left the room"}));
    message.val("");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

function showPrivateMessage(message) {
    $("#greetings").append("<tr><td style='background-color: chartreuse'>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
    $("#sendMessage").click(function () {
        sendMessage();
    });
    $("#sendPrivateMessage").click(function () {
        sendPrivateMessage();
    });
});

