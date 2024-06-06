$(document).ready(function () {
    $('.editarPerfil').on('click', function (event) {
        event.preventDefault();
        const userId = $(this).data('id');

        $.get('/profile/edit', function (data) {
            $('#editProfileContainer').html(data);
            $('#editProfileForm').on('submit', function (e) {
                e.preventDefault();
                $.ajax({
                    type: 'POST',
                    url: $('#editProfileForm').attr('action'),
                    data: $('#editProfileForm').serialize(),
                    success: function (response) {
                        alert('Perfil actualizado con éxito.');
                        window.location.href = '/profile'; // Recargar la página para reflejar los cambios
                    },
                    error: function (error) {
                        alert('Error al actualizar el perfil: ' + error.responseText);
                    }
                });
            });
        });
    });
});