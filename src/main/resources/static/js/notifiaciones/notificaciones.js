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

const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/notifications', function (notification) {
        showNotification(notification.body);
    });
});

function showNotification(message) {
    const notificationsDiv = document.getElementById('notifications');
    const notification = document.createElement('div');
    notification.classList.add('notification', 'is-info');
    notification.innerText = message;
    notificationsDiv.appendChild(notification);
}

// Lottie animation
lottie.loadAnimation({
    container: document.getElementById('lottie-carrito'), // the dom element that will contain the animation
    renderer: 'svg',
    loop: true,
    autoplay: true,
    path: '/static/animations/carrito.json' // the path to the animation json
});