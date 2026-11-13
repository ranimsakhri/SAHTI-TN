import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export type ProfessionalType = 'NUTRITIONNISTE' | 'COACH';

export interface CoachingRequest {
  id: number;
  clientFirstName: string;
  clientLastName: string;
  clientEmail: string;
  type: ProfessionalType;
  message: string | null;
  status: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class CoachingRequestsService {
  private readonly http = inject(HttpClient);
  private readonly base = 'http://localhost:8081/api/coaching-requests';

  create(request: { type: ProfessionalType; message: string }) {
    return this.http.post<CoachingRequest>(this.base, request);
  }

  inbox() {
    return this.http.get<CoachingRequest[]>(`${this.base}/inbox`);
  }
}
