import axios from 'axios'

const API = axios.create({ baseURL: 'http://localhost:8080/api/payments' })

export const processPayment = (data) => API.post('/pay', data)
export const getPaymentMethods = () => API.get('/methods')