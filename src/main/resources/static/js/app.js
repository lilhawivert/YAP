var stompClient = null;

setTimeout(connect, 1000);


function connect() {
    
    const id = document.getElementById("id").textContent;
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.send("/send/id="+id, {}, JSON.stringify({'message': document.getElementById("username").textContent + " joined the room."}))
        stompClient.subscribe('/send/id='+id, function (request) {
            const requestBody = JSON.parse(request.body);
            if(typeof requestBody.username !== "undefined") showMessage(requestBody.username, requestBody.message);
            else if(typeof requestBody.load !== "undefined") {
                const loaded = onLoadVideo(requestBody.load);
                if(!loaded) ratingSystemSetUp();
                else ratingSystemReset();
            }
            else if(typeof requestBody.likeCount !== "undefined") {
                syncRatings(requestBody.likeCount, requestBody.dislikeCount);
            }
            else syncVideoForAll(requestBody.state, requestBody.timestamp);
        });
    });
}

function syncRatings(likeCount, dislikeCount) {
    const likeLabel = document.getElementById("likeLabel");
    const dislikeLabel = document.getElementById("dislikeLabel");
    likeLabel.textContent = likeCount;
    dislikeLabel.textContent = dislikeCount;
}

function syncVideoForAll(state, timestamp) {
    if(state === 2) {
        pauseVideo();
    }

    else if(state === 1) {
        startVideo();
    }

    else {
        updateTimestamp(timestamp);
    }
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage(id) {
    stompClient.send("/send/id=" + id, {}, JSON.stringify({'message': $("#message").val(), 'username': document.getElementById("username").textContent}));
}

function showMessage(username, message) {
    if(username) $("#greetings").append("<tr><td>" + username + " : " + message + "</td></tr>");
    else $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { 
        const roomId = document.getElementById("id").textContent;
        sendMessage(roomId); 
    });
});

