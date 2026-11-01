import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  flagComplaint(complaintId: number, flagType: string, reason: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/complaints/${complaintId}/flag`, { flagType, reason }, { headers: this.getAuthHeaders() });
  }

  getUserRisk(userId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/users/${userId}/risk`, { headers: this.getAuthHeaders() });
  }

  blockUser(userId: number, reason: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/users/${userId}/block`, { reason }, { headers: this.getAuthHeaders() });
  }

  unblockUser(userId: number): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/users/${userId}/unblock`, {}, { headers: this.getAuthHeaders() });
  }
}
