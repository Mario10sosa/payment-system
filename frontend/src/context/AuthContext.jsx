import { createContext, useContext, useState } from 'react'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser]   = useState(
    JSON.parse(localStorage.getItem('user')) || null
  )
  const [token, setToken] = useState(
    localStorage.getItem('token') || null
  )

  const loginUser = (userData, tokenData) => {
    setUser(userData)
    setToken(tokenData)
    localStorage.setItem('user',  JSON.stringify(userData))
    localStorage.setItem('token', tokenData)
  }

  const logoutUser = () => {
    setUser(null)
    setToken(null)
    localStorage.removeItem('user')
    localStorage.removeItem('token')
  }

  return (
    <AuthContext.Provider value={{ user, token, loginUser, logoutUser }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)