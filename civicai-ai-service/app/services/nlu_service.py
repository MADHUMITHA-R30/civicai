import re
from typing import Dict, Any, List

class NLUService:
    
    # 15 Civic Categories & Department Mapping
    CATEGORY_DEPT_MAP = {
        "Street Light Not Working": "Electrical",
        "Road Damage / Pothole": "Roads & Infrastructure",
        "Garbage / Waste Problem": "Sanitation & Waste Management",
        "Electricity Problem": "Electrical",
        "Stagnant Water": "Water & Drainage",
        "Drainage / Sewage Problem": "Water & Drainage",
        "Water Supply Problem": "Water & Drainage",
        "Environmental Pollution": "Environment & Pollution",
        "Illegal Dumping": "Sanitation & Waste Management",
        "Public Toilet Problem": "Sanitation & Waste Management",
        "Traffic / Road Safety Problem": "Public Safety",
        "Tree / Public Greenery Problem": "Environment & Pollution",
        "Public Infrastructure Damage": "Roads & Infrastructure",
        "Noise Pollution": "Environment & Pollution",
        "Other": "General Civic Services"
    }

    KEYWORD_CATEGORY_RULES = [
        (r'light|lamp|dark|bulb|wire|transformer|electricity|power cut|current', "Street Light Not Working", "Electrical"),
        (r'pothole|road|asphalt|tar|crack|pavement|bridge|divider|footpath', "Road Damage / Pothole", "Roads & Infrastructure"),
        (r'garbage|waste|trash|dump|bin|overflow|litter|cleanliness', "Garbage / Waste Problem", "Sanitation & Waste Management"),
        (r'drain|drainage|sewage|stagnant|waterlogging|gutter|clogged|overflowing water', "Drainage / Sewage Problem", "Water & Drainage"),
        (r'water supply|pipeline|dirty water|tap water|no water', "Water Supply Problem", "Water & Drainage"),
        (r'smoke|pollution|factory|air|chemical|smell|odor', "Environmental Pollution", "Environment & Pollution"),
        (r'toilet|restroom|washroom', "Public Toilet Problem", "Sanitation & Waste Management"),
        (r'traffic|signal|accident|speed breaker|road safety', "Traffic / Road Safety Problem", "Public Safety"),
        (r'tree|branch|park|garden|greenery', "Tree / Public Greenery Problem", "Environment & Pollution"),
    ]

    @classmethod
    def analyze_complaint(cls, raw_text: str, citizen_history_count: int = 0) -> Dict[str, Any]:
        text_lower = raw_text.lower()
        
        # 1. Category and Department Detection
        matched_category = "Other"
        matched_dept = "General Civic Services"
        confidence = 0.75
        
        for pattern, cat, dept in cls.KEYWORD_CATEGORY_RULES:
            if re.search(pattern, text_lower):
                matched_category = cat
                matched_dept = dept
                confidence = 0.92
                break
                
        # 2. Extract Phone Number
        phone_match = re.search(r'\b[6-9]\d{9}\b', raw_text)
        extracted_contact = phone_match.group(0) if phone_match else ""

        # 3. Extract Address / Location clues
        address_patterns = [
            r'(?:near|at|opposite|behind|beside|on|in)\s+([A-Za-z0-9\s,]+?)(?=\.|\,|phone|contact|for|\d{10}|$)',
            r'([A-Za-z0-9\s,]+(?:road|street|nagar|colony|lane|market|park|cross|main|sector|entrance|gate|school|hospital))'
        ]
        extracted_address = ""
        for p in address_patterns:
            m = re.search(p, raw_text, re.IGNORECASE)
            if m:
                extracted_address = m.group(1).strip()
                break
        if not extracted_address:
            extracted_address = "Location mentioned in complaint details"

        # 4. Severity & Priority Calculation with Explainability
        reasons: List[str] = []
        base_score = 50

        # Keywords boosting severity
        if re.search(r'school|hospital|market|bus stop|main road|highway|children|patient', text_lower):
            base_score += 20
            reasons.append("Location is near a sensitive public area (school/hospital/main road)")
            
        if re.search(r'hazard|dangerous|spark|accident|urgent|emergency|flood|leak|overflowing', text_lower):
            base_score += 15
            reasons.append("High public safety hazard or risk detected in text")

        if re.search(r'\d+\s*(?:days|weeks|months|days not working|three days|week)', text_lower):
            base_score += 10
            reasons.append("Prolonged duration reported by citizen")

        if citizen_history_count > 5:
            base_score += 5
            reasons.append("Reported by experienced citizen contributor")

        if not reasons:
            reasons.append("Standard civic maintenance report")

        final_priority_score = min(98, max(25, base_score))

        if final_priority_score >= 80:
            severity = "CRITICAL"
        elif final_priority_score >= 65:
            severity = "HIGH"
        elif final_priority_score >= 45:
            severity = "MEDIUM"
        else:
            severity = "LOW"

        return {
            "issue_category": matched_category,
            "department": matched_dept,
            "description": raw_text.strip(),
            "address": extracted_address,
            "contact_number": extracted_contact,
            "severity": severity,
            "priority_score": final_priority_score,
            "confidence": confidence,
            "reasons": reasons
        }
