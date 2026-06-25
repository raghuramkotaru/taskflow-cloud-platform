import { useCallback, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchBoard, fetchTasks } from '../api/client';
import type { Board, Task } from '../api/types';
import TaskCard from '../components/TaskCard';

export default function BoardDetailPage() {
  const { boardId } = useParams();
  const id = Number(boardId);
  const [board, setBoard] = useState<Board | null>(null);
  const [tasks, setTasks] = useState<Task[]>([]);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const load = useCallback(() => {
    setLoading(true);
    Promise.all([fetchBoard(id), fetchTasks(id)])
      .then(([b, t]) => {
        setBoard(b);
        setTasks(t);
        setError(null);
      })
      .catch(() => setError('Could not load this board.'))
      .finally(() => setLoading(false));
  }, [id]);

  useEffect(() => {
    load();
  }, [load]);

  if (loading) return <p>Loading board…</p>;
  if (error) return <p className="error">{error}</p>;
  if (!board) return <p>Board not found.</p>;

  const otherDescriptions = tasks.map((t) => t.description ?? t.title);

  return (
    <section>
      <h1>{board.name}</h1>
      {board.description && <p>{board.description}</p>}
      <h2>Tasks</h2>
      {tasks.length === 0 && <p>No tasks yet.</p>}
      {tasks.map((task) => (
        <TaskCard
          key={task.id}
          task={task}
          otherTaskDescriptions={otherDescriptions.filter(
            (_, i) => tasks[i].id !== task.id
          )}
        />
      ))}
    </section>
  );
}
