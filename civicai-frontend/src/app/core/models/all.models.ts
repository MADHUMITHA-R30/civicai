export interface User {
  id: number;
  name: string;
  email: string;
  mobile: string;
  role: 'ROLE_CITIZEN' | 'ROLE_DEPARTMENT_ADMIN' | 'ROLE_SUPER_ADMIN';
  departmentId?: number;
  departmentName?: string;
  accountStatus?: string;
}

export interface AuthResponse {
  token: string;
  id: number;
  name: string;
  email: string;
  mobile: string;
  role: 'ROLE_CITIZEN' | 'ROLE_DEPARTMENT_ADMIN' | 'ROLE_SUPER_ADMIN';
  departmentId?: number;
  departmentName?: string;
}

export interface Department {
  id: number;
  name: string;
  code: string;
  description: string;
  status: string;
}

export interface IssueCategory {
  id: number;
  name: string;
  description: string;
  defaultDepartment?: Department;
  active: boolean;
}

export interface AiAnalysis {
  extractedCategory: string;
  extractedAddress: string;
  extractedContact: string;
  severity: string;
  priorityScore: number;
  departmentPrediction: string;
  confidence: number;
  duplicateProbability?: number;
  priorityExplanation: string;
}

export interface Resolution {
  id: number;
  resolutionMessage: string;
  resolvedByName: string;
  resolvedByDepartment: string;
  resolvedAt: string;
  citizenVerificationStatus: 'PENDING' | 'VERIFIED' | 'DISPUTED';
  citizenFeedback?: string;
  afterMediaPaths: string[];
}

export interface Complaint {
  id: number;
  complaintNumber: string;
  citizenId: number;
  citizenName: string;
  citizenMobile: string;
  categoryId: number;
  categoryName: string;
  departmentId?: number;
  departmentName?: string;
  description: string;
  address: string;
  latitude?: number;
  longitude?: number;
  contactNumber?: string;
  priority: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  priorityScore: number;
  status: 'SUBMITTED' | 'AI_ANALYZED' | 'ASSIGNED' | 'ACKNOWLEDGED' | 'IN_PROGRESS' | 'RESOLVED' | 'VERIFIED' | 'RESOLUTION_DISPUTED' | 'REOPENED' | 'REJECTED' | 'FALSE_SPAM' | 'DUPLICATE';
  aiConfidence?: number;
  isVoiceReport?: boolean;
  createdAt: string;
  updatedAt: string;
  originalMediaPaths: string[];
  resolution?: Resolution;
  aiAnalysis?: AiAnalysis;
  clusterReference?: string;
  clusterReportCount?: number;
}

export interface Hotspot {
  locationName: string;
  latitude: number;
  longitude: number;
  complaintCount: number;
  primaryCategory: string;
  riskLevel: string;
}

export interface RecurringIssue {
  locationName: string;
  category: string;
  historicalCount: number;
  timeFrame: string;
  aiRecommendation: string;
}

export interface DepartmentAnalytics {
  departmentName: string;
  totalIssues: number;
  newIssues: number;
  inProgressIssues: number;
  criticalIssues: number;
  resolvedIssues: number;
  duplicateCount: number;
  falseSpamCount: number;
  resolutionRatePercentage: number;
  avgResolutionTimeDays: number;
  satisfactionRating: number;
  issuesByCategory: { [key: string]: number };
  issuesByPriority: { [key: string]: number };
  issuesByStatus: { [key: string]: number };
  hotspots: Hotspot[];
  recurringIssues: RecurringIssue[];
}
