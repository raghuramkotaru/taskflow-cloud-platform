"""MongoDB-backed activity log store.

Uses pymongo. In tests the pymongo client is swapped for a mongomock client,
so the same code path runs with no live database.
"""
from __future__ import annotations

import os
from typing import List

from pymongo import DESCENDING, MongoClient
from pymongo.collection import Collection

from app.models.schemas import ActivityRecord, ActivityRequest

_DEFAULT_URI = "mongodb://localhost:27017"
_DB_NAME = os.getenv("MONGO_DB", "taskflow")
_COLLECTION = "activity"


class ActivityStore:
    def __init__(self, client: MongoClient):
        self._collection: Collection = client[_DB_NAME][_COLLECTION]

    def log(self, request: ActivityRequest) -> ActivityRecord:
        record = ActivityRecord(
            task_id=request.task_id,
            action=request.action,
            actor=request.actor,
            detail=request.detail,
        )
        self._collection.insert_one(record.model_dump())
        return record

    def list_for_task(self, task_id: str) -> List[ActivityRecord]:
        cursor = (
            self._collection.find({"task_id": task_id})
            .sort("created_at", DESCENDING)
        )
        return [ActivityRecord(**_strip_id(doc)) for doc in cursor]


def _strip_id(doc: dict) -> dict:
    doc.pop("_id", None)
    return doc


def build_client() -> MongoClient:
    uri = os.getenv("MONGO_URI", _DEFAULT_URI)
    return MongoClient(uri)
