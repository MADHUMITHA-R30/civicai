class RiskScoringService:
    
    @staticmethod
    def calculate_user_risk(total_submitted: int, verified_count: int, false_spam_count: int) -> dict:
        if total_submitted == 0:
            return {
                "reliability_score": 100.0,
                "risk_level": "LOW",
                "flagged_recommendation": False
            }
            
        # False complaint penalty weight
        false_ratio = false_spam_count / float(total_submitted)
        verified_ratio = verified_count / float(total_submitted)
        
        reliability = (verified_ratio * 100.0) - (false_ratio * 150.0) + 20.0
        reliability = round(max(0.0, min(100.0, reliability)), 1)
        
        if false_spam_count >= 3 and false_ratio >= 0.4:
            risk_level = "CRITICAL"
            flagged = True
        elif false_spam_count >= 2 or false_ratio >= 0.25:
            risk_level = "HIGH"
            flagged = True
        elif false_spam_count >= 1:
            risk_level = "MEDIUM"
            flagged = False
        else:
            risk_level = "LOW"
            flagged = False
            
        return {
            "reliability_score": reliability,
            "risk_level": risk_level,
            "flagged_recommendation": flagged
        }
