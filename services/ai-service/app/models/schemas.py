"""Pydantic request/response models for the AI service."""
from __future__ import annotations

from datetime import datetime, timezone
from typing import List, Optional

from pydantic import BaseModel, Field


class SuggestRequest(BaseModel):
    description: str = Field(..., min_length=1, description="The new task description")
    existing_tasks: List[str] = Field(
        default_factory=list,
        description="Descriptions of existing tasks to compare against",
    )
    top_k: int = Field(5, ge=1, le=50, description="Max number of similar tasks to return")


class SimilarTask(BaseModel):
    index: int
    description: str
    similarity: float


class SuggestResponse(BaseModel):
    priority_score: float = Field(
        ..., description="Computed priority score in [0, 100]"
    )
    similar_tasks: List[SimilarTask]


class ActivityRequest(BaseModel):
    task_id: str = Field(..., min_length=1)
    action: str = Field(..., min_length=1, description="e.g. created, commented, moved")
    actor: str = Field(..., min_length=1, description="Username / actor id")
    detail: Optional[str] = None


class ActivityRecord(BaseModel):
    task_id: str
    action: str
    actor: str
    detail: Optional[str] = None
    created_at: datetime = Field(default_factory=lambda: datetime.now(timezone.utc))
