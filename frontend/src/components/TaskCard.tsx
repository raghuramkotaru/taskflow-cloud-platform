import { useEffect, useState } from 'react';
import { fetchComments, suggestPriority } from '../api/client';
import type { Comment, SuggestResponse, Task } from '../api/types';

interface Props {
  task: Task;
  otherTaskDescriptions: string[];
}

export default function TaskCard({ task, otherTaskDescriptions }: Props) {
  const [comments, setComments] = useState<Comment[]>([]);
  const [suggestion, setSuggestion] = useState<SuggestResponse | null>(null);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchComments(task.id)
      .then(setComments)
      .catch(() => {
        /* comments are non-critical for rendering the card */
      });
  }, [task.id]);

  const onSuggest = async () => {
    setBusy(true);
    setError(null);
    try {
      const result = await suggestPriority(
        task.description ?? task.title,
        otherTaskDescriptions
      );
      setSuggestion(result);
    } catch {
      setError('Suggestion service unavailable.');
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="card task-card">
      <div>
        <strong>{task.title}</strong> <span className="status">{task.status}</span>
      </div>
      {task.description && <p>{task.description}</p>}
      <div>Priority: {task.priority}</div>

      <button onClick={onSuggest} disabled={busy}>
        {busy ? 'Analyzing…' : 'Suggest priority'}
      </button>

      {error && <p className="error">{error}</p>}

      {suggestion && (
        <div className="suggestion" role="status">
          <div>
            Suggested priority score: <strong>{suggestion.priority_score}</strong>
          </div>
          {suggestion.similar_tasks.length > 0 && (
            <div>
              Most similar: "{suggestion.similar_tasks[0].description}" (
              {suggestion.similar_tasks[0].similarity})
            </div>
          )}
        </div>
      )}

      {comments.length > 0 && (
        <div>
          <h4>Comments</h4>
          <ul>
            {comments.map((c) => (
              <li key={c.id}>{c.body}</li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}
