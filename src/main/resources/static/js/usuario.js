const token = localStorage.getItem('token');

if (!token) {
    console.error('Error: Token de autenticación no encontrado.');
    alert('Error de autenticación. Por favor, inicia sesión nuevamente.');
}

// Función para cargar los vehículos del usuario en un select
function cargarVehiculos() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
        console.error('Error: Usuario no autenticado.');
        return;
    }

    fetch(`/api/vehiculos/${usuarioId}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener los vehículos.');
        }
        return response.json();
    })
    .then(vehiculos => {
        const selectVehiculo = document.getElementById('vehiculo');
        selectVehiculo.innerHTML = ''; // Limpiar opciones anteriores
        selectVehiculo.innerHTML = '<option value="">Seleccionar Vehículo</option>';

        if (vehiculos.length === 0) {
            selectVehiculo.innerHTML = '<option value="">No hay vehículos registrados</option>';
        } else {
            vehiculos.forEach(vehiculo => {
                const option = document.createElement('option');
                option.value = vehiculo.id; // Puedes usar otro identificador si lo prefieres
                option.textContent = `${vehiculo.modelo} - ${vehiculo.marca} (${vehiculo.placa})`;
                selectVehiculo.appendChild(option);
            });
        }
    })
    .catch(error => {
        console.error('Error al obtener vehículos:', error);
        alert('Hubo un problema al cargar los vehículos.');
    });
}

// Función asincrónica para cargar los peajes
async function fetchPeajes() {
    try {
        const response = await fetch('/api/zonas', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error en la solicitud');
        }

        const data = await response.json();
        console.log('Peajes recibidos:', data);
        const peajeSelect = document.getElementById('peaje');
        peajeSelect.innerHTML = ''; // Limpiar opciones anteriores
        peajeSelect.innerHTML = '<option value="">Seleccionar Peaje</option>';

        if (data.length === 0) {
            peajeSelect.innerHTML = '<option value="">No hay peajes disponibles</option>';
        } else {
            data.forEach(peaje => {
                const option = document.createElement('option');
                option.value = peaje.id;
                option.textContent = peaje.nombre;
                peajeSelect.appendChild(option);
            });
        }

    } catch (error) {
        console.error('Error al obtener peajes:', error);
        alert('Hubo un problema al cargar los peajes.');
    }
}

/// Función para cargar la tarifa al seleccionar un vehículo y un peaje
function cargarTarifa() {
    const vehiculoId = document.getElementById('vehiculo').value;
    const zonaId = document.getElementById('peaje').value;

    if (!vehiculoId || !zonaId) {
        document.getElementById('tarifa').value = ''; // Limpiar la tarifa si no se ha seleccionado ambos
        return;
    }

    fetch(`/api/pagos/tarifaEfectivoPago?vehiculoId=${vehiculoId}&zonaId=${zonaId}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener la tarifa.');
        }
        return response.json();
    })
    .then(tarifa => {
        document.getElementById('tarifa').value = tarifa;
    })
    .catch(error => {
        console.error('Error al obtener la tarifa:', error);
        alert('Hubo un problema al cargar la tarifa.');
    });
}

