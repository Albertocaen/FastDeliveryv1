$(document).ready(function(){
    $('.load-content').on('click', function(e){
        e.preventDefault();
        const url = $(this).data('url');
        $.ajax({
            url: url,
            success: function(data) {
                $('#contenido-dinamico').html(data);
            },
            error: function() {
                $('#contenido-dinamico').html('<p>El contenido no pudo ser cargado.</p>');
            }
        });
    });
});

$(document).ready(function() {
    $('#productosCarrusel').carousel({
        interval: false
    });
});
