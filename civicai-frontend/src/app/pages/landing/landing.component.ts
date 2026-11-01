import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { VoiceReportModalComponent } from '../../shared/components/voice-report-modal/voice-report-modal.component';
import { ComplaintService } from '../../core/services/complaint.service';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterLink, VoiceReportModalComponent],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  showVoiceModal = false;
  complaintService = inject(ComplaintService);
  auth = inject(AuthService);
  router = inject(Router);

  onVoiceConfirmed(payload: any) {
    this.showVoiceModal = false;

    if (!this.auth.isLoggedIn()) {
      this.router.navigate(['/login']);
      return;
    }

    this.complaintService.createComplaint(payload).subscribe(res => {
      this.router.navigate(['/complaint', res.id]);
    });
  }
}