// Función para manejar el pago
function realizarPago(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del formulario

    const vehiculoId = document.getElementById('vehiculo').value;
    const zonaId = document.getElementById('peaje').value;

    if (!vehiculoId || !zonaId) {
        alert('Por favor, selecciona un vehículo y un peaje.');
        return;
    }

    fetch(`/api/pagos/realizarPago?vehiculoId=${vehiculoId}&zonaId=${zonaId}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al realizar el pago.');
        }
        return response.json();
    })
    .then(pagoDTO => {
        alert('Pago realizado con éxito. ID de pago: ' + pagoDTO.id);
        // Limpiar campos del formulario después del pago
        document.getElementById('vehiculo').value = '';
        document.getElementById('peaje').value = '';
        document.getElementById('tarifa').value = '';
        // Aquí puedes agregar más lógica después de un pago exitoso, como actualizar el historial de pagos
    })
    .catch(error => {
        console.error('Error al realizar el pago:', error);
        alert('Hubo un problema al realizar el pago.');
    });
}

// Event listeners para cargar la tarifa cuando se selecciona un vehículo o peaje
document.getElementById('vehiculo').addEventListener('change', cargarTarifa);
document.getElementById('peaje').addEventListener('change', cargarTarifa);

// Event listener para manejar el pago cuando se envía el formulario
document.getElementById('form-pago').addEventListener('submit', realizarPago);

// Llamar a las funciones para cargar vehículos y peajes al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarVehiculos();
    fetchPeajes();
});


                        // RECARGAR TELEPASS

 document.addEventListener('DOMContentLoaded', () => {
    const formRecargaTelepass = document.getElementById('form-recarga-telepass');

    formRecargaTelepass.addEventListener('submit', async (event) => {
        event.preventDefault(); // Evitar el envío estándar del formulario

        const telepassId = document.getElementById('telepassselect').value;
        const tipoPagoId = document.getElementById('tipo-pago').value;
        const montoRecarga = document.getElementById('monto').valueAsNumber;

        if (!telepassId || !tipoPagoId || isNaN(montoRecarga) || montoRecarga <= 0) {
            alert('Por favor completa todos los campos correctamente.');
            return;
        }

        const token = localStorage.getItem('token'); // Asegúrate de tener el token válido

        try {
            const response = await fetch(`/api/telepass/recargar/${telepassId}?montoRecarga=${montoRecarga}&idPago=${tipoPagoId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (!response.ok) {
                throw new Error('Error al recargar el Telepass.');
            }

            const telepassRecargado = await response.json();
            alert('Telepass recargado exitosamente.');

            // Limpiar campos del formulario
            document.getElementById('telepassselect').value = '';
            document.getElementById('tipo-pago').value = '';
            document.getElementById('monto').value = '';

            // Puedes realizar alguna actualización adicional en tu interfaz si es necesario
        } catch (error) {
            console.error('Error al recargar el Telepass:', error);
            alert('Hubo un problema al recargar el Telepass.');
        }
    });

    // Cargar select de Telepasses y tipos de pago al cargar la página
    cargarTelepassesActivos();
    cargarTiposDePago();
});


