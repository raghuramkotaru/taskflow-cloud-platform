import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { afterEach, describe, expect, it, vi } from 'vitest';
import BoardListPage from '../pages/BoardListPage';
import * as client from '../api/client';

afterEach(() => {
  vi.restoreAllMocks();
});

describe('BoardListPage', () => {
  it('renders boards returned by the API', async () => {
    vi.spyOn(client, 'fetchBoards').mockResolvedValue([
      {
        id: 1,
        name: 'Product Launch',
        description: 'Ship v1',
        ownerId: 1,
        createdAt: '2026-01-01T00:00:00Z',
      },
    ]);

    render(
      <MemoryRouter>
        <BoardListPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText('Product Launch')).toBeInTheDocument();
    });
  });

  it('shows an error when the API fails', async () => {
    vi.spyOn(client, 'fetchBoards').mockRejectedValue(new Error('boom'));

    render(
      <MemoryRouter>
        <BoardListPage />
      </MemoryRouter>
    );

    await waitFor(() => {
      expect(screen.getByText(/could not load boards/i)).toBeInTheDocument();
    });
  });
});
