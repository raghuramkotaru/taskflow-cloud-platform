import { Link, Outlet } from 'react-router-dom';

export default function App() {
  return (
    <div className="app">
      <header className="app-header">
        <Link to="/" className="brand">
          TaskFlow
        </Link>
        <span className="tagline">Boards, tasks, and AI-assisted prioritization</span>
      </header>
      <main className="app-main">
        <Outlet />
      </main>
    </div>
  );
}
