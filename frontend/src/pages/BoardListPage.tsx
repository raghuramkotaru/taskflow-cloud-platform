import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { fetchBoards } from '../api/client';
import type { Board } from '../api/types';

export default function BoardListPage() {
  const [boards, setBoards] = useState<Board[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchBoards()
      .then(setBoards)
      .catch(() => setError('Could not load boards. Is the core-service running?'))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p>Loading boards…</p>;
  if (error) return <p className="error">{error}</p>;

  return (
    <section>
      <h1>Boards</h1>
      {boards.length === 0 && <p>No boards yet.</p>}
      {boards.map((board) => (
        <div className="card" key={board.id}>
          <h3>
            <Link to={`/boards/${board.id}`}>{board.name}</Link>
          </h3>
          {board.description && <p>{board.description}</p>}
        </div>
      ))}
    </section>
  );
}
