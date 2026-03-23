import axios from 'axios'

const API = axios.create({
    baseURL: 'http://localhost:8080/api/payments'
})

const DB_API = axios.create({
    baseURL: 'http://localhost:8080/api/db'
})

// el token en cada petición automáticamente
API.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// ── Endpoints principales ─────────────────────

export const processPayment = (data) =>
    API.post('/pay', data)

export const getPaymentMethods = () =>
    API.get('/methods')

// ★ PATRÓN PROTOTYPE — clona y cambia el monto
export const cloneAndPay = (originalData, newAmount) =>
    API.post(`/clone?newAmount=${newAmount}`, originalData)

// ★ PATRÓN PROTOTYPE — clona y cambia el método
export const cloneWithNewMethod = (originalData, newMethod) =>
    API.post(`/clone-method?newMethod=${newMethod}`, originalData)

// ★ Lista todos los pagos
export const getPayments = () =>
    API.get('/list')

// ★ Lista pagos del usuario logueado
export const getMyPayments = () =>
    API.get('/my-payments')

// ★ PATRÓN SINGLETON — verificar conexión
export const getDbStatus = () =>
    DB_API.get('/status')