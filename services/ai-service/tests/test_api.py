def test_health(client):
    resp = client.get("/api/v1/health")
    assert resp.status_code == 200
    assert resp.json()["status"] == "UP"


def test_suggest_endpoint(client):
    resp = client.post(
        "/api/v1/suggest",
        json={
            "description": "Set up CI/CD pipeline",
            "existing_tasks": ["Configure CI pipeline in GitHub Actions", "Order lunch"],
        },
    )
    assert resp.status_code == 200
    body = resp.json()
    assert "priority_score" in body
    assert len(body["similar_tasks"]) == 2
    assert body["similar_tasks"][0]["similarity"] >= body["similar_tasks"][1]["similarity"]


def test_activity_log_and_retrieve(client):
    post = client.post(
        "/api/v1/activity",
        json={
            "task_id": "task-1",
            "action": "commented",
            "actor": "alice",
            "detail": "Looks good",
        },
    )
    assert post.status_code == 201
    assert post.json()["task_id"] == "task-1"

    get = client.get("/api/v1/activity/task-1")
    assert get.status_code == 200
    records = get.json()
    assert len(records) == 1
    assert records[0]["action"] == "commented"
    assert records[0]["actor"] == "alice"


def test_activity_empty_for_unknown_task(client):
    resp = client.get("/api/v1/activity/does-not-exist")
    assert resp.status_code == 200
    assert resp.json() == []
