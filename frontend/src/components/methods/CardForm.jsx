export default function CardForm({ data, onChange }) {
  return (
    <div className="row g-3">
      <div className="col-12">
        <label className="form-label fw-semibold">Número de tarjeta</label>
        <div className="input-group">
          <span className="input-group-text"><i className="bi bi-credit-card"></i></span>
          <input
            type="text"
            className="form-control"
            placeholder="1234 5678 9012 3456"
            maxLength={16}
            value={data.cardNumber || ''}
            onChange={e => onChange('cardNumber', e.target.value.replace(/\D/g, ''))}
          />
        </div>
      </div>
      <div className="col-12">
        <label className="form-label fw-semibold">Nombre en la tarjeta</label>
        <input
          type="text"
          className="form-control"
          placeholder="JUAN PEREZ"
          value={data.cardHolderName || ''}
          onChange={e => onChange('cardHolderName', e.target.value.toUpperCase())}
        />
      </div>
      <div className="col-6">
        <label className="form-label fw-semibold">Vencimiento</label>
        <input
          type="text"
          className="form-control"
          placeholder="MM/AA"
          maxLength={5}
          value={data.expiryDate || ''}
          onChange={e => onChange('expiryDate', e.target.value)}
        />
      </div>
      <div className="col-6">
        <label className="form-label fw-semibold">CVV</label>
        <input
          type="password"
          className="form-control"
          placeholder="123"
          maxLength={3}
          value={data.cvv || ''}
          onChange={e => onChange('cvv', e.target.value.replace(/\D/g, ''))}
        />
      </div>
    </div>
  )
}