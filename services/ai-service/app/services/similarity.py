"""TF-IDF + cosine similarity based task suggestion logic.

Fully offline: no external LLM API calls. Uses scikit-learn's TfidfVectorizer
and cosine_similarity to rank existing tasks by textual similarity to a new
task, and derives a heuristic priority score from those similarities.
"""
from __future__ import annotations

from typing import List

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

from app.models.schemas import SimilarTask, SuggestResponse


def suggest(description: str, existing_tasks: List[str], top_k: int = 5) -> SuggestResponse:
    """Rank existing tasks by similarity to `description` and compute a priority.

    The priority score is a 0-100 heuristic: the more (and more strongly) a task
    resembles existing work, the higher its priority, since it likely relates to
    an active thread of work. A task with no textual overlap gets a low score.
    """
    cleaned = [t for t in existing_tasks if t and t.strip()]

    if not cleaned:
        # Nothing to compare against: assign a neutral baseline priority.
        return SuggestResponse(priority_score=50.0, similar_tasks=[])

    corpus = [description] + cleaned
    vectorizer = TfidfVectorizer(stop_words="english")
    matrix = vectorizer.fit_transform(corpus)

    # Compare the new task (row 0) against every existing task (rows 1..n).
    sims = cosine_similarity(matrix[0:1], matrix[1:]).flatten()

    ranked = sorted(
        (
            SimilarTask(index=i, description=cleaned[i], similarity=round(float(s), 4))
            for i, s in enumerate(sims)
        ),
        key=lambda st: st.similarity,
        reverse=True,
    )

    top = ranked[:top_k]

    max_sim = max((st.similarity for st in top), default=0.0)
    priority_score = round(min(100.0, 30.0 + max_sim * 70.0), 2)

    return SuggestResponse(priority_score=priority_score, similar_tasks=top)
