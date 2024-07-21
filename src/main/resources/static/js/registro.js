document.getElementById('register-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario
    registerUser();
});

async function registerUser() {
    const nombre = document.getElementById('nombre').value;
    const apellido = document.getElementById('apellido').value;
    const cedula = document.getElementById('cedula').value;
    const correo = document.getElementById('correo').value;
    const contrasena = document.getElementById('contrasena').value;
    const fechaNacimiento = document.getElementById('fechaNacimiento').value;
    const genero = document.getElementById('genero').value;

    const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nombre,
            apellido,
            cedula,
            correo,
            contrasena,
            fechaNacimiento,
            genero,
        })
    });

    if (response.ok) {
        const data = await response.json();
        alert('Registro exitoso');
        window.location.href = '/login'; // Redirigir a la página de login u otra página
    } else {
        alert('Error en el registro');
    }
}
