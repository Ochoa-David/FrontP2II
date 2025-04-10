const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const app = express();
const port = 3000;
app.use(cors());
app.use(bodyParser.json());

app.post('/login', (req, res) => {
  const { usuario, clave } = req.body;
  console.log("Intento de login:", usuario, clave);
  if (usuario === "admin" && clave === "1234") {
    res.status(200).json({ token: "abc123" });
  } else {
    res.status(401).json({ error: "Credenciales inválidas" });
  }
});

app.post("/fechas-disponibles", (req, res) => {
  const { origen, destino } = req.body;
  if (origen && destino && origen !== destino) {
    return res.json({
      fechas: ["2025-04-12", "2025-04-14", "2025-04-18"]
    });
  } else {
    return res.status(400).json({ error: "Datos inválidos" });
  }
});

app.post('/reservarViaje', (req, res) => {
  const datos = req.body;

  console.log("Reserva Viaje recibida:");
  console.log("Nombre:", datos[0]);
  console.log("ID:", datos[1]);
  console.log("Origen:", datos[2]);
  console.log("Destino:", datos[3]);
  console.log("Fecha:", datos[4]);
  console.log("Con equipaje:", datos[5]);
  console.log("Cantidad equipaje:", datos[6]);
  console.log("Peso equipaje:", datos[7]);

  res.status(200).json({ mensaje: "Reserva procesada correctamente" });
});

app.post('/reservarEnvio', (req, res) => {
  const datos = req.body;

  console.log("Reserva envio recibida:");
  console.log("Nombre Remitente:", datos[0]);
  console.log("ID Remitente:", datos[1]);
  console.log("Nombre Destinatario:", datos[2]);
  console.log("ID Destinatario:", datos[3]);
  console.log("Sede envio:", datos[4]);
  console.log("Sede entrega:", datos[5]);
  console.log("Peso paquete:", datos[6]);

  res.status(200).json({ mensaje: "Reserva procesada correctamente" });
});

app.listen(port, () => {
  console.log(`Servidor escuchando en http://localhost:${port}`);
});
