$(document).on('click', '.deletePedidoLink', function (e) {
    e.preventDefault();
    const pedidoId = $(this).data('id');
    swal({
        title: "¿Estás seguro?",
        text: "Una vez eliminado, no podrás recuperar este pedido.",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    })
        .then((willDelete) => {
            if (willDelete) {
                window.location.href = `/pedidos/delete/${pedidoId}`;
            }
        });
});
