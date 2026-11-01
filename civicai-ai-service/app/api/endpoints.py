from fastapi import APIRouter, File, UploadFile, Form, HTTPException
from typing import Optional
from app.schemas.request_response import (
    TranscribeRequest, TranscribeResponse,
    ComplaintAnalysisRequest, ComplaintAnalysisResponse,
    DuplicateCheckRequest, DuplicateCheckResponse,
    RiskScoreRequest, RiskScoreResponse
)
from app.services.stt_service import STTService
from app.services.nlu_service import NLUService
from app.services.duplicate_service import DuplicateDetectionService
from app.services.risk_service import RiskScoringService

router = APIRouter(prefix="/api/ai", tags=["AI Processing"])

@router.post("/transcribe", response_model=TranscribeResponse)
async def transcribe_audio(payload: Optional[TranscribeRequest] = None):
    try:
        audio_b64 = payload.audio_base64 if payload else None
        res = STTService.process_voice_or_audio(audio_base64=audio_b64)
        return TranscribeResponse(**res)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/transcribe-file", response_model=TranscribeResponse)
async def transcribe_audio_file(file: UploadFile = File(...)):
    try:
        content = await file.read()
        res = STTService.process_voice_or_audio(raw_audio_bytes=content)
        return TranscribeResponse(**res)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/analyze-complaint", response_model=ComplaintAnalysisResponse)
async def analyze_complaint(req: ComplaintAnalysisRequest):
    try:
        extracted_dict = NLUService.analyze_complaint(
            raw_text=req.raw_text,
            citizen_history_count=req.citizen_history_count or 0
        )
        return ComplaintAnalysisResponse(success=True, extracted=extracted_dict)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/detect-duplicate", response_model=DuplicateCheckResponse)
async def detect_duplicate(req: DuplicateCheckRequest):
    try:
        result = DuplicateDetectionService.check_duplicate(
            category_id=req.category_id,
            description=req.description,
            address=req.address,
            latitude=req.latitude,
            longitude=req.longitude,
            existing_complaints=req.existing_complaints
        )
        return DuplicateCheckResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/calculate-risk", response_model=RiskScoreResponse)
async def calculate_user_risk(req: RiskScoreRequest):
    try:
        result = RiskScoringService.calculate_user_risk(
            total_submitted=req.total_submitted,
            verified_count=req.verified_count,
            false_spam_count=req.false_spam_count
        )
        return RiskScoreResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
