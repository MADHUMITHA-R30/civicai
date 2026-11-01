import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ComplaintService } from '../../core/services/complaint.service';
import { Complaint } from '../../core/models/all.models';
import { BeforeAfterSliderComponent } from '../../shared/components/before-after-slider/before-after-slider.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-complaint-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, BeforeAfterSliderComponent],
  templateUrl: './complaint-detail.component.html',
  styleUrls: ['./complaint-detail.component.css']
})
export class ComplaintDetailComponent implements OnInit {
  route = inject(ActivatedRoute);
  complaintService = inject(ComplaintService);
  auth = inject(AuthService);

  complaint: Complaint | null = null;
  loading = true;

  // Verification & Dispute Modal
  showVerifyModal = false;
  isResolvedAnswer = true;
  feedbackText = '';

  ngOnInit() {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadComplaint(id);
  }

  loadComplaint(id: number) {
    this.loading = true;
    this.complaintService.getComplaintById(id).subscribe({
      next: (data) => {
        this.complaint = data;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  openVerification(answer: boolean) {
    this.isResolvedAnswer = answer;
    this.showVerifyModal = true;
  }

  submitVerification() {
    if (!this.complaint) return;

    this.complaintService.verifyResolution(this.complaint.id, this.isResolvedAnswer, this.feedbackText).subscribe({
      next: (updated) => {
        this.complaint = updated;
        this.showVerifyModal = false;
        alert(this.isResolvedAnswer ? 'Thank you! Resolution verified successfully.' : 'Dispute registered. Department administration alerted.');
      }
    });
  }
}
