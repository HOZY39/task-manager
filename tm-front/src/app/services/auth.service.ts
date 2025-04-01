import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';
  private apiUser = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user);
  }

  logout(): void {
    localStorage.removeItem('token'); // delete token
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken(); // Check if token exists
  }

  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;
    
    try {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub;  // In JWT, 'sub' is the username
    } catch (error) {
      console.error('Error decoding token:', error);
      return null;
    }
  }
  getUserRole(username: string): Observable<string> {
    return this.http.get(`http://localhost:8080/api/user/${username}/role`, { responseType: 'text' });
}

}
