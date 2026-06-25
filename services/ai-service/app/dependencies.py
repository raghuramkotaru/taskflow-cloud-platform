"""Shared dependency wiring for the FastAPI app."""
from __future__ import annotations

from functools import lru_cache

from app.services.activity_store import ActivityStore, build_client


@lru_cache(maxsize=1)
def get_activity_store() -> ActivityStore:
    return ActivityStore(build_client())
