$(document).ready(function() {
    // Función para mostrar el formulario de nuevo producto y ocultar el de editar
    $('#btn-new').click(function() {
        $.ajax({
            url: '/productos/new',
            type: 'GET',
            success: function(response) {
                $('#editCont').removeClass('active'); // Oculta el contenedor de editar
                $('#editCont').empty();
                $('#newCont').html(response);
                $('#newCont').addClass('active'); // Muestra el contenedor de nuevo
            },
            error: function() {
                Swal.fire('Error', 'No se pudo cargar el formulario de nuevo producto.', 'error');
            }
        });
    });

    // Función para mostrar el formulario de editar producto y ocultar el de nuevo
    $('.editarPerfil').on('click', function(event) {
        event.preventDefault();
        const productId = $(this).data('id');

        $.ajax({
            url: '/productos/edit/' + productId,
            type: 'GET',
            success: function(response) {
                $('#newCont').removeClass('active'); // Oculta el contenedor de nuevo
                $('#newCont').empty();
                $('#editCont').html(response);
                $('#editCont').addClass('active'); // Muestra el contenedor de editar
                $('#editProfileForm').on('submit', function(e) {
                    e.preventDefault();
                    $.ajax({
                        type: 'POST',
                        url: $('#editProfileForm').attr('action'),
                        data: new FormData(this),
                        processData: false,
                        contentType: false,
                        success: function(response) {
                            Swal.fire('Éxito', 'Producto modificado con éxito', 'success');
                            window.location.href = '/productos';
                        },
                        error: function(error) {
                            Swal.fire('Error', 'Error en la carga del producto: ' + error.responseText, 'error');
                        }
                    });
                });
            },
            error: function() {
                Swal.fire('Error', 'No se pudo cargar el formulario de edición del producto.', 'error');
            }
        });
    });
});
