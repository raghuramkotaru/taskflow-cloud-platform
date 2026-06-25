export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

export interface Board {
  id: number;
  name: string;
  description: string | null;
  ownerId: number;
  createdAt: string;
}

export interface Task {
  id: number;
  title: string;
  description: string | null;
  status: TaskStatus;
  priority: number;
  boardId: number;
  assigneeId: number | null;
  createdAt: string;
}

export interface Comment {
  id: number;
  body: string;
  authorId: number;
  taskId: number;
  createdAt: string;
}

export interface SimilarTask {
  index: number;
  description: string;
  similarity: number;
}

export interface SuggestResponse {
  priority_score: number;
  similar_tasks: SimilarTask[];
}
