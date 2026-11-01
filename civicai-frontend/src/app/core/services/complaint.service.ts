import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Complaint, IssueCategory, Department } from '../models/all.models';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ComplaintService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  getCategories(): Observable<IssueCategory[]> {
    return this.http.get<IssueCategory[]>(`${this.baseUrl}/categories`);
  }

  getDepartments(): Observable<Department[]> {
    return this.http.get<Department[]>(`${this.baseUrl}/departments`);
  }

  createComplaint(data: any): Observable<Complaint> {
    return this.http.post<Complaint>(`${this.baseUrl}/complaints`, data, { headers: this.getAuthHeaders() });
  }

  processVoiceInput(audioBase64: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/complaints/voice-process`, { audioBase64 }, { headers: this.getAuthHeaders() });
  }

  checkDuplicate(categoryId: number, description: string, address: string, latitude?: number, longitude?: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/complaints/check-duplicate`, { categoryId, description, address, latitude, longitude }, { headers: this.getAuthHeaders() });
  }

  getMyComplaints(): Observable<Complaint[]> {
    return this.http.get<Complaint[]>(`${this.baseUrl}/complaints/my`, { headers: this.getAuthHeaders() });
  }

  getComplaintById(id: number): Observable<Complaint> {
    return this.http.get<Complaint>(`${this.baseUrl}/complaints/${id}`, { headers: this.getAuthHeaders() });
  }

  getDepartmentComplaints(deptId?: number, status?: string, priority?: string): Observable<Complaint[]> {
    let params = [];
    if (deptId) params.push(`deptId=${deptId}`);
    if (status) params.push(`status=${status}`);
    if (priority) params.push(`priority=${priority}`);
    const queryStr = params.length ? `?${params.join('&')}` : '';

    return this.http.get<Complaint[]>(`${this.baseUrl}/complaints/department${queryStr}`, { headers: this.getAuthHeaders() });
  }

  updateStatus(complaintId: number, status: string, remarks?: string): Observable<Complaint> {
    return this.http.put<Complaint>(`${this.baseUrl}/complaints/${complaintId}/status`, { status, remarks }, { headers: this.getAuthHeaders() });
  }

  resolveComplaint(complaintId: number, resolutionMessage: string, afterPhotoBase64: string): Observable<Complaint> {
    return this.http.post<Complaint>(`${this.baseUrl}/complaints/${complaintId}/resolve`, { resolutionMessage, afterPhotoBase64 }, { headers: this.getAuthHeaders() });
  }

  verifyResolution(complaintId: number, isResolved: boolean, feedback?: string): Observable<Complaint> {
    return this.http.post<Complaint>(`${this.baseUrl}/complaints/${complaintId}/verify`, { isResolved, feedback }, { headers: this.getAuthHeaders() });
  }
}
