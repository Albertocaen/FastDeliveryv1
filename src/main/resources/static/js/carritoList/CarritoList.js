$(document).ready(function () {
    $('.agregarCarrito').on('click', function (event) {
        event.preventDefault();
        const idProducto = $(this).data('id');
        const cantidad = 1; // Puede ser un campo en el formulario en el futuro
        $.ajax({
            type: 'POST',
            url: '/carrito/agregar',
            data: {idProducto: idProducto, cantidad: cantidad},
            success: function (response) {
                $('#carritoItems').text(response.itemsCount);
                Swal.fire({
                    title: '¡Producto agregado!',
                    text: 'El producto ha sido agregado al carrito.',
                    icon: 'success',
                    confirmButtonText: 'Aceptar'
                });
            },
            error: function (xhr, status, error) {
                Swal.fire({
                    title: 'Error',
                    text: 'Hubo un problema al agregar el producto al carrito.',
                    icon: 'error',
                    confirmButtonText: 'Aceptar'
                });
            }
        });
    });
});
