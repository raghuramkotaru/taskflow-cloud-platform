from app.services import similarity


def test_suggest_ranks_most_similar_first():
    result = similarity.suggest(
        "Fix login authentication bug",
        [
            "Resolve authentication login error on signin page",
            "Buy office snacks",
            "Update the marketing landing page copy",
        ],
    )
    assert result.similar_tasks
    # The authentication-related task should rank highest.
    top = result.similar_tasks[0]
    assert "authentication" in top.description.lower()
    assert top.similarity >= result.similar_tasks[-1].similarity


def test_suggest_empty_existing_returns_neutral():
    result = similarity.suggest("Some new task", [])
    assert result.similar_tasks == []
    assert result.priority_score == 50.0


def test_priority_score_in_range():
    result = similarity.suggest(
        "Deploy service to production",
        ["Deploy service to production cluster", "Write tests"],
    )
    assert 0.0 <= result.priority_score <= 100.0
    # Strong overlap should push priority above the 30 baseline floor.
    assert result.priority_score > 30.0
