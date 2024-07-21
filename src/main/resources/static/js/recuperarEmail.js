document.getElementById('recoveryForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;

    if (!validateEmail(email)) {
        alert('Por favor, introduce una dirección de correo electrónico válida.');
        return;
    }

    fetch('/api/recuperar-email', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: new URLSearchParams({
            'email': email
        })
    }).then(response => {
        if (response.ok) {
            alert('Solicitud de recuperación de contraseña enviada exitosamente.');
            window.location.href = 'login.html';
        } else {
            alert('Error al enviar la solicitud de recuperación de contraseña.');
        }
    }).catch(err => console.error('Error:', err));
});

function validateEmail(email) {
    const re = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    return re.test(String(email).toLowerCase());
}

document.querySelector('.form_close').addEventListener('click', function() {
    window.location.href = 'index.html';
});
