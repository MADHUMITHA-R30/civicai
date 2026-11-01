import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DepartmentAnalytics } from '../models/all.models';
import { AuthService } from './auth.service';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {
  private baseUrl = `${environment.apiUrl}/analytics`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  private getAuthHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : '',
      'Content-Type': 'application/json'
    });
  }

  getDepartmentAnalytics(deptId?: number): Observable<DepartmentAnalytics> {
    const query = deptId ? `?deptId=${deptId}` : '';
    return this.http.get<DepartmentAnalytics>(`${this.baseUrl}/department${query}`, { headers: this.getAuthHeaders() });
  }
}
