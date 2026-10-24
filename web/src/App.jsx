import { useEffect, useState } from 'react';
import { fetchReports } from './api';
import './App.css';

function App() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  function load() {
    setLoading(true);
    setError(null);
    fetchReports()
      .then((data) => setReports(data))
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }

  useEffect(load, []);

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>City Reports — Staff Dashboard</h1>
        <button type="button" onClick={load}>Refresh</button>
      </header>

      {loading && <p className="state">Loading reports…</p>}

      {error && (
        <div className="state error">
          <p><strong>Could not load reports.</strong></p>
          <p>{error}</p>
          <p className="hint">
            The backend endpoint <code>GET /api/reports</code> is still a stub
            (HTTP 501). This error is expected until the backend list endpoint
            is implemented — it confirms the web app is reaching the backend.
          </p>
        </div>
      )}

      {!loading && !error && reports.length === 0 && (
        <p className="state">No reports yet.</p>
      )}

      {!loading && !error && reports.length > 0 && (
        <table className="reports">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Category</th>
              <th>Status</th>
              <th>Created</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((r) => (
              <tr key={r.id}>
                <td>{r.id}</td>
                <td>{r.title}</td>
                <td>{r.category}</td>
                <td>{r.status}</td>
                <td>{r.createdAt}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;
