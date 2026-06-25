import { render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { afterEach, describe, expect, it, vi } from 'vitest';
import TaskCard from '../components/TaskCard';
import type { Task } from '../api/types';
import * as client from '../api/client';

const task: Task = {
  id: 1,
  title: 'Set up CI',
  description: 'Configure GitHub Actions',
  status: 'TODO',
  priority: 2,
  boardId: 1,
  assigneeId: null,
  createdAt: '2026-01-01T00:00:00Z',
};

afterEach(() => {
  vi.restoreAllMocks();
});

describe('TaskCard', () => {
  it('renders the task title and status', () => {
    vi.spyOn(client, 'fetchComments').mockResolvedValue([]);
    render(<TaskCard task={task} otherTaskDescriptions={[]} />);
    expect(screen.getByText('Set up CI')).toBeInTheDocument();
    expect(screen.getByText('TODO')).toBeInTheDocument();
  });

  it('shows a suggested priority score after clicking the button', async () => {
    vi.spyOn(client, 'fetchComments').mockResolvedValue([]);
    vi.spyOn(client, 'suggestPriority').mockResolvedValue({
      priority_score: 87.5,
      similar_tasks: [{ index: 0, description: 'CI pipeline', similarity: 0.8 }],
    });

    render(<TaskCard task={task} otherTaskDescriptions={['CI pipeline']} />);
    await userEvent.click(screen.getByRole('button', { name: /suggest priority/i }));

    await waitFor(() => {
      expect(screen.getByText('87.5')).toBeInTheDocument();
    });
    expect(client.suggestPriority).toHaveBeenCalledWith('Configure GitHub Actions', [
      'CI pipeline',
    ]);
  });
});
