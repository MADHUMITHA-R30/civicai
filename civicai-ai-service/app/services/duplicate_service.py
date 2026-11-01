from typing import List, Dict, Any, Tuple
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
import math

class DuplicateDetectionService:
    
    @staticmethod
    def haversine_distance(lat1: float, lon1: float, lat2: float, lon2: float) -> float:
        """Calculate geographical distance in meters between two lat/lon points."""
        if lat1 is None or lon1 is None or lat2 is None or lon2 is None:
            return 999999.0
        R = 6371000  # radius of Earth in meters
        phi1 = math.radians(lat1)
        phi2 = math.radians(lat2)
        delta_phi = math.radians(lat2 - lat1)
        delta_lambda = math.radians(lon2 - lon1)
        
        a = math.sin(delta_phi / 2.0)**2 + math.cos(phi1) * math.cos(phi2) * math.sin(delta_lambda / 2.0)**2
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
        return R * c

    @classmethod
    def check_duplicate(cls, category_id: int, description: str, address: str, 
                        latitude: float, longitude: float, 
                        existing_complaints: List[Dict[str, Any]]) -> Dict[str, Any]:
        if not existing_complaints:
            return {
                "is_duplicate": False,
                "highest_similarity": 0.0,
                "primary_cluster_complaint_id": None,
                "matched_complaints": []
            }
        
        matches = []
        highest_score = 0.0
        primary_id = None
        
        # Prepare text vectorization
        descriptions = [c.get("description", "") for c in existing_complaints]
        descriptions.insert(0, description)
        
        try:
            vectorizer = TfidfVectorizer(stop_words='english')
            tfidf_matrix = vectorizer.fit_transform(descriptions)
            cosine_sims = cosine_similarity(tfidf_matrix[0:1], tfidf_matrix[1:]).flatten()
        except Exception:
            # Fallback simple string overlap
            cosine_sims = [0.0] * len(existing_complaints)
            
        for idx, existing in enumerate(existing_complaints):
            text_sim = float(cosine_sims[idx]) if idx < len(cosine_sims) else 0.0
            
            # Address/Category match boost
            category_match = (existing.get("categoryId") == category_id or existing.get("category_id") == category_id)
            dist_meters = cls.haversine_distance(latitude, longitude, existing.get("latitude"), existing.get("longitude"))
            
            # Address substring match boost
            existing_addr = (existing.get("address") or "").lower()
            new_addr = (address or "").lower()
            addr_overlap = (existing_addr in new_addr or new_addr in existing_addr) if existing_addr and new_addr else False
            
            combined_score = text_sim * 0.6
            if category_match:
                combined_score += 0.2
            if dist_meters < 500 or addr_overlap:
                combined_score += 0.2
                
            combined_score = round(min(1.0, combined_score), 2)
            
            if combined_score > highest_score:
                highest_score = combined_score
                primary_id = existing.get("id")
                
            if combined_score >= 0.50:
                matches.append({
                    "complaint_id": existing.get("id"),
                    "complaint_number": existing.get("complaintNumber", f"CIV-2026-{existing.get('id'):05d}"),
                    "category_name": existing.get("categoryName", "Civic Issue"),
                    "address": existing.get("address", ""),
                    "status": existing.get("status", "SUBMITTED"),
                    "similarity_score": combined_score,
                    "location_distance_meters": dist_meters if dist_meters < 999990 else None
                })

        return {
            "is_duplicate": highest_score >= 0.70,
            "highest_similarity": highest_score,
            "primary_cluster_complaint_id": primary_id if highest_score >= 0.70 else None,
            "matched_complaints": sorted(matches, key=lambda x: x["similarity_score"], reverse=True)
        }
