import os
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.api.endpoints import router as ai_router

app = FastAPI(
    title="CivicAI AI Engine Microservice",
    description="Python FastAPI service providing STT, NLU information extraction, duplicate clustering, priority calculation, and citizen risk scoring.",
    version="1.0.0"
)

# Parse CORS origins from environment variable (comma-separated or wildcard)
raw_origins = os.environ.get("CORS_ALLOWED_ORIGINS", "*")
allowed_origins = [o.strip() for o in raw_origins.split(",") if o.strip()] or ["*"]

app.add_middleware(
    CORSMiddleware,
    allow_origins=allowed_origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(ai_router)

@app.get("/")
def read_root():
    return {
        "status": "online",
        "service": "CivicAI Python AI Engine",
        "version": "1.0.0",
        "endpoints": [
            "/api/ai/transcribe",
            "/api/ai/analyze-complaint",
            "/api/ai/detect-duplicate",
            "/api/ai/calculate-risk"
        ]
    }

if __name__ == "__main__":
    import uvicorn
    port = int(os.environ.get("PORT", 8000))
    reload_enabled = os.environ.get("ENV", "development").lower() == "development"
    uvicorn.run("main:app", host="0.0.0.0", port=port, reload=reload_enabled)
