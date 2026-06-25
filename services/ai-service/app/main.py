from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.routers import activity, suggest

app = FastAPI(
    title="TaskFlow AI Service",
    description="Offline TF-IDF task suggestion and MongoDB activity log",
    version="0.1.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(suggest.router)
app.include_router(activity.router)


@app.get("/api/v1/health")
def health() -> dict:
    return {"status": "UP", "service": "ai-service"}
