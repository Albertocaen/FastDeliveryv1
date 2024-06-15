$(document).ready(function() {
    console.log("newAndEdit.js cargado correctamente");

    // Función para mostrar el formulario de nuevo producto y ocultar el de editar
    $('#btn-new').click(function() {
        console.log("Botón 'Nuevo' clicado");
        $.ajax({
            url: '/productos/new',
            type: 'GET',
            success: function(response) {
                console.log("Respuesta para nuevo producto: ", response);
                $('#editCont').removeClass('active'); // Oculta el contenedor de editar
                $('#editCont').empty();
                $('#newCont').html(response);
                $('#newCont').addClass('active'); // Muestra el contenedor de nuevo
            },
            error: function() {
                console.error('Error al cargar el formulario de nuevo producto');
                Swal.fire('Error', 'No se pudo cargar el formulario de nuevo producto.', 'error');
            }
        });
    });

    // Función para mostrar el formulario de editar producto y ocultar el de nuevo
    $('.editarPerfil').on('click', function(event) {
        event.preventDefault();
        const productId = $(this).data('id');
        console.log("Producto ID: " + productId);

        $.ajax({
            url: '/productos/edit/' + productId,
            type: 'GET',
            success: function(response) {
                console.log("Respuesta para editar producto: ", response);
                $('#newCont').removeClass('active'); // Oculta el contenedor de nuevo
                $('#newCont').empty();
                $('#editCont').html(response);
                $('#editCont').addClass('active'); // Muestra el contenedor de editar

                // Asegúrate de que el elemento #editProfileForm está presente antes de añadir el evento submit
                if ($('#editProfileForm').length) {
                    console.log("Formulario de edición encontrado");
                    $('#editProfileForm').on('submit', function(e) {
                        e.preventDefault();
                        $.ajax({
                            type: 'POST',
                            url: $('#editProfileForm').attr('action'),
                            data: new FormData(this),
                            processData: false,
                            contentType: false,
                            success: function(response) {
                                console.log("Producto modificado con éxito");
                                Swal.fire('Éxito', 'Producto modificado con éxito', 'success');
                                window.location.href = '/productos';
                            },
                            error: function(error) {
                                console.error('Error en la carga del producto: ', error.responseText);
                                Swal.fire('Error', 'Error en la carga del producto: ' + error.responseText, 'error');
                            }
                        });
                    });
                } else {
                    console.error('Formulario de edición no encontrado');
                }
            },
            error: function() {
                console.error('Error al cargar el formulario de edición del producto');
                Swal.fire('Error', 'No se pudo cargar el formulario de edición del producto.', 'error');
            }
        });
    });
});
