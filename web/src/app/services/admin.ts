import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminService {
  private http = inject(HttpClient);
  private base = 'http://localhost:8081/api/admin/professionals';

  list(status?: string) {
    return this.http.get<any[]>(this.base, { params: status ? { status } : {} });
  }
  approve(id: number) {
    return this.http.post<any>(`${this.base}/${id}/approve`, {});
  }
  reject(id: number, motif: string) {
    return this.http.post<any>(`${this.base}/${id}/reject`, { motif });
  }
  document(id: number, doc: string) {
    return this.http.get(`${this.base}/${id}/documents/${doc}`, { responseType: 'blob' });
  }
}
