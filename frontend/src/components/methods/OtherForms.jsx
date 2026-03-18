export function PaypalForm({ data, onChange }) {
  return (
    <div>
      <label className="form-label fw-semibold">Correo de PayPal</label>
      <div className="input-group">
        <span className="input-group-text"><i className="bi bi-envelope"></i></span>
        <input
          type="email"
          className="form-control"
          placeholder="correo@paypal.com"
          value={data.email || ''}
          onChange={e => onChange('email', e.target.value)}
        />
      </div>
      <small className="text-muted">Serás redirigido a PayPal para confirmar.</small>
    </div>
  )
}

export function NequiForm({ data, onChange }) {
  return (
    <div>
      <label className="form-label fw-semibold">Número de celular Nequi</label>
      <div className="input-group">
        <span className="input-group-text">🟣</span>
        <span className="input-group-text">+57</span>
        <input
          type="tel"
          className="form-control"
          placeholder="3001234567"
          maxLength={10}
          value={data.phone || ''}
          onChange={e => onChange('phone', e.target.value.replace(/\D/g, ''))}
        />
      </div>
      <small className="text-muted">Recibirás una notificación push en tu app.</small>
    </div>
  )
}

export function DaviplataForm({ data, onChange }) {
  return (
    <div>
      <label className="form-label fw-semibold">Número de celular Daviplata</label>
      <div className="input-group">
        <span className="input-group-text">🔴</span>
        <span className="input-group-text">+57</span>
        <input
          type="tel"
          className="form-control"
          placeholder="3001234567"
          maxLength={10}
          value={data.phone || ''}
          onChange={e => onChange('phone', e.target.value.replace(/\D/g, ''))}
        />
      </div>
      <small className="text-muted">Recibirás una solicitud de pago en Daviplata.</small>
    </div>
  )
}

export function CryptoForm({ data, onChange }) {
  return (
    <div className="row g-3">
      <div className="col-12">
        <label className="form-label fw-semibold">Criptomoneda</label>
        <select
          className="form-select"
          value={data.cryptoCurrency || 'BTC'}
          onChange={e => onChange('cryptoCurrency', e.target.value)}
        >
          <option value="BTC">₿ Bitcoin (BTC)</option>
          <option value="ETH">⬡ Ethereum (ETH)</option>
          <option value="USDT">₮ Tether (USDT)</option>
        </select>
      </div>
      <div className="col-12">
        <label className="form-label fw-semibold">Dirección de wallet</label>
        <div className="input-group">
          <span className="input-group-text"><i className="bi bi-wallet2"></i></span>
          <input
            type="text"
            className="form-control font-monospace"
            placeholder="1A1zP1eP5QGefi2DMPTfTL5SLmv7Divf..."
            value={data.walletAddress || ''}
            onChange={e => onChange('walletAddress', e.target.value)}
          />
        </div>
      </div>
    </div>
  )
}