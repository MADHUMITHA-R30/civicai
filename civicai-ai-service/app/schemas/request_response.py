from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any

class TranscribeRequest(BaseModel):
    audio_base64: Optional[str] = None
    language: Optional[str] = "en"

class TranscribeResponse(BaseModel):
    success: bool
    text: str
    confidence: float
    language_detected: str

class ComplaintAnalysisRequest(BaseModel):
    raw_text: str
    latitude: Optional[float] = None
    longitude: Optional[float] = None
    citizen_history_count: Optional[int] = 0

class ExtractedInfo(BaseModel):
    issue_category: str
    department: str
    description: str
    address: str
    contact_number: str
    severity: str
    priority_score: int
    confidence: float
    reasons: List[str]

class ComplaintAnalysisResponse(BaseModel):
    success: bool
    extracted: ExtractedInfo

class DuplicateCheckRequest(BaseModel):
    category_id: int
    description: str
    address: str
    latitude: Optional[float] = None
    longitude: Optional[float] = None
    existing_complaints: List[Dict[str, Any]] = []

class DuplicateMatch(BaseModel):
    complaint_id: int
    complaint_number: str
    category_name: str
    address: str
    status: str
    similarity_score: float
    location_distance_meters: Optional[float] = None

class DuplicateCheckResponse(BaseModel):
    is_duplicate: bool
    highest_similarity: float
    primary_cluster_complaint_id: Optional[int] = None
    matched_complaints: List[DuplicateMatch]

class RiskScoreRequest(BaseModel):
    total_submitted: int
    verified_count: int
    false_spam_count: int

class RiskScoreResponse(BaseModel):
    reliability_score: float
    risk_level: str  # LOW, MEDIUM, HIGH, CRITICAL
    flagged_recommendation: bool
