$(document).ready(function () {
    // Asignar evento de clic a los enlaces
    $('.load-content').on('click', function (event) {
        event.preventDefault();
        const url = $(this).data('url');

        $.get(url, function (data) {
            $('#contenido-dinamico').html(data).addClass('fade-in');
        }).fail(function () {
            $('#contenido-dinamico').html('<p>El contenido no pudo ser cargado.</p>');
        });
    });

    // Cargar contenido por defecto
    const defaultUrl = $('.load-content').first().data('url');
    if (defaultUrl) {
        $.get(defaultUrl, function (data) {
            $('#contenido-dinamico').html(data).addClass('fade-in');
        }).fail(function () {
            $('#contenido-dinamico').html('<p>El contenido no pudo ser cargado.</p>');
        });
    }
});
