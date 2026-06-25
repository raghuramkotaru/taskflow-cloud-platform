import axios from 'axios';
import type { Board, Comment, SuggestResponse, Task } from './types';

const CORE_BASE = import.meta.env.VITE_CORE_API ?? 'http://localhost:8080';
const AI_BASE = import.meta.env.VITE_AI_API ?? 'http://localhost:8000';

// Demo header-based auth: a real user id seeded by the core-service migration.
const DEMO_USER_ID = import.meta.env.VITE_USER_ID ?? '1';

const core = axios.create({
  baseURL: CORE_BASE,
  headers: { 'X-User-Id': DEMO_USER_ID },
});

const ai = axios.create({ baseURL: AI_BASE });

export async function fetchBoards(): Promise<Board[]> {
  const { data } = await core.get<Board[]>('/api/v1/boards');
  return data;
}

export async function fetchBoard(boardId: number): Promise<Board> {
  const { data } = await core.get<Board>(`/api/v1/boards/${boardId}`);
  return data;
}

export async function fetchTasks(boardId: number): Promise<Task[]> {
  const { data } = await core.get<Task[]>(`/api/v1/boards/${boardId}/tasks`);
  return data;
}

export async function createTask(
  boardId: number,
  title: string,
  description: string
): Promise<Task> {
  const { data } = await core.post<Task>(`/api/v1/boards/${boardId}/tasks`, {
    title,
    description,
  });
  return data;
}

export async function fetchComments(taskId: number): Promise<Comment[]> {
  const { data } = await core.get<Comment[]>(`/api/v1/tasks/${taskId}/comments`);
  return data;
}

export async function suggestPriority(
  description: string,
  existingTasks: string[]
): Promise<SuggestResponse> {
  const { data } = await ai.post<SuggestResponse>('/api/v1/suggest', {
    description,
    existing_tasks: existingTasks,
  });
  return data;
}
