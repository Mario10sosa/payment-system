import axios from 'axios'

const API = axios.create({
    baseURL: 'http://localhost:8080/api/auth'
})

// ★ Registro de usuario
export const register = (data) =>
    API.post('/register', data)

// ★ Login de usuario
export const login = (data) =>
    API.post('/login', data)

// ★ Ver perfil — requiere token
export const getProfile = (token) =>
    API.get('/profile', {
        headers: { Authorization: `Bearer ${token}` }
    })