import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthResponse, User } from '../models/all.models';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  currentUser = signal<AuthResponse | null>(null);

  constructor(private http: HttpClient) {
    const saved = localStorage.getItem('civicai_user');
    if (saved) {
      try {
        this.currentUser.set(JSON.parse(saved));
      } catch (e) {
        localStorage.removeItem('civicai_user');
      }
    }
  }

  login(identifier: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { identifier, password }).pipe(
      tap(user => {
        localStorage.setItem('civicai_user', JSON.stringify(user));
        this.currentUser.set(user);
      })
    );
  }

  register(data: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, data).pipe(
      tap(user => {
        localStorage.setItem('civicai_user', JSON.stringify(user));
        this.currentUser.set(user);
      })
    );
  }

  logout() {
    localStorage.removeItem('civicai_user');
    this.currentUser.set(null);
  }

  isLoggedIn(): boolean {
    return this.currentUser() !== null;
  }

  getRole(): string | null {
    return this.currentUser()?.role || null;
  }

  getDepartmentId(): number | undefined {
    return this.currentUser()?.departmentId;
  }

  getToken(): string | null {
    return this.currentUser()?.token || null;
  }
}