// Función para cargar los telepasses activos del usuario en un select
function cargarTelepassesActivos() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
        console.error('Error: Usuario no autenticado.');
        return;
    }

    fetch(`/api/telepass/telepassActivo/${usuarioId}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener los telepasses activos.');
        }
        return response.json();
    })
    .then(telepasses => {
        const selectTelepass = document.getElementById('telepassselect');
        selectTelepass.innerHTML = '<option value="">Seleccionar Telepass</option>';

        if (telepasses.length === 0) {
            selectTelepass.innerHTML = '<option value="">No hay telepasses activos</option>';
        } else {
            telepasses.forEach(telepass => {
                const option = document.createElement('option');
                option.value = telepass.id;
                option.textContent = telepass.nombre;
                selectTelepass.appendChild(option);
            });
        }
    })
    .catch(error => {
        console.error('Error al obtener telepasses activos:', error);
        alert('Hubo un problema al cargar los telepasses activos.');
    });
}

// Función para cargar los tipos de pago en un select
function cargarTiposDePago() {
    fetch('/api/tipopago', {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener los tipos de pago.');
        }
        return response.json();
    })
    .then(tiposDePago => {
        const selectTipoPago = document.getElementById('tipo-pago');
        selectTipoPago.innerHTML = '<option value="">Seleccionar Tipo de Pago</option>';

        tiposDePago.forEach(tipo => {
            const option = document.createElement('option');
            option.value = tipo.id;
            option.textContent = tipo.descripcion;
            selectTipoPago.appendChild(option);
        });
    })
    .catch(error => {
        console.error('Error al obtener tipos de pago:', error);
        alert('Hubo un problema al cargar los tipos de pago.');
    });
}

// Llamar a las funciones para cargar telepasses y tipos de pago al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarTelepassesActivos();
    cargarTiposDePago();
});


// Llamar a las funciones al cargar la página o al mostrar la sección de pago
document.addEventListener('DOMContentLoaded', () => {
    cargarVehiculos();
    fetchPeajes();
});

// También puedes llamar a estas funciones cuando se muestre la sección de pago específicamente
document.querySelector('a[onclick="showSection(\'pago\')"]').addEventListener('click', () => {
    cargarVehiculos();
    fetchPeajes();
});

  // Función para cargar el historial de pagos
function cargarHistorialPagos() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
        console.error('Error: Usuario no autenticado.');
        return;
    }

    fetch(`/api/pagos/usuario/${usuarioId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener el historial de pagos.');
            }
            return response.json();
        })
        .then(pagos => {
            const historialTableBody = document.getElementById('historial-table-body');
            historialTableBody.innerHTML = ''; // Limpiar las filas existentes

            if (pagos.length === 0) {
                // Mostrar mensaje de que no hay pagos
                historialTableBody.innerHTML = '<tr><td colspan="6">No hay historial de pagos.</td></tr>';
            } else {
                // Agregar las filas dinámicamente
                pagos.forEach(pago => {
                    const row = historialTableBody.insertRow();
                    row.insertCell().textContent = pago.fechaPago; // Ajustar según la estructura de tu DTO
                    row.insertCell().textContent = pago.monto; // Ajustar según la estructura de tu DTO
                    row.insertCell().textContent = pago.vehiculo; // Ajustar según la estructura de tu DTO
                    row.insertCell().textContent = pago.zona; // Ajustar según la estructura de tu DTO
                    // Añadir más celdas según necesites para el tramo y precio, adaptando al DTO
                });
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al obtener el historial de pagos.');
        });
}

