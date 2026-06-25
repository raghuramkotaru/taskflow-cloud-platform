from fastapi import APIRouter

from app.models.schemas import SuggestRequest, SuggestResponse
from app.services import similarity

router = APIRouter(prefix="/api/v1", tags=["suggest"])


@router.post("/suggest", response_model=SuggestResponse)
def suggest_priority(request: SuggestRequest) -> SuggestResponse:
    return similarity.suggest(
        request.description, request.existing_tasks, request.top_k
    )
