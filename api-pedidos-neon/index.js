require('dotenv').config();
const express = require('express');
const cors = require('cors');
const pool = require('./db');

const app = express();
app.use(cors());
app.use(express.json());

// Muestra en la consola cada petición que llega desde la app (útil para la demostración)
app.use((req, res, next) => {
  res.on('finish', () => console.log(`${new Date().toLocaleTimeString()}  ${req.method} ${req.originalUrl} -> ${res.statusCode}`));
  next();
});

const UUID_REGEX = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

function validar(body) {
  const { uuid, producto, cantidad, fecha_registro } = body;
  if (!UUID_REGEX.test(uuid || '')) return 'uuid no válido';
  if (!producto || !producto.trim()) return 'Ingresa el nombre del producto';
  if (producto.trim().length > 120) return 'El producto admite hasta 120 caracteres';
  if (!Number.isInteger(cantidad) || cantidad <= 0) return 'La cantidad debe ser un entero mayor a 0';
  if (Number.isNaN(Date.parse(fecha_registro || ''))) return 'fecha_registro no válida';
  return null;
}

app.get('/', (req, res) => res.json({ mensaje: 'API Pedidos - Neon' }));

// Lista de pedidos recibidos (para verificar desde el navegador)
app.get('/api/pedidos', async (req, res) => {
  try {
    const { rows } = await pool.query(
      `SELECT id, uuid, producto, cantidad, fecha_registro, recibido_en
       FROM pedidos ORDER BY recibido_en DESC`
    );
    res.json(rows);
  } catch (e) {
    console.error(e);
    res.status(500).json({ mensaje: 'Error en el servidor' });
  }
});

// Recibe un pedido del celular.
// Es idempotente: si el mismo uuid llega dos veces, no se duplica y responde el mismo id.
app.post('/api/pedidos', async (req, res) => {
  const error = validar(req.body);
  if (error) return res.status(400).json({ mensaje: error });

  const { uuid, producto, cantidad, fecha_registro } = req.body;
  try {
    const { rows } = await pool.query(
      `INSERT INTO pedidos (uuid, producto, cantidad, fecha_registro)
       VALUES ($1, $2, $3, $4)
       ON CONFLICT (uuid) DO UPDATE SET uuid = EXCLUDED.uuid
       RETURNING id, uuid, (xmax = 0) AS nuevo`,
      [uuid, producto.trim(), cantidad, fecha_registro]
    );
    const pedido = rows[0];
    // 201 = se creó ahora; 200 = ya existía (reenvío)
    res.status(pedido.nuevo ? 201 : 200).json({ id: pedido.id, uuid: pedido.uuid });
  } catch (e) {
    console.error(e);
    res.status(500).json({ mensaje: 'Error en el servidor' });
  }
});

const PORT = process.env.PORT || 3001;
// 0.0.0.0 permite que el celular entre por la IP de la PC en la red Wi-Fi
app.listen(PORT, '0.0.0.0', () => console.log(`API escuchando en http://localhost:${PORT}`));
