# App22 · Pedidos offline con Room y sincronización con Neon

**Desarrollo de Aplicaciones Móviles · Semana 10 · Universidad Continental**  
Autor: Jean Piero Huamani Rodriguez

El vendedor registra pedidos (producto y cantidad) **con o sin Internet**. Se guardan primero en **Room** (offline-first) y luego se envían a **Neon PostgreSQL** con el botón **Sincronizar**, a través de una API REST **idempotente** (el UUID generado en el celular evita duplicados con `ON CONFLICT (uuid)`). Una franja **roja/verde** avisa cuando se pierde o vuelve la conexión (`ConnectivityManager` + `callbackFlow`).

Estados de cada pedido: **PENDIENTE** → **SINCRONIZADO** (con el id de Neon) o **ERROR** (se reintenta).

## Estructura
```
app/  model/ Pedido, PedidoMapper, PedidoRepository, MonitorConexion
           local/  PedidoEntity, PedidoDao (Flow), AppDatabase (Room)
           remote/ PedidoDto, PedidoApiService, RetrofitClient
      viewmodel/ PedidoUiState, PedidoViewModel
      view/ PantallaPedidos, BannerConexion, PanelSincronizacion, FormularioPedido, PedidoItem
api-pedidos-neon/  index.js (GET y POST /api/pedidos idempotente), db.js, schema.sql
```

## Ejecutar
```
cd api-pedidos-neon
copy .env.example .env      (DATABASE_URL de Neon, PORT=3001)
npm install
npm run init-db
npm start                   (http://localhost:3001/api/pedidos)
```
La app usa `http://10.0.2.2:3001/` en el emulador (puerto 3001 para poder correr a la vez la API de app19).
