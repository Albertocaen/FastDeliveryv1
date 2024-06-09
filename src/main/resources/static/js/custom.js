$(document).ready(function () {
    $('.load-content').on('click', function (event) {
        event.preventDefault();
        const url = $(this).data('url');

        $.get(url, function (data) {
            $('#contenido-dinamico').html(data).addClass('fade-in');
        }).fail(function () {
            $('#contenido-dinamico').html('<p>El contenido no pudo ser cargado.</p>');
        });
    });
});
