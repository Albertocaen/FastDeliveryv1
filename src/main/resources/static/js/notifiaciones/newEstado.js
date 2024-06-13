$(document).ready(function () {
    $('#newPedidoLink').on('click', function (event) {
        event.preventDefault();
        $.get('/pedidos/new', function (data) {
            $('#newPedidoContainer').html(data);
            $('#newPedidoContainer form').on('submit', function (e) {
                e.preventDefault();
                $.ajax({
                    type: 'POST',
                    url: '/pedidos',
                    data: $(this).serialize(),
                    success: function (response) {
                        $('#newPedidoContainer').html('<p>Pedido creado con éxito.</p>');
                        // Opcional: Recargar la lista de pedidos o agregar el nuevo pedido a la tabla
                    },
                    error: function (error) {
                        $('#newPedidoContainer').html('<p>Error al crear el pedido.</p>');
                    }
                });
            });
        });
    });

    $(document).on('click', '.estadoPedidoLink', function (event) {
        event.preventDefault();
        const pedidoId = $(this).data('id');
        $.get('/pedidos/estado/' + pedidoId, function (data) {
            $('#estadoPedidoContainer').html(data);
            $('#estadoPedidoContainer form').on('submit', function (e) {
                e.preventDefault();
                $.ajax({
                    type: 'POST',
                    url: '/pedidos/estado',
                    data: $(this).serialize(),
                    success: function (response) {
                        $('#estadoPedidoContainer').html('<p>Estado del pedido actualizado con éxito.</p>');
                        // Opcional: Actualizar el estado en la tabla
                    },
                    error: function (error) {
                        $('#estadoPedidoContainer').html('<p>Error al actualizar el estado del pedido.</p>');
                    }
                });
            });
        });
    });
});