// Llamar a la función al cargar la sección "Historial Pagos"
document.querySelector('a[onclick="showSection(\'historial\')"]').addEventListener('click', cargarHistorialPagos);
document.addEventListener("DOMContentLoaded", cargarHistorialPagos);


  // Función para solicitar un Telepass para el vehículo seleccionado
  function solicitarTelepass() {
    const vehiculoId = document.getElementById('selectVehiculo').value;
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
      console.error('Error: Usuario no autenticado.');
      return;
    }

    if (!vehiculoId) {
      alert('Por favor, seleccione un vehículo.');
      return;
    }

    // Realizar la solicitud POST para crear un Telepass
    fetch('/api/telepass/crear', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        vehiculoId: parseInt(vehiculoId),
        usuarioId: parseInt(usuarioId),
        activo: true // Ajustar según tus necesidades
      })
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('El Vehiculo ya tiene asociado un telepass');
      }
      return response.json();
    })
    .then(data => {
      console.log('Telepass creado:', data);
      alert('Telepass solicitado exitosamente.');
      // Limpiar el select de vehículos después de la solicitud
      document.getElementById('selectVehiculo').value = '';
      // Recargar la lista de telepases
      cargarTelepasses();
    })
    .catch(error => {
      console.error('Error:', error);
      alert('El Vehiculo ya tiene asociado un telepass.');
    });
  }

  // Asociar la función solicitarTelepass al botón correspondiente
  document.getElementById('solicitarTelepassBtn').addEventListener('click', solicitarTelepass);

  // Función para cargar los vehículos del usuario en un select
  function cargarVehiculosSelect() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
      console.error('Error: Usuario no autenticado.');
      return;
    }

    fetch(`/api/vehiculos/${usuarioId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al obtener los vehículos.');
      }
      return response.json();
    })
    .then(vehiculos => {
      const selectVehiculo = document.getElementById('selectVehiculo');
      // Limpiar opciones existentes
      selectVehiculo.innerHTML = '';

      if (vehiculos.length === 0) {
        selectVehiculo.innerHTML = '<option value="">No hay vehículos registrados</option>';
      } else {
        // Agregar las opciones al select
        vehiculos.forEach(vehiculo => {
          const option = document.createElement('option');
          option.value = vehiculo.id; // Puedes usar otro identificador si lo prefieres
          option.textContent = `${vehiculo.modelo} - ${vehiculo.marca} (${vehiculo.placa})`;
          selectVehiculo.appendChild(option);
        });
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al obtener los vehículos.');
    });
  }

  // Llamar a esta función al mostrar la sección "Gestión de Telepasses"
  document.querySelector('a[onclick="showSection(\'telepass\')"]').addEventListener('click', function() {
    cargarVehiculosSelect();
    cargarTelepasses();
  });

  document.addEventListener("DOMContentLoaded", (e) => {
    cargarVehiculosSelect();
    cargarTelepasses();
  });

  // Función para cargar los telepases del usuario
  function cargarTelepasses() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
      console.error('Error: Usuario no autenticado.');
      return;
    }

    fetch(`/api/telepass/usuario/${usuarioId}`)
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al obtener los telepases.');
      }
      return response.json();
    })
    .then(telepasses => {
      const telepassTableBody = document.getElementById('telepassTableBody');
      // Limpiar las filas existentes
      telepassTableBody.innerHTML = '';

      if (telepasses.length === 0) {
        document.getElementById('emptyMessageTelepass').style.display = 'block';
      } else {
        document.getElementById('emptyMessageTelepass').style.display = 'none';
        // Agregar las filas dinámicamente
        telepasses.forEach(telepass => {
          const row = telepassTableBody.insertRow();
          row.insertCell().textContent = telepass.nombre;
          row.insertCell().textContent = telepass.usuarioId;
          row.insertCell().textContent = telepass.vehiculoId;
          row.insertCell().textContent = telepass.saldo;
          row.insertCell().textContent = telepass.activo ? 'Sí' : 'No';
          // Agregar acciones si es necesario
          // Ejemplo: row.insertCell().innerHTML = '<button onclick="eliminarTelepass(' + telepass.id + ')">Eliminar</button>';
        });
      }
    })
    .catch(error => {
      console.error('Error:', error);
      alert('Error al obtener los telepases.');
    });
  }

  // Llamar a esta función al mostrar la sección "Gestión de Telepasses"
  document.querySelector('a[onclick="showSection(\'telepass\')"]').addEventListener('click', cargarTelepasses);

  document.addEventListener("DOMContentLoaded", (e) => {
    cargarTelepasses();
  });

  // Función para mostrar una sección específica y ocultar las demás
  function showSection(sectionId) {
    const sections = document.querySelectorAll(".content");
    sections.forEach((section) => (section.style.display = "none"));
    document.getElementById(sectionId).style.display = "block";
  }

  // Función para cargar las opciones de categorías desde el servidor
  function cargarCategorias() {
    fetch('/api/vehiculo-categorias')
    .then(response => {
      if (!response.ok) {
        throw new Error('Error al cargar las categorías.');
      }
      return response.json();
    })
    .then(data => {
      let selectCategoria = document.getElementById('categoria');

      // Limpiar opciones existentes (si las hay)
      selectCategoria.innerHTML = '';

      // Crear y agregar nuevas opciones
      data.forEach(categoria => {
        let option = document.createElement('option');
        option.value = categoria.id; // Asignar el valor del ID de la categoría
        option.textContent = categoria.tipo; // Mostrar el nombre de la categoría
        selectCategoria.appendChild(option);
      });
    })
    .catch(error => {
      console.error('Error al cargar categorías:', error);
      // Puedes manejar el error mostrando un mensaje al usuario
    });
  }

  // Función para cargar los vehículos del usuario en la tabla
function cargarVehiculosUsuario() {
    const usuarioId = localStorage.getItem('usuarioId');

    if (!usuarioId) {
        console.error('Error: Usuario no autenticado.');
        return;
    }

    fetch(`/api/vehiculos/${usuarioId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los vehículos.');
            }
            return response.json();
        })
        .then(vehiculos => {
            const vehiculosTableBody = document.getElementById('vehiculos-table-body');
            vehiculosTableBody.innerHTML = ''; // Limpiar filas existentes

            if (vehiculos.length === 0) {
                const row = vehiculosTableBody.insertRow();
                const cell = row.insertCell();
                cell.colSpan = 5;
                cell.textContent = 'No hay vehículos registrados.';
            } else {
                vehiculos.forEach(vehiculo => {
                    const row = vehiculosTableBody.insertRow();
                    row.insertCell().textContent = vehiculo.modelo;
                    row.insertCell().textContent = vehiculo.marca;
                    row.insertCell().textContent = vehiculo.placa;
                    row.insertCell().textContent = vehiculo.color;
                    row.insertCell().textContent = vehiculo.categoriaId; // Ajustar según el nombre del campo en el objeto vehiculo
                });
            }
        })
        .catch(error => {
            console.error('Error al obtener los vehículos:', error);
            alert('Hubo un problema al cargar los vehículos.');
        });
}

