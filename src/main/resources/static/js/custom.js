$(document).ready(function () {
    // Función para cargar contenido
    function loadContent(url) {
        $.get(url, function (data) {
            $('#contenido-dinamico').html(data).addClass('fade-in');
        }).fail(function () {
            $('#contenido-dinamico').html('<p>El contenido no pudo ser cargado.</p>');
        });
    }

    // Asignar evento de clic a los enlaces
    $('.load-content').on('click', function (event) {
        event.preventDefault();
        const url = $(this).data('url');
        loadContent(url);
    });

    // Cargar contenido por defecto
    const defaultUrl = $('.load-content').first().data('url');
    if (defaultUrl) {
        loadContent(defaultUrl);
    }
});
