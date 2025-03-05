import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth'; // Adres backendu

  constructor(private http: HttpClient) {}

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, user);
  }

  logout(): void {
    localStorage.removeItem('token'); // Usuwamy token przy wylogowaniu
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken(); // Sprawdza, czy token istnieje
  }

  getUsernameFromToken(): string | null {
    const token = this.getToken();
    if (!token) return null;
    
    try {
      const decodedToken: any = jwtDecode(token);
      return decodedToken.sub;  // W JWT standardowo 'sub' oznacza nazwę użytkownika
    } catch (error) {
      console.error('Błąd dekodowania tokena:', error);
      return null;
    }
  }

}
