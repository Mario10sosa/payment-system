import { useState } from 'react'
import { useAuth } from './context/AuthContext'
import LoginForm from './components/LoginForm'
import RegisterForm from './components/RegisterForm'
import PaymentForm from './components/PaymentForm'

export default function App() {
  const { user, logoutUser } = useAuth()
  const [showRegister, setShowRegister] = useState(false)

  // Si no está logueado muestra login o registro
  if (!user) {
    return (
      <div className="min-vh-100 bg-light d-flex align-items-center justify-content-center py-5">
        {showRegister
          ? <RegisterForm onSwitch={() => setShowRegister(false)} />
          : <LoginForm    onSwitch={() => setShowRegister(true)}  />
        }
      </div>
    )
  }

  // Si está logueado muestra el formulario de pagos
  return (
    <div className="min-vh-100 bg-light d-flex flex-column align-items-center justify-content-center py-5 gap-3">

      {/* Navbar usuario */}
      <div
        className="d-flex justify-content-between align-items-center px-4 py-2 rounded-3 shadow-sm"
        style={{ maxWidth: 520, width: '100%', background: 'white' }}
      >
        <div className="d-flex align-items-center gap-2">
          <div
            className="rounded-circle d-flex align-items-center justify-content-center"
            style={{ width: 36, height: 36, background: '#e0e7ff' }}
          >
            <i className="bi bi-person-fill text-primary"></i>
          </div>
          <div>
            <p className="mb-0 fw-semibold small">{user.nombre}</p>
            <p className="mb-0 text-muted" style={{ fontSize: 11 }}>{user.email}</p>
          </div>
        </div>
        <button
          className="btn btn-sm btn-outline-danger"
          onClick={logoutUser}
        >
          <i className="bi bi-box-arrow-right me-1"></i>Salir
        </button>
      </div>

      <PaymentForm />
    </div>
  )
}