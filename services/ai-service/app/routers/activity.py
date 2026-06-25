from typing import List

from fastapi import APIRouter, Depends

from app.dependencies import get_activity_store
from app.models.schemas import ActivityRecord, ActivityRequest
from app.services.activity_store import ActivityStore

router = APIRouter(prefix="/api/v1", tags=["activity"])


@router.post("/activity", response_model=ActivityRecord, status_code=201)
def log_activity(
    request: ActivityRequest,
    store: ActivityStore = Depends(get_activity_store),
) -> ActivityRecord:
    return store.log(request)


@router.get("/activity/{task_id}", response_model=List[ActivityRecord])
def get_activity(
    task_id: str,
    store: ActivityStore = Depends(get_activity_store),
) -> List[ActivityRecord]:
    return store.list_for_task(task_id)