// Llamar a la función al cargar la página o al mostrar la sección específica
document.addEventListener('DOMContentLoaded', () => {
    cargarVehiculosUsuario();
});





  // CREAR VEHICULO
  // Función para cargar las categorías de vehículos en el select
function cargarCategoriasVehiculos() {
    fetch('/api/vehiculo-categorias', {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al obtener las categorías de vehículos.');
        }
        return response.json();
    })
    .then(categorias => {
        const selectCategoria = document.getElementById('categoria');
        selectCategoria.innerHTML = '<option value="">Seleccionar Categoría</option>';

        categorias.forEach(categoria => {
            const option = document.createElement('option');
            option.value = categoria.id;
            option.textContent = categoria.tipo;
            selectCategoria.appendChild(option);
        });
    })
    .catch(error => {
        console.error('Error al obtener las categorías de vehículos:', error);
        alert('Hubo un problema al cargar las categorías de vehículos.');
    });
}

// Función para manejar el envío del formulario de ingreso de vehículo
document.getElementById('form-vehiculo').addEventListener('submit', function(event) {
    event.preventDefault();

    const modelo = document.getElementById('modelo').value;
    const marca = document.getElementById('marca').value;
    const placa = document.getElementById('placa').value;
    const color = document.getElementById('color').value;
    const categoriaId = document.getElementById('categoria').value;
    const usuarioId = localStorage.getItem('usuarioId');
    const token = localStorage.getItem('token');
    console.log(usuarioId)
     console.log(token)

    // Registrar el vehículo
    fetch('/api/vehiculos/crear', {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            modelo,
            marca,
            placa,
            color,
            categoriaId,
            usuarioId,
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Error al registrar el vehículo.');
        }
        return response.json();
    })
    .then(data => {
        alert('Vehículo registrado con éxito.');
        // Aquí puedes agregar cualquier lógica adicional que desees después de un registro exitoso
    })
    .catch(error => {
        console.error('Error al registrar el vehículo:', error);
        alert('Hubo un problema al registrar el vehículo.');
    });
});

// Llamar a la función para cargar las categorías de vehículos al cargar la página
document.addEventListener('DOMContentLoaded', cargarCategoriasVehiculos);

  document.addEventListener('DOMContentLoaded', () => {
    // Event listener para el botón "Salir"
    document.querySelector('.salirFo').addEventListener('click', () => {
        // Aquí podrías agregar lógica para limpiar datos de sesión si es necesario

        // Redirigir a la página de autenticación
        window.location.href = '/auth'; // Reemplaza '/auth' con la ruta correcta a tu página de autenticación
    });
});
