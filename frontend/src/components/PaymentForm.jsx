import { useState } from 'react'
import CardForm from './methods/CardForm'
import { PaypalForm, NequiForm, DaviplataForm, CryptoForm } from './methods/OtherForms'
import { processPayment } from '../services/paymentService'

const METHODS = [
  { id: 'CARD',      label: 'Tarjeta',       icon: 'bi-credit-card',      color: '#0d6efd' },
  { id: 'PAYPAL',    label: 'PayPal',        icon: 'bi-paypal',           color: '#003087' },
  { id: 'NEQUI',     label: 'Nequi',         icon: 'bi-phone',            color: '#7b2d8b' },
  { id: 'DAVIPLATA', label: 'Daviplata',     icon: 'bi-phone-fill',       color: '#e3000b' },
  { id: 'CRYPTO',    label: 'Criptomonedas', icon: 'bi-currency-bitcoin', color: '#f7931a' },
]

export default function PaymentForm() {
  const [method, setMethod]     = useState(null)
  const [formData, setFormData] = useState({})
  const [amount, setAmount]     = useState('')
  const [loading, setLoading]   = useState(false)
  const [result, setResult]     = useState(null)
  const [error, setError]       = useState(null)

  const handleFieldChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }))
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    if (!method) { setError('Selecciona un método de pago'); return }
    if (!amount || Number(amount) <= 0) { setError('Ingresa un monto válido'); return }

    setLoading(true)
    setError(null)
    setResult(null)

    try {
      const payload = { ...formData, paymentMethod: method, amount: Number(amount) }
      const res = await processPayment(payload)
      setResult({ status: 'SUCCESS', data: res.data })
    } catch (err) {
      const msg = err.response?.data?.errorMessage || 'Error al procesar el pago'
      setResult({ status: 'FAILED', data: err.response?.data || {}, message: msg })
    } finally {
      setLoading(false)
    }
  }

  const handleReset = () => {
    setMethod(null)
    setFormData({})
    setAmount('')
    setResult(null)
    setError(null)
  }

  const selectedMethod = METHODS.find(m => m.id === method)

  // ── Pantalla de resultado ──────────────────
  if (result) {
    const ok = result.status === 'SUCCESS'
    return (
      <div className="card shadow border-0" style={{ maxWidth: 480, width: '100%' }}>
        <div className="card-body text-center p-5">
          <div
            className="rounded-circle d-inline-flex align-items-center justify-content-center mb-4"
            style={{ width: 80, height: 80, background: ok ? '#d1fae5' : '#fee2e2' }}
          >
            <i
              className={`bi ${ok ? 'bi-check-lg' : 'bi-x-lg'} fs-2`}
              style={{ color: ok ? '#059669' : '#dc2626' }}
            />
          </div>

          <h4 className="fw-bold mb-1">{ok ? '¡Pago exitoso!' : 'Pago fallido'}</h4>
          <p className="text-muted mb-4">
            {ok ? 'Tu transacción fue procesada correctamente.' : result.message}
          </p>

          {ok && (
            <div className="bg-light rounded-3 p-3 text-start mb-4">
              <div className="d-flex justify-content-between mb-2">
                <span className="text-muted small">Método</span>
                <span className="fw-semibold small">{result.data.paymentMethod}</span>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <span className="text-muted small">Monto</span>
                <span className="fw-semibold small">
                  ${Number(result.data.amount).toLocaleString('es-CO')} COP
                </span>
              </div>
              <div className="d-flex justify-content-between mb-2">
                <span className="text-muted small">Transacción</span>
                <span className="fw-semibold small font-monospace">{result.data.transactionId}</span>
              </div>
              <div className="d-flex justify-content-between">
                <span className="text-muted small">Recibo</span>
                <span className="fw-semibold small font-monospace">{result.data.receiptId}</span>
              </div>
            </div>
          )}

          <button className="btn btn-primary w-100" onClick={handleReset}>
            <i className="bi bi-arrow-left me-2"></i>Nuevo pago
          </button>
        </div>
      </div>
    )
  }

  // ── Formulario principal ───────────────────
  return (
    <div className="card shadow border-0" style={{ maxWidth: 520, width: '100%' }}>

      {/* Header */}
      <div
        className="card-header border-0 pt-4 pb-3 px-4"
        style={{ background: 'linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%)' }}
      >
        <h5 className="text-white mb-0 fw-bold">
          <i className="bi bi-shield-lock me-2"></i>Pago seguro
        </h5>
        <small className="text-white opacity-75">Patrón Template Method + Factory</small>
      </div>

      <div className="card-body p-4">
        <form onSubmit={handleSubmit}>

          {/* Monto */}
          <div className="mb-4">
            <label className="form-label fw-semibold">Total a pagar</label>
            <div className="input-group input-group-lg">
              <span className="input-group-text fw-bold">$</span>
              <input
                type="number"
                className="form-control"
                placeholder="0"
                min="1"
                value={amount}
                onChange={e => setAmount(e.target.value)}
              />
              <span className="input-group-text text-muted">COP</span>
            </div>
          </div>

          {/* Selector de método */}
          <div className="mb-4">
            <label className="form-label fw-semibold">Método de pago</label>
            <div className="row g-2">
              {METHODS.map(m => (
                <div className="col-6" key={m.id}>
                  <button
                    type="button"
                    className={`btn w-100 d-flex align-items-center gap-2 py-2 px-3 border
                      ${method === m.id ? 'text-white border-0' : 'btn-outline-secondary text-dark'}`}
                    style={method === m.id
                      ? { background: m.color, boxShadow: `0 4px 14px ${m.color}55` }
                      : {}}
                    onClick={() => { setMethod(m.id); setFormData({}); setError(null) }}
                  >
                    <i className={`bi ${m.icon}`}></i>
                    <span className="small fw-semibold">{m.label}</span>
                    {method === m.id && (
                      <i className="bi bi-check-circle-fill ms-auto small"></i>
                    )}
                  </button>
                </div>
              ))}
            </div>
          </div>

          {/* Formulario dinámico según método */}
          {method && (
            <div className="mb-4 p-3 bg-light rounded-3 border">
              <div className="d-flex align-items-center gap-2 mb-3">
                <i
                  className={`bi ${selectedMethod.icon}`}
                  style={{ color: selectedMethod.color, fontSize: 18 }}
                ></i>
                <span className="fw-semibold">{selectedMethod.label}</span>
              </div>

              {method === 'CARD'      && <CardForm data={formData} onChange={handleFieldChange} />}
              {method === 'PAYPAL'    && <PaypalForm data={formData} onChange={handleFieldChange} />}
              {method === 'NEQUI'     && <NequiForm data={formData} onChange={handleFieldChange} />}
              {method === 'DAVIPLATA' && <DaviplataForm data={formData} onChange={handleFieldChange} />}
              {method === 'CRYPTO'    && <CryptoForm data={formData} onChange={handleFieldChange} />}
            </div>
          )}

          {/* Error */}
          {error && (
            <div className="alert alert-danger py-2 d-flex align-items-center gap-2 mb-3">
              <i className="bi bi-exclamation-triangle-fill"></i>
              <span className="small">{error}</span>
            </div>
          )}

          {/* Botón pagar */}
          <button
            type="submit"
            className="btn btn-primary btn-lg w-100 fw-semibold"
            disabled={loading || !method}
          >
            {loading
              ? <><span className="spinner-border spinner-border-sm me-2"></span>Procesando...</>
              : <><i className="bi bi-lock-fill me-2"></i>
                  Pagar {amount ? `$${Number(amount).toLocaleString('es-CO')} COP` : ''}</>
            }
          </button>

          <p className="text-center text-muted small mt-3 mb-0">
            <i className="bi bi-shield-check me-1"></i>
            Transacción cifrada · Patrón Abstract Method
          </p>

        </form>
      </div>
    </div>
  )
}