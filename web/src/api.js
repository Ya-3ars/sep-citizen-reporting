const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export async function fetchReports({ status, category } = {}) {
  const params = new URLSearchParams();
  if (status) params.set('status', status);
  if (category) params.set('category', category);
  const query = params.toString();

  const res = await fetch(`${BASE_URL}/api/reports${query ? `?${query}` : ''}`);
  if (!res.ok) {
    throw new Error(`Backend returned ${res.status} ${res.statusText}`);
  }
  return res.json();
}
