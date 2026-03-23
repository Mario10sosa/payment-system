import { useState } from 'react'
import CardForm from './methods/CardForm'
import { PaypalForm, NequiForm, DaviplataForm, CryptoForm } from './methods/OtherForms'
import { processPayment, cloneAndPay, getDbStatus, getPayments } from '../services/paymentService'

const METHODS = [
  { id: 'CARD',      label: 'Tarjeta',       icon: 'bi-credit-card',      color: '#0d6efd' },
  { id: 'PAYPAL',    label: 'PayPal',        icon: 'bi-paypal',           color: '#003087' },
  { id: 'NEQUI',     label: 'Nequi',         icon: 'bi-phone',            color: '#7b2d8b' },
  { id: 'DAVIPLATA', label: 'Daviplata',     icon: 'bi-phone-fill',       color: '#e3000b' },
  { id: 'CRYPTO',    label: 'Criptomonedas', icon: 'bi-currency-bitcoin', color: '#f7931a' },
]

export default function PaymentForm() {
  const [method, setMethod]           = useState(null)
  const [formData, setFormData]       = useState({})
  const [amount, setAmount]           = useState('')
  const [loading, setLoading]         = useState(false)
  const [result, setResult]           = useState(null)
  const [error, setError]             = useState(null)
  const [lastRequest, setLastRequest] = useState(null)
  const [cloneAmount, setCloneAmount] = useState('')
  const [dbStatus, setDbStatus]       = useState(null)
  const [payments, setPayments]       = useState([])
  const [showList, setShowList]       = useState(false)
  const [loadingList, setLoadingList] = useState(false)

  const handleFieldChange = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }))
  }

  // ── Procesar pago normal ───────────────────
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
      setLastRequest(payload)
    } catch (err) {
      const msg = err.response?.data?.errorMessage || 'Error al procesar el pago'
      setResult({ status: 'FAILED', data: err.response?.data || {}, message: msg })
    } finally {
      setLoading(false)
    }
  }

  // ★ PROTOTYPE — clonar con nuevo monto ──────
  const handleClone = async () => {
    if (!cloneAmount || Number(cloneAmount) <= 0) {
      setError('Ingresa un monto válido para clonar')
      return
    }
    setLoading(true)
    setError(null)
    try {
      const res = await cloneAndPay(lastRequest, Number(cloneAmount))
      setResult({ status: 'SUCCESS', data: res.data })
      setCloneAmount('')
    } catch (err) {
      const msg = err.response?.data?.errorMessage || 'Error al clonar el pago'
      setResult({ status: 'FAILED', data: err.response?.data || {}, message: msg })
    } finally {
      setLoading(false)
    }
  }

  // ★ SINGLETON — verificar conexión DB ───────
  const handleDbStatus = async () => {
    try {
      const res = await getDbStatus()
      setDbStatus(res.data)
    } catch (err) {
      setDbStatus({ connected: false, message: '❌ Sin conexión' })
    }
  }

  // ★ Cargar listado de pagos ─────────────────
  const handleShowList = async () => {
    setShowList(true)
    setLoadingList(true)
    try {
      const res = await getPayments()
      setPayments(res.data)
    } catch (err) {
      console.error(err)
    } finally {
      setLoadingList(false)
    }
  }

  const handleReset = () => {
    setMethod(null)
    setFormData({})
    setAmount('')
    setResult(null)
    setError(null)
    setCloneAmount('')
    setDbStatus(null)
  }

  const selectedMethod = METHODS.find(m => m.id === method)

  // ── Pantalla de resultado ──────────────────
  if (result) {
    const ok = result.status === 'SUCCESS'
    return (
      <div className="card shadow border-0" style={{ maxWidth: 500, width: '100%' }}>
        <div className="card-body text-center p-5">

          {/* Ícono resultado */}
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

          {/* Detalles del pago */}
          {ok && (
            <div className="bg-light rounded-3 p-3 text-start mb-3">
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
                <span className="fw-semibold small font-monospace">
                  {result.data.transactionId}
                </span>
              </div>
              <div className="d-flex justify-content-between">
                <span className="text-muted small">Recibo</span>
                <span className="fw-semibold small font-monospace">
                  {result.data.receiptId}
                </span>
              </div>
            </div>
          )}

          {/* ★ PROTOTYPE — Repetir pago */}
          {ok && lastRequest && (
            <div className="border rounded-3 p-3 mb-3 text-start">
              <p className="fw-semibold small mb-2 text-primary">
                <i className="bi bi-arrow-repeat me-2"></i>
                Repetir pago
              </p>
              <div className="input-group mb-2">
                <span className="input-group-text fw-bold">$</span>
                <input
                  type="number"
                  className="form-control"
                  placeholder="Nuevo monto"
                  min="1"
                  value={cloneAmount}
                  onChange={e => setCloneAmount(e.target.value)}
                />
                <span className="input-group-text text-muted">COP</span>
              </div>
              <button
                className="btn btn-outline-primary w-100"
                onClick={handleClone}
                disabled={loading}
              >
                {loading
                  ? <><span className="spinner-border spinner-border-sm me-2"></span>Clonando...</>
                  : <><i className="bi bi-copy me-2"></i>pagar con nuevo monto</>
                }
              </button>
              {error && (
                <div className="alert alert-danger py-2 mt-2 small mb-0">
                  <i className="bi bi-exclamation-triangle-fill me-2"></i>
                  {error}
                </div>
              )}
            </div>
          )}

          {/* ★ SINGLETON — Estado conexión */}
          <div className="border rounded-3 p-3 mb-3 text-start">
            <p className="fw-semibold small mb-2 text-success">
              <i className="bi bi-database me-2"></i>
              Singleton — Conexión MySQL
            </p>
            <button
              className="btn btn-outline-success w-100 mb-2"
              onClick={handleDbStatus}
            >
              <i className="bi bi-plug me-2"></i>
              Verificar conexión Singleton
            </button>
            {dbStatus && (
              <div className={`alert py-2 mb-0 small
                ${dbStatus.connected ? 'alert-success' : 'alert-danger'}`}>
                <div className="d-flex justify-content-between mb-1">
                  <span>Estado</span>
                  <span className="fw-semibold">
                    {dbStatus.connected ? '✅ Activa' : '❌ Inactiva'}
                  </span>
                </div>
                <div className="d-flex justify-content-between mb-1">
                  <span>Instancia</span>
                  <span className="fw-semibold font-monospace small">
                    {dbStatus.instance}
                  </span>
                </div>
                <div className="d-flex justify-content-between">
                  <span>Patrón</span>
                  <span className="fw-semibold">{dbStatus.pattern}</span>
                </div>
              </div>
            )}
          </div>

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
        <small className="text-white opacity-75">
          Abstract · Factory · Builder · Prototype · Singleton
        </small>
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

          {/* Formulario dinámico */}
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

          {/* Botón ver historial */}
          <button
            type="button"
            className="btn btn-outline-secondary w-100 mt-3"
            onClick={handleShowList}
          >
            <i className="bi bi-list-ul me-2"></i>
            Ver historial de pagos
          </button>

        </form>
      </div>

      {/* ★ MODAL — Historial de pagos */}
      {showList && (
        <>
          {/* Fondo oscuro */}
          <div
            style={{
              position: 'fixed', top: 0, left: 0,
              width: '100%', height: '100%',
              background: 'rgba(0,0,0,0.5)',
              zIndex: 1000
            }}
            onClick={() => setShowList(false)}
          />

          {/* Ventana flotante */}
          <div
            style={{
              position: 'fixed',
              top: '50%', left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '90%', maxWidth: 600,
              maxHeight: '80vh',
              zIndex: 1001,
              overflowY: 'auto',
              borderRadius: 12,
              background: 'white',
              boxShadow: '0 20px 60px rgba(0,0,0,0.3)'
            }}
          >
            {/* Header modal */}
            <div
              className="d-flex justify-content-between align-items-center p-4"
              style={{
                background: 'linear-gradient(135deg, #0d6efd 0%, #0a58ca 100%)',
                borderRadius: '12px 12px 0 0',
                position: 'sticky', top: 0, zIndex: 1
              }}
            >
              <div>
                <h5 className="text-white mb-0 fw-bold">
                  <i className="bi bi-clock-history me-2"></i>
                  Historial de pagos
                </h5>
                <small className="text-white opacity-75">
                  {payments.length} transacciones registradas
                </small>
              </div>
              <button
                className="btn btn-sm btn-light"
                onClick={() => setShowList(false)}
              >
                <i className="bi bi-x-lg"></i>
              </button>
            </div>

            {/* Contenido modal */}
            <div className="p-4">
              {loadingList ? (
                <div className="text-center py-5">
                  <span className="spinner-border text-primary"></span>
                  <p className="text-muted mt-2">Cargando pagos...</p>
                </div>
              ) : payments.length === 0 ? (
                <div className="text-center py-5">
                  <i className="bi bi-inbox fs-1 text-muted"></i>
                  <p className="text-muted mt-2">No hay pagos registrados</p>
                </div>
              ) : (
                <div className="d-flex flex-column gap-2">
                  {payments.map(p => (
                    <div
                      key={p.id}
                      className="border rounded-3 p-3 d-flex justify-content-between align-items-center"
                    >
                      <div className="d-flex align-items-center gap-3">
                        <div
                          className="rounded-circle d-flex align-items-center justify-content-center"
                          style={{
                            width: 40, height: 40,
                            background: p.status === 'SUCCESS' ? '#d1fae5' : '#fee2e2'
                          }}
                        >
                          <i
                            className={`bi ${p.status === 'SUCCESS' ? 'bi-check' : 'bi-x'}`}
                            style={{ color: p.status === 'SUCCESS' ? '#059669' : '#dc2626' }}
                          />
                        </div>
                        <div>
                          <p className="mb-0 fw-semibold small">{p.paymentMethod}</p>
                          <p className="mb-0 text-muted" style={{ fontSize: 11 }}>
                            {p.transactionId || p.errorMessage}
                          </p>
                          <p className="mb-0 text-muted" style={{ fontSize: 11 }}>
                            {p.timestamp
                              ? new Date(p.timestamp).toLocaleString('es-CO')
                              : '—'}
                          </p>
                        </div>
                      </div>
                      <div className="text-end">
                        <p className="mb-0 fw-bold small">
                          {p.amount
                            ? `$${Number(p.amount).toLocaleString('es-CO')} COP`
                            : '—'}
                        </p>
                        <span
                          className={`badge ${p.status === 'SUCCESS' ? 'bg-success' : 'bg-danger'}`}
                          style={{ fontSize: 10 }}
                        >
                          {p.status}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </>
      )}

    </div>
  )
}