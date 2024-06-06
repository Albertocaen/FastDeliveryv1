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
                alert('Producto agregado al carrito');
            },
            error: function (xhr, status, error) {
                alert('Error al agregar el producto al carrito: ' + error);
            }
        });
    });
});
