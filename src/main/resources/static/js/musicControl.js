document.addEventListener("DOMContentLoaded", function () {
    const backgroundMusic = document.getElementById('background-music');
    const musicIcon = document.getElementById('music-icon');
    let isPlaying = false;

    function toggleMusic() {
        if (isPlaying) {
            backgroundMusic.pause();
            musicIcon.src = '/static/uploads/610e8867f8bb190004769dd9.png'; // Cambia al icono de play
        } else {
            backgroundMusic.play();
            musicIcon.src = '/static/uploads/610e8867f8bb190004769dd9.png'; // Cambia al icono de pausa
        }
        isPlaying = !isPlaying;
    }

    window.toggleMusic = toggleMusic;
});
