import { useState } from 'react'
import { login } from '../services/userService'
import { useAuth } from '../context/AuthContext'

export default function LoginForm({ onSwitch }) {
  const [email, setEmail]       = useState('')
  const [password, setPassword] = useState('')
  const [loading, setLoading]   = useState(false)
  const [error, setError]       = useState(null)
  const { loginUser }           = useAuth()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError(null)
    try {
      const res = await login({ email, password })
      loginUser(res.data.user, res.data.token)
    } catch (err) {
      setError(err.response?.data?.error || 'Error al iniciar sesión')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="card shadow border-0" style={{ maxWidth: 420, width: '100%' }}>
      <div
        className="card-header border-0 pt-4 pb-3 px-4"
        style={{ background: 'linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%)' }}
      >
        <h5 className="text-white mb-0 fw-bold">
          <i className="bi bi-person-lock me-2"></i>Iniciar sesión
        </h5>
        <small className="text-white opacity-75">Sistema de pagos</small>
      </div>

      <div className="card-body p-4">
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label fw-semibold">Email</label>
            <div className="input-group">
              <span className="input-group-text">
                <i className="bi bi-envelope"></i>
              </span>
              <input
                type="email"
                className="form-control"
                placeholder="correo@gmail.com"
                value={email}
                onChange={e => setEmail(e.target.value)}
                required
              />
            </div>
          </div>

          <div className="mb-4">
            <label className="form-label fw-semibold">Contraseña</label>
            <div className="input-group">
              <span className="input-group-text">
                <i className="bi bi-lock"></i>
              </span>
              <input
                type="password"
                className="form-control"
                placeholder="••••••"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
              />
            </div>
          </div>

          {error && (
            <div className="alert alert-danger py-2 small mb-3">
              <i className="bi bi-exclamation-triangle-fill me-2"></i>
              {error}
            </div>
          )}

          <button
            type="submit"
            className="btn btn-primary w-100 fw-semibold"
            disabled={loading}
          >
            {loading
              ? <><span className="spinner-border spinner-border-sm me-2"></span>Entrando...</>
              : <><i className="bi bi-box-arrow-in-right me-2"></i>Iniciar sesión</>
            }
          </button>

          <p className="text-center text-muted small mt-3 mb-0">
            ¿No tienes cuenta?{' '}
            <button
              type="button"
              className="btn btn-link btn-sm p-0"
              onClick={onSwitch}
            >
              Regístrate
            </button>
          </p>
        </form>
      </div>
    </div>
  )
}