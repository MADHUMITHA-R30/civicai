import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, Router } from '@angular/router';
import { VoiceReportModalComponent } from '../../shared/components/voice-report-modal/voice-report-modal.component';
import { ComplaintService } from '../../core/services/complaint.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-citizen-home',
  standalone: true,
  imports: [CommonModule, RouterLink, VoiceReportModalComponent],
  templateUrl: './citizen-home.component.html',
  styleUrls: ['./citizen-home.component.css']
})
export class CitizenHomeComponent {
  showVoiceModal = false;
  complaintService = inject(ComplaintService);
  auth = inject(AuthService);
  router = inject(Router);

  onVoiceConfirmed(payload: any) {
    this.showVoiceModal = false;
    this.complaintService.createComplaint(payload).subscribe(res => {
      this.router.navigate(['/complaint', res.id]);
    });
  }
}
