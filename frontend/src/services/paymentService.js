import axios from 'axios'

// ── Configuración base ────────────────────────
const API = axios.create({
    baseURL: 'http://localhost:8080/api/payments'
})

const DB_API = axios.create({
    baseURL: 'http://localhost:8080/api/db'
})


// Endpoints principales

 //Procesa un pago nuevo.

export const processPayment = (data) => {
    console.log('processPayment:', data.paymentMethod, '$' + data.amount)
    return API.post('/pay', data)
}


 //Obtiene los métodos de pago disponibles.

export const getPaymentMethods = () =>
    API.get('/methods')

// GET /api/payments/list — lista todos los pagos
export const getPayments = () =>
    API.get('/list')

// PATRÓN PROTOTYPE


 //Clona un pago existente y cambia solo el monto.

export const cloneAndPay = (originalData, newAmount) => {
    console.log('clonando pago...')
    console.log('    Original →', originalData.paymentMethod, '$' + originalData.amount)
    console.log('    Nuevo monto → $' + newAmount)
    return API.post(`/clone?newAmount=${newAmount}`, originalData)
}


 //Clona un pago y cambia el método de pago.

export const cloneWithNewMethod = (originalData, newMethod) => {
    console.log('clonando con nuevo método...')
    console.log('    Original →', originalData.paymentMethod)
    console.log('    Nuevo método →', newMethod)
    return API.post(`/clone-method?newMethod=${newMethod}`, originalData)
}

// PATRÓN SINGLETON

 //Verifica el estado de la conexión Singleton.

export const getDbStatus = () => {
    console.log('verificando conexión...')
    return DB_API.get('/status')
}