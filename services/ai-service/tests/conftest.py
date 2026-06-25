import mongomock
import pytest
from fastapi.testclient import TestClient

from app.dependencies import get_activity_store
from app.main import app
from app.services.activity_store import ActivityStore


@pytest.fixture
def client():
    mock_client = mongomock.MongoClient()
    store = ActivityStore(mock_client)
    app.dependency_overrides[get_activity_store] = lambda: store
    with TestClient(app) as c:
        yield c
    app.dependency_overrides.clear()
