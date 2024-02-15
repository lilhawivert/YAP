const searchButton = document.getElementById("button_search");
let searched = false;

searchButton.addEventListener("click", (e) => {
    if (searched) { 
        removeVideos();
    }
    searched = true;

    const query = document.getElementById("search").value;
    axios.get(`https://youtube.googleapis.com/youtube/v3/search?part=snippet&q=${query}&key=`).then((res) => { //key
        const videoDiv = document.getElementById("video_container");
        for (let i = 0; i < res.data.items.length; i++) {
            const currentAPI = res.data.items[i];
            const newVid = document.createElement("div");
            const videoId = currentAPI.id.videoId;
            newVid.setAttribute("id", currentAPI.id.videoId);
            newVid.setAttribute("class", "video col-12 py-2 card flex-column-reverse");
            const thumbnail = document.createElement("img");
            thumbnail.setAttribute("class", "w-100")
            const thumbnailDiv = document.createElement("div");
            thumbnailDiv.setAttribute("class", "thumbnailDiv");
            thumbnailDiv.appendChild(thumbnail);
            thumbnail.src = currentAPI.snippet.thumbnails.high.url;
            const title = document.createElement("a");
            title.innerHTML = currentAPI.snippet.title;
            title.style.maxWidth = "300px";
            title.classList.add("text-truncate", "d-inline-block");
            title.setAttribute("href", `javascript:;`);
            title.setAttribute("onclick", `loadVideo("${videoId}")`)
            const uD = document.createElement("p");
            uD.innerHTML = calcDate(currentAPI.snippet.publishTime);
            const desc = document.createElement("p");
            desc.innerHTML = currentAPI.snippet.description;
            const titleDiv = document.createElement("div");
            titleDiv.setAttribute("class", "titleDiv");
            titleDiv.appendChild(title);
            titleDiv.appendChild(uD);
            titleDiv.appendChild(desc);
            newVid.appendChild(titleDiv);
            newVid.appendChild(thumbnailDiv);
            videoDiv.appendChild(newVid);
        }
    })
})

function removeVideos() {
    const videos = document.getElementById("video_container");
    while (videos.firstChild) {
        videos.removeChild(videos.firstChild);
    }
}

function loadVideo(videoId) {
    removeVideos();
    loadSpecVideo(videoId);
}

function calcDate(upload) {
    const date = (new Date().getFullYear()) - parseInt(upload.substring(0, 4));
    if (date > 0) {
        return "Vor " + date + (date === 1 ? "Jahr" : " Jahren");
    }
    else {
        const date2 = (new Date().getMonth()) - parseInt(upload.substring(5, 7))
        if (date2 > 0) {
            return "Vor " + date2 + " Monaten"
        }
        else {
            return "Gerade erst hochgeladen";
        }
    }
}